package dev.revere.alley.feature.main.menu;

import dev.revere.alley.common.constants.PluginConstant;
import dev.revere.alley.common.reflect.ReflectionService;
import dev.revere.alley.common.reflect.internal.types.BookReflectionServiceImpl;
import dev.revere.alley.common.text.CC;
import dev.revere.alley.common.text.Symbol;
import dev.revere.alley.core.config.ConfigService;
import dev.revere.alley.feature.main.menu.button.AlleyMainPanelFunctionButton;
import dev.revere.alley.library.menu.Button;
import dev.revere.alley.library.menu.Menu;
import dev.revere.alley.library.menu.impl.LoreButton;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @author Emmy
 * @project alley-practice
 * @since 28/09/2025
 */
public class AlleyMainPanelMenu extends Menu {
    private final String name;

    public AlleyMainPanelMenu() {
        this.name = this.plugin.getService(PluginConstant.class).getName();
    }

    @Override
    public String getTitle(Player player) {
        return "&8" + this.name + " Panel";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        List<LinkButtonData> linkButtons = Arrays.asList(
                new LinkButtonData(1, 7, "&f&lGithub Repository", "https://github.com/hmEmmy/alley-practice", Arrays.asList(
                        CC.MENU_BAR,
                        "&fVisit the GitHub repository",
                        "&ffor the latest updates,",
                        "&fsource code, and more.",
                        "",
                        "&aClick to visit!",
                        CC.MENU_BAR
                )),
                new LinkButtonData(3, 4, "&9&lDiscord Server", "https://discord.com/invite/eT4B65k5E4", Arrays.asList(
                        CC.MENU_BAR,
                        "&fJoin our Discord server",
                        "&ffor support, updates,",
                        "&fcommunity events, and more.",
                        "",
                        "&aClick to join!",
                        CC.MENU_BAR
                )),
                new LinkButtonData(5, 12, "&b&lBuiltByBit Resource Page", "https://builtbybit.com/resources/alley-next-generation-practice-core.73088/", Arrays.asList(
                        CC.MENU_BAR,
                        "&fVisit the BuiltByBit resource",
                        "&fpage for reviews, ratings,",
                        "&fand more.",
                        "",
                        "&aClick to visit!",
                        CC.MENU_BAR
                )),
                new LinkButtonData(7, 11, "&e&lSpigotMC Resource Page", "https://www.spigotmc.org/resources/alley-next-generation-practice-core.127500/", Arrays.asList(
                        CC.MENU_BAR,
                        "&fVisit the SpigotMC resource",
                        "&fpage for reviews, ratings,",
                        "&fand more.",
                        "",
                        "&aClick to visit!",
                        CC.MENU_BAR
                ))
        );

        linkButtons.forEach(data -> buttons.put(data.slot, createLinkButton(data)));

        buttons.put(13, new LoreButton(
                Material.ENCHANTED_BOOK,
                "&4&l" + Symbol.HEART + " &6&lTHANK YOU DEAR USER &4&l" + Symbol.HEART,
                Arrays.asList(
                        "",
                        "&6&lAbout " + this.name,
                        "&6&l│ &fCreated by: &6Emmy &7(github.com/hmEmmy)",
                        "&6&l│ &fMaintained by: &6Revere Inc. &7(github.com/RevereInc)",
                        "&6&l│ &fPrimary Contributors: &6" + this.plugin.getDescription().getAuthors().toString().replace("[", "").replace("]", "").replace(",", "&7,&6"),
                        "&6&l│ &fVersion: &6" + this.plugin.getDescription().getVersion(),
                        "&6&l│ &fLicense: &6CC BY-NC-SA 4.0",
                        "&6&l│ &fWebsite: &6https://revere.dev",
                        "",
                        "&fStar the repository or sponsor our organization to",
                        "&fsupport this &a&lFREE &fand &a&lOPEN-SOURCE &fproject.",
                        "",
                        "&6&l│ &fSponsors: &6https://github.com/sponsors/RevereInc",
                        "&6&l│ &fGitHub: &6https://github.com/RevereInc/alley-practice",
                        "",
                        "&aWe appreciate your interest in &6&l" + this.name + "&a!"
                )
        ));

        List<ManagerButtonData> managerButtons = Arrays.asList(
                new ManagerButtonData(28, Material.PAPER, "&6&lReload Config Files", Arrays.asList(
                        CC.MENU_BAR,
                        "&fReload all configuration files,",
                        "",
                        "&aClick to reload!",
                        CC.MENU_BAR
                ), p -> {
                    player.sendMessage(CC.translate("&6&lAlley &freloading..."));
                    this.plugin.getService(ConfigService.class).reloadConfigs();
                    player.sendMessage(CC.translate("&6&lAlley &a&lreloaded&f."));
                    p.closeInventory();
                    return null;
                }),
                new ManagerButtonData(29, Material.COMPASS, "&6&lMatch Manager", Arrays.asList(
                        CC.MENU_BAR,
                        "&fModerate and manage ongoing matches.",
                        "&fCancel, view detailed info, and more.",
                        "",
                        "&aClick to manage!",
                        CC.MENU_BAR
                ), p -> null),
                new ManagerButtonData(30, Material.ANVIL, "&6&lArena Manager", Arrays.asList(
                        CC.MENU_BAR,
                        "&fCreate, edit, and manage arenas.",
                        "&fSet spawn points, and more.",
                        "",
                        "&aClick to manage!",
                        CC.MENU_BAR
                ), p -> null),
                new ManagerButtonData(31, Material.CHEST, "&6&lKit Manager", Arrays.asList(
                        CC.MENU_BAR,
                        "&fCreate, edit, and manage kits.",
                        "&fDefine loadouts for players.",
                        "",
                        "&aClick to manage!",
                        CC.MENU_BAR
                ), p -> null),
                new ManagerButtonData(32, Material.ARMOR_STAND, "&6&lDetailed Hook Info", Arrays.asList(
                        CC.MENU_BAR,
                        "&fView detailed information about",
                        "&fthe plugin's hooks and integrations.",
                        "",
                        "&aClick to view!",
                        CC.MENU_BAR
                ), p -> null),
                new ManagerButtonData(33, Material.BOOKSHELF, "&6&lView Documentation", Arrays.asList(
                        CC.MENU_BAR,
                        "&fAccess the official documentation",
                        "&ffor setup guides, API references,",
                        "&fand more.",
                        "",
                        "&aClick to view!",
                        CC.MENU_BAR
                ), p -> null)
        );

        managerButtons.forEach(data -> buttons.put(data.slot, new AlleyMainPanelFunctionButton(data.material, 0, data.title, data.description, data.action)));

        this.addBorder(buttons, 15, 6);
        return buttons;
    }

    @Override
    public int getSize() {
        return 9 * 6;
    }

    private AlleyMainPanelFunctionButton createLinkButton(LinkButtonData data) {
        return new AlleyMainPanelFunctionButton(
                Material.INK_SACK,
                data.inkSackColor,
                data.title,
                data.description,
                (p) -> {
                    p.closeInventory();
                    ReflectionService reflectionService = this.plugin.getService(ReflectionService.class);
                    BookReflectionServiceImpl bookReflectionService = reflectionService.getReflectionService(BookReflectionServiceImpl.class);

                    BookMeta bookMeta = (BookMeta) new ItemStack(Material.WRITTEN_BOOK).getItemMeta();
                    bookMeta.setTitle(this.name + " - " + data.shortTitle);
                    bookMeta.setAuthor(this.name);
                    bookMeta.setPages(" \n\n " + " \n\n" + "§a§lCLICK TO VISIT!\n\n" + " \n\n" + "§6" + data.url);
                    ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
                    book.setItemMeta(bookMeta);
                    bookReflectionService.openBook(p, book);
                    return null;
                }
        );
    }

    private static class LinkButtonData {
        private final int slot;
        private final int inkSackColor;
        private final String title;
        private final String url;
        private final String shortTitle;
        private final List<String> description;

        /**
         * Constructor for the private static LinkButtonData class.
         *
         * @param slot         The inventory slot for the button.
         * @param inkSackColor The color data for the ink sack (0-15).
         * @param title        The display title of the button.
         * @param url          The URL to open when clicked.
         * @param description  The lore for the item stack.
         */
        LinkButtonData(int slot, int inkSackColor, String title, String url, List<String> description) {
            this.slot = slot;
            this.inkSackColor = inkSackColor;
            this.title = title;
            this.url = url;
            this.description = description;
            this.shortTitle = title.replace("&f&l", "").replace("&9&l", "").replace("&b&l", "").replace("&e&l", "").trim();
        }
    }

    private static class ManagerButtonData {
        private final int slot;
        private final Material material;
        private final String title;
        private final List<String> description;
        private final Function<Player, Void> action;

        /**
         * Constructor for the private static ManagerButtonData class.
         *
         * @param slot        The inventory slot for the button.
         * @param material    The material type for the button.
         * @param title       The display title of the button.
         * @param description A brief description of the button's function.
         * @param action      The action to perform when clicked.
         */
        ManagerButtonData(int slot, Material material, String title, List<String> description, Function<Player, Void> action) {
            this.slot = slot;
            this.material = material;
            this.title = title;
            this.description = description;
            this.action = action;
        }
    }
}