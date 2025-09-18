package dev.revere.alley.feature.party.command.impl.member;

import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.profile.Profile;
import dev.revere.alley.core.profile.ProfileService;
import dev.revere.alley.feature.party.Party;
import dev.revere.alley.feature.party.PartyService;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import dev.revere.alley.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 25/05/2024 - 18:27
 */
public class PartyInviteCommand extends BaseCommand {
    @Override
    @CommandData(
            name = "party.invite",
            aliases = "p.invite",
            usage = "party invite <player>",
            description = "Invite a player to your party."
    )
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        PartyService partyService = this.plugin.getService(PartyService.class);
        ProfileService profileService = this.plugin.getService(ProfileService.class);

        if (command.length() < 1) {
            player.sendMessage(CC.translate("&cUsage: /party invite <player>"));
            return;
        }

        String target = args[0];
        Player targetPlayer = Bukkit.getPlayer(target);

        if (targetPlayer == null) {
            player.sendMessage(CC.translate("&cThe player you are trying to invite is not online."));
            return;
        }

        if (targetPlayer == command.getPlayer()) {
            player.sendMessage(CC.translate("&cYou cannot invite yourself to a party."));
            return;
        }

        Party party = partyService.getPartyByMember(player.getUniqueId());
        if (party == null) {
            player.sendMessage(CC.translate(this.getMessage(GlobalMessagesLocaleImpl.PARTY_NOT_IN)));
            return;
        }

        if (party.getLeader() != player) {
            player.sendMessage(CC.translate("&cYou must be the party leader to invite players."));
            return;
        }

        Profile targetProfile = profileService.getProfile(targetPlayer.getUniqueId());
        if (!targetProfile.getProfileData().getSettingData().isPartyInvitesEnabled()) {
            player.sendMessage(CC.translate(this.getMessage(GlobalMessagesLocaleImpl.PLAYER_DISABLED_PARTY_INVITES).replace("{player}", target)));
            return;
        }

        if (party.getMembers().contains(targetPlayer.getUniqueId())) {
            player.sendMessage(CC.translate("&6" + targetPlayer.getName() + " &cis already in your party."));
            return;
        }

        partyService.sendInvite(party, player, targetPlayer);
        party.notifyParty("&6" + targetPlayer.getName() + " &awas invited to the party by &6" + player.getName() + "&a.");
    }
}