package dev.revere.alley.game.ffa.menu;

import dev.revere.alley.api.menu.Button;
import dev.revere.alley.game.ffa.AbstractFFAMatch;
import dev.revere.alley.tool.item.ItemBuilder;
import dev.revere.alley.util.SoundUtil;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

/**
 * @author Emmy
 * @project Alley
 * @date 23/05/2024 - 01:29
 */
@AllArgsConstructor
public class FFAButton extends Button {
    private final AbstractFFAMatch match;

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(match.getKit().getIcon())
                .name("&b&l" + match.getName())
                .durability(match.getKit().getDurability())
                .lore(Arrays.asList(
                        "",
                        "&fPlaying: &b" + match.getPlayers().size() + "/" + match.getMaxPlayers(),
                        "&fArena: &b" + match.getArena().getName(),
                        "&fKit: &b" + match.getKit().getName(),
                        "",
                        "&fClick to join the &b" + match.getName() + " &fqueue.")
                )
                .hideMeta()
                .build();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarSlot) {
        if (clickType != ClickType.LEFT) return;
        SoundUtil.playSuccess(player);
        match.join(player);
    }
}