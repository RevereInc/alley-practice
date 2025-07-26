package dev.revere.alley.api.assemble;

import dev.revere.alley.config.ConfigService;
import dev.revere.alley.profile.ProfileService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.enums.ProfileState;
import dev.revere.alley.provider.scoreboard.impl.FFAScoreboardImpl;
import dev.revere.alley.provider.scoreboard.impl.LobbyScoreboardImpl;
import dev.revere.alley.provider.scoreboard.impl.QueueScoreboardImpl;
import dev.revere.alley.provider.scoreboard.impl.SpectatorScoreboardImpl;
import dev.revere.alley.provider.scoreboard.impl.match.MatchScoreboardImpl;
import dev.revere.alley.tool.animation.AnimationRepository;
import dev.revere.alley.tool.animation.enums.AnimationType;
import dev.revere.alley.tool.animation.type.config.impl.ScoreboardTitleAnimation;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @date 27/03/2024 - 14:27
 */
public class AssembleAdapterImpl implements AssembleAdapter {
    private final AnimationRepository animationRepository;
    private final ProfileService profileService;
    private final ConfigService configService;

    private final LobbyScoreboardImpl lobbyScoreboardImpl = new LobbyScoreboardImpl();
    private final QueueScoreboardImpl queueScoreboardImpl = new QueueScoreboardImpl();
    private final MatchScoreboardImpl matchScoreboardImpl = new MatchScoreboardImpl();
    private final SpectatorScoreboardImpl spectatorScoreboardImpl = new SpectatorScoreboardImpl();
    private final FFAScoreboardImpl ffaScoreboardImpl = new FFAScoreboardImpl();

    public AssembleAdapterImpl(AnimationRepository animationRepository, ProfileService profileService, ConfigService configService) {
        this.animationRepository = animationRepository;
        this.profileService = profileService;
        this.configService = configService;
    }

    @Override
    public String getTitle(Player player) {
        return this.animationRepository.getAnimation(ScoreboardTitleAnimation.class, AnimationType.CONFIG).getText();
    }

    /**
     * Get the lines of the scoreboard.
     *
     * @param player The player to get the lines for.
     * @return The lines of the scoreboard.
     */
    @Override
    public List<String> getLines(Player player) {
        Profile profile = this.profileService.getProfile(player.getUniqueId());

        if (profile.getProfileData().getSettingData().isScoreboardEnabled()) {

            if (profile.getState() == ProfileState.EDITING) {
                return Collections.emptyList();
            }

            List<String> lines = new ArrayList<>();

            switch (profile.getState()) {
                case LOBBY:
                    lines.addAll(this.lobbyScoreboardImpl.getLines(profile));
                    break;
                case WAITING:
                    lines.addAll(this.queueScoreboardImpl.getLines(profile));
                    break;
                case PLAYING:
                    lines.addAll(this.matchScoreboardImpl.getLines(profile, player));
                    break;
                case SPECTATING:
                    lines.addAll(this.spectatorScoreboardImpl.getLines(profile));
                    break;
                case FFA:
                    lines.addAll(this.ffaScoreboardImpl.getLines(profile, player));
                    break;
            }

            List<String> footer = this.configService.getScoreboardConfig().getStringList("scoreboard.footer-addition");
            footer.forEach(line -> lines.add(CC.translate(line)));

            lines.replaceAll(line -> line.replace("{sidebar}", this.getScoreboardLines(profile)));
            return lines;
        }
        return null;
    }

    /**
     * Method to either show the scoreboard lines or not.
     *
     * @param profile The profile to get the scoreboard lines for.
     * @return The scoreboard lines.
     */
    private String getScoreboardLines(Profile profile) {
        if (profile.getProfileData().getSettingData().isShowScoreboardLines()) {
            return this.configService.getScoreboardConfig().getString("scoreboard.sidebar-format");
        }
        return "";
    }
}