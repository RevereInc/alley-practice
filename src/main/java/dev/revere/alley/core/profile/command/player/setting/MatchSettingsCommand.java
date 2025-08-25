package dev.revere.alley.core.profile.command.player.setting;

import dev.revere.alley.common.text.CC;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 19/05/2024 - 11:27
 */

public class MatchSettingsCommand extends BaseCommand {
    @Override
    @CommandData(name = "matchsettings")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        //new MatchSettingsMenu().openMenu(player);
        player.closeInventory();
        player.sendMessage(CC.translate("&cThis command is not available yet."));
    }
}
