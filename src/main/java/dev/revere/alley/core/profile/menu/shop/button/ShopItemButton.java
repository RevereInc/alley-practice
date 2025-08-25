package dev.revere.alley.core.profile.menu.shop.button;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.common.item.ItemBuilder;
import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.config.ConfigService;
import dev.revere.alley.core.profile.Profile;
import dev.revere.alley.core.profile.ProfileService;
import dev.revere.alley.feature.cosmetic.model.BaseCosmetic;
import dev.revere.alley.library.menu.Button;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
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
public class ShopItemButton extends Button {
    private final BaseCosmetic cosmetic;

    @Override
    public ItemStack getButtonItem(Player player) {
        boolean hasPermission = player.hasPermission(cosmetic.getPermission());

        List<String> lore = new ArrayList<>();
        lore.add(CC.MENU_BAR);
        lore.addAll(cosmetic.getDisplayLore());
        lore.add("");

        if (hasPermission) {
            lore.add("&aYou already own this item.");
        } else {
            lore.add(" &fPrice: &6$" + cosmetic.getPrice());
            lore.add("");
            lore.add("&aClick to purchase.");
        }
        lore.add(CC.MENU_BAR);

        return new ItemBuilder(cosmetic.getIcon())
                .name("&6&l" + cosmetic.getName())
                .lore(lore)
                .hideMeta()
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        Profile profile = AlleyPlugin.getInstance().getService(ProfileService.class).getProfile(player.getUniqueId());

        if (player.hasPermission(cosmetic.getPermission())) {
            player.sendMessage(CC.translate("&cYou already own this cosmetic."));
            this.playFail(player);
            return;
        }

        if (profile.getProfileData().getCoins() < cosmetic.getPrice()) {
            player.sendMessage(CC.translate("&cYou don't have enough coins to purchase this."));
            this.playFail(player);
            return;
        }

        profile.getProfileData().setCoins(profile.getProfileData().getCoins() - cosmetic.getPrice());

        FileConfiguration config = AlleyPlugin.getInstance().getService(ConfigService.class).getSettingsConfig();
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), config.get("command.grant-cosmetic-permission-command").toString().replace("{player}", player.getName()).replace("%permission%", cosmetic.getPermission()));

        player.sendMessage(CC.translate("&aSuccessfully purchased the &6" + cosmetic.getName() + " &acosmetic!"));
        this.playSuccess(player);
    }
}