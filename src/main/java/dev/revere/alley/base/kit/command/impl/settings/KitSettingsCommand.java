package dev.revere.alley.base.kit.command.impl.settings;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.base.kit.setting.KitSettingService;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 5/26/2024
 */
public class KitSettingsCommand extends BaseCommand {
    @CommandData(name = "kit.settings", isAdminOnly = true)
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        KitSettingService kitSettingService = this.plugin.getService(KitSettingService.class);

        player.sendMessage("");
        player.sendMessage(CC.translate("&6&lKit Settings List &f(" + kitSettingService.getSettings().size() + "&f)"));
        if (kitSettingService.getSettings().isEmpty()) {
            player.sendMessage(CC.translate(" &f● &cNo Kit Settings available."));
        }
        kitSettingService.getSettings().forEach(setting -> player.sendMessage(CC.translate(" &f● &6" + setting.getName() + " &8(&7" + setting.getDescription() + "&7)")));
        player.sendMessage("");
    }
}