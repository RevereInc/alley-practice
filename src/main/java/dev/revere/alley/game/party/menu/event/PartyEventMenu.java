package dev.revere.alley.game.party.menu.event;

import dev.revere.alley.Alley;
import dev.revere.alley.api.menu.Button;
import dev.revere.alley.api.menu.Menu;
import dev.revere.alley.config.locale.impl.PartyLocale;
import dev.revere.alley.game.party.Party;
import dev.revere.alley.game.party.menu.event.impl.PartyEventFFAMenu;
import dev.revere.alley.game.party.menu.event.impl.PartyEventSplitMenu;
import dev.revere.alley.profile.ProfileService;
import dev.revere.alley.tool.item.ItemBuilder;
import dev.revere.alley.util.chat.CC;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Emmy
 * @project Alley
 * @date 08/10/2024 - 18:29
 */
@AllArgsConstructor
public class PartyEventMenu extends Menu {
    @Override
    public String getTitle(Player player) {
        return "&6&lChoose a party event type";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        final Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(11, new PartyEventButton(
                Material.DIAMOND_SWORD, 0,
                "&6&lTeam split",
                Arrays.asList(
                        CC.MENU_BAR,
                        "&7Split the party into",
                        "&72 teams and fight",
                        "&7against each other.",
                        "",
                        "&aClick to select a kit.",
                        CC.MENU_BAR
                )
        ));

        buttons.put(13, new PartyEventButton(
                Material.GOLD_AXE, 0,
                "&6&lFree for all",
                Arrays.asList(
                        CC.MENU_BAR,
                        "&7Every player fights",
                        "&7against each other.",
                        "",
                        "&aClick to select a kit.",
                        CC.MENU_BAR
                )
        ));

        buttons.put(15, new PartyEventButton(
                Material.REDSTONE, 0,
                "&cBest of 3 Sumo",
                Arrays.asList(
                        CC.MENU_BAR,
                        "&7This event is not",
                        "&7implemented yet.",
                        "",
                        "&c&mClick to start the event.",
                        CC.MENU_BAR
                )
        ));

        this.addGlass(buttons, 15);

        return buttons;
    }

    @Override
    public int getSize() {
        return 3 * 9;
    }

    @AllArgsConstructor
    private static class PartyEventButton extends Button {
        private Material material;
        private int durability;
        private String name;
        private List<String> lore;

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(material)
                    .name(name)
                    .lore(lore)
                    .durability(durability)
                    .hideMeta()
                    .build();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            if (clickType != ClickType.LEFT) return;

            Party party = Alley.getInstance().getService(ProfileService.class).getProfile(player.getUniqueId()).getParty();
            if (party == null) {
                player.sendMessage(CC.translate(PartyLocale.NOT_IN_PARTY.getMessage()));
                return;
            }

            if (party.getLeader() != player) {
                player.sendMessage(CC.translate(PartyLocale.NOT_LEADER.getMessage()));
                return;
            }

            if (party.getMembers().size() < 2) {
                player.sendMessage(CC.translate("&cYou need at least 2 players in your party to start an event."));
                return;
            }

            switch (material) {
                case DIAMOND_SWORD:
                    new PartyEventSplitMenu().openMenu(player);
                    break;
                case GOLD_AXE:
                    new PartyEventFFAMenu().openMenu(player);
                    break;
                case INK_SACK:
                    // Start best of 3 sumo event
                    break;
            }
        }
    }
}