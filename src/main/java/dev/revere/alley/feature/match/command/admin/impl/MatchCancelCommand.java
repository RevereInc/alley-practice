package dev.revere.alley.feature.match.command.admin.impl;

import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import dev.revere.alley.core.profile.Profile;
import dev.revere.alley.core.profile.ProfileService;
import dev.revere.alley.core.profile.enums.ProfileState;
import dev.revere.alley.feature.match.MatchState;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 5/26/2024
 */
public class MatchCancelCommand extends BaseCommand {
    @CommandData(
            name = "match.cancel",
            isAdminOnly = true,
            usage = "match cancel <player>",
            description = "Forcefully cancel a player's match."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length != 1) {
            player.sendMessage(CC.translate("&6Usage: &e/match cancel &6<player>"));
            return;
        }

        Player target = player.getServer().getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(this.getMessage(GlobalMessagesLocaleImpl.ERROR_INVALID_PLAYER));
            return;
        }

        Profile profile = this.plugin.getService(ProfileService.class).getProfile(target.getUniqueId());

        if (profile.getState() != ProfileState.PLAYING || profile.getMatch() == null) {
            player.sendMessage(CC.translate("&cThat player is not in a match."));
            return;
        }

        profile.getMatch().handleRoundEnd();
        profile.getMatch().setState(MatchState.ENDING_MATCH);
        profile.getMatch().getRunnable().setStage(4);

        player.sendMessage(CC.translate("&aYou have ended the match for &6" + target.getName() + "&a."));
    }
}
