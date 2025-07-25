package dev.revere.alley.game.party.menu.duel;

import dev.revere.alley.Alley;
import dev.revere.alley.api.menu.Button;
import dev.revere.alley.api.menu.pagination.PaginatedMenu;
import dev.revere.alley.game.party.PartyService;
import dev.revere.alley.game.party.menu.duel.button.DuelOtherPartyButton;
import org.bukkit.entity.Player;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Emmy
 * @project Alley
 * @date 08/10/2024 - 21:01
 */
public class DuelOtherPartyMenu extends PaginatedMenu {
    @Override
    public String getPrePaginatedTitle(Player player) {
        return "&6&lDuel other parties";
    }

    @Override
    public Map<Integer, Button> getGlobalButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        this.addGlassHeader(buttons, 15);

        return buttons;
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        Alley.getInstance().getService(PartyService.class).getParties().stream()
                .sorted(Comparator.comparing(party -> party.getLeader().getName()))
                //.filter(party -> !party.getLeader().equals(player))
                .sorted(Comparator.comparingInt(party -> party.getMembers().size()))
                .forEach(party -> buttons.put(buttons.size(), new DuelOtherPartyButton(party)))
        ;

        return buttons;
    }

    @Override
    public int getSize() {
        return 9 * 5;
    }
}