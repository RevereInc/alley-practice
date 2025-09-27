package dev.revere.alley.core.profile.data.command.ranked.impl;

import dev.revere.alley.common.PlayerUtil;
import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import dev.revere.alley.core.profile.Profile;
import dev.revere.alley.core.profile.ProfileService;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @since 13/03/2025
 */
public class RankedUnbanCommand extends BaseCommand {
    @CommandData(
            name = "ranked.unban",
            isAdminOnly = true,
            usage = "ranked unban <player>",
            description = "Unban a player from ranked matches."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            command.sendUsage();
            return;
        }

        String targetName = args[0];
        OfflinePlayer target = PlayerUtil.getOfflinePlayerByName(targetName);
        if (target == null) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ERROR_INVALID_PLAYER));
            return;
        }

        Profile profile = this.plugin.getService(ProfileService.class).getProfile(target.getUniqueId());
        if (profile == null) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ERROR_INVALID_PLAYER));
            return;
        }

        if (!profile.getProfileData().isRankedBanned()) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.RANKED_PLAYER_NOT_BANNED)
                    .replace("{name-color}", String.valueOf(profile.getNameColor()))
                    .replace("{player}", target.getName())
            );
            return;
        }

        profile.getProfileData().setRankedBanned(false);
        if (this.getBoolean(GlobalMessagesLocaleImpl.RANKED_PLAYER_UNBAN_BROADCAST_BOOLEAN)) {
            List<String> message = this.getStringList(GlobalMessagesLocaleImpl.RANKED_PLAYER_UNBAN_BROADCAST);
            for (String line : message) {
                this.plugin.getServer().broadcastMessage(CC.translate(line
                        .replace("{name-color}", String.valueOf(profile.getNameColor()))
                        .replace("{player}", target.getName())
                        .replace("{reason}", "N/A")
                        .replace("{ban-id}", "N/A")
                        .replace("{duration}", "N/A") //TODO
                ));
            }
        }

        if (this.getBoolean(GlobalMessagesLocaleImpl.RANKED_UNBAN_MESSAGE_NOTICE_BOOLEAN)) {
            if (target.isOnline()) {
                Player targetPlayer = (Player) target;
                List<String> message = this.getStringList(GlobalMessagesLocaleImpl.RANKED_UNBAN_MESSAGE_NOTICE);
                for (String line : message) {
                    targetPlayer.sendMessage(line
                            .replace("{name-color}", String.valueOf(profile.getNameColor()))
                            .replace("{player}", target.getName())
                            .replace("{reason}", "N/A")
                            .replace("{ban-id}", "N/A")
                            .replace("{duration}", "N/A") //TODO
                    );
                }
            }
        }
    }
}