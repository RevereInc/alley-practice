package dev.revere.alley.feature.hotbar.command.impl.manage;

import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import dev.revere.alley.core.profile.Profile;
import dev.revere.alley.core.profile.ProfileService;
import dev.revere.alley.core.profile.enums.ProfileState;
import dev.revere.alley.feature.hotbar.HotbarService;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Hamza
 * @since 01/11/2025
 */
public class HotbarRefreshCommand extends BaseCommand {
    @CommandData(
            name = "hotbar.refresh",
            isAdminOnly = true,
            usage = "hotbar refresh",
            description = "Refresh hotbar items from storage."
    )
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        HotbarService hotbarService = this.plugin.getService(HotbarService.class);

        try {
            hotbarService.refreshCache();
        } catch (Exception e) {
            e.printStackTrace();
            sender.sendMessage(CC.translate("&cAn error occurred while refreshing hotbar items from storage."));
            return;
        }

        try {
            plugin.getServer().getOnlinePlayers().forEach(player -> {
                Profile profile = plugin.getService(ProfileService.class).getProfile(player.getUniqueId());
                if (profile.isInGame()) {
                    return;
                }
                hotbarService.applyHotbarItems(plugin.getServer().getPlayer(profile.getUuid()));
            });
        } catch (Exception e) {
            sender.sendMessage(CC.translate("&cNo players are online to refresh hotbar items for."));
            return;
        }

        sender.sendMessage(this.getString(GlobalMessagesLocaleImpl.HOTBAR_REFRESHED));
    }
}