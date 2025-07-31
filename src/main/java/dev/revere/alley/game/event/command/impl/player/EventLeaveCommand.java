package dev.revere.alley.game.event.command.impl.player;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.game.event.EventService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.ProfileService;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project alley-practice
 * @since 29/07/2025
 */
public class EventLeaveCommand extends BaseCommand {
    @CommandData(
            name = "event.leave",
            usage = "/event leave",
            description = "Leave the current event"
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        EventService eventService = this.plugin.getService(EventService.class);
        if (!eventService.isEventRunning()) {
            player.sendMessage(CC.translate("&cThere is no event currently running."));
            return;
        }

        ProfileService profileService = this.plugin.getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());
        if (profile.getEvent() == null) {
            player.sendMessage(CC.translate("&cYou are not currently in an event."));
            return;
        }

        profile.getEvent().handleLeave(player, true);
    }
}
