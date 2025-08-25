package dev.revere.alley.feature.kit.command.helper.impl;

import dev.revere.alley.common.item.EnchantUtil;
import dev.revere.alley.common.text.CC;
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
    @CommandData(name = "enchant", permission = "alley.command.enchant")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            player.sendMessage(CC.translate("&cUsage: /enchant (enchantment) (level)"));
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
            player.sendMessage(CC.translate("&cEnchantment level must be a number!"));
            return;
        }

        ItemStack itemInHand = player.getInventory().getItemInHand();
        if (itemInHand == null || itemInHand.getType() == Material.AIR) {
            player.sendMessage(CC.translate("&cYou must be holding an item to enchant!"));
            return;
        }

        String displayName = itemInHand.getItemMeta().getDisplayName() == null ? itemInHand.getType().name() : itemInHand.getItemMeta().getDisplayName();

        itemInHand.addUnsafeEnchantment(enchantment, level);
        player.sendMessage(CC.translate("&aSuccessfully enchanted the &6" + displayName + " &aitem with &6" + enchantment.getName() + " &alevel &6" + level + "&a!"));
    }
}