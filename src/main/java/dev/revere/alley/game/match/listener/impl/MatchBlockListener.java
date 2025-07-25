package dev.revere.alley.game.match.listener.impl;

import dev.revere.alley.Alley;
import dev.revere.alley.base.arena.impl.StandAloneArena;
import dev.revere.alley.base.kit.setting.impl.mechanic.KitSettingBuildImpl;
import dev.revere.alley.base.kit.setting.impl.mechanic.KitSettingTimedBlocksImpl;
import dev.revere.alley.base.kit.setting.impl.mode.*;
import dev.revere.alley.game.match.Match;
import dev.revere.alley.game.match.enums.MatchState;
import dev.revere.alley.game.match.impl.BedMatch;
import dev.revere.alley.game.match.impl.HideAndSeekMatch;
import dev.revere.alley.game.match.player.enums.BaseRaiderRole;
import dev.revere.alley.game.match.player.impl.MatchGamePlayerImpl;
import dev.revere.alley.game.match.player.participant.GameParticipant;
import dev.revere.alley.profile.ProfileService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.enums.ProfileState;
import dev.revere.alley.tool.reflection.ReflectionRepository;
import dev.revere.alley.tool.reflection.impl.BlockAnimationReflectionServiceImpl;
import dev.revere.alley.util.ListenerUtil;
import dev.revere.alley.util.chat.CC;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * @author Emmy
 * @project Alley
 * @since 08/02/2025
 */
public class MatchBlockListener implements Listener {
    @EventHandler()
    public void onBlockFromEvent(BlockFromToEvent event) {

    }

    @EventHandler
    private void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ProfileService profileService = Alley.getInstance().getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());

        Match match = profile.getMatch();
        if (match == null) {
            return;
        }

        GameParticipant<MatchGamePlayerImpl> participant = match.getParticipant(player);

        switch (profile.getState()) {
            case PLAYING:
                MatchState matchState = match.getState();
                if (matchState == MatchState.STARTING || matchState == MatchState.ENDING_MATCH || matchState == MatchState.RESTARTING_ROUND) {
                    event.setCancelled(true);
                    return;
                }

                if (match.getKit().isSettingEnabled(KitSettingBuildImpl.class)) {
                    Block block = event.getBlock();

                    if (match.getKit().isSettingEnabled(KitSettingRaiding.class)) {
                        if (participant.getLeader().getData().getRole() == BaseRaiderRole.TRAPPER) {
                            match.addBlockToBrokenBlocksMap(block.getState(), block.getLocation());
                            return;
                        } else {
                            event.setCancelled(true);
                        }
                        return;
                    }

                    if (match.getKit().isSettingEnabled(KitSettingHideAndSeek.class)) {
                        HideAndSeekMatch matchHideAndSeek = (HideAndSeekMatch) profile.getMatch();
                        GameParticipant<MatchGamePlayerImpl> seekers = matchHideAndSeek.getParticipantA();

                        boolean isSeeker = seekers.containsPlayer(player.getUniqueId());

                        if (matchHideAndSeek.getGameEndTask() == null && isSeeker) {
                            player.sendMessage(CC.translate("&cYou cannot break blocks during the hiding phase!"));
                            event.setCancelled(true);
                            return;
                        }
                        return;
                    }

                    if (match.getKit().isSettingEnabled(KitSettingBed.class)) {
                        BedMatch matchBed = (BedMatch) profile.getMatch();
                        if (matchBed == null) {
                            event.setCancelled(true);
                            return;
                        }

                        if (!ListenerUtil.isBedFightProtectedBlock(block.getType())) {
                            if (!matchBed.isNearBed(block)) {
                                event.setCancelled(true);
                                return;
                            }

                            event.setCancelled(true);
                            return;
                        }

                        StandAloneArena arena = (StandAloneArena) match.getArena();
                        if (block.getType() != Material.BED_BLOCK) {
                            return;
                        }

                        if (!arena.isEnemyBed(block, participant)) {
                            player.sendMessage(CC.translate("You cannot break your own bed!"));
                            event.setCancelled(true);
                            return;
                        }

                        GameParticipant<MatchGamePlayerImpl> opponent = matchBed.getParticipantA().containsPlayer(player.getUniqueId())
                                ? matchBed.getParticipantB()
                                : matchBed.getParticipantA();

                        event.setCancelled(true);

                        if (opponent == null) {
                            return;
                        }

                        block.setType(Material.AIR);

                        match.addBlockToBrokenBlocksMap(block.getState(), block.getLocation());
                        opponent.setBedBroken(true);
                        matchBed.alertBedDestruction(player, opponent);
                        return;
                    }

                    if (match.getKit().isSettingEnabled(KitSettingBridges.class)) {
                        if (block.getType() == Material.STAINED_CLAY) {
                            match.addBlockToBrokenBlocksMap(block.getState(), block.getLocation());
                            return;
                        }
                    }

                    BlockState blockState = event.getBlock().getState();

                    if (match.getPlacedBlocks().containsKey(blockState)) {
                        match.removeBlockFromPlacedBlocksMap(blockState, event.getBlock().getLocation());
                    } else {
                        event.setCancelled(true);
                    }
                    return;
                }

                if (match.getKit().isSettingEnabled(KitSettingSpleef.class)) {
                    Block block = event.getBlock();
                    if (block.getType() != Material.SNOW_BLOCK) {
                        event.setCancelled(true);
                        return;
                    }

                    match.addBlockToBrokenBlocksMap(block.getState(), block.getLocation());
                    event.getBlock().setType(Material.AIR);

                    int amount = ThreadLocalRandom.current().nextInt(3, 6);
                    if (amount > 0) {
                        ItemStack snowballs = new ItemStack(Material.SNOW_BALL, amount);
                        player.getInventory().addItem(snowballs);
                    }
                    return;
                }

                event.setCancelled(true);
                break;

            case SPECTATING:
                event.setCancelled(true);
                break;
        }
    }

    @EventHandler
    private void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Block placedBlock = event.getBlockPlaced();

        ProfileService profileService = Alley.getInstance().getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());

        Match match = profile.getMatch();
        if (match == null) {
            return;
        }

        switch (profile.getState()) {
            case PLAYING:
                if (match.getState() == MatchState.STARTING) {
                    if (match.getKit().isSettingEnabled(KitSettingBuildImpl.class)
                            && placedBlock.getType() == Material.TNT) {
                        return;
                    }

                    event.setCancelled(true);
                }

                if (match.getState() == MatchState.ENDING_MATCH) event.setCancelled(true);
                if (match.getState() == MatchState.RESTARTING_ROUND) event.setCancelled(true);

                if (match.getArena() instanceof StandAloneArena) {
                    StandAloneArena arena = (StandAloneArena) match.getArena();
                    Location blockLocation = placedBlock.getLocation();

                    if (blockLocation.getBlockY() > arena.getHeightLimit()) {
                        player.sendMessage(CC.translate("&cYou cannot place blocks above the height limit!"));
                        event.setCancelled(true);
                        return;
                    }

                    if ((arena.getTeam1Portal() != null && blockLocation.distance(arena.getTeam1Portal()) <= arena.getPortalRadius()) ||
                            (arena.getTeam2Portal() != null && blockLocation.distance(arena.getTeam2Portal()) <= arena.getPortalRadius())) {
                        player.sendMessage(CC.translate("&cYou cannot build near a portal!"));
                        event.setCancelled(true);
                        return;
                    }
                }

                if (match.getKit().isSettingEnabled(KitSettingBuildImpl.class)) {
                    if (match.getKit().isSettingEnabled(KitSettingRaiding.class)) {
                        GameParticipant<MatchGamePlayerImpl> participant = match.getParticipant(player);
                        if (participant.getLeader().getData().getRole() == BaseRaiderRole.TRAPPER) {
                            match.addBlockToPlacedBlocksMap(placedBlock.getState(), placedBlock.getLocation());
                        } else {
                            event.setCancelled(true);
                        }
                        return;
                    }

                    if (match.getKit().isSettingEnabled(KitSettingHideAndSeek.class)) {
                        HideAndSeekMatch matchHideAndSeek = (HideAndSeekMatch) profile.getMatch();
                        GameParticipant<MatchGamePlayerImpl> seekers = matchHideAndSeek.getParticipantA();

                        boolean isSeeker = seekers.containsPlayer(player.getUniqueId());

                        if (matchHideAndSeek.getGameEndTask() == null && isSeeker) {
                            player.sendMessage(CC.translate("&cYou cannot place blocks during the hiding phase!"));
                            event.setCancelled(true);
                            return;
                        }
                    }

                    match.addBlockToPlacedBlocksMap(placedBlock.getState(), event.getBlockPlaced().getLocation());

                    handleTimedBlockPlacement(event, match, profileService);
                    return;
                }

                event.setCancelled(true);
                break;
            case SPECTATING:
                event.setCancelled(true);
                break;
        }
    }

    @EventHandler
    private void onProjectileHit(ProjectileHitEvent event) {
        if (event.getEntityType() != EntityType.SNOWBALL || event.getHitBlock() == null) {
            return;
        }

        Block hitBlock = event.getHitBlock();
        if (hitBlock.getType() != Material.SNOW && hitBlock.getType() != Material.SNOW_BLOCK) {
            return;
        }

        if (!(event.getEntity().getShooter() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getEntity().getShooter();
        ProfileService profileService = Alley.getInstance().getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());

        if (profile.getState() != ProfileState.PLAYING) return;
        if (profile.getMatch().getState() != MatchState.RUNNING) return;
        if (!profile.getMatch().getKit().isSettingEnabled(KitSettingSpleef.class)) return;

        profile.getMatch().addBlockToBrokenBlocksMap(hitBlock.getState(), hitBlock.getLocation());
        hitBlock.setType(Material.AIR);
    }

    @EventHandler
    private void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ProfileService profileService = Alley.getInstance().getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());

        if (profile.getState() == ProfileState.SPECTATING) {
            Block block = event.getClickedBlock();
            if (block == null || block.getType() == Material.AIR) return;

            event.setCancelled(true);
        }
    }

    @EventHandler
    private void onDoorOpen(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ProfileService profileService = Alley.getInstance().getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());

        if (profile.getState() != ProfileState.PLAYING) return;
        if (profile.getMatch() == null) return;
        if (profile.getMatch().getKit().isSettingEnabled(KitSettingRaiding.class)) {
            Block block = event.getClickedBlock();
            if (block == null || block.getType() == Material.AIR) return;

            Action action = event.getAction();
            if (action == Action.RIGHT_CLICK_BLOCK) {
                if (block.getType() == Material.SIGN || block.getType() == Material.SIGN_POST || block.getType() == Material.WALL_SIGN) {
                    return;
                }

                GameParticipant<MatchGamePlayerImpl> participant = profile.getMatch().getParticipant(player);
                if (participant.getLeader().getData().getRole() == BaseRaiderRole.RAIDER && ListenerUtil.isInteractiveBlock(block.getType())) {
                    if (event.getPlayer().isSneaking() && event.getItem().getType() == Material.ENDER_PEARL && event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock().getType().name().contains("FENCE")) {
                        return;
                    }
                    event.setCancelled(true);
                    player.sendMessage(CC.translate("&cYou cannot interact as a raider!"));
                }
            }
        } else {
            if (event.getClickedBlock() == null) {
                return;
            }

            if (ListenerUtil.isInteractiveBlock(event.getClickedBlock().getType())) {
                event.setCancelled(true);
                player.sendMessage(CC.translate("&cYou cannot interact during a match!"));
            }
        }
    }

    @EventHandler
    public void onSign(SignChangeEvent event) {
        String[] lines = event.getLines();

        int elevatorLineIndex = findElevatorLine(lines);
        if (elevatorLineIndex == -1 || elevatorLineIndex >= 3) {
            return;
        }

        String direction = lines[elevatorLineIndex + 1];
        if (!isValidDirection(direction)) {
            breakSignAndNotifyPlayer(event);
            return;
        }

        createElevatorSign(event, direction);
    }

    @EventHandler
    public void onSignInteract(PlayerInteractEvent event) {
        Block clickedBlock = event.getClickedBlock();
        if (clickedBlock == null || !isSignBlock(clickedBlock)) {
            return;
        }

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        Sign sign = (Sign) clickedBlock.getState();
        if (!isElevatorSign(sign)) {
            return;
        }

        String direction = sign.getLine(2);
        Player player = event.getPlayer();

        if (direction.equalsIgnoreCase("Up")) {
            teleportUp(player, clickedBlock.getLocation());
        } else if (direction.equalsIgnoreCase("Down")) {
            teleportDown(player, clickedBlock.getLocation());
        }
    }

    private int findElevatorLine(String[] lines) {
        for (int i = 0; i < lines.length; i++) {
            if (lines[i].equalsIgnoreCase("[Elevator]")) {
                return i;
            }
        }
        return -1;
    }

    private boolean isValidDirection(String direction) {
        return direction != null &&
                (direction.equalsIgnoreCase("Up") || direction.equalsIgnoreCase("Down"));
    }

    private void breakSignAndNotifyPlayer(SignChangeEvent event) {
        event.getBlock().breakNaturally();
        event.getPlayer().sendMessage(CC.translate("&cInvalid direction."));
    }

    private void createElevatorSign(SignChangeEvent event, String direction) {
        event.setLine(0, "");
        event.setLine(1, ChatColor.translateAlternateColorCodes('&', "&c[Elevator]"));
        event.setLine(2, ChatColor.translateAlternateColorCodes('&', direction));
        event.setLine(3, "");
    }

    private boolean isSignBlock(Block block) {
        Material type = block.getType();
        return type == Material.WALL_SIGN || type == Material.SIGN_POST;
    }

    private boolean isElevatorSign(Sign sign) {
        return sign.getLine(1).equalsIgnoreCase(ChatColor.RED + "[Elevator]");
    }

    private void teleportUp(Player player, Location signLocation) {
        Location searchLocation = signLocation.clone().add(0.0, 1.0, 0.0);

        while (searchLocation.getY() < 254.0) {
            if (searchLocation.getBlock().getType() != Material.AIR) {
                Location safeSpot = findSafeSpotAbove(searchLocation);
                if (safeSpot != null) {
                    teleportPlayerToLocation(player, safeSpot);
                    return;
                }
            }
            searchLocation.add(0.0, 1.0, 0.0);
        }

        player.sendMessage(CC.translate("&cCould not teleport."));
    }

    private void teleportDown(Player player, Location signLocation) {
        Location searchLocation = signLocation.clone().subtract(0.0, 1.0, 0.0);

        while (searchLocation.getY() > 2.0) {
            if (searchLocation.getBlock().getType() != Material.AIR) {
                Location safeSpot = findSafeSpotBelow(searchLocation);
                if (safeSpot != null) {
                    teleportPlayerToLocation(player, safeSpot);
                    return;
                }
            }
            searchLocation.subtract(0.0, 1.0, 0.0);
        }

        player.sendMessage(CC.translate("&cCould not teleport."));
    }

    private Location findSafeSpotAbove(Location startLocation) {
        Location checkLocation = startLocation.clone();

        while (checkLocation.getY() < 254) {
            Location aboveLocation = checkLocation.clone().add(0.0, 1.0, 0.0);

            if (checkLocation.getBlock().getType() == Material.AIR &&
                    aboveLocation.getBlock().getType() == Material.AIR) {
                return checkLocation;
            }

            checkLocation.add(0.0, 1.0, 0.0);
        }

        return null;
    }

    private Location findSafeSpotBelow(Location startLocation) {
        Location checkLocation = startLocation.clone();

        while (checkLocation.getY() > 2.0) {
            Location belowLocation = checkLocation.clone().subtract(0.0, 1.0, 0.0);

            if (checkLocation.getBlock().getType() == Material.AIR &&
                    belowLocation.getBlock().getType() == Material.AIR) {
                return checkLocation;
            }

            checkLocation.subtract(0.0, 1.0, 0.0);
        }

        return null;
    }

    private void teleportPlayerToLocation(Player player, Location targetLocation) {
        Location playerLocation = player.getLocation();
        Location teleportLocation = new Location(
                targetLocation.getWorld(),
                targetLocation.getX() + 0.5,
                targetLocation.getY(),
                targetLocation.getZ() + 0.5,
                playerLocation.getYaw(),
                playerLocation.getPitch()
        );

        player.teleport(teleportLocation);
    }

    /**
     * Handles the logic for blocks that disappear after a set time.
     * This is triggered from the onBlockPlace event.
     *
     * @param event          The BlockPlaceEvent.
     * @param match          The current match.
     * @param profileService The profile service instance.
     */
    private void handleTimedBlockPlacement(BlockPlaceEvent event, Match match, ProfileService profileService) {
        if (!match.getKit().isSettingEnabled(KitSettingTimedBlocksImpl.class)) {
            return;
        }

        Block placedBlock = event.getBlockPlaced();
        final BlockState originalState = placedBlock.getState();
        Material type = placedBlock.getType();

        if (type != Material.WOOL && type != Material.STAINED_CLAY) {
            return;
        }

        final Player player = event.getPlayer();
        final int DURATION_TICKS = 100;

        BlockAnimationReflectionServiceImpl animationService = Alley.getInstance().getService(ReflectionRepository.class).getReflectionService(BlockAnimationReflectionServiceImpl.class);
        int animationId = placedBlock.getLocation().hashCode();

        List<Player> playersInMatch = match.getParticipants().stream()
                .flatMap(participant -> participant.getPlayers().stream())
                .map(MatchGamePlayerImpl::getTeamPlayer)
                .filter(p -> p != null && p.isOnline())
                .collect(Collectors.toList());

        animationService.sendBreakAnimationSequence(playersInMatch, placedBlock, animationId, DURATION_TICKS);

        ItemStack itemToReturn = new ItemStack(event.getItemInHand());
        itemToReturn.setAmount(1);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!match.getPlacedBlocks().containsKey(originalState)) {
                    return;
                }

                Profile currentProfile = profileService.getProfile(player.getUniqueId());
                boolean playerIsStillPlaying = player.getGameMode() == GameMode.SURVIVAL && currentProfile != null && currentProfile.getMatch() == match && currentProfile.getState() == ProfileState.PLAYING && match.getState() == MatchState.RUNNING;

                if (placedBlock.getLocation().getBlock().getType() == type) {
                    playersInMatch.forEach(p -> animationService.sendBreakAnimation(p, placedBlock, animationId, -1));

                    placedBlock.setType(Material.AIR);
                    match.removeBlockFromPlacedBlocksMap(placedBlock.getState(), placedBlock.getLocation());

                    if (playerIsStillPlaying) {
                        for (int i = 0; i < player.getInventory().getSize(); i++) {
                            ItemStack itemInSlot = player.getInventory().getItem(i);
                            if (itemInSlot != null && itemInSlot.isSimilar(itemToReturn)) {
                                if (itemInSlot.getAmount() < itemInSlot.getMaxStackSize()) {
                                    itemInSlot.setAmount(itemInSlot.getAmount() + 1);
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }.runTaskLater(Alley.getInstance(), DURATION_TICKS);
    }
}