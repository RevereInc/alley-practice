package dev.revere.alley.feature.queue.command.player;

import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import dev.revere.alley.feature.queue.QueueService;
import dev.revere.alley.core.profile.ProfileService;
import dev.revere.alley.core.profile.Profile;
import dev.revere.alley.core.profile.enums.ProfileState;
import dev.revere.alley.common.text.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 25/05/2024 - 16:45
 */
public class QueuesCommand extends BaseCommand {
    @Override
    @CommandData(
            name = "queues",
            aliases = {"selectqueue", "joinqueue"},
            usage = "queues",
            description = "Open the queue selection menu."
    )
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        ProfileService profileService = this.plugin.getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());
        if (!profile.getState().equals(ProfileState.LOBBY)) {
            player.sendMessage(CC.translate("&cYou must be at spawn in order to execute this command :v"));
            return;
        }

        if (profile.getParty() != null) {
            player.sendMessage(CC.translate("&cYou must leave your party to queue for a game."));
            return;
        }

        this.plugin.getService(QueueService.class).getQueueMenu().openMenu(player);
    }
}
