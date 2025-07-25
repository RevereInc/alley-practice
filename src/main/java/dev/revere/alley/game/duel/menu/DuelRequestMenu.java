package dev.revere.alley.game.duel.menu;

import dev.revere.alley.Alley;
import dev.revere.alley.api.menu.Button;
import dev.revere.alley.api.menu.Menu;
import dev.revere.alley.base.kit.Kit;
import dev.revere.alley.base.queue.QueueService;
import dev.revere.alley.base.queue.Queue;
import dev.revere.alley.game.duel.DuelRequestService;
import dev.revere.alley.tool.item.ItemBuilder;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Emmy
 * @project Alley
 * @date 17/10/2024 - 20:11
 */
@AllArgsConstructor
public class DuelRequestMenu extends Menu {
    protected final Alley plugin = Alley.getInstance();
    private final Player targetPlayer;

    @Override
    public String getTitle(Player player) {
        return "&6&lDuel " + this.targetPlayer.getName();
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        int slot = 10;
        for (Queue queue : Alley.getInstance().getService(QueueService.class).getQueues()) {
            if (!queue.isRanked() && !queue.isDuos() && queue.getKit().isEnabled()) {
                slot = this.skipIfSlotCrossingBorder(slot);
                buttons.put(slot++, new DuelButton(this.targetPlayer, queue.getKit()));
            }
        }

        this.addGlass(buttons, 15);

        return buttons;
    }

    @Override
    public int getSize() {
        return 9 * 6;
    }

    @AllArgsConstructor
    private static class DuelButton extends Button {
        protected final Alley plugin = Alley.getInstance();
        private Player targetPlayer;
        private Kit kit;

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(this.kit.getIcon())
                    .name(this.kit.getMenuTitle())
                    .lore(
                            "",
                            "&aClick to select!"
                    )
                    .durability(this.kit.getDurability())
                    .hideMeta()
                    .build();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            if (clickType != ClickType.LEFT) return;

            if (player.hasPermission("alley.duel.arena.selector")) {
                new DuelArenaSelectorMenu(this.targetPlayer, this.kit).openMenu(player);
                return;
            }

            player.closeInventory();

            Alley.getInstance().getService(DuelRequestService.class).createAndSendRequest(player, this.targetPlayer, this.kit, null);
        }
    }
}