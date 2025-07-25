package dev.revere.alley.game.party.command.impl.member;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.config.locale.impl.PartyLocale;
import dev.revere.alley.game.party.PartyService;
import dev.revere.alley.profile.ProfileService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.enums.ChatChannel;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author Remi
 * @project Alley
 * @date 5/25/2024
 */
public class PartyChatCommand extends BaseCommand {
    @CommandData(name = "party.chat", aliases = {"p.chat", "pc"})
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        ProfileService profileService = this.plugin.getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());
        String message = Arrays.stream(args).map(argument -> argument + " ").collect(Collectors.joining());

        if (args.length == 0) {
            if (profile.getProfileData().getSettingData().getChatChannel().equals(ChatChannel.PARTY.toString())) {
                profile.getProfileData().getSettingData().setChatChannel(ChatChannel.GLOBAL.toString());
                player.sendMessage(CC.translate("&aSet your chat channel to &6global&a."));
            } else {
                profile.getProfileData().getSettingData().setChatChannel(ChatChannel.PARTY.toString());
                player.sendMessage(CC.translate("&aSet your chat channel to &6party&a."));
            }
            return;
        }

        if (profile.getParty() == null) {
            player.sendMessage(CC.translate(PartyLocale.NOT_IN_PARTY.getMessage()));
            return;
        }

        if (!profile.getProfileData().getSettingData().isPartyMessagesEnabled()) {
            player.sendMessage(CC.translate(PartyLocale.DISABLED_PARTY_CHAT.getMessage()));
            return;
        }

        String partyMessage = this.plugin.getService(PartyService.class).getChatFormat().replace("{player}", player.getName()).replace("{message}", message);
        profile.getParty().notifyParty(partyMessage);
    }
}