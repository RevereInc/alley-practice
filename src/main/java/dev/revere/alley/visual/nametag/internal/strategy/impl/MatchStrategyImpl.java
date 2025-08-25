package dev.revere.alley.visual.nametag.internal.strategy.impl;

import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.profile.enums.ProfileState;
import dev.revere.alley.feature.kit.setting.types.mode.KitSettingHideAndSeek;
import dev.revere.alley.feature.match.Match;
import dev.revere.alley.feature.match.internal.types.HideAndSeekMatch;
import dev.revere.alley.feature.match.model.GameParticipant;
import dev.revere.alley.visual.nametag.NametagView;
import dev.revere.alley.visual.nametag.NametagVisibility;
import dev.revere.alley.visual.nametag.internal.strategy.NametagStrategy;
import dev.revere.alley.visual.nametag.model.NametagContext;
import org.bukkit.ChatColor;

/**
 * @author Remi
 * @project alley-practice
 * @date 27/06/2025
 */
public class MatchStrategyImpl implements NametagStrategy {
    @Override
    public NametagView createNametagView(NametagContext context) {
        if (context.getViewerProfile().getState() != ProfileState.PLAYING) {
            return null;
        }

        Match match = context.getViewerProfile().getMatch();
        if (match == null) {
            return null;
        }

        if (context.getTargetProfile().getMatch() == null || !context.getTargetProfile().getMatch().equals(match)) {
            return null;
        }

        if (match.getKit().isSettingEnabled(KitSettingHideAndSeek.class)) {
            HideAndSeekMatch hideAndSeekMatch = (HideAndSeekMatch) match;
            GameParticipant<?> seekers = hideAndSeekMatch.getParticipantA();

            boolean viewerIsSeeker = seekers.containsPlayer(context.getViewer().getUniqueId());
            boolean targetIsSeeker = seekers.containsPlayer(context.getTarget().getUniqueId());

            if (viewerIsSeeker) {
                if (targetIsSeeker) {
                    return new NametagView(CC.translate("&a"), "", NametagVisibility.ALWAYS);
                } else {
                    return new NametagView(CC.translate("&c"), "", NametagVisibility.NEVER);
                }
            } else {
                if (targetIsSeeker) {
                    return new NametagView(CC.translate("&c"), "", NametagVisibility.ALWAYS);
                } else {
                    return new NametagView(CC.translate("&a"), "", NametagVisibility.ALWAYS);
                }
            }
        }

        if (!match.isTeamMatch()) {
            return null;
        }

        if (match.isInSameTeam(context.getViewer(), context.getTarget())) {
            return new NametagView(CC.translate(ChatColor.GREEN.toString()), "");
        } else {
            return new NametagView(CC.translate(ChatColor.RED.toString()), "");
        }
    }
}