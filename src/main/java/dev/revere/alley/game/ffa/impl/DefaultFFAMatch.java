package dev.revere.alley.game.ffa.impl;

import dev.revere.alley.Alley;
import dev.revere.alley.base.arena.Arena;
import dev.revere.alley.base.combat.CombatService;
import dev.revere.alley.base.hotbar.HotbarService;
import dev.revere.alley.base.kit.Kit;
import dev.revere.alley.base.spawn.SpawnService;
import dev.revere.alley.base.visibility.VisibilityService;
import dev.revere.alley.adapter.knockback.KnockbackAdapter;
import dev.revere.alley.game.ffa.FFAMatch;
import dev.revere.alley.game.ffa.enums.FFAState;
import dev.revere.alley.game.ffa.player.GameFFAPlayer;
import dev.revere.alley.profile.ProfileService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.data.impl.ProfileFFAData;
import dev.revere.alley.profile.enums.ProfileState;
import dev.revere.alley.tool.reflection.ReflectionService;
import dev.revere.alley.tool.reflection.impl.ActionBarReflectionServiceImpl;
import dev.revere.alley.util.PlayerUtil;
import dev.revere.alley.util.chat.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 5/27/2024
 */
public class DefaultFFAMatch extends FFAMatch {
    protected final Alley plugin = Alley.getInstance();

    /**
     * Constructor for the DefaultFFAMatchImpl class.
     *
     * @param name       The name of the match
     * @param arena      The arena the match is being played in
     * @param kit        The kit the players are using
     * @param maxPlayers The maximum amount of players allowed in the match
     */
    public DefaultFFAMatch(String name, Arena arena, Kit kit, int maxPlayers) {
        super(name, arena, kit, maxPlayers);
    }

    /**
     * Join a player to the FFA match.
     *
     * @param player The player
     */
    @Override
    public void join(Player player) {
        GameFFAPlayer gameFFAPlayer = new GameFFAPlayer(player.getUniqueId(), player.getName());
        if (this.getPlayers().size() >= this.getMaxPlayers()) {
            player.sendMessage(CC.translate("&cThis FFA match is full. " + getMaxPlayers() + " players are already in the match."));
            return;
        }

        this.getPlayers().add(gameFFAPlayer);
        this.getPlayers().forEach(ffaPlayer -> ffaPlayer.getPlayer().sendMessage(CC.translate("&a" + player.getName() + " has joined the FFA match.")));
        this.setupPlayer(player);
    }

    /**
     * Force a player to join the FFA match.
     *
     * @param player The player
     */
    public void forceJoin(Player player) {
        GameFFAPlayer gameFFAPlayer = new GameFFAPlayer(player.getUniqueId(), player.getName());
        this.getPlayers().add(gameFFAPlayer);
        this.getPlayers().forEach(ffaPlayer -> ffaPlayer.getPlayer().sendMessage(CC.translate("&a" + player.getName() + " has been forced into the FFA match.")));
        this.setupPlayer(player);
    }

    /**
     * Leave a player from the FFA match.
     *
     * @param player The player
     */
    @Override
    public void leave(Player player) {
        ProfileService profileService = this.plugin.getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());

        GameFFAPlayer gameFFAPlayer = this.getGameFFAPlayer(player);
        this.getPlayers().remove(gameFFAPlayer);

        this.getPlayers().forEach(ffaPlayer -> ffaPlayer.getPlayer().sendMessage(CC.translate(
                "&c" + profile.getFancyName() + " has left the FFA match."))
        );

        player.sendMessage(CC.translate("&aYou have left the FFA match."));

        profile.setState(ProfileState.LOBBY);
        profile.setFfaMatch(null);
        profile.getProfileData().getFfaData().get(this.getKit().getName()).resetKillstreak();

        this.plugin.getService(VisibilityService.class).updateVisibility(player);

        PlayerUtil.reset(player, false, true);
        this.plugin.getService(SpawnService.class).teleportToSpawn(player);
        this.plugin.getService(HotbarService.class).applyHotbarItems(player);
    }

    /**
     * Setup a player for the FFA match.
     *
     * @param player The player
     */
    @Override
    public void setupPlayer(Player player) {
        GameFFAPlayer gameFFAPlayer = this.getGameFFAPlayer(player);
        gameFFAPlayer.setState(FFAState.SPAWN);

        ProfileService profileService = this.plugin.getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());
        profile.setState(ProfileState.FFA);
        profile.setFfaMatch(this);

        this.plugin.getService(VisibilityService.class).updateVisibility(player);
        this.plugin.getService(KnockbackAdapter.class).getKnockbackImplementation().applyKnockback(player.getPlayer(), getKit().getKnockbackProfile());

        PlayerUtil.reset(player, true, true);

        Arena arena = this.getArena();
        player.teleport(arena.getPos1());

        Kit kit = this.getKit();
        player.getInventory().setArmorContents(kit.getArmor());
        player.getInventory().setContents(kit.getItems());
    }

    /**
     * Handle the respawn of a player.
     *
     * @param player The player
     */
    public void handleRespawn(Player player) {
        ProfileService profileService = this.plugin.getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());
        profile.setState(ProfileState.FFA);
        profile.setFfaMatch(this);

        Arena arena = this.getArena();

        Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
            player.teleport(arena.getPos1());

            Kit kit = this.getKit();
            player.getInventory().clear();
            player.getInventory().setArmorContents(kit.getArmor());
            player.getInventory().setContents(kit.getItems());
            player.updateInventory();
        }, 1L);

        GameFFAPlayer gameFFAPlayer = this.getGameFFAPlayer(player);
        gameFFAPlayer.setState(FFAState.SPAWN);
    }

    /**
     * Handle the death of a player.
     *
     * @param player The player who died.
     * @param killer The killer / last attacker of the player who died.
     */
    @Override
    public void handleDeath(Player player, Player killer) {
        ProfileService profileService = this.plugin.getService(ProfileService.class);

        if (killer == null) {
            Profile profile = profileService.getProfile(player.getUniqueId());
            ProfileFFAData ffaData = profile.getProfileData().getFfaData().get(this.getKit().getName());
            ffaData.incrementDeaths();
            ffaData.resetKillstreak();

            this.getPlayers().forEach(ffaPlayer -> ffaPlayer.getPlayer().sendMessage(CC.translate(
                    "&c" + profile.getFancyName() + " has died."))
            );
            this.handleRespawn(player);
            return;
        }

        Profile killerProfile = profileService.getProfile(killer.getUniqueId());
        ProfileFFAData killerFfaData = killerProfile.getProfileData().getFfaData().get(getKit().getName());
        if (killerFfaData != null) {
            killerFfaData.incrementKills();
            killerFfaData.incrementKillstreak();
        }

        Profile profile = profileService.getProfile(player.getUniqueId());
        ProfileFFAData ffaData = profile.getProfileData().getFfaData().get(getKit().getName());
        ffaData.incrementDeaths();
        ffaData.resetKillstreak();

        this.plugin.getService(ReflectionService.class).getReflectionService(ActionBarReflectionServiceImpl.class).sendDeathMessage(killer, player);
        this.plugin.getService(CombatService.class).resetCombatLog(player);

        this.getPlayers().forEach(ffaPlayer -> ffaPlayer.getPlayer().sendMessage(CC.translate(
                "&6" + profile.getFancyName() + " &ahas been killed by &6" + killerProfile.getFancyName() + "&a."))
        );
        this.sendKillstreakAlertMessage(killer);

        this.handleRespawn(player);
    }
}