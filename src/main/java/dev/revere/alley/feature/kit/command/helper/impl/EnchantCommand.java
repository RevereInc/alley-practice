package dev.revere.alley.feature.kit.command.helper.impl;

import dev.revere.alley.common.item.EnchantUtil;
import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author Emmy
 * @project Alley
 * @date 28/05/2024 - 20:28
 */
public class EnchantCommand extends BaseCommand {
    @CommandData(
            name = "enchant",
            isAdminOnly = true,
            usage = "enchant <enchantment> <level>",
            description = "Enchant the item in your hand with the specified enchantment and level"
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            command.sendUsage();
            return;
        }

        Enchantment enchantment = EnchantUtil.getEnchantment(args[0]);
        if (enchantment == null) {
            player.sendMessage(CC.translate("&cInvalid enchantment name!"));
            player.sendMessage(CC.translate("&cValid enchantments: &7" + EnchantUtil.getSortedEnchantments().toUpperCase()));
            return;
        }

        int level;
        try {
            level = Integer.parseInt(args[1]);
        } catch (NumberFormatException exception) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ERROR_INVALID_NUMBER).replace("{input}", args[1]));
            return;
        }

        ItemStack itemInHand = player.getInventory().getItemInHand();
        if (itemInHand == null || itemInHand.getType() == Material.AIR) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ERROR_YOU_MUST_HOLD_ITEM));
            return;
        }

        String displayName = itemInHand.getItemMeta().getDisplayName() == null ? itemInHand.getType().name() : itemInHand.getItemMeta().getDisplayName();

        itemInHand.addUnsafeEnchantment(enchantment, level);
        player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ITEM_ENCHANTED)
                .replace("{enchantment}", enchantment.getName())
                .replace("{level}", String.valueOf(level))
                .replace("{item-name}", displayName));
    }
}