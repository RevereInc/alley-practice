package dev.revere.alley.game.party.menu.event.impl.button;

import dev.revere.alley.Alley;
import dev.revere.alley.api.menu.Button;
import dev.revere.alley.base.arena.Arena;
import dev.revere.alley.base.arena.ArenaService;
import dev.revere.alley.base.kit.Kit;
import dev.revere.alley.config.locale.impl.PartyLocale;
import dev.revere.alley.game.party.PartyService;
import dev.revere.alley.game.party.Party;
import dev.revere.alley.game.party.menu.event.impl.PartyEventSplitArenaSelectorMenu;
import dev.revere.alley.profile.ProfileService;
import dev.revere.alley.tool.item.ItemBuilder;
import dev.revere.alley.util.chat.CC;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * @author Emmy
 * @project Alley
 * @since 16/06/2025
 */
@AllArgsConstructor
public class PartyEventSplitButton extends Button {
    protected final Alley plugin = Alley.getInstance();
    private final Kit kit;

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(this.kit.getIcon())
                .name("&6&l" + this.kit.getDisplayName())
                .lore(
                        CC.MENU_BAR,
                        "&aClick to select!",
                        CC.MENU_BAR
                )
                .durability(this.kit.getDurability())
                .hideMeta()
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (clickType != ClickType.LEFT) return;

        if (player.hasPermission("alley.party.arena.selector")) {
            new PartyEventSplitArenaSelectorMenu(this.kit).openMenu(player);
            return;
        }

        Party party = Alley.getInstance().getService(ProfileService.class).getProfile(player.getUniqueId()).getParty();
        if (party == null) {
            player.closeInventory();
            player.sendMessage(CC.translate(PartyLocale.NOT_IN_PARTY.getMessage()));
            return;
        }

        Arena arena = Alley.getInstance().getService(ArenaService.class).getRandomArena(this.kit);
        Alley.getInstance().getService(PartyService.class).startMatch(this.kit, arena, party);
    }
}