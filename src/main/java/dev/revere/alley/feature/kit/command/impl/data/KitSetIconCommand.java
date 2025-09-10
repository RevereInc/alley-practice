package dev.revere.alley.feature.kit.command.impl.data;

import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import dev.revere.alley.feature.kit.KitService;
import dev.revere.alley.feature.kit.Kit;
import dev.revere.alley.core.locale.internal.types.KitLocaleImpl;
import dev.revere.alley.common.text.CC;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 5/26/2024
 */
public class KitSetIconCommand extends BaseCommand {
    @CommandData(
            name = "kit.seticon",
            isAdminOnly = true,
            usage = "kit seticon <kitName>",
            description = "Set the icon of a kit to the item in your hand."
    )
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
            player.sendMessage(CC.translate(this.getMessage(KitLocaleImpl.KIT_NOT_FOUND)));
            return;
        }

        kit.setIcon(player.getItemInHand().getType());
        kit.setDurability(player.getItemInHand().getDurability());
        kitService.saveKit(kit);
        player.sendMessage(this.getMessage(KitLocaleImpl.KIT_ICON_SET)
                .replace("{kit-name}", kit.getName())
                .replace("{icon}", player.getItemInHand().getType().name().toUpperCase() + ":" + player.getItemInHand().getDurability())
        );
    }
}
