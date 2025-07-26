package dev.revere.alley.profile.menu.shop;

import dev.revere.alley.Alley;
import dev.revere.alley.api.menu.Button;
import dev.revere.alley.api.menu.Menu;
import dev.revere.alley.api.menu.impl.BackButton;
import dev.revere.alley.feature.cosmetic.CosmeticType;
import dev.revere.alley.feature.cosmetic.BaseCosmeticRepository;
import dev.revere.alley.feature.cosmetic.CosmeticService;
import dev.revere.alley.profile.menu.shop.button.ShopItemButton;
import dev.revere.alley.util.StringUtil;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Remi
 * @project Alley
 * @date 6/23/2025
 */
@RequiredArgsConstructor
public class ShopCategoryMenu extends Menu {

    private final CosmeticType cosmeticType;

    @Override
    public String getTitle(Player player) {
        return "&6&lShop - " + StringUtil.formatEnumName(cosmeticType) + "s";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        buttons.put(0, new BackButton(new ShopMenu()));

        BaseCosmeticRepository<?> repository = Alley.getInstance().getService(CosmeticService.class).getRepository(cosmeticType);
        if (repository != null) {
            repository.getCosmetics().stream()
                    .filter(cosmetic -> cosmetic.getIcon() != null && cosmetic.getPrice() > 0)
                    .forEach(cosmetic -> buttons.put(cosmetic.getSlot(), new ShopItemButton(cosmetic)));
        }

        this.addBorder(buttons, 15, 5);

        return buttons;
    }
}