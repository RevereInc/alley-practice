package dev.revere.alley.core.profile.command.admin.ranked.impl;

import dev.revere.alley.common.PlayerUtil;
import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.locale.internal.impl.ErrorLocaleImpl;
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
public class RankedUnbanCommand extends BaseCommand {

    //TODO: Locale

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
            player.sendMessage(CC.translate("&6Usage: &e/ranked unban &6<player>"));
            return;
        }

        String targetName = args[0];
        OfflinePlayer target = PlayerUtil.getOfflinePlayerByName(targetName);
        if (target == null) {
            player.sendMessage(this.getMessage(ErrorLocaleImpl.INVALID_PLAYER));
            return;
        }

        Profile profile = this.plugin.getService(ProfileService.class).getProfile(target.getUniqueId());
        if (profile == null) {
            player.sendMessage(CC.translate("&cProfile not found."));
            return;
        }

        if (!profile.getProfileData().isRankedBanned()) {
            player.sendMessage(CC.translate("&cThis player is not banned from ranked matches."));
            return;
        }

        profile.getProfileData().setRankedBanned(false);
        Bukkit.broadcastMessage(CC.translate("&a&l" + target.getName() + " &7has been unbanned from ranked matches."));

        if (target.isOnline()) {
            Player targetPlayer = (Player) target;
            targetPlayer.sendMessage(CC.translate("&a&lYou have been unbanned from ranked matches."));
        }
    }
}