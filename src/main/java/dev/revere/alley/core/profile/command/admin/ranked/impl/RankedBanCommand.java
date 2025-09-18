package dev.revere.alley.core.profile.command.admin.ranked.impl;

import dev.revere.alley.common.PlayerUtil;
import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import dev.revere.alley.core.profile.Profile;
import dev.revere.alley.core.profile.ProfileService;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @since 13/03/2025
 */
public class RankedBanCommand extends BaseCommand {

    //TODO: Locale

    @CommandData(
            name = "ranked.ban",
            isAdminOnly = true,
            usage = "ranked ban <player>",
            description = "Ban a player from ranked matches."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&6Usage: &e/ranked ban &6<player>"));
            return;
        }

        String targetName = args[0];
        OfflinePlayer target = PlayerUtil.getOfflinePlayerByName(targetName);
        if (target == null) {
            player.sendMessage(this.getMessage(GlobalMessagesLocaleImpl.ERROR_INVALID_PLAYER));
            return;
        }

        Profile profile = this.plugin.getService(ProfileService.class).getProfile(target.getUniqueId());
        if (profile == null) {
            player.sendMessage(CC.translate("&cProfile not found."));
            return;
        }

        if (profile.getProfileData().isRankedBanned()) {
            player.sendMessage(CC.translate("&cThis player is already banned from ranked matches."));
            return;
        }

        profile.getProfileData().setRankedBanned(true);
        Bukkit.broadcastMessage(CC.translate("&c&l" + target.getName() + " &7has been banned from ranked matches."));

        if (target.isOnline()) {
            Player targetPlayer = (Player) target;
            targetPlayer.sendMessage(CC.translate("&c&lYou have been banned from ranked matches."));
        }
    }
}