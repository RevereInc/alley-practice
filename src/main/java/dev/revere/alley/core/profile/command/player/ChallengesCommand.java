package dev.revere.alley.core.profile.command.player;

import dev.revere.alley.common.text.CC;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 14/09/2024 - 23:03
 */
public class ChallengesCommand extends BaseCommand {
    @CommandData(name = "challenges")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        player.sendMessage(CC.translate("&cThis feature is not yet implemented."));
    }
}