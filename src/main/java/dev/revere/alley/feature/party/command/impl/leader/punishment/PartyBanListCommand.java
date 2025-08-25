package dev.revere.alley.feature.party.command.impl.leader.punishment;

import dev.revere.alley.common.text.CC;
import dev.revere.alley.feature.party.PartyService;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 11/12/2024 - 13:46
 */
public class PartyBanListCommand extends BaseCommand {
    @CommandData(name = "party.banlist", aliases = "p.banlist")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        player.sendMessage("");
        player.sendMessage(CC.translate("&c&lBanned Members in your party:"));
        this.plugin.getService(PartyService.class).getPartyByLeader(player).getBannedPlayers().forEach(bannedMember -> player.sendMessage(CC.translate("&7- &c" + Bukkit.getPlayer(bannedMember).getName())));
        player.sendMessage("");
    }
}