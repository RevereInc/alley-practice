package dev.revere.alley.feature.cosmetic.menu.button;

import dev.revere.alley.Alley;
import dev.revere.alley.api.menu.Button;
import dev.revere.alley.feature.cosmetic.impl.killeffect.AbstractKillEffect;
import dev.revere.alley.feature.cosmetic.impl.soundeffect.AbstractSoundEffect;
import dev.revere.alley.feature.cosmetic.interfaces.ICosmetic;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.tool.item.ItemBuilder;
import dev.revere.alley.util.chat.CC;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * @author Remi
 * @project Alley
 * @date 5/26/2024
 */
@AllArgsConstructor
public class CosmeticButton extends Button {
    protected final Alley plugin = Alley.getInstance();
    private final ICosmetic cosmetic;

    @Override
    public ItemStack getButtonItem(Player player) {
        Profile profile = this.plugin.getProfileService().getProfile(player.getUniqueId());
        boolean hasPermission = player.hasPermission(cosmetic.getPermission());
        boolean isSelected = profile.getProfileData().getCosmeticData().isSelectedCosmetic(cosmetic);

        String lore;
        if (hasPermission) {
            lore = isSelected ? "&cYou already have this cosmetic selected." : "&aClick to select this cosmetic.";
        } else {
            lore = "&c&lYou do not have permission to select this cosmetic.";
        }

        return new ItemBuilder(cosmetic.getIcon())
                .name("&b&l" + cosmetic.getName())
                .lore(
                        "",
                        "&f● &bDescription: &7" + cosmetic.getDescription(),
                        "",
                        lore

                )
                .build();
    }

    /**
     * Handles the click event for the button.
     *
     * @param player    the player who clicked the button
     * @param clickType the type of click
     */
    @Override
    public void clicked(Player player, ClickType clickType) {
        if (clickType == ClickType.MIDDLE || clickType == ClickType.RIGHT || clickType == ClickType.NUMBER_KEY || clickType == ClickType.DROP || clickType == ClickType.SHIFT_LEFT || clickType == ClickType.SHIFT_RIGHT) {
            return;
        }
        this.playNeutral(player);

        Profile profile = this.plugin.getProfileService().getProfile(player.getUniqueId());
        if (profile.getProfileData().getCosmeticData().isSelectedCosmetic(cosmetic)) {
            player.sendMessage(CC.translate("&cYou already have this cosmetic selected."));
            return;
        }

        if (!player.hasPermission(cosmetic.getPermission())) {
            player.sendMessage(CC.translate("&cYou do not have permission to select this cosmetic."));
            return;
        }

        if (cosmetic instanceof AbstractKillEffect) {
            profile.getProfileData().getCosmeticData().setSelectedKillEffect(cosmetic.getName());
        } else if (cosmetic instanceof AbstractSoundEffect) {
            profile.getProfileData().getCosmeticData().setSelectedSoundEffect(cosmetic.getName());
        }

        player.sendMessage(CC.translate("&aYou have successfully selected the &b" + cosmetic.getName() + " &acosmetic."));
    }
}
