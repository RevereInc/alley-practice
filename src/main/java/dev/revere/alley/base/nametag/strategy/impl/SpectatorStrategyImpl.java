package dev.revere.alley.base.nametag.strategy.impl;

import dev.revere.alley.base.nametag.NametagContext;
import dev.revere.alley.base.nametag.NametagView;
import dev.revere.alley.base.nametag.strategy.NametagStrategy;
import dev.revere.alley.game.match.Match;
import dev.revere.alley.game.match.player.participant.GameParticipant;
import dev.revere.alley.profile.enums.ProfileState;
import dev.revere.alley.util.chat.CC;
import org.bukkit.ChatColor;

import java.util.List;

/**
 * @author Remi
 * @project alley-practice
 * @date 27/06/2025
 */
public class SpectatorStrategyImpl implements NametagStrategy {
    @Override
    public NametagView createNametagView(NametagContext context) {
        if (context.getViewerProfile().getState() != ProfileState.SPECTATING) {
            return null;
        }

        if (context.getTargetProfile().getState() != ProfileState.PLAYING) {
            return null;
        }

        Match match = context.getViewerProfile().getMatch();
        if (match == null || !match.equals(context.getTargetProfile().getMatch())) {
            return null;
        }

        GameParticipant<?> targetParticipant = match.getParticipant(context.getTarget());
        if (targetParticipant == null) {
            return null;
        }

        List<? extends GameParticipant<?>> participantsInMatch = match.getParticipants();

        int teamIndex = participantsInMatch.indexOf(targetParticipant);

        if (teamIndex == 0) {
            return new NametagView(CC.translate(ChatColor.BLUE.toString()), "");
        } else if (teamIndex == 1) {
            return new NametagView(CC.translate(ChatColor.RED.toString()), "");
        }

        return null;
    }
}