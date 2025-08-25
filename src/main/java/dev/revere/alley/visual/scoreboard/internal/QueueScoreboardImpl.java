package dev.revere.alley.visual.scoreboard.internal;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.common.text.CC;
import dev.revere.alley.common.time.TimeUtil;
import dev.revere.alley.core.config.ConfigService;
import dev.revere.alley.core.profile.Profile;
import dev.revere.alley.core.profile.ProfileService;
import dev.revere.alley.core.profile.enums.ProfileState;
import dev.revere.alley.feature.level.LevelService;
import dev.revere.alley.visual.scoreboard.Scoreboard;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @since 30/04/2025
 */
public class QueueScoreboardImpl implements Scoreboard {
    @Override
    public List<String> getLines(Profile profile) {
        ConfigService configService = AlleyPlugin.getInstance().getService(ConfigService.class);
        ProfileService profileService = AlleyPlugin.getInstance().getService(ProfileService.class);
        LevelService levelService = AlleyPlugin.getInstance().getService(LevelService.class);

        List<String> scoreboardLines = new ArrayList<>();
        for (String line : configService.getScoreboardConfig().getStringList("scoreboard.lines.waiting")) {
            scoreboardLines.add(CC.translate(line)
                    .replaceAll("\\{online}", String.valueOf(Bukkit.getOnlinePlayers().size()))
                    .replaceAll("\\{playing}", String.valueOf(safeCountState(profileService, ProfileState.PLAYING)))
                    .replaceAll("\\{in-queue}", String.valueOf(safeCountState(profileService, ProfileState.WAITING)))
                    .replaceAll("\\{wins}", String.valueOf(profile.getProfileData().getTotalWins()))
                    .replaceAll("\\{queued-type}", profile.getQueueProfile().getQueue().getQueueType())
                    .replaceAll("\\{level}", String.valueOf(levelService.getLevel(profile.getProfileData().getGlobalLevel()).getDisplayName()))
                    .replaceAll("\\{queued-time}", TimeUtil.getFormattedElapsedTime(profile.getQueueProfile().getElapsedTime()))
                    .replaceAll("\\{dot-animation}", this.getDotAnimation().getCurrentFrame())
                    .replaceAll("\\{queued-kit}", profile.getQueueProfile().getQueue().getKit().getDisplayName())
            );
        }

        return scoreboardLines;
    }

    @Override
    public List<String> getLines(Profile profile, Player player) {
        return Collections.emptyList();
    }
}