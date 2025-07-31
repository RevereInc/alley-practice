package dev.revere.alley.game.event.command.impl.player;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.game.event.EventService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.ProfileService;
import dev.revere.alley.profile.enums.ProfileState;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project alley-practice
 * @since 29/07/2025
 */
public class EventJoinCommand extends BaseCommand {
    @CommandData(
            name = "event.join",
            usage = "/event join",
            description = "Join an event"
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        Profile profile = this.plugin.getService(ProfileService.class).getProfile(player.getUniqueId());

        if (profile.getState() != ProfileState.LOBBY) {
            player.sendMessage(CC.translate("&cYou can't join an event in your current state."));
            return;
        }

        if (profile.getEvent() != null) {
            player.sendMessage(CC.translate("&cYou are already in an event."));
            return;
        }

        EventService eventService = this.plugin.getService(EventService.class);
        if (!eventService.isEventRunning()) {
            player.sendMessage(CC.translate("&cThere is no event to join."));
            return;
        }

        eventService.getActiveEvent().handleJoin(player, true);
    }
}