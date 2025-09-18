package dev.revere.alley.feature.arena.listener;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.locale.LocaleService;
import dev.revere.alley.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import dev.revere.alley.feature.arena.selection.ArenaSelection;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/**
 * @author Remi
 * @project Alley
 * @date 5/20/2024
 */
public class ArenaListener implements Listener {
    @EventHandler(priority = EventPriority.LOW)
    private void onPlayerInteractEvent(PlayerInteractEvent event) {
        if (event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            ItemStack itemStack = event.getItem();
            if (itemStack != null && itemStack.equals(ArenaSelection.SELECTION_TOOL)) {
                Player player = event.getPlayer();

                Block clickedBlock = event.getClickedBlock();
                int locationType = 0;

                ArenaSelection arenaSelection = ArenaSelection.createSelection(player);

                if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    arenaSelection.setMaximum(clickedBlock.getLocation());
                    locationType = 2;
                } else if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                    arenaSelection.setMinimum(clickedBlock.getLocation());
                    locationType = 1;
                }

                event.setCancelled(true);
                event.setUseItemInHand(PlayerInteractEvent.Result.DENY);
                event.setUseInteractedBlock(PlayerInteractEvent.Result.DENY);

                int getBlockX = clickedBlock.getLocation().getBlockX();
                int getBlockY = clickedBlock.getLocation().getBlockY();
                int getBlockZ = clickedBlock.getLocation().getBlockZ();

                player.sendMessage(CC.translate(AlleyPlugin.getInstance().getService(LocaleService.class).getMessage(GlobalMessagesLocaleImpl.ARENA_SELECTED_BOUNDARY)
                        .replace("{boundary-type}", locationType == 1 ? "minimum" : "maximum")
                        .replace("{x}", String.valueOf(getBlockX))
                        .replace("{y}", String.valueOf(getBlockY))
                        .replace("{z}", String.valueOf(getBlockZ))
                ));
            }
        }
    }
}