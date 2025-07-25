package dev.revere.alley.profile.command.admin.ranked.impl;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.profile.ProfileService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.util.PlayerUtil;
import dev.revere.alley.util.chat.CC;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @since 13/03/2025
 */
public class RankedUnbanCommand extends BaseCommand {
    @CommandData(name = "ranked.unban", isAdminOnly = true, description = "Unban a player from ranked matches.", usage = "/ranked unban <player>")
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
            player.sendMessage(CC.translate("&cPlayer not found."));
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