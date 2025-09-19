package dev.revere.alley.feature.match.command.admin.impl;

import dev.revere.alley.core.locale.internal.impl.message.GameMessagesLocaleImpl;
import dev.revere.alley.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import dev.revere.alley.feature.match.Match;
import dev.revere.alley.core.profile.ProfileService;
import dev.revere.alley.core.profile.Profile;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @since 16/06/2025
 */
public class MatchResetBlocksCommand extends BaseCommand {
    @CommandData(
            name = "match.resetblocks",
            isAdminOnly = true,
            usage = "match resetblocks",
            description = "Reset all block changes in your current match."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        ProfileService profileService = this.plugin.getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());
        Match match = profile.getMatch();
        if (match == null) {
            player.sendMessage(this.getMessage(GlobalMessagesLocaleImpl.ERROR_YOU_NOT_PLAYING_MATCH));
            return;
        }

        match.resetBlockChanges();
        if (this.getBoolean(GameMessagesLocaleImpl.MATCH_BLOCKS_RESET_MESSAGE_ENABLED_BOOLEAN)) {
            List<String> messages = this.getMessageList(GameMessagesLocaleImpl.MATCH_BLOCKS_RESET_MESSAGE_FORMAT);
            for (String message : messages) {
                match.sendMessage(message
                        .replace("{name-color}", String.valueOf(profile.getNameColor()))
                        .replace("{player}", player.getName())
                );
            }
        }
    }
}