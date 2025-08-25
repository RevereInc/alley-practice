package dev.revere.alley.core.profile.menu.shop.button;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.common.item.ItemBuilder;
import dev.revere.alley.common.text.CC;
import dev.revere.alley.common.text.StringUtil;
import dev.revere.alley.core.profile.ProfileService;
import dev.revere.alley.core.profile.menu.shop.ShopCategoryMenu;
import dev.revere.alley.feature.cosmetic.CosmeticService;
import dev.revere.alley.feature.cosmetic.internal.repository.BaseCosmeticRepository;
import dev.revere.alley.feature.cosmetic.model.CosmeticType;
import dev.revere.alley.library.menu.Button;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Remi
 * @project Alley
 * @date 6/23/2025
 */
@RequiredArgsConstructor
public class ShopCategoryButton extends Button {
    private final CosmeticType cosmeticType;
    private final Material icon;

    @Override
    public ItemStack getButtonItem(Player player) {
        String friendlyName = StringUtil.formatEnumName(cosmeticType);

        int totalCount = 0;
        int ownedCount = 0;
        int balance = AlleyPlugin.getInstance().getService(ProfileService.class).getProfile(player.getUniqueId()).getProfileData().getCoins();

        BaseCosmeticRepository<?> repository = AlleyPlugin.getInstance().getService(CosmeticService.class).getRepository(cosmeticType);
        if (repository != null) {
            totalCount = repository.getCosmetics().size();
            ownedCount = (int) repository.getCosmetics().stream()
                    .filter(c -> player.hasPermission(c.getPermission()))
                    .count();
        }

        int percentage = (totalCount == 0) ? 0 : (int) (((double) ownedCount / totalCount) * 100);

        String description = cosmeticType.getDescription();

        List<String> lore = new ArrayList<>();
        lore.add(CC.MENU_BAR);
        lore.add(String.format("&7%s", description));
        lore.add("");
        lore.add(String.format("&6│ &fUnlocked: &6%d/%d &7(%d%%)", ownedCount, totalCount, percentage));
        lore.add(String.format("&6│ &fBalance: &6$%d", balance));
        lore.add("");
        lore.add("&aClick to view.");
        lore.add(CC.MENU_BAR);

        return new ItemBuilder(this.icon)
                .name("&6&l" + friendlyName + "s")
                .lore(lore)
                .hideMeta()
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        this.playNeutral(player);
        new ShopCategoryMenu(cosmeticType).openMenu(player);
    }
}