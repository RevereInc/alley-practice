package dev.revere.alley.game.match;

import dev.revere.alley.base.arena.Arena;
import dev.revere.alley.base.kit.Kit;
import dev.revere.alley.base.kit.setting.KitSetting;
import dev.revere.alley.base.kit.setting.impl.mode.*;
import dev.revere.alley.base.queue.QueueService;
import dev.revere.alley.base.queue.Queue;
import dev.revere.alley.config.ConfigService;
import dev.revere.alley.plugin.AlleyContext;
import dev.revere.alley.plugin.annotation.Service;
import dev.revere.alley.game.match.impl.*;
import dev.revere.alley.game.match.player.impl.MatchGamePlayerImpl;
import dev.revere.alley.game.match.player.participant.GameParticipant;
import dev.revere.alley.profile.ProfileService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.tool.logger.Logger;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Remi
 * @project Alley
 * @date 5/21/2024
 */
@Getter
@Service(provides = MatchService.class, priority = 220)
public class MatchServiceImpl implements MatchService {
    @FunctionalInterface
    private interface MatchFactory {
        Match create(Queue queue, Kit kit, Arena arena, boolean isRanked, GameParticipant<MatchGamePlayerImpl> pA, GameParticipant<MatchGamePlayerImpl> pB);
    }

    private final ProfileService profileService;
    private final QueueService queueService;
    private final ConfigService configService;

    private final List<Match> matches = new CopyOnWriteArrayList<>();
    private final List<String> blockedCommands = new ArrayList<>();
    private final Map<Class<? extends KitSetting>, MatchFactory> matchFactoryRegistry = new LinkedHashMap<>();

    /**
     * Constructor for DI.
     */
    public MatchServiceImpl(ProfileService profileService, QueueService queueService, ConfigService configService) {
        this.profileService = profileService;
        this.queueService = queueService;
        this.configService = configService;
    }

    @Override
    public void initialize(AlleyContext context) {
        this.registerMatchFactories();
        this.loadBlockedCommands();
    }

    @Override
    public void shutdown(AlleyContext context) {
        if (this.matches.isEmpty()) {
            return;
        }
        Logger.info("Ending " + this.matches.size() + " active matches due to server shutdown...");
        new ArrayList<>(this.matches).forEach(Match::endMatch);
        this.matches.clear();
    }

    @Override
    public List<Match> getMatches() {
        return Collections.unmodifiableList(matches);
    }

    @Override
    public void addMatch(Match match) {
        if (match != null) {
            this.matches.add(match);
        }
    }

    @Override
    public void removeMatch(Match match) {
        this.matches.remove(match);
    }

    /**
     * Registers all known match types and their creation logic.
     * To add a new gamemode, you only need to add a single line here.
     */
    private void registerMatchFactories() {
        matchFactoryRegistry.put(KitSettingBed.class, BedMatch::new);
        matchFactoryRegistry.put(KitSettingLives.class, LivesMatch::new);
        matchFactoryRegistry.put(KitSettingCheckpoint.class, CheckpointMatch::new);
        matchFactoryRegistry.put(KitSettingHideAndSeek.class, HideAndSeekMatch::new);
        matchFactoryRegistry.put(KitSettingStickFight.class, (q, k, ar, r, pA, pB) -> new RoundsMatch(q, k, ar, r, pA, pB, 5));
        matchFactoryRegistry.put(KitSettingRounds.class, (q, k, ar, r, pA, pB) -> new RoundsMatch(q, k, ar, r, pA, pB, 3));
    }

    @Override
    public void createAndStartMatch(Kit kit, Arena arena, GameParticipant<MatchGamePlayerImpl> participantA, GameParticipant<MatchGamePlayerImpl> participantB, boolean teamMatch, boolean affectStatistics, boolean isRanked) {
        Profile profileA = this.profileService.getProfile(participantA.getPlayers().get(0).getUuid());
        Profile profileB = this.profileService.getProfile(participantB.getPlayers().get(0).getUuid());
        if (profileA.getMatch() != null || profileB.getMatch() != null) {
            return;
        }

        Queue matchingQueue = this.queueService.getQueues().stream()
                .filter(queue -> queue.getKit().equals(kit))
                .findFirst()
                .orElse(null);

        MatchFactory factory = null;
        for (Map.Entry<Class<? extends KitSetting>, MatchFactory> entry : matchFactoryRegistry.entrySet()) {
            if (kit.isSettingEnabled(entry.getKey())) {
                factory = entry.getValue();
                break;
            }
        }

        Match match;
        if (factory != null) {
            match = factory.create(matchingQueue, kit, arena, isRanked, participantA, participantB);
        } else {
            match = new DefaultMatch(matchingQueue, kit, arena, isRanked, participantA, participantB);
        }

        match.setTeamMatch(teamMatch);
        match.setAffectStatistics(affectStatistics);

        this.addMatch(match);

        match.startMatch();
    }

    private void loadBlockedCommands() {
        FileConfiguration config = this.configService.getSettingsConfig();

        List<String> configBlockedCommands = config.getStringList("game.blocked-commands");
        if (configBlockedCommands.isEmpty()) {
            Logger.info("No blocked commands found in the configuration. Please check your settings.yml file.");
            return;
        }

        this.blockedCommands.addAll(configBlockedCommands);
    }
}
