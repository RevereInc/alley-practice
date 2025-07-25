package dev.revere.alley.base.queue.menu.button;

import dev.revere.alley.Alley;
import dev.revere.alley.api.menu.Button;
import dev.revere.alley.base.arena.Arena;
import dev.revere.alley.base.arena.ArenaService;
import dev.revere.alley.base.kit.Kit;
import dev.revere.alley.tool.item.ItemBuilder;
import dev.revere.alley.util.chat.CC;
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
        Arena arena = Alley.getInstance().getService(ArenaService.class).getRandomArena(this.kit);
        if (arena == null) {
            player.sendMessage("No arena available.");
            return;
        }

        player.sendMessage(CC.translate("&cThis feature is not yet available."));
    }
}