package dev.revere.alley.feature.cosmetic.menu;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.common.text.StringUtil;
import dev.revere.alley.feature.cosmetic.CosmeticService;
import dev.revere.alley.feature.cosmetic.internal.repository.BaseCosmeticRepository;
import dev.revere.alley.feature.cosmetic.menu.button.CosmeticButton;
import dev.revere.alley.feature.cosmetic.model.CosmeticType;
import dev.revere.alley.library.menu.Button;
import dev.revere.alley.library.menu.Menu;
import dev.revere.alley.library.menu.impl.BackButton;
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
public class CosmeticTypeMenu extends Menu {

    private final CosmeticType cosmeticType;

    @Override
    public String getTitle(Player player) {
        String friendlyName = StringUtil.formatEnumName(cosmeticType);
        return "&6&l" + friendlyName;
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        final Map<Integer, Button> buttons = new HashMap<>();
        buttons.put(0, new BackButton(new CosmeticsMenu()));

        BaseCosmeticRepository<?> repository = AlleyPlugin.getInstance().getService(CosmeticService.class).getRepository(this.cosmeticType);
        if (repository != null) {
            repository.getCosmetics().stream()
                    .filter(cosmetic -> cosmetic.getIcon() != null)
                    .forEach(cosmetic -> buttons.put(cosmetic.getSlot(), new CosmeticButton(cosmetic)));
        }

        this.addBorder(buttons, 15, 5);
        return buttons;
    }
}