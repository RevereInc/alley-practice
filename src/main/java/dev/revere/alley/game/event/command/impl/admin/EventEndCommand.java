package dev.revere.alley.game.event.command.impl.admin;

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
 * @since 31/07/2025
 */
public class EventEndCommand extends BaseCommand {
    @CommandData(
        name = "event.end",
        isAdminOnly = true,
        description = "Ends the current event.",
        usage = "/eventend"
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        EventService eventService = this.plugin.getService(EventService.class);
        if (eventService.getActiveEvent() == null) {
            player.sendMessage(CC.translate("&cThere is no active event to end."));
            return;
        }

        eventService.getActiveEvent().stopEvent();
    }
}
