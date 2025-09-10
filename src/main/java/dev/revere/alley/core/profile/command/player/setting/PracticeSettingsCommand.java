package dev.revere.alley.core.profile.command.player.setting;

import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import dev.revere.alley.core.profile.menu.setting.PracticeSettingsMenu;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 19/05/2024 - 11:27
 */

public class PracticeSettingsCommand extends BaseCommand {
    @CommandData(
            name = "psettings",
            usage = "practicesettings",
            description = "Open the practice settings menu."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        new PracticeSettingsMenu().openMenu(player);
    }
}
