package dev.revere.alley.feature.hotbar.command.impl.storage;

import dev.revere.alley.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import dev.revere.alley.feature.hotbar.HotbarService;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 29/04/2024 - 18:45
 */
public class HotbarGiveAllCommand extends BaseCommand {
    @CommandData(
            name = "hotbaritems",
            aliases = {"spawnitems", "lobbyitems"},
            isAdminOnly = true,
            usage = "hotbaritems",
            description = "Gives the player the spawn items."
    )
    @Override
    public void onCommand(CommandArgs args) {
        Player player = args.getPlayer();

        this.plugin.getService(HotbarService.class).applyHotbarItems(player);
        player.sendMessage(this.getString(GlobalMessagesLocaleImpl.SPAWN_ITEMS_GIVEN));
    }
}