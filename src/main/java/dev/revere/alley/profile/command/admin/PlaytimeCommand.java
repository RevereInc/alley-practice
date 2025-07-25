package dev.revere.alley.profile.command.admin;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.profile.ProfileService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.data.impl.ProfilePlayTimeData;
import dev.revere.alley.tool.date.DateFormatter;
import dev.revere.alley.tool.date.enums.DateFormat;
import dev.revere.alley.util.PlayerUtil;
import dev.revere.alley.util.chat.CC;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @date 24/05/2024 - 18:45
 */
public class PlaytimeCommand extends BaseCommand {
    @Override
    @CommandData(name = "playtime", isAdminOnly = true, inGameOnly = false)
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (command.length() < 1) {
            sender.sendMessage(CC.translate("&cUsage: /playtime (player)"));
            return;
        }

        OfflinePlayer targetPlayer = PlayerUtil.getOfflinePlayerByName(args[0]);
        if (targetPlayer == null) {
            sender.sendMessage(CC.translate("&cThe player you are trying to check is not online."));
            return;
        }

        Profile targetProfile = this.plugin.getService(ProfileService.class).getProfile(targetPlayer.getUniqueId());
        if (targetProfile == null) {
            sender.sendMessage(CC.translate("&cThe player profile could not be found."));
            return;
        }

        ProfilePlayTimeData playTimeData = targetProfile.getProfileData().getPlayTimeData();

        long totalPlayTime = playTimeData.getTotal();
        long totalTimeInSeconds = totalPlayTime / 1000;
        int days = (int) (totalTimeInSeconds / 86400);
        int hours = (int) (totalTimeInSeconds / 3600);
        int minutes = (int) ((totalTimeInSeconds % 3600) / 60);
        int seconds = (int) (totalTimeInSeconds % 60);

        long firstJoin = targetProfile.getFirstJoin();
        DateFormatter firstJoinFormatted = new DateFormatter(DateFormat.TIME_PLUS_DATE, firstJoin);

        List<String> messages = new ArrayList<>();
        messages.add("");
        messages.add("&6&l" + targetPlayer.getName() + "'s Playtime");
        messages.add("  &f&l● &6Days: &f" + days);
        messages.add("  &f&l● &6Hours: &f" + hours);
        messages.add("  &f&l● &6Minutes: &f" + minutes);
        messages.add("  &f&l● &6Seconds: &f" + seconds);
        messages.add("");
        messages.add("&fTheir first join was on &6" + firstJoinFormatted.setFancy(ChatColor.AQUA, ChatColor.WHITE) + "&f.");
        messages.add("");

        if (targetProfile.isOnline()) {
            messages.add(" &c&lNote: &7" + targetPlayer.getName() + " is currently online.");
            messages.add(" &7Their playtime will update as soon as they log off.");
            messages.add("");
        }

        messages.forEach(message -> sender.sendMessage(CC.translate(message)));
    }
}