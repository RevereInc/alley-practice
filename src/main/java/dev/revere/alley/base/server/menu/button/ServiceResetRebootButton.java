package dev.revere.alley.base.server.menu.button;

import dev.revere.alley.Alley;
import dev.revere.alley.api.menu.Button;
import dev.revere.alley.base.server.ServerService;
import dev.revere.alley.tool.item.ItemBuilder;
import dev.revere.alley.util.chat.CC;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

/**
 * @author Emmy
 * @project Alley
 * @since 09/03/2025
 */
public class ServiceResetRebootButton extends Button {

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(Material.EMERALD)
                .name("&a&lUnprepare Reboot")
                .lore(
                        "&fThis will undo the preparation",
                        "&ffor a reboot by allowing",
                        "&fqueueing again.",
                        "",
                        "&aClick to allow queueing again!"
                )
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (clickType != ClickType.LEFT) return;

        if (!player.isOp()) {
            player.sendMessage(CC.translate("&cYou do not have permission to use this button."));
            return;
        }

        Alley.getInstance().getService(ServerService.class).setQueueingAllowed(true);
        Arrays.asList(
                "",
                "&a&lQUEUEING IS NO LONGER DISABLED!",
                ""
        ).forEach(line -> Bukkit.broadcastMessage(CC.translate(line)));
    }
}
