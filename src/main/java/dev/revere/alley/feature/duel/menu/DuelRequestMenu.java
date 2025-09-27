package dev.revere.alley.feature.duel.menu;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.common.item.ItemBuilder;
import dev.revere.alley.core.locale.LocaleService;
import dev.revere.alley.core.locale.internal.impl.SettingsLocaleImpl;
import dev.revere.alley.feature.duel.DuelRequestService;
import dev.revere.alley.feature.kit.Kit;
import dev.revere.alley.feature.queue.Queue;
import dev.revere.alley.feature.queue.QueueService;
import dev.revere.alley.library.menu.Button;
import dev.revere.alley.library.menu.Menu;
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
    protected final AlleyPlugin plugin = AlleyPlugin.getInstance();
    private final Player targetPlayer;

    @Override
    public String getTitle(Player player) {
        return "&6&lDuel " + this.targetPlayer.getName();
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        int slot = 10;
        for (Queue queue : AlleyPlugin.getInstance().getService(QueueService.class).getQueues()) {
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
        protected final AlleyPlugin plugin = AlleyPlugin.getInstance();
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

            if (player.hasPermission(this.plugin.getService(LocaleService.class).getString(SettingsLocaleImpl.PERMISSION_DONATOR_DUEL_ARENA_SELECTOR))) {
                new DuelArenaSelectorMenu(this.targetPlayer, this.kit).openMenu(player);
                return;
            }

            player.closeInventory();

            AlleyPlugin.getInstance().getService(DuelRequestService.class).createAndSendRequest(player, this.targetPlayer, this.kit, null);
        }
    }
}