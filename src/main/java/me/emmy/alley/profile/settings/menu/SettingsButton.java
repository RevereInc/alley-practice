package me.emmy.alley.profile.settings.menu;

import lombok.AllArgsConstructor;
import me.emmy.alley.utils.menu.Button;
import me.emmy.alley.utils.pagination.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Created by Emmy
 * Project: Alley
 * Date: 23/05/2024 - 01:27
 */
@AllArgsConstructor
public class SettingsButton extends Button {

    private String displayName;
    private Material material;
    private short data;
    private List<String> lore;

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(material)
                .name(displayName)
                .durability(data)
                .lore(lore)
                .hideMeta()
                .build();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarSlot) {
        if (clickType == ClickType.MIDDLE || clickType == ClickType.RIGHT || clickType == ClickType.NUMBER_KEY || clickType == ClickType.DROP || clickType == ClickType.SHIFT_LEFT || clickType == ClickType.SHIFT_RIGHT) {
            return;
        }

        switch (material) {
            case FEATHER:
                player.performCommand("togglepartymessages");
                playNeutral(player);
                break;
            case NAME_TAG:
                player.performCommand("togglepartyinvites");
                playNeutral(player);
                break;
            case CARPET:
                if (data == (short) 5) {
                    player.performCommand("togglescoreboard");
                }
                playNeutral(player);
                break;
            case ITEM_FRAME:
                player.performCommand("toggletablist");
                playNeutral(player);
                break;
            case BOOK:
                player.performCommand("cosmetics");
                playNeutral(player);
                break;
            case WATCH:
                player.performCommand("toggleworldtime");
                playNeutral(player);
                break;
        }
        playNeutral(player);
    }
}

