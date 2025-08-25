package dev.revere.alley.feature.party.command.impl.leader;

import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.config.internal.locale.impl.PartyLocale;
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
    @CommandData(name = "party.event")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        Profile profile = plugin.getService(ProfileService.class).getProfile(player.getUniqueId());

        if (profile.getParty() == null) {
            player.sendMessage(CC.translate(PartyLocale.NOT_IN_PARTY.getMessage()));
            return;
        }

        if (profile.getState() != ProfileState.LOBBY) {
            player.sendMessage(CC.translate("&cYou are not in the lobby!"));
            return;
        }

        new PartyEventMenu().openMenu(player);
    }
}
