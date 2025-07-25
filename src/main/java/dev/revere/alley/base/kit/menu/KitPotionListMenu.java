package dev.revere.alley.base.kit.menu;

import dev.revere.alley.Alley;
import dev.revere.alley.api.menu.Button;
import dev.revere.alley.api.menu.pagination.PaginatedMenu;
import dev.revere.alley.base.kit.KitService;
import dev.revere.alley.base.kit.Kit;
import dev.revere.alley.tool.item.ItemBuilder;
import dev.revere.alley.util.chat.CC;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Emmy
 * @project Alley
 * @since 25/06/2025
 */
@AllArgsConstructor
public class KitPotionListMenu extends PaginatedMenu {
    private final Kit kit;

    @Override
    public String getPrePaginatedTitle(Player player) {
        return "&6&lPotions for " + this.kit.getDisplayName();
    }

    @Override
    public Map<Integer, Button> getGlobalButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        this.addGlassHeader(buttons, 15);

        return buttons;
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        List<PotionEffect> potionEffects = this.kit.getPotionEffects();
        int index = 0;

        for (PotionEffect potionEffect : potionEffects) {
            buttons.put(index++, new KitPotionButton(this.kit, potionEffect));
        }

        return buttons;
    }

    @AllArgsConstructor
    private static class KitPotionButton extends Button {
        private final Kit kit;
        private final PotionEffect potionEffect;

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.POTION)
                    .name("&6" + this.potionEffect.getType().getName())
                    .lore(
                            "&7Duration: &6" + this.potionEffect.getDuration() / 20 + " seconds",
                            "&7Amplifier: &6" + this.potionEffect.getAmplifier(),
                            "",
                            "&7Click to remove this potion effect."
                    )
                    .hideMeta()
                    .build();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            if (clickType != ClickType.LEFT) return;

            this.kit.getPotionEffects().remove(this.potionEffect);
            Alley.getInstance().getService(KitService.class).saveKit(this.kit);
            player.sendMessage(CC.translate("&cYou have removed the potion effect: &6" + this.potionEffect.getType().getName() + "&c from the kit: &6" + this.kit.getDisplayName() + "&c."));
            new KitPotionListMenu(this.kit).openMenu(player);
        }
    }
}