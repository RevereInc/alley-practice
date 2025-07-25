package dev.revere.alley.provider.scoreboard.impl;

import dev.revere.alley.Alley;
import dev.revere.alley.config.ConfigService;
import dev.revere.alley.feature.level.LevelService;
import dev.revere.alley.profile.ProfileService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.enums.ProfileState;
import dev.revere.alley.provider.scoreboard.Scoreboard;
import dev.revere.alley.util.TimeUtil;
import dev.revere.alley.util.chat.CC;
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
        ConfigService configService = Alley.getInstance().getService(ConfigService.class);
        ProfileService profileService = Alley.getInstance().getService(ProfileService.class);
        LevelService levelService = Alley.getInstance().getService(LevelService.class);

        List<String> scoreboardLines = new ArrayList<>();
        for (String line : configService.getScoreboardConfig().getStringList("scoreboard.lines.waiting")) {
            scoreboardLines.add(CC.translate(line)
                    .replaceAll("\\{online}", String.valueOf(Bukkit.getOnlinePlayers().size()))
                    .replaceAll("\\{playing}", String.valueOf(profileService.getProfiles().values().stream().filter(profile1 -> profile1.getState() == ProfileState.PLAYING).count()))
                    .replaceAll("\\{in-queue}", String.valueOf(profileService.getProfiles().values().stream().filter(profile1 -> profile1.getState() == ProfileState.WAITING).count()))
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