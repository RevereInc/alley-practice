package dev.revere.alley.feature.queue.menu.button;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.common.item.ItemBuilder;
import dev.revere.alley.common.text.CC;
import dev.revere.alley.feature.arena.Arena;
import dev.revere.alley.feature.arena.ArenaService;
import dev.revere.alley.feature.kit.Kit;
import dev.revere.alley.library.menu.Button;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * @author Emmy
 * @project Alley
 * @since 16/04/2025
 */
@AllArgsConstructor
public class BotButton extends Button {
    private final Kit kit;

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(kit.getIcon())
                .name(kit.getName())
                //TODO: This is just temporary, we're going to make this dynamic for all type of bots, not only a statically defined one. FOR TESTING PURPOSES ONLY.
                .lore(
                        "&7Click to fight a bot!",
                        "",
                        "&7Kit: &6" + kit.getName(),
                        "",
                        "&7Click to start the fight!"
                )
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        Arena arena = AlleyPlugin.getInstance().getService(ArenaService.class).getRandomArena(this.kit);
        if (arena == null) {
            player.sendMessage("No arena available.");
            return;
        }

        player.sendMessage(CC.translate("&cThis feature is not yet available."));
    }
}