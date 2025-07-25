package dev.revere.alley.game.match.menu;

import dev.revere.alley.Alley;
import dev.revere.alley.api.menu.Button;
import dev.revere.alley.api.menu.pagination.PaginatedMenu;
import dev.revere.alley.game.match.Match;
import dev.revere.alley.game.match.MatchService;
import dev.revere.alley.profile.ProfileService;
import dev.revere.alley.tool.item.ItemBuilder;
import dev.revere.alley.util.chat.CC;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Remi
 * @project Alley
 * @date 5/26/2024
 */
public class CurrentMatchesMenu extends PaginatedMenu {
    /**
     * Gets the title of the menu.
     *
     * @param player the player viewing the menu
     * @return the title of the menu
     */
    @Override
    public String getPrePaginatedTitle(Player player) {
        return "&6&lCurrent Matches (" + Alley.getInstance().getService(MatchService.class).getMatches().size() + ")";
    }

    /**
     * Gets the buttons to display in the menu.
     *
     * @param player the player viewing the menu
     * @return the buttons to display
     */
    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        final Map<Integer, Button> buttons = new ConcurrentHashMap<>();
        int slot = 0;

        for (Match match : Alley.getInstance().getService(MatchService.class).getMatches()) {
            buttons.put(slot++, new CurrentMatchButton(match));
        }

        return buttons;
    }

    /**
     * Gets the buttons to display in the global section of the menu.
     *
     * @param player the player viewing the menu
     * @return the global buttons
     */
    @Override
    public Map<Integer, Button> getGlobalButtons(Player player) {
        final Map<Integer, Button> buttons = new HashMap<>();

        this.addGlassHeader(buttons, 15);

        buttons.put(4, new RefreshButton());
        return buttons;
    }

    @RequiredArgsConstructor
    public static class CurrentMatchButton extends Button {
        private final Match match;

        /**
         * Gets the item stack for the button.
         *
         * @param player the player viewing the button
         * @return the item stack
         */
        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(match.getKit().getIcon()).name("&6&l" + match.getParticipants().get(0).getLeader().getUsername() + " &7vs &6&l" + match.getParticipants().get(1).getLeader().getUsername()).durability(match.getKit().getDurability()).hideMeta()
                    .lore(
                            CC.MENU_BAR,
                            " &f● &6Arena: &f" + match.getArena().getName(),
                            " &f● &6Kit: &f" + match.getKit().getDisplayName(),
                            " &f● &6Queue: &f" + (match.getQueue() == null ? "None" : match.getQueue().getQueueType()),
                            " ",
                            "&aClick to spectate.",
                            CC.MENU_BAR
                    )
                    .hideMeta().build();
        }

        /**
         * Handles the click event for the button.
         *
         * @param player       the player who clicked the button
         * @param slot         the slot the button was clicked in
         * @param clickType    the type of click
         * @param hotbarButton the hotbar button clicked
         */
        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            if (clickType != ClickType.LEFT) return;

            if (Alley.getInstance().getService(ProfileService.class).getProfile(player.getUniqueId()).getMatch() != null) {
                player.sendMessage(CC.translate("&cYou can't spectate a match in your current state."));
                return;
            }

            match.addSpectator(player);
            this.playNeutral(player);
        }
    }

    @AllArgsConstructor
    public static class RefreshButton extends Button {
        /**
         * Gets the item stack for the button.
         *
         * @param player the player viewing the button
         * @return the item stack
         */
        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.CARPET)
                    .name("&6&lRefresh")
                    .lore(" &f● &6Press to refresh the matches")
                    .durability(2)
                    .build();
        }

        /**
         * Handles the click event for the button.
         *
         * @param player       the player who clicked the button
         * @param slot         the slot the button was clicked in
         * @param clickType    the type of click
         * @param hotbarButton the hotbar button clicked
         */
        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            if (clickType != ClickType.LEFT) return;
            new CurrentMatchesMenu().openMenu(player);
            this.playNeutral(player);
        }
    }
}
