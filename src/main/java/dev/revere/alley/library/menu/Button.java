package dev.revere.alley.library.menu;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.common.PlayerUtil;
import dev.revere.alley.common.constants.PluginConstant;
import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.locale.LocaleEntry;
import dev.revere.alley.core.locale.LocaleService;
import dev.revere.alley.core.profile.Profile;
import dev.revere.alley.core.profile.ProfileService;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.UUID;

public abstract class Button {
    /**
     * Creates a placeholder button with the specified material, data value, and title.
     *
     * @param material The material of the placeholder item.
     * @param data     The data value of the placeholder item.
     * @param title    The title of the placeholder item.
     * @return A Button instance representing the placeholder.
     */
    public static Button placeholder(final Material material, final byte data, String... title) {
        return new Button() {
            public ItemStack getButtonItem(Player player) {
                ItemStack it = new ItemStack(material, 1, data);
                ItemMeta meta = it.getItemMeta();

                meta.setDisplayName(StringUtils.join(title));
                it.setItemMeta(meta);

                return it;
            }
        };
    }

    /**
     * Plays a sound to the player indicating a failed action.
     *
     * @param player The player to play the sound to.
     */
    public void playFail(Player player) {
        player.playSound(player.getLocation(), Sound.NOTE_BASS, 20F, 0.1F);
    }

    /**
     * Plays a sound to the player indicating a successful action.
     *
     * @param player The player to play the sound to.
     */
    public void playSuccess(Player player) {
        player.playSound(player.getLocation(), Sound.NOTE_PLING, 20F, 15F);
    }

    /**
     * Plays a neutral sound to the player.
     *
     * @param player The player to play the sound to.
     */
    public void playNeutral(Player player) {
        player.playSound(player.getLocation(), Sound.NOTE_STICKS, 20F, 15F);
    }

    /**
     * Gets the item to be displayed for this button.
     *
     * @param player The player viewing the button.
     * @return The ItemStack representing the button.
     */
    public abstract ItemStack getButtonItem(Player player);

    /**
     * Handles the click event for the button.
     *
     * @param player    The player who clicked the button.
     * @param clickType The type of click.
     */
    public void clicked(Player player, ClickType clickType) {

    }

    /**
     * Handles the click event for the button with additional parameters exclusively for advanced use cases.
     *
     * @param player     The player who clicked the button.
     * @param slot       The slot that was clicked.
     * @param clickType  The type of click.
     * @param hotbarSlot The hotbar slot that was clicked.
     */
    public void clicked(Player player, int slot, ClickType clickType, int hotbarSlot) {

    }

    /**
     * Determines if the click event should be cancelled.
     *
     * @param player    The player who clicked the button.
     * @param clickType The type of click.
     * @return True if the event should be cancelled, false otherwise.
     */
    public boolean shouldCancel(Player player, ClickType clickType) {
        return true;
    }

    /**
     * Determines if the button should update after being clicked.
     *
     * @param player    The player who clicked the button.
     * @param clickType The type of click.
     * @return True if the button should update, false otherwise.
     */
    public boolean shouldUpdate(Player player, ClickType clickType) {
        return false;
    }

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
        return AlleyPlugin.getInstance().getService(PluginConstant.class).getAdminPermissionPrefix();
    }

    /**
     * Gets the locale service.
     *
     * @return The locale service.
     */
    private LocaleService getLocaleService() {
        return AlleyPlugin.getInstance().getService(LocaleService.class);
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