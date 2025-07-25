package dev.revere.alley.game.party.menu.duel.button;

import dev.revere.alley.Alley;
import dev.revere.alley.api.menu.Button;
import dev.revere.alley.game.duel.menu.DuelRequestMenu;
import dev.revere.alley.game.party.Party;
import dev.revere.alley.profile.ProfileService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.tool.item.ItemBuilder;
import dev.revere.alley.util.chat.CC;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Emmy
 * @project Alley
 * @since 16/06/2025
 */
@AllArgsConstructor
public class DuelOtherPartyButton extends Button {
    private final Alley plugin = Alley.getInstance();
    private final Party party;

    @Override
    public ItemStack getButtonItem(Player player) {
        List<String> lore = getLore();

        ItemStack itemStack = new ItemBuilder(new ItemStack(Material.SKULL_ITEM, 1, (short) 3))
                .name("&6&l" + party.getLeader().getName() + "'s Party")
                .lore(lore)
                .build();
        SkullMeta meta = (SkullMeta) itemStack.getItemMeta();
        Player leader = Bukkit.getPlayer(party.getLeader().getName());
        if (leader != null) {
            meta.setOwner(leader.getName());
        }
        itemStack.setItemMeta(meta);

        return itemStack;
    }

    /**
     * Get the lore of the item.
     *
     * @return the lore of the item
     */
    private @NotNull List<String> getLore() {
        List<String> lore = new ArrayList<>();
        lore.add(CC.MENU_BAR);
        lore.add(" &6Members: &f(" + party.getMembers().size() + ")");
        for (UUID memberId : party.getMembers()) {
            Player member = Bukkit.getPlayer(memberId);
            if (member != null) {
                lore.add("&6│ &f" + member.getName());
            }
        }
        lore.add("");
        lore.add("&aClick to duel this party.");
        lore.add(CC.MENU_BAR);
        return lore;
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (clickType != ClickType.LEFT) return;

        ProfileService profileService = Alley.getInstance().getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());
        Party playerParty = profile.getParty();

        if (playerParty == null) {
            player.sendMessage(CC.translate("&cYou are not in a party."));
            return;
        }

        if (!playerParty.isLeader(player)) {
            player.sendMessage(CC.translate("&cYou must be the leader of your party to challenge another party."));
            return;
        }

        if (party.getLeader().equals(player)) {
            player.sendMessage(CC.translate("&cYou can't duel your own party."));
            return;
        }

        Player targetLeader = Bukkit.getPlayer(party.getLeader().getUniqueId());
        if (targetLeader == null) {
            player.sendMessage(CC.translate("&cThe leader of that party is not online."));
            return;
        }

        player.closeInventory();
        new DuelRequestMenu(targetLeader).openMenu(player);
    }
}