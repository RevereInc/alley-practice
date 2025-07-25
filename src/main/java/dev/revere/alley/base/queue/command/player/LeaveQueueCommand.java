package dev.revere.alley.base.queue.command.player;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.profile.ProfileService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.enums.ProfileState;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 5/21/2024
 */
public class LeaveQueueCommand extends BaseCommand {
    @CommandData(name = "leavequeue")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        ProfileService profileService = this.plugin.getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());
        if (!profile.getState().equals(ProfileState.WAITING)) {
            player.sendMessage(CC.translate("&cYou are not in a queue."));
            return;
        }

        if (profile.getParty() != null && !profile.getParty().isLeader(player)) {
            player.sendMessage(CC.translate("&cYou must be the party leader to leave the queue."));
            return;
        }

        profile.getQueueProfile().getQueue().removePlayer(profile.getQueueProfile());
    }
}