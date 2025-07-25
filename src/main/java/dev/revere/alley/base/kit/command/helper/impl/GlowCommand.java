package dev.revere.alley.base.kit.command.helper.impl;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.tool.reflection.utility.ReflectionUtility;
import dev.revere.alley.util.chat.CC;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author Emmy
 * @project Alley
 * @date 14/07/2025
 */
public class GlowCommand extends BaseCommand {
    @CommandData(
            name = "glow",
            description = "Toggles the enchantment glow on the item in your hand",
            usage = "glow <true|false>",
            isAdminOnly = true
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&6Usage: &e/glow &6<true|false>"));
            return;
        }

        ItemStack item = player.getItemInHand();
        if (item == null || item.getType() == Material.AIR) {
            player.sendMessage(CC.translate("&cYou must be holding an item to modify glow state."));
            return;
        }

        boolean glow = Boolean.parseBoolean(args[0]);

        ItemStack result = ReflectionUtility.setGlowing(item, glow);
        player.setItemInHand(result);

        player.sendMessage(CC.translate("&aGlow has been &6" + (glow ? "enabled" : "disabled") + "&a for your item."));
    }
}
