package dev.revere.alley.feature.cosmetic.menu.button;

import dev.revere.alley.Alley;
import dev.revere.alley.api.menu.Button;
import dev.revere.alley.feature.cosmetic.BaseCosmetic;
import dev.revere.alley.profile.ProfileService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.data.impl.ProfileCosmeticData;
import dev.revere.alley.tool.item.ItemBuilder;
import dev.revere.alley.util.chat.CC;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class CosmeticButton extends Button {
    protected final Alley plugin = Alley.getInstance();
    private final BaseCosmetic cosmetic;

    @Override
    public ItemStack getButtonItem(Player player) {
        ProfileService profileService = Alley.getInstance().getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());
        boolean isSelected = profile.getProfileData().getCosmeticData().isSelected(cosmetic);
        boolean hasPermission = player.hasPermission(cosmetic.getPermission());

        List<String> lore = new ArrayList<>();
        lore.add(CC.MENU_BAR);
        lore.addAll(cosmetic.getDisplayLore());
        lore.add("");
        if (hasPermission) {
            lore.add(isSelected ? "&eSelected." : "&aClick to select.");
        } else {
            lore.add("&cYou do not own this cosmetic.");
        }
        lore.add(CC.MENU_BAR);

        return new ItemBuilder(cosmetic.getIcon())
                .name("&6&l" + cosmetic.getName())
                .lore(lore)
                .glow(isSelected)
                .hideMeta()
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (clickType == ClickType.MIDDLE || clickType == ClickType.RIGHT || clickType == ClickType.NUMBER_KEY || clickType == ClickType.DROP || clickType == ClickType.SHIFT_LEFT || clickType == ClickType.SHIFT_RIGHT) {
            return;
        }

        ProfileService profileService = Alley.getInstance().getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());
        ProfileCosmeticData cosmeticData = profile.getProfileData().getCosmeticData();

        if (cosmeticData.isSelected(cosmetic)) {
            player.sendMessage(CC.translate("&cYou already have this cosmetic selected."));
            this.playFail(player);
            return;
        }

        if (!player.hasPermission(cosmetic.getPermission())) {
            player.sendMessage(CC.translate("&cYou do not have permission to use this cosmetic."));
            this.playFail(player);
            return;
        }

        cosmeticData.setSelected(cosmetic);

        this.playSuccess(player);
        player.sendMessage(CC.translate("&aYou have selected the &6" + cosmetic.getName() + " &acosmetic."));
    }
}