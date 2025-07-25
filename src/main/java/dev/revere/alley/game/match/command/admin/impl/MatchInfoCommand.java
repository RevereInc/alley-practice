package dev.revere.alley.game.match.command.admin.impl;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.profile.ProfileService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.util.chat.CC;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 15/09/2024 - 11:39
 */
public class MatchInfoCommand extends BaseCommand {
    @CommandData(name = "match.info", permission = "alley.command.match.info", inGameOnly = false)
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (args.length == 0) {
            sender.sendMessage(CC.translate("&6Usage: &e/match info &6<player>"));
            return;
        }

        String playerName = args[0];
        Player target = Bukkit.getPlayer(playerName);
        if (target == null) {
            sender.sendMessage(CC.translate("&cPlayer not found."));
            return;
        }

        Profile profile = this.plugin.getService(ProfileService.class).getProfile(target.getUniqueId());
        if (profile.getMatch() == null) {
            sender.sendMessage(CC.translate("&cThis player is not in a match."));
            return;
        }

        sender.sendMessage(CC.translate("&c&lMatch Information"));
        sender.sendMessage(CC.translate(" &f&l● &fPlayers:"));
        profile.getMatch().getParticipants().forEach(gameParticipant -> gameParticipant.getPlayers().forEach(gamePlayer -> {
            sender.sendMessage(CC.translate("   &f* &c" + gamePlayer.getUsername()));
        }));

        sender.sendMessage(CC.translate(" &f&l● &fSpectators:"));
        if (profile.getMatch().getSpectators().isEmpty()) {
            sender.sendMessage(CC.translate("   &f* &cNone"));
        } else {
            profile.getMatch().getSpectators().forEach(spectator ->
                    sender.sendMessage(CC.translate("   &f* &c" + Bukkit.getOfflinePlayer(spectator).getName()))
            );
        }
        sender.sendMessage(CC.translate(" &f&l● &fKit: &c" + profile.getMatch().getKit().getName()));
        sender.sendMessage(CC.translate(" &f&l● &fArena: &c" + profile.getMatch().getArena().getName()));
        sender.sendMessage(CC.translate(" &f&l● &fState: &c" + profile.getMatch().getState()));
        sender.sendMessage("");
    }
}
