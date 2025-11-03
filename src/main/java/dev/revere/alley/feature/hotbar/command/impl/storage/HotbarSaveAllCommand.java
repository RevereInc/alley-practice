package dev.revere.alley.feature.hotbar.command.impl.storage;

import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import dev.revere.alley.feature.hotbar.HotbarService;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Hamza
 * @since 01/11/2025
 */
public class HotbarSaveAllCommand extends BaseCommand {
    @CommandData(
            name = "hotbar.saveall",
            isAdminOnly = true,
            usage = "hotbar saveall",
            description = "Save all hotbar items to storage."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        HotbarService hotbarService = plugin.getService(HotbarService.class);
        hotbarService.getHotbarItems().forEach(hotbarService::saveToConfig);

        player.sendMessage(CC.translate(this.getString(GlobalMessagesLocaleImpl.HOTBAR_SAVED_ALL)));
    }
}