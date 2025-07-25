package dev.revere.alley.command.impl.other;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.util.chat.CC;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

/**
 * @author Emmy
 * @project Alley
 * @date 28/10/2024 - 08:47
 */
public class RefillCommand extends BaseCommand {
    @CommandData(name = "refill", permission = "alley.command.refill", usage = "refill", description = "Refill your inventory with health potions")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        Arrays.stream(player.getInventory().getContents()).forEach(item -> {
            if (item == null) {
                player.getInventory().addItem(new ItemStack(Material.POTION, 1, (short) 16421));
            }
        });

        player.sendMessage(CC.translate("&aYou've refilled &6your inventory &awith &6health &apotions."));
    }
}