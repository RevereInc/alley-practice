package dev.revere.alley.game.party.menu.event.impl.button;

import dev.revere.alley.Alley;
import dev.revere.alley.api.menu.Button;
import dev.revere.alley.base.arena.Arena;
import dev.revere.alley.base.kit.Kit;
import dev.revere.alley.config.locale.impl.PartyLocale;
import dev.revere.alley.game.party.PartyService;
import dev.revere.alley.game.party.Party;
import dev.revere.alley.profile.ProfileService;
import dev.revere.alley.tool.item.ItemBuilder;
import dev.revere.alley.util.chat.CC;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * @author Emmy
 * @project Alley
 * @since 16/06/2025
 */
@AllArgsConstructor
public class PartyEventSplitArenaSelectorButton extends Button {
    protected final Alley plugin = Alley.getInstance();
    private final Kit kit;
    private final Arena arena;

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(Material.PAPER)
                .name("&6&l" + this.arena.getName())
                .lore(
                        " &f● &6Kit: &f" + this.kit.getDisplayName(),
                        "",
                        "&aClick to select!"
                )
                .durability(0)
                .hideMeta()
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (clickType != ClickType.LEFT) return;

        Party party = Alley.getInstance().getService(ProfileService.class).getProfile(player.getUniqueId()).getParty();
        if (party == null) {
            player.closeInventory();
            player.sendMessage(CC.translate(PartyLocale.NOT_IN_PARTY.getMessage()));
            return;
        }

        PartyService partyService = Alley.getInstance().getService(PartyService.class);
        partyService.startMatch(this.kit, this.arena, party);
    }
}