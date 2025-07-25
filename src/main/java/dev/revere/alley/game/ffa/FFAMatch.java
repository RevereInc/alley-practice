package dev.revere.alley.game.ffa;

import dev.revere.alley.Alley;
import dev.revere.alley.base.arena.Arena;
import dev.revere.alley.base.combat.CombatService;
import dev.revere.alley.base.hotbar.HotbarService;
import dev.revere.alley.base.kit.Kit;
import dev.revere.alley.base.spawn.SpawnService;
import dev.revere.alley.base.visibility.VisibilityService;
import dev.revere.alley.config.ConfigService;
import dev.revere.alley.game.ffa.player.GameFFAPlayer;
import dev.revere.alley.profile.ProfileService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.data.impl.ProfileFFAData;
import dev.revere.alley.profile.enums.ProfileState;
import dev.revere.alley.util.chat.CC;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Remi
 * @project Alley
 * @date 5/27/2024
 */
@Getter
@Setter
public abstract class FFAMatch {
    protected final Alley plugin = Alley.getInstance();

    private final String name;

    private final Arena arena;
    private final Kit kit;

    private int maxPlayers;

    private List<GameFFAPlayer> players;
    private List<UUID> spectators;

    /**
     * Constructor for the AbstractFFAMatch class.
     *
     * @param name       The name of the match
     * @param arena      The arena the match is being played in
     * @param kit        The kit the players are using
     * @param maxPlayers The maximum amount of players allowed in the match
     */
    public FFAMatch(String name, Arena arena, Kit kit, int maxPlayers) {
        this.name = name;
        this.arena = arena;
        this.kit = kit;
        this.maxPlayers = maxPlayers;
        this.players = new ArrayList<>();
        this.spectators = new ArrayList<>();
    }

    public abstract void join(Player player);

    public abstract void setupPlayer(Player player);

    public abstract void handleDeath(Player player, Player killer);

    public abstract void handleRespawn(Player player);

    public abstract void leave(Player player);


    /**
     * Method to spectate the match as a player.
     *
     * @param player The player who wants to spectate the match.
     */
    public void addSpectator(Player player) {
        ProfileService profileService = this.plugin.getService(ProfileService.class);
        VisibilityService visibilityService = this.plugin.getService(VisibilityService.class);
        HotbarService hotbarService = this.plugin.getService(HotbarService.class);

        Profile profile = profileService.getProfile(player.getUniqueId());

        if (this.arena.getCenter() == null) {
            player.sendMessage(CC.translate("&cThe arena is not set up for spectating"));
            return;
        }

        profile.setState(ProfileState.SPECTATING);
        profile.setFfaMatch(this);

        visibilityService.updateVisibility(player);
        hotbarService.applyHotbarItems(player);

        player.teleport(this.arena.getCenter());
        player.spigot().setCollidesWithEntities(false);
        player.setAllowFlight(true);
        player.setFlying(true);

        this.spectators.add(player.getUniqueId());
    }

    /**
     * Method to remove a player from the spectator list and reset their state.
     *
     * @param player The player to remove from the spectator list.
     */
    public void removeSpectator(Player player) {
        ProfileService profileService = this.plugin.getService(ProfileService.class);
        VisibilityService visibilityService = this.plugin.getService(VisibilityService.class);
        HotbarService hotbarService = this.plugin.getService(HotbarService.class);
        SpawnService spawnService = this.plugin.getService(SpawnService.class);

        Profile profile = profileService.getProfile(player.getUniqueId());
        profile.setState(ProfileState.LOBBY);
        profile.setFfaMatch(null);

        visibilityService.updateVisibility(player);
        hotbarService.applyHotbarItems(player);

        player.spigot().setCollidesWithEntities(true);
        player.setAllowFlight(false);
        player.setFlying(false);

        spawnService.teleportToSpawn(player);

        this.spectators.remove(player.getUniqueId());
    }

    /**
     * Method to get an instance of GameFFAPlayer from a Player object.
     *
     * @param player The Player object to get the GameFFAPlayer from.
     * @return The GameFFAPlayer instance associated with the Player, or null if not found.
     */
    public GameFFAPlayer getGameFFAPlayer(Player player) {
        return this.players.stream()
                .filter(ffaPlayer -> ffaPlayer.getUuid().equals(player.getUniqueId()))
                .findFirst()
                .orElse(null);
    }

    /**
     * Handle the combat log of a player and killer, updating stats of the combat logger, ect...
     *
     * @param player The player
     * @param killer The killer
     */
    public void handleCombatLog(Player player, Player killer) {
        ProfileService profileService = this.plugin.getService(ProfileService.class);
        CombatService combatService = this.plugin.getService(CombatService.class);

        Profile profile = profileService.getProfile(player.getUniqueId());
        ProfileFFAData ffaData = profile.getProfileData().getFfaData().get(this.getKit().getName());
        ffaData.incrementDeaths();
        ffaData.resetKillstreak();

        Profile killerProfile = profileService.getProfile(killer.getUniqueId());
        ProfileFFAData killerFfaData = killerProfile.getProfileData().getFfaData().get(this.getKit().getName());
        killerFfaData.incrementKills();
        killerFfaData.incrementKillstreak();

        this.getPlayers().forEach(ffaPlayer -> ffaPlayer.getPlayer().sendMessage(CC.translate("&7(Combat Log) &c" + player.getName() + " has been killed by " + killer.getName() + ".")));
        this.sendKillstreakAlertMessage(killer);

        combatService.resetCombatLog(player);
    }

    /**
     * Teleports a player to the safe zone of the FFA arena.
     *
     * @param player The player to teleport.
     */
    public void teleportToSafeZone(Player player) {
        CombatService combatService = this.plugin.getService(CombatService.class);
        if (combatService.isPlayerInCombat(player.getUniqueId())) {
            player.sendMessage(CC.translate("&cYou're still in combat!"));
            return;
        }

        Location ffaSpawn = this.arena.getPos1();
        player.teleport(ffaSpawn);
        player.sendMessage(CC.translate("&aTeleported to the safe zone!"));
    }

    /**
     * Alerts all players in the match if a player reaches a killstreak of 5 or more.
     *
     * @param player The player who reached the killstreak.
     */
    public void sendKillstreakAlertMessage(Player player) {
        ConfigService configService = this.plugin.getService(ConfigService.class);
        FileConfiguration config = configService.getMessagesConfig();
        if (!config.getBoolean("ffa.killstreak-alert.enabled")) {
            return;
        }

        ProfileService profileService = this.plugin.getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());
        ProfileFFAData ffaData = profile.getProfileData().getFfaData().get(this.getKit().getName());

        int interval = config.getInt("ffa.killstreak-alert.interval");
        List<String> messages = config.getStringList("ffa.killstreak-alert.message");

        if (ffaData.getKillstreak() % interval == 0) {
            this.getPlayers().forEach(ffaPlayer -> {
                for (String message : messages) {
                    ffaPlayer.getPlayer().sendMessage(CC.translate(message
                            .replace("{player}", player.getName())
                            .replace("{name-color}", String.valueOf(profile.getNameColor()))
                            .replace("{killstreak}", String.valueOf(ffaData.getKillstreak()))));
                }
            });
        }
    }
}