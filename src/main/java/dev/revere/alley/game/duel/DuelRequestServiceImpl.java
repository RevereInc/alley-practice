package dev.revere.alley.game.duel;

import dev.revere.alley.base.arena.Arena;
import dev.revere.alley.base.arena.ArenaService;
import dev.revere.alley.base.arena.impl.StandAloneArena;
import dev.revere.alley.base.kit.Kit;
import dev.revere.alley.base.server.ServerService;
import dev.revere.alley.game.match.MatchService;
import dev.revere.alley.game.match.player.impl.MatchGamePlayerImpl;
import dev.revere.alley.game.match.player.participant.GameParticipant;
import dev.revere.alley.game.match.player.participant.TeamGameParticipant;
import dev.revere.alley.game.party.Party;
import dev.revere.alley.plugin.annotation.Service;
import dev.revere.alley.profile.ProfileService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.enums.ProfileState;
import dev.revere.alley.util.chat.CC;
import dev.revere.alley.util.chat.ClickableUtil;
import lombok.Getter;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Emmy
 * @project Alley
 * @date 17/10/2024 - 20:02
 */
@Getter
@Service(provides = DuelRequestService.class, priority = 260)
public class DuelRequestServiceImpl implements DuelRequestService {
    private final ProfileService profileService;
    private final ArenaService arenaService;
    private final MatchService matchService;
    private final ServerService serverService;

    private final List<DuelRequest> duelRequests = new ArrayList<>();

    public DuelRequestServiceImpl(ProfileService profileService, ArenaService arenaService, MatchService matchService, ServerService serverService) {
        this.profileService = profileService;
        this.arenaService = arenaService;
        this.matchService = matchService;
        this.serverService = serverService;
    }

    @Override
    public void createAndSendRequest(Player sender, Player initialTarget, Kit kit, @Nullable Arena arena) {
        Profile senderProfile = this.profileService.getProfile(sender.getUniqueId());
        Profile initialTargetProfile = this.profileService.getProfile(initialTarget.getUniqueId());

        Party senderParty = senderProfile.getParty();
        Party targetParty = initialTargetProfile.getParty();

        boolean isPartyDuel = senderParty != null && targetParty != null;
        Player finalTarget;

        if (isPartyDuel) {
            finalTarget = Bukkit.getPlayer(targetParty.getLeader().getUniqueId());
        } else {
            finalTarget = initialTarget;
        }

        if (isRequestInvalid(sender, senderProfile, finalTarget, isPartyDuel)) {
            return;
        }

        Arena finalArena = arena != null ? arena : this.arenaService.getRandomArena(kit);
        if (finalArena instanceof StandAloneArena) {
            finalArena = this.arenaService.getArenaByName(finalArena.getName());
        }

        if (finalArena == null) {
            sender.sendMessage(CC.translate("&cCould not find an available arena for that kit."));
            return;
        }

        DuelRequest duelRequest = new DuelRequest(sender, finalTarget, kit, finalArena, isPartyDuel);
        this.addDuelRequest(duelRequest);

        sender.sendMessage(CC.translate("&aYou have successfully sent a " + (isPartyDuel ? "party " : "") + "duel request to &6" + finalTarget.getName() + "&a."));
        sendInvite(sender, finalTarget, kit, finalArena, isPartyDuel);
    }

    @Override
    public void acceptPendingRequest(DuelRequest duelRequest) {
        if (!isValidAcceptRequest(duelRequest)) {
            return;
        }

        if (duelRequest.isParty()) {
            Profile senderProfile = this.profileService.getProfile(duelRequest.getSender().getUniqueId());
            Profile targetProfile = this.profileService.getProfile(duelRequest.getTarget().getUniqueId());

            Party partyA = senderProfile.getParty();
            Party partyB = targetProfile.getParty();

            if (partyA == null || partyB == null) {
                duelRequest.getSender().sendMessage(CC.translate("&cThe duel could not be started because one of the parties has disbanded."));
                duelRequest.getTarget().sendMessage(CC.translate("&cThe duel could not be started because one of the parties has disbanded."));
                removeDuelRequest(duelRequest);
                return;
            }

            MatchGamePlayerImpl leaderA = new MatchGamePlayerImpl(duelRequest.getSender().getUniqueId(), duelRequest.getSender().getName());
            MatchGamePlayerImpl leaderB = new MatchGamePlayerImpl(duelRequest.getTarget().getUniqueId(), duelRequest.getTarget().getName());

            GameParticipant<MatchGamePlayerImpl> participantA = new TeamGameParticipant<>(leaderA);
            GameParticipant<MatchGamePlayerImpl> participantB = new TeamGameParticipant<>(leaderB);

            UUID leaderAUUID = leaderA.getUuid();
            for (UUID memberUUID : partyA.getMembers()) {
                if (!memberUUID.equals(leaderAUUID)) {
                    Player memberPlayer = Bukkit.getPlayer(memberUUID);
                    if (memberPlayer != null) {
                        participantA.addPlayer(new MatchGamePlayerImpl(memberPlayer.getUniqueId(), memberPlayer.getName()));
                    }
                }
            }

            UUID leaderBUUID = leaderB.getUuid();
            for (UUID memberUUID : partyB.getMembers()) {
                if (!memberUUID.equals(leaderBUUID)) {
                    Player memberPlayer = Bukkit.getPlayer(memberUUID);
                    if (memberPlayer != null) {
                        participantB.addPlayer(new MatchGamePlayerImpl(memberPlayer.getUniqueId(), memberPlayer.getName()));
                    }
                }
            }

            boolean isTeamMatch = (!participantA.getPlayers().isEmpty() || !participantB.getPlayers().isEmpty());

            this.matchService.createAndStartMatch(
                    duelRequest.getKit(), this.arenaService.selectArenaWithPotentialTemporaryCopy(duelRequest.getArena()), participantA, participantB, isTeamMatch, false, false
            );

        } else {
            MatchGamePlayerImpl playerA = new MatchGamePlayerImpl(duelRequest.getSender().getUniqueId(), duelRequest.getSender().getName());
            MatchGamePlayerImpl playerB = new MatchGamePlayerImpl(duelRequest.getTarget().getUniqueId(), duelRequest.getTarget().getName());

            GameParticipant<MatchGamePlayerImpl> participantA = new GameParticipant<>(playerA);
            GameParticipant<MatchGamePlayerImpl> participantB = new GameParticipant<>(playerB);

            this.matchService.createAndStartMatch(
                    duelRequest.getKit(), this.arenaService.selectArenaWithPotentialTemporaryCopy(duelRequest.getArena()), participantA, participantB, false, false, false
            );
        }
        this.removeDuelRequest(duelRequest);
    }

    @Override
    public DuelRequest getDuelRequest(Player sender, Player target) {
        for (DuelRequest duelRequest : this.duelRequests) {
            if (duelRequest.getSender().equals(sender) && duelRequest.getTarget().equals(target) || (duelRequest.getSender().equals(target) && duelRequest.getTarget().equals(sender))) {
                return duelRequest;
            }
        }
        return null;
    }

    /**
     * The new, powerful validation method. It checks all conditions for sending a duel request.
     * This is now the single source of truth for validation.
     *
     * @return true if the request is invalid, false otherwise.
     */
    private boolean isRequestInvalid(Player sender, Profile senderProfile, Player finalTarget, boolean isPartyDuel) {
        if (finalTarget == null) {
            sender.sendMessage(CC.translate("&cThe target player (or their party leader) is not online."));
            return true;
        }

        if (sender.equals(finalTarget)) {
            sender.sendMessage(CC.translate("&cYou cannot duel yourself."));
            return true;
        }

        if (senderProfile.getState() != ProfileState.LOBBY) {
            sender.sendMessage(CC.translate("&cYou must be in the lobby to duel."));
            return true;
        }

        Profile finalTargetProfile = this.profileService.getProfile(finalTarget.getUniqueId());
        if (finalTargetProfile.getState() != ProfileState.LOBBY) {
            sender.sendMessage(CC.translate("&cThe target player is not in the lobby."));
            return true;
        }

        if (isPartyDuel) {
            if (!senderProfile.getParty().isLeader(sender)) {
                sender.sendMessage(CC.translate("&cYou must be the leader of your party to challenge another party."));
                return true;
            }
            if (senderProfile.getParty().equals(finalTargetProfile.getParty())) {
                sender.sendMessage(CC.translate("&cYou cannot duel your own party."));
                return true;
            }
        } else {
            if (senderProfile.getParty() != null || finalTargetProfile.getParty() != null) {
                sender.sendMessage(CC.translate("&cTo send a 1v1 duel, neither you nor your target can be in a party."));
                return true;
            }
        }

        if (getDuelRequest(sender, finalTarget) != null) {
            sender.sendMessage(CC.translate("&cYou already have a pending duel request with that player/party."));
            return true;
        }

        return false;
    }

    /**
     * Send an invitation to the target player.
     *
     * @param sender the sender
     * @param target the target
     * @param kit    the kit
     * @param arena  the arena
     */
    private void sendInvite(Player sender, Player target, Kit kit, Arena arena, boolean isParty) {
        String title = isParty ? "&6&lParty Duel Request" : "&6&lDuel Request";
        TextComponent fromComponent = new TextComponent();
        if (isParty) {
            Party senderParty = this.profileService.getProfile(sender.getUniqueId()).getParty();
            int partySize = (senderParty != null) ? senderParty.getMembers().size() : 1;

            fromComponent.setText(CC.translate(String.format("&f&l ● &fFrom: &6%s's Party (&a%d&6)", sender.getName(), partySize)));

            StringBuilder hoverText = new StringBuilder();
            hoverText.append(CC.translate("&6&lParticipants:\n"));

            if (senderParty != null) {
                List<UUID> members = new ArrayList<>(senderParty.getMembers());
                final int limit = 5;
                int displayCount = Math.min(members.size(), limit);

                for (int i = 0; i < displayCount; i++) {
                    OfflinePlayer member = Bukkit.getOfflinePlayer(members.get(i));
                    hoverText.append("&6").append(member.getName());

                    if (i < displayCount - 1) {
                        hoverText.append("&f, ");
                    }
                }

                if (members.size() > limit) {
                    hoverText.append("&f, ...");
                }
            }

            HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{new TextComponent(CC.translate(hoverText.toString()))});
            fromComponent.setHoverEvent(hoverEvent);

        } else {
            fromComponent.setText(CC.translate("&f&l ● &fFrom: &6" + sender.getName()));
        }

        TextComponent arenaComponent = new TextComponent(CC.translate("&f&l ● &fArena: &6" + arena.getDisplayName()));
        TextComponent kitComponent = new TextComponent(CC.translate("&f&l ● &fKit: &6" + kit.getName()));

        TextComponent acceptComponent = getClickable(sender);

        target.sendMessage("");
        target.spigot().sendMessage(new TextComponent(CC.translate(title)));
        target.spigot().sendMessage(fromComponent);
        target.spigot().sendMessage(arenaComponent);
        target.spigot().sendMessage(kitComponent);
        target.spigot().sendMessage(acceptComponent);
        target.sendMessage("");
    }

    /**
     * Checks if the accept request is valid.
     *
     * @param duelRequest the duel request
     * @return true if the request is valid, false otherwise
     */
    private boolean isValidAcceptRequest(DuelRequest duelRequest) {
        if (duelRequest.getSender() == null || duelRequest.getTarget() == null) {
            return false;
        }

        if (!this.serverService.isQueueingAllowed()) {
            return false;
        }

        if (duelRequest.hasExpired()) return false;

        Profile profile = this.profileService.getProfile(duelRequest.getSender().getUniqueId());
        if (profile.getState() != ProfileState.LOBBY) {
            duelRequest.getSender().sendMessage(CC.translate("&cYou can only accept duel requests in the lobby."));
            return false;
        }

        if (duelRequest.getTarget() == null) {
            duelRequest.getSender().sendMessage(CC.translate("&cThat player is not online."));
            return false;
        }

        Profile targetProfile = this.profileService.getProfile(duelRequest.getTarget().getUniqueId());
        if (targetProfile.getState() != ProfileState.LOBBY) {
            duelRequest.getSender().sendMessage(CC.translate("&cThat player is not in the lobby."));
            return false;
        }

        if (targetProfile.getParty() != null && profile.getParty() == null) {
            duelRequest.getSender().sendMessage(CC.translate("&cYou cannot accept a duel request from a player in a party if you are not in a party."));
            return false;
        }

        if (targetProfile.getParty() != null && targetProfile.getParty().getMembers().contains(duelRequest.getSender().getUniqueId())) {
            duelRequest.getSender().sendMessage(CC.translate("&cYou cannot accept a duel request from a player in your party."));
            return false;
        }

        if (targetProfile.getParty() == null && profile.getParty() != null) {
            duelRequest.getSender().sendMessage(CC.translate("&cYou cannot accept a duel request from a player who is not in a party."));
            return false;
        }

        if (duelRequest.isParty() && profile.getParty() == null) {
            duelRequest.getSender().sendMessage(CC.translate("&cYou can only accept party duel requests if you are in a party."));
            return false;
        }
        return true;
    }

    /**
     * Add a duel request to the list of duel requests.
     *
     * @param duelRequest the duel
     */
    public void addDuelRequest(DuelRequest duelRequest) {
        this.duelRequests.add(duelRequest);
    }

    /**
     * Remove duel request from the list of duel requests.
     *
     * @param duelRequest the duel
     */
    public void removeDuelRequest(DuelRequest duelRequest) {
        this.duelRequests.remove(duelRequest);
    }

    /**
     * Get the clickable component for the duel request.
     *
     * @param sender the sender
     * @return the clickable component
     */
    private TextComponent getClickable(Player sender) {
        return ClickableUtil.createComponent(
                " &a(Click To Accept)",
                "/accept " + sender.getName(),
                "&aClick to accept &6" + sender.getName() + "&a's duel request."
        );
    }
}