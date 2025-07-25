package dev.revere.alley.profile.command.admin.statistic;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.profile.ProfileService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.menu.reset.ResetConfirmMenu;
import dev.revere.alley.util.PlayerUtil;
import dev.revere.alley.util.chat.CC;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * @author Emmy
 * @project Alley
 * @date 02/01/2025 - 20:58
 */
public class ResetStatsCommand extends BaseCommand {
    @CommandData(name = "resetstats", aliases = {"wipestats",}, isAdminOnly = true)
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&6Usage: &e/resetstats &6<player>"));
            return;
        }

        Player onlineTarget = Bukkit.getPlayerExact(args[0]);
        OfflinePlayer target = onlineTarget != null ? onlineTarget : PlayerUtil.getOfflinePlayerByName(args[0]);

        UUID uuid = target.getUniqueId();
        if (uuid == null) {
            player.sendMessage(CC.translate("&cThat player is invalid."));
            return;
        }

        Profile profile = this.plugin.getService(ProfileService.class).getProfile(uuid);
        if (profile == null) {
            player.sendMessage(CC.translate("&cThat player does not exist."));
            return;
        }

        new ResetConfirmMenu(uuid).openMenu(player);
    }
}