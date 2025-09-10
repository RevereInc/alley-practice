package dev.revere.alley.feature.party.command.impl.leader;

import dev.revere.alley.core.locale.internal.types.ErrorLocaleImpl;
import dev.revere.alley.core.locale.internal.types.PartyLocaleImpl;
import dev.revere.alley.core.profile.Profile;
import dev.revere.alley.core.profile.ProfileService;
import dev.revere.alley.core.profile.enums.ProfileState;
import dev.revere.alley.feature.party.menu.event.PartyEventMenu;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project alley-practice
 * @date 22/07/2025
 */
public class PartyEventMenuCommand extends BaseCommand {
    @CommandData(
            name = "party.event",
            aliases = {"p.event"},
            usage = "party event",
            description = "Open the party event menu."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        Profile profile = plugin.getService(ProfileService.class).getProfile(player.getUniqueId());

        if (profile.getParty() == null) {
            player.sendMessage(this.getMessage(PartyLocaleImpl.NOT_IN_PARTY));
            return;
        }

        if (profile.getState() != ProfileState.LOBBY) {
            player.sendMessage(this.getMessage(ErrorLocaleImpl.MUST_BE_IN_LOBBY));
            return;
        }

        new PartyEventMenu().openMenu(player);
    }
}
