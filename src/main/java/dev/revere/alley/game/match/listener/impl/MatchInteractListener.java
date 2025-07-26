package dev.revere.alley.game.match.listener.impl;

import dev.revere.alley.Alley;
import dev.revere.alley.base.kit.setting.impl.mode.KitSettingCheckpoint;
import dev.revere.alley.game.match.impl.CheckpointMatch;
import dev.revere.alley.game.match.player.impl.MatchGamePlayerImpl;
import dev.revere.alley.game.match.player.participant.GameParticipant;
import dev.revere.alley.profile.ProfileService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.enums.ProfileState;
import dev.revere.alley.tool.reflection.ReflectionService;
import dev.revere.alley.tool.reflection.impl.TitleReflectionServiceImpl;
import dev.revere.alley.util.ListenerUtil;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * @author Emmy
 * @project Alley
 * @since 08/02/2025
 */
public class MatchInteractListener implements Listener {
    @EventHandler
    private void handleParkourInteraction(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ProfileService profileService = Alley.getInstance().getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());

        if (profile.getState() != ProfileState.PLAYING) return;
        if (!profile.getMatch().getKit().isSettingEnabled(KitSettingCheckpoint.class)) return;

        if (event.getAction() != Action.PHYSICAL) {
            return;
        }

        Block block = event.getClickedBlock();

        if (ListenerUtil.notSteppingOnPlate(block)) return;

        CheckpointMatch matchCheckpoint = (CheckpointMatch) profile.getMatch();
        MatchGamePlayerImpl matchGamePlayer = matchCheckpoint.getGamePlayer(player);
        if (matchGamePlayer == null) return;
        if (ListenerUtil.checkSteppingOnIronPressurePlate(block)) {
            Location checkpointLocation = player.getLocation();

            matchGamePlayer.setCheckpoint(checkpointLocation);

            boolean isNewCheckpoint = matchGamePlayer.getCheckpoints().stream()
                    .noneMatch(location -> location.getX() == checkpointLocation.getX() &&
                            location.getY() == checkpointLocation.getY() &&
                            location.getZ() == checkpointLocation.getZ());

            if (isNewCheckpoint) {
                matchGamePlayer.getCheckpoints().add(checkpointLocation);
                matchGamePlayer.setCheckpointCount(matchGamePlayer.getCheckpointCount() + 1);

                Alley.getInstance().getService(ReflectionService.class).getReflectionService(TitleReflectionServiceImpl.class).sendTitle(
                        player,
                        "&aCHECKPOINT!",
                        "&7(" + player.getLocation().getBlockX() + ", " + player.getLocation().getBlockY() + ", " + player.getLocation().getBlockZ() + ")"
                );
            }

            return;
        }

        if (ListenerUtil.checkSteppingOnGoldPressurePlate(block)) {
            GameParticipant<MatchGamePlayerImpl> opponent = matchCheckpoint.getParticipantA().containsPlayer(player.getUniqueId())
                    ? matchCheckpoint.getParticipantB()
                    : matchCheckpoint.getParticipantA();

            opponent.setLostCheckpoint(true);
            opponent.getPlayers().forEach(gamePlayer -> gamePlayer.setDead(true));
            opponent.getPlayers().stream().findAny().ifPresent(gamePlayer -> {
                matchCheckpoint.handleDeath(gamePlayer.getTeamPlayer(), EntityDamageEvent.DamageCause.CUSTOM);
            });
        }
    }
}