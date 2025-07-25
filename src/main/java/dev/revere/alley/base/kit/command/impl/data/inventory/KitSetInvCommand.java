package dev.revere.alley.base.kit.command.impl.data.inventory;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.base.kit.KitService;
import dev.revere.alley.base.kit.Kit;
import dev.revere.alley.config.locale.impl.KitLocale;
import dev.revere.alley.util.InventoryUtil;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author Emmy
 * @project Alley
 * @date 28/04/2024 - 22:23
 */
public class KitSetInvCommand extends BaseCommand {
    @CommandData(name = "kit.setinventory", aliases = "kit.setinv", permission = "practice.admin")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&6Usage: &e/kit setinventory &6<kitName>"));
            return;
        }

        KitService kitService = this.plugin.getService(KitService.class);
        Kit kit = kitService.getKit(args[0]);
        if (kit == null) {
            player.sendMessage(CC.translate(KitLocale.KIT_NOT_FOUND.getMessage()));
            return;
        }

        ItemStack[] inventory = InventoryUtil.cloneItemStackArray(player.getInventory().getContents());
        ItemStack[] armor = InventoryUtil.cloneItemStackArray(player.getInventory().getArmorContents());

        kit.setItems(inventory);
        kit.setArmor(armor);
        kitService.saveKit(kit);
        player.sendMessage(CC.translate(KitLocale.KIT_INVENTORY_SET.getMessage().replace("{kit-name}", kit.getName())));
    }
}