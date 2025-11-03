package dev.revere.alley.feature.hotbar.command.impl.data;

import dev.revere.alley.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import dev.revere.alley.feature.hotbar.HotbarItem;
import dev.revere.alley.feature.hotbar.HotbarService;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author Hamza
 * @since 02/11/2025
 */
public class HotbarMaterialCommand extends BaseCommand {
    @CommandData(
            name = "hotbar.material",
            isAdminOnly = true,
            usage = "hotbar material <name>",
            description = "Set the material of a hotbar item."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            command.sendUsage();
            return;
        }

        HotbarService hotbarService = this.plugin.getService(HotbarService.class);

        String name = args[0];

        HotbarItem hotbarItem = hotbarService.getHotbarItem(name);
        if (hotbarItem == null) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.HOTBAR_NOT_FOUND).replace("{hotbar-name}", name));
            return;
        }

        ItemStack itemInHand = player.getInventory().getItemInHand();
        hotbarItem.setMaterial(itemInHand.getType());
        hotbarItem.setDurability(itemInHand.getDurability());
        hotbarService.saveToConfig(hotbarItem);
        player.sendMessage(this.getString(GlobalMessagesLocaleImpl.HOTBAR_SET_MATERIAL).replace("{hotbar-name}", name).replace("{material}", itemInHand.getType().name() + ":" + itemInHand.getDurability()));
    }
}