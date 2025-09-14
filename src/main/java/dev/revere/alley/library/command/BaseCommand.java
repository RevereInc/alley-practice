package dev.revere.alley.library.command;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.common.PlayerUtil;
import dev.revere.alley.common.constants.PluginConstant;
import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.locale.LocaleEntry;
import dev.revere.alley.core.locale.LocaleService;
import dev.revere.alley.core.profile.Profile;
import dev.revere.alley.core.profile.ProfileService;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.UUID;

public abstract class BaseCommand {
    protected final AlleyPlugin plugin;

    /**
     * Constructor for the BaseCommand class.
     */
    public BaseCommand() {
        this.plugin = AlleyPlugin.getInstance();
        this.plugin.getService(CommandFramework.class).registerCommands(this);
    }

    /**
     * Method to be called when a command is executed.
     *
     * @param command The command.
     */
    public abstract void onCommand(CommandArgs command);

    /**
     * Either fetches the profile of an online player or retrieves the offline profile.
     *
     * @param target The name of the player.
     * @param sender The command sender.
     * @return The profile of the player, or null if not found.
     */
    public Profile getOfflineProfile(String target, CommandSender sender) {
        OfflinePlayer offlinePlayer = PlayerUtil.getOfflinePlayerByName(target);
        if (offlinePlayer == null) {
            sender.sendMessage(CC.translate("&cThat player does not exist."));
            return null;
        }

        UUID uuid = offlinePlayer.getUniqueId();
        if (uuid == null) {
            sender.sendMessage(CC.translate("&cThat player is invalid."));
            return null;
        }

        ProfileService profileService = AlleyPlugin.getInstance().getService(ProfileService.class);
        Profile profile = profileService.getProfile(uuid);
        if (profile == null) {
            sender.sendMessage(CC.translate("&cThat player does not have a profile."));
            return null;
        }

        return profile;
    }

    /**
     * Fetches the profile of a player by their UUID.
     *
     * @param uuid The UUID of the player.
     * @return The profile of the player, or null if not found.
     */
    public Profile getProfile(UUID uuid) {
        ProfileService profileService = AlleyPlugin.getInstance().getService(ProfileService.class);
        return profileService.getProfile(uuid);
    }

    /**
     * Gets the admin permission prefix for the bootstrap.
     *
     * @return The admin permission prefix.
     */
    public String getAdminPermission() {
        return this.plugin.getService(PluginConstant.class).getAdminPermissionPrefix();
    }

    /**
     * Gets the locale service.
     *
     * @return The locale service.
     */
    private LocaleService getLocaleService() {
        return this.plugin.getService(LocaleService.class);
    }

    /**
     * Fetches a localized message from the locale service.
     *
     * @param entry The locale entry.
     * @return The localized message.
     */
    public String getMessage(LocaleEntry entry) {
        return this.getLocaleService().getMessage(entry);
    }

    /**
     * Fetches a localized list of messages from the locale service.
     *
     * @param entry The locale entry.
     * @return The localized list of messages.
     */
    public List<String> getMessageList(LocaleEntry entry) {
        return this.getLocaleService().getMessageList(entry);
    }

    /**
     * Fetches a boolean value from the locale service.
     *
     * @param entry The locale entry.
     * @return The boolean value.
     */
    public boolean getBoolean(LocaleEntry entry) {
        return this.getLocaleService().getBoolean(entry);
    }

    /**
     * Fetches an integer value from the locale service.
     *
     * @param entry The locale entry.
     * @return The integer value.
     */
    public int getInt(LocaleEntry entry) {
        return this.getLocaleService().getInt(entry);
    }

    /**
     * Fetches a double value from the locale service.
     *
     * @param entry The locale entry.
     * @return The double value.
     */
    public double getDouble(LocaleEntry entry) {
        return this.getLocaleService().getDouble(entry);
    }
}