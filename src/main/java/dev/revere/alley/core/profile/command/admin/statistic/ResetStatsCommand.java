package dev.revere.alley.core.profile.command.admin.statistic;

import dev.revere.alley.common.PlayerUtil;
import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.locale.internal.impl.ErrorLocaleImpl;
import dev.revere.alley.core.profile.Profile;
import dev.revere.alley.core.profile.ProfileService;
import dev.revere.alley.core.profile.menu.reset.ResetConfirmMenu;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
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
    @CommandData(
            name = "resetstats",
            aliases = {"wipestats",},
            isAdminOnly = true,
            usage = "resetstats <player>",
            description = "Reset a player's statistics."
    )
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
            player.sendMessage(this.getMessage(ErrorLocaleImpl.INVALID_PLAYER));
            return;
        }

        Profile profile = this.plugin.getService(ProfileService.class).getProfile(uuid);
        if (profile == null) {
            player.sendMessage(this.getMessage(ErrorLocaleImpl.INVALID_PLAYER));
            return;
        }

        new ResetConfirmMenu(uuid).openMenu(player);
    }
}