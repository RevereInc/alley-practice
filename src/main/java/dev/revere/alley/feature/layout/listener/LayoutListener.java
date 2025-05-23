package dev.revere.alley.feature.layout.listener;

import dev.revere.alley.Alley;
import dev.revere.alley.feature.layout.data.LayoutData;
import dev.revere.alley.util.chat.CC;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @since 04/05/2025
 */
public class LayoutListener implements Listener {
    protected final Alley plugin;

    /**
     * Constructor for the LayoutListener class.
     *
     * @param plugin The Alley plugin instance.
     */
    public LayoutListener(Alley plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item == null || item.getType() != Material.BOOK) return;
        if (!item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) return;

        String clickedName = ChatColor.stripColor(item.getItemMeta().getDisplayName());

        for (List<LayoutData> layoutList : this.plugin.getProfileService()
                .getProfile(player.getUniqueId())
                .getProfileData().getLayoutData().getLayouts().values()) {

            for (LayoutData layout : layoutList) {
                if (ChatColor.stripColor(layout.getDisplayName()).equalsIgnoreCase(clickedName)) {
                    player.getInventory().setContents(layout.getItems());
                    player.sendMessage(CC.translate("&aYou have selected the layout &b" + layout.getDisplayName() + "&a."));
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }
}