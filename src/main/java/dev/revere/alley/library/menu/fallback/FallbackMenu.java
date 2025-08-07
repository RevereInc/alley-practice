package dev.revere.alley.library.menu.fallback;

import dev.revere.alley.common.item.ItemBuilder;
import dev.revere.alley.library.menu.Button;
import dev.revere.alley.library.menu.Menu;
import dev.revere.alley.library.menu.fallback.enums.FallbackType;
import dev.revere.alley.library.menu.impl.DisplayButton;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Emmy
 * @project alley-practice
 * @since 29/07/2025
 */
@AllArgsConstructor
public class FallbackMenu extends Menu {
    private final FallbackType fallbackType;

    @Override
    public String getTitle(Player player) {
        return "&cFallback Menu";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(4, new DisplayButton(
                new ItemBuilder(Material.RED_ROSE)
                        .name("&c&lOops! Something went wrong...")
                        .lore(
                                "",
                                "&fIt looks like there might be a small hiccup",
                                "&fwith the configuration settings.",
                                "",
                                "&c&lIssue:",
                                " &f" + this.fallbackType.getReadableMessage(),
                                "",
                                "&a&lHow to fix:",
                                " &fPlease double-check your config file",
                                " &fand ensure everything is set up correctly.",
                                "",
                                "&c&lNot Fixed?",
                                " &fIf the issue persists, please reach out to",
                                " &fthe server administrators for assistance.",
                                "",
                                "&fDiscord: &6https://discord.gg/revere",
                                "&fGitHub: &6https://github.com/RevereInc/alley-practice"
                        )
                        .hideMeta()
                        .build(), true
        ));

        return buttons;
    }
}