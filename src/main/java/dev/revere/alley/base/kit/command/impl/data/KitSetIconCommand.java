package dev.revere.alley.base.kit.command.impl.data;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.base.kit.KitService;
import dev.revere.alley.base.kit.Kit;
import dev.revere.alley.config.locale.impl.KitLocale;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 5/26/2024
 */
public class KitSetIconCommand extends BaseCommand {
    @CommandData(name = "kit.seticon", isAdminOnly = true)
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&6Usage: &e/kit seticon &6<kitName>"));
            return;
        }

        KitService kitService = this.plugin.getService(KitService.class);
        Kit kit = kitService.getKit(args[0]);
        if (kit == null) {
            player.sendMessage(CC.translate(KitLocale.KIT_NOT_FOUND.getMessage()));
            return;
        }

        kit.setIcon(player.getItemInHand().getType());
        kit.setDurability(player.getItemInHand().getDurability());
        kitService.saveKit(kit);
        player.sendMessage(CC.translate(KitLocale.KIT_ICON_SET.getMessage()).replace("{kit-name}", kit.getName()).replace("{icon}", player.getItemInHand().getType().name().toUpperCase() + ":" + player.getItemInHand().getDurability()));
    }
}
