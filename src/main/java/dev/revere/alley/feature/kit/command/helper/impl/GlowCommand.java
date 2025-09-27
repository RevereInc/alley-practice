package dev.revere.alley.feature.kit.command.helper.impl;

import dev.revere.alley.common.reflect.utility.ReflectionUtility;
import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
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
            command.sendUsage();
            return;
        }

        ItemStack item = player.getItemInHand();
        if (item == null || item.getType() == Material.AIR) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ERROR_YOU_MUST_HOLD_ITEM));
            return;
        }

        boolean glow = Boolean.parseBoolean(args[0]);

        ItemStack result = ReflectionUtility.setGlowing(item, glow);
        player.setItemInHand(result);

        player.sendMessage(CC.translate("&aGlow has been &6" + (glow ? "enabled" : "disabled") + "&a for your item."));
    }
}
