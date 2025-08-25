package dev.revere.alley.feature.party.listener;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.profile.Profile;
import dev.revere.alley.core.profile.ProfileService;
import dev.revere.alley.core.profile.enums.ChatChannel;
import dev.revere.alley.feature.party.Party;
import dev.revere.alley.feature.party.PartyService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * @author Remi
 * @project Alley
 * @date 5/25/2024
 */
public class PartyListener implements Listener {
    @EventHandler
    private void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        ProfileService profileService = AlleyPlugin.getInstance().getService(ProfileService.class);
        PartyService partyService = AlleyPlugin.getInstance().getService(PartyService.class);

        Profile profile = profileService.getProfile(event.getPlayer().getUniqueId());

        if (profile.getProfileData().getSettingData().getChatChannel().equalsIgnoreCase(ChatChannel.PARTY.toString())) {
            if (profile.getParty() == null) {
                player.sendMessage(CC.translate("&cYou're not in a party."));
                event.setCancelled(true);
                return;
            }

            if (!profile.getProfileData().getSettingData().isPartyMessagesEnabled()) {
                player.sendMessage(CC.translate("&cYou have party messages disabled."));
                event.setCancelled(true);
                return;
            }

            String partyMessage = partyService.getChatFormat().replace("{player}", player.getName()).replace("{message}", event.getMessage());
            profile.getParty().notifyParty(partyMessage);
            event.setCancelled(true);
            return;
        }

        if (event.getMessage().startsWith("#") || event.getMessage().startsWith("!")) {
            if (profile.getParty() == null) {
                player.sendMessage(CC.translate("&cYou're not in a party."));
                event.setCancelled(true);
                return;
            }

            if (!profile.getProfileData().getSettingData().isPartyMessagesEnabled()) {
                player.sendMessage(CC.translate("&cYou have party messages disabled."));
                event.setCancelled(true);
                return;
            }

            String partyMessage = partyService.getChatFormat().replace("{player}", player.getName()).replace("{message}", event.getMessage().substring(1));
            profile.getParty().notifyParty(partyMessage);
            event.setCancelled(true);
        }
    }

    @EventHandler
    private void onPlayerKick(PlayerKickEvent event) {
        Player player = event.getPlayer();
        ProfileService profileService = AlleyPlugin.getInstance().getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());

        Party party = profile.getParty();
        if (party == null) {
            return;
        }

        if (party.getLeader() == player) {
            AlleyPlugin.getInstance().getService(PartyService.class).disbandParty(player);
            return;
        }

        AlleyPlugin.getInstance().getService(PartyService.class).leaveParty(player);
    }

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        ProfileService profileService = AlleyPlugin.getInstance().getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());

        Party party = profile.getParty();
        if (party == null) {
            return;
        }

        if (party.getLeader() == player) {
            AlleyPlugin.getInstance().getService(PartyService.class).disbandParty(player);
            return;
        }

        AlleyPlugin.getInstance().getService(PartyService.class).leaveParty(player);
    }
}