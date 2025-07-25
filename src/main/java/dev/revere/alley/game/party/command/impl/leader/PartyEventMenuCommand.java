package dev.revere.alley.game.party.command.impl.leader;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.config.locale.impl.PartyLocale;
import dev.revere.alley.game.party.menu.event.PartyEventMenu;
import dev.revere.alley.profile.ProfileService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.enums.ProfileState;
import dev.revere.alley.util.chat.CC;
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
