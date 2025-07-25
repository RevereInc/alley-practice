package dev.revere.alley.base.kit.command.impl.manage;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.base.kit.KitService;
import dev.revere.alley.base.kit.Kit;
import dev.revere.alley.config.locale.impl.KitLocale;
import dev.revere.alley.util.chat.CC;
import dev.revere.alley.util.chat.ClickableUtil;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 08/10/2024 - 19:56
 */
public class KitViewCommand extends BaseCommand {
    @CommandData(name = "kit.view", permission = "alley.admin.view")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&6Usage: &e/kit view &6<kitName>"));
            return;
        }

        Kit kit = this.plugin.getService(KitService.class).getKit(args[0]);
        if (kit == null) {
            player.sendMessage(CC.translate(KitLocale.KIT_NOT_FOUND.getMessage()));
            return;
        }

        player.sendMessage("");
        player.sendMessage(CC.translate("&6&lKit " + kit.getName() + " &f(" + (kit.isEnabled() ? "&aEnabled" : "&cDisabled") + "&f)"));
        player.sendMessage(CC.translate(" &f● &6Display Name: &f" + kit.getDisplayName()));
        player.sendMessage(CC.translate(" &f● &6Name: &f" + kit.getName()));
        player.sendMessage(CC.translate(" &f● &6Icon: &f" + kit.getIcon().name().toLowerCase() + " &7(" + kit.getDurability() + ")"));
        player.sendMessage(CC.translate(" &f● &6Disclaimer: &f" + kit.getDisclaimer()));
        player.sendMessage(CC.translate(" &f● &6Description: &f" + kit.getDescription()));
        player.sendMessage(CC.translate(" &f● &6FFA: &f" + (kit.isFfaEnabled() ? "&aEnabled" : "&cDisabled")));
        player.spigot().sendMessage(ClickableUtil.createComponent(
                "  &a(Click here to view the kit settings)",
                "/kit viewsettings " + kit.getName(),
                "&7Click to view the settings of the kit &6" + kit.getName())
        );
        player.sendMessage("");
    }
}