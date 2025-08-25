package dev.revere.alley.feature.layout.internal;

import dev.revere.alley.bootstrap.AlleyContext;
import dev.revere.alley.bootstrap.annotation.Service;
import dev.revere.alley.common.item.ItemBuilder;
import dev.revere.alley.common.logger.Logger;
import dev.revere.alley.core.config.ConfigService;
import dev.revere.alley.core.profile.Profile;
import dev.revere.alley.core.profile.ProfileService;
import dev.revere.alley.feature.kit.KitCategory;
import dev.revere.alley.feature.layout.LayoutService;
import dev.revere.alley.feature.layout.data.LayoutData;
import dev.revere.alley.feature.layout.menu.LayoutMenu;
import dev.revere.alley.library.menu.Menu;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @since 03/05/2025
 */
@Getter
@Service(provides = LayoutService.class, priority = 350)
public class LayoutServiceImpl implements LayoutService {
    private final ConfigService configService;
    private final ProfileService profileService;

    private Menu layoutMenu;

    /**
     * DI Constructor for the LayoutServiceImpl class.
     *
     * @param configService  The ConfigService instance.
     * @param profileService The ProfileService instance.
     */
    public LayoutServiceImpl(ConfigService configService, ProfileService profileService) {
        this.configService = configService;
        this.profileService = profileService;
    }

    @Override
    public void initialize(AlleyContext context) {
        this.layoutMenu = this.determineMenu();
    }

    private Menu determineMenu() {
        FileConfiguration config = this.configService.getMenusConfig();
        String menuType = config.getString("layout-menu.type", "DEFAULT");

        switch (menuType) {
            case "MODERN":
                Logger.error("Modern layout menu is not implemented yet. Defaulting to classic layout menu.");
                return new LayoutMenu(KitCategory.NORMAL);
            case "DEFAULT":
                return new LayoutMenu(KitCategory.NORMAL);
        }

        Logger.error("Invalid layout menu type specified in config.yml. Defaulting to modern layout menu.");
        return new LayoutMenu(KitCategory.NORMAL);
    }

    @Override
    public ItemStack getLayoutBook(LayoutData layout) {
        return new ItemBuilder(Material.BOOK)
                .name(layout.getDisplayName())
                .lore("&7Click to select this layout.")
                .hideMeta().build();
    }

    @Override
    public void giveBooks(Player player, String kitName) {
        Profile profile = this.profileService.getProfile(player.getUniqueId());
        if (profile == null) return;

        List<LayoutData> layouts = profile.getProfileData().getLayoutData().getLayouts().get(kitName);
        if (layouts == null) return;

        layouts.forEach(layout -> player.getInventory().addItem(this.getLayoutBook(layout)));
    }
}