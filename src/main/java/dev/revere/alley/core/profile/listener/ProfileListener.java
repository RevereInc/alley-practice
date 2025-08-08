package dev.revere.alley.core.profile.listener;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.common.constants.PluginConstant;
import dev.revere.alley.feature.hotbar.HotbarService;
import dev.revere.alley.feature.spawn.SpawnService;
import dev.revere.alley.feature.tournament.model.Tournament;
import dev.revere.alley.feature.visibility.VisibilityService;
import dev.revere.alley.core.config.ConfigService;
import dev.revere.alley.feature.music.MusicService;
import dev.revere.alley.core.profile.ProfileService;
import dev.revere.alley.core.profile.Profile;
import dev.revere.alley.core.profile.enums.ProfileState;
import dev.revere.alley.adapter.core.CoreAdapter;
import dev.revere.alley.common.PlayerUtil;
import dev.revere.alley.common.text.CC;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.InventoryHolder;

/**
 * @author Emmy
 * @project Alley
 * @since 19/04/2024
 */
public class ProfileListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onLogin(PlayerLoginEvent event) {
        if (!AlleyPlugin.getInstance().isEnabled()) {
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, CC.translate("&cThe server is still loading, please try again in a few seconds."));
            return;
        }

        if (event.getResult() != PlayerLoginEvent.Result.ALLOWED) {
            return;
        }

        Profile profile = new Profile(event.getPlayer().getUniqueId());
        profile.setName(event.getPlayer().getName());
        profile.load();

        ProfileService profileService = AlleyPlugin.getInstance().getService(ProfileService.class);
        profileService.getProfile(event.getPlayer().getUniqueId());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onJoin(PlayerJoinEvent event) {
        if (!AlleyPlugin.getInstance().isEnabled()) {
            event.getPlayer().kickPlayer(CC.translate("&cThe server is still loading, please try again in a few seconds."));
            return;
        }

        event.setJoinMessage(null);

        Player player = event.getPlayer();
        ProfileService profileService = AlleyPlugin.getInstance().getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());

        this.handlePlayerJoin(profile, player);
        this.sendJoinMessage(player);
    }

    @EventHandler
    private void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();

        ProfileService profileService = AlleyPlugin.getInstance().getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());

        if (profile.getState() == ProfileState.LOBBY
                || validateTournament(profile)
                || profile.getState() == ProfileState.SPECTATING
                || profile.getState() == ProfileState.EDITING
                || profile.getState() == ProfileState.WAITING) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private void onPlayerQuitEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        ProfileService profileService = AlleyPlugin.getInstance().getService(ProfileService.class);
        MusicService musicService = AlleyPlugin.getInstance().getService(MusicService.class);

        Profile profile = profileService.getProfile(player.getUniqueId());

        event.setQuitMessage(null);

        musicService.stopMusic(player);

        profile.updatePlayTime();
        profile.setOnline(false);
        profile.save();

        // profileService.removeProfile(player.getUniqueId());
    }

    @EventHandler
    private void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        ProfileService profileService = AlleyPlugin.getInstance().getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());

        if (profile.getState() == ProfileState.LOBBY
                || validateTournament(profile)
                || profile.getState() == ProfileState.EDITING
                || profile.getState() == ProfileState.SPECTATING) {
            if (player.getGameMode() == GameMode.CREATIVE) return;
            event.setCancelled(true);

            Block block = event.getClickedBlock();
            if (block != null && block.getState() instanceof InventoryHolder) {
                if (block.getType() == Material.CHEST || block.getType() == Material.DISPENSER || block.getType() == Material.FURNACE || block.getType() == Material.BREWING_STAND) {
                    event.setCancelled(true);
                }
            }
        }
    }

    /**
     * Handles the player joining the server.
     * This method sets the player's profile state to LOBBY, updates their name
     * and online status, including other profile-related data.
     * Also teleports the player to the spawn and applies the lobby hotbar items.
     *
     * @param profile The profile of the player.
     * @param player  The player who joined.
     */
    private void handlePlayerJoin(Profile profile, Player player) {
        CoreAdapter coreAdapter = AlleyPlugin.getInstance().getService(CoreAdapter.class);
        SpawnService spawnService = AlleyPlugin.getInstance().getService(SpawnService.class);
        HotbarService hotbarService = AlleyPlugin.getInstance().getService(HotbarService.class);
        VisibilityService visibilityService = AlleyPlugin.getInstance().getService(VisibilityService.class);
        MusicService musicService = AlleyPlugin.getInstance().getService(MusicService.class);

        profile.setState(ProfileState.LOBBY);
        profile.setName(player.getName());
        profile.setOnline(true);
        profile.setMatch(null);
        profile.setParty(null);
        profile.setFfaMatch(null);
        profile.setTournament(null);

        profile.setNameColor(coreAdapter.getCore().getPlayerColor(player));
        profile.getProfileData().getSettingData().setTimeBasedOnProfileSetting(player);
        profile.getProfileData().getPlayTimeData().setLastLogin(System.currentTimeMillis());
        profile.getProfileData().determineLevel();

        player.setFlySpeed(1 * 0.1F);
        player.setWalkSpeed(2 * 0.1F);
        player.getInventory().setHeldItemSlot(0);

        PlayerUtil.reset(player, false, true);

        spawnService.teleportToSpawn(player);
        hotbarService.applyHotbarItems(player);
        visibilityService.updateVisibility(player);
        musicService.startMusic(player);

        player.updateInventory();
    }

    /**
     * Sends a welcome message to the player when they join the server.
     * The message is configured in the messages.yml file.
     *
     * @param player The player who joined.
     */
    private void sendJoinMessage(Player player) {
        ConfigService configService = AlleyPlugin.getInstance().getService(ConfigService.class);
        PluginConstant constants = AlleyPlugin.getInstance().getService(PluginConstant.class);

        FileConfiguration msgConfig = configService.getMessagesConfig();
        if (msgConfig.getBoolean("welcome-message.enabled")) {
            String playerName = player.getName();
            String version = constants.getVersion();
            String authors = constants.getAuthors().toString().replace("[", "").replace("]", "");

            for (String message : msgConfig.getStringList("welcome-message.message")) {
                player.sendMessage(CC.translate(message)
                        .replace("{player}", playerName)
                        .replace("{version}", version)
                        .replace("{author}", authors)
                );
            }
        }
    }

    private boolean validateTournament(Profile profile) {
        Tournament tournament = profile.getTournament();

        return tournament != null && profile.getState().equals(ProfileState.TOURNAMENT_LOBBY);
    }
}