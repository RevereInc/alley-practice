package dev.revere.alley.feature.match.task;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.common.text.CC;
import dev.revere.alley.common.time.TimeUtil;
import dev.revere.alley.core.locale.LocaleService;
import dev.revere.alley.core.locale.internal.impl.message.GameMessagesLocaleImpl;
import dev.revere.alley.core.locale.internal.impl.VisualsLocaleImpl;
import dev.revere.alley.feature.kit.Kit;
import dev.revere.alley.feature.kit.setting.types.mode.KitSettingRounds;
import dev.revere.alley.feature.match.Match;
import dev.revere.alley.feature.match.MatchState;
import dev.revere.alley.feature.match.internal.types.RoundsMatch;
import org.bukkit.Sound;

import java.util.List;

/**
 * @author Emmy
 * @project alley-practice
 * @since 12/09/2025
 */
public class MatchTaskManager {
    private final AlleyPlugin plugin = AlleyPlugin.getInstance();
    private final Match match;

    /**
     * Constructor for the MatchTaskManager class.
     *
     * @param match The match instance.
     */
    public MatchTaskManager(Match match) {
        this.match = match;
    }

    public void handleStartingStage() {
        if (this.match.getRunnable().getStage() == 0) {
            this.plugin.getServer().getScheduler().runTask(this.plugin, this.match::handleRoundStart);
            this.match.setState(MatchState.RUNNING);

            this.sendMatchStartedMessage();
            this.sendDisclaimer();

            this.playSoundStarted();
        } else {
            this.sendStartingMessage();
            this.playSoundStarting();
        }
    }

    /**
     * Handle restarting the round stage for rounds matches.
     *
     * @throws IllegalStateException If the match is not an instance of {@link RoundsMatch}.
     */
    public void handleRestartingRoundStage() throws IllegalStateException {
        if (!(this.match instanceof RoundsMatch)) {
            throw new IllegalStateException("Attempted to restart round on a non-rounds match.");
        }

        if (this.match.getRunnable().getStage() == 0) {
            this.plugin.getServer().getScheduler().runTask(this.plugin, this.match::handleRoundStart);
            this.match.setState(MatchState.RUNNING);

            RoundsMatch roundsMatch = (RoundsMatch) this.match;
            this.sendRoundStartedMessage(roundsMatch.getCurrentRound());
            this.playSoundStarted();
        } else {
            this.sendRestartingMessage();
            this.playSoundStarting();
        }
    }

    public void handleEndingRoundStage() {
        if (this.match.canStartRound()) {
            this.match.setState(MatchState.RESTARTING_ROUND);
            this.match.getRunnable().setStage(4);
        }
    }

    public void handleEndingStage() {
        if (this.match.getRunnable().getStage() == 0) {
            this.plugin.getServer().getScheduler().runTask(this.plugin, this.match::endMatch);
        }
    }

    /**
     * End the match if the time is up based on different kit settings.
     *
     * @return If the time limit has been reached.
     */
    public boolean endMatchIfTimeLimitExceeded() {
        LocaleService localeService = this.plugin.getService(LocaleService.class);
        long elapsedTime = System.currentTimeMillis() - match.getStartTime();
        long timeLimit;

        //TODO: Make this a setting or something...

        if (this.match.getKit().isSettingEnabled(KitSettingRounds.class)) {
            timeLimit = 900_000; // 15 minutes
        } else {
            timeLimit = 1800_000; // 30 minutes (default)
        }

        if (this.match.getState() == MatchState.RUNNING && elapsedTime >= timeLimit) {
            String formattedTime = TimeUtil.formatLongMin(timeLimit);

            List<String> message = localeService.getMessageList(GameMessagesLocaleImpl.MATCH_TIME_LIMIT_EXCEEDED_FORMAT);
            message.replaceAll(line -> line.replace("{time-limit}", formattedTime));
            message.forEach(line -> this.match.sendMessage(CC.translate(line)));

            this.match.setState(MatchState.ENDING_MATCH);
            this.match.getRunnable().setStage(4);

            return true;
        }

        return false;
    }

    public void sendDisclaimer() {
        LocaleService localeService = this.plugin.getService(LocaleService.class);
        boolean isEnabled = localeService.getBoolean(GameMessagesLocaleImpl.MATCH_STARTED_DISCLAIMER_ENABLED_BOOLEAN);
        if (!isEnabled) {
            return;
        }

        Kit kit = this.match.getKit();
        String disclaimer = kit.getDisclaimer() == null ? "&c&lError: Missing Disclaimer" : kit.getDisclaimer();

        List<String> format = localeService.getMessageList(GameMessagesLocaleImpl.MATCH_STARTED_DISCLAIMER_FORMAT);
        format.forEach(message -> this.match.sendMessage(message
                .replace("{kit-disclaimer}", disclaimer)
                .replace("{kit-name}", kit.getName())
        ));
    }

    public void sendStartingMessage() {
        LocaleService localeService = this.plugin.getService(LocaleService.class);

        boolean messageEnabled = localeService.getBoolean(GameMessagesLocaleImpl.MATCH_STARTING_MESSAGE_ENABLED_BOOLEAN);
        if (messageEnabled) {
            List<String> format = localeService.getMessageList(GameMessagesLocaleImpl.MATCH_STARTING_MESSAGE_FORMAT);
            format.forEach(message -> this.match.sendMessage(message
                    .replace("{kit-name}", this.match.getKit().getName())
                    .replace("{arena-name}", this.match.getArena().getName())
                    .replace("{stage}", String.valueOf(this.match.getRunnable().getStage()))
            ));
        }

        String header = localeService.getMessage(VisualsLocaleImpl.TITLE_MATCH_STARTING_HEADER).replace("{stage}", String.valueOf(this.match.getRunnable().getStage()));
        String footer = localeService.getMessage(VisualsLocaleImpl.TITLE_MATCH_STARTING_FOOTER);

        int fadeIn = localeService.getInt(VisualsLocaleImpl.TITLE_MATCH_STARTING_FADE_IN);
        int stay = localeService.getInt(VisualsLocaleImpl.TITLE_MATCH_STARTING_STAY);
        int fadeOut = localeService.getInt(VisualsLocaleImpl.TITLE_MATCH_STARTING_FADEOUT);

        this.match.sendTitle(header, footer, fadeIn, stay, fadeOut, false);
    }

    public void sendMatchStartedMessage() {
        LocaleService localeService = this.plugin.getService(LocaleService.class);

        boolean messageEnabled = localeService.getBoolean(GameMessagesLocaleImpl.MATCH_STARTED_MESSAGE_ENABLED_BOOLEAN);
        if (messageEnabled) {
            List<String> format = localeService.getMessageList(GameMessagesLocaleImpl.MATCH_STARTED_MESSAGE_FORMAT);
            format.forEach(message -> this.match.sendMessage(message
                    .replace("{kit-name}", this.match.getKit().getName())
                    .replace("{arena-name}", this.match.getArena().getName())
            ));
        }

        String header = localeService.getMessage(VisualsLocaleImpl.TITLE_MATCH_STARTED_HEADER);
        String footer = localeService.getMessage(VisualsLocaleImpl.TITLE_MATCH_STARTED_FOOTER);

        int fadeIn = localeService.getInt(VisualsLocaleImpl.TITLE_MATCH_STARTED_FADE_IN);
        int stay = localeService.getInt(VisualsLocaleImpl.TITLE_MATCH_STARTED_STAY);
        int fadeOut = localeService.getInt(VisualsLocaleImpl.TITLE_MATCH_STARTED_FADEOUT);

        this.match.sendTitle(header, footer, fadeIn, stay, fadeOut, false);
    }

    private void sendRestartingMessage() {
        LocaleService localeService = this.plugin.getService(LocaleService.class);

        if (!(this.match instanceof RoundsMatch)) {
            throw new IllegalStateException("Attempted to send restarting round title on a non-rounds match.");
        }

        RoundsMatch roundsMatch = (RoundsMatch) this.match;
        int currentRound = roundsMatch.getCurrentRound();

        String header = localeService.getMessage(VisualsLocaleImpl.TITLE_MATCH_RESTARTING_ROUND_HEADER).replace("{stage}", String.valueOf(this.match.getRunnable().getStage()));
        String footer = localeService.getMessage(VisualsLocaleImpl.TITLE_MATCH_RESTARTING_ROUND_FOOTER).replace("{current-round}", String.valueOf(currentRound));

        int fadeIn = localeService.getInt(VisualsLocaleImpl.TITLE_MATCH_RESTARTING_ROUND_FADE_IN);
        int stay = localeService.getInt(VisualsLocaleImpl.TITLE_MATCH_RESTARTING_ROUND_STAY);
        int fadeOut = localeService.getInt(VisualsLocaleImpl.TITLE_MATCH_RESTARTING_ROUND_FADEOUT);

        this.match.sendTitle(header, footer, fadeIn, stay, fadeOut, false);
    }

    /**
     * Send a message to all players that a new round has started.
     *
     * @param currentRound The current round number.
     */
    public void sendRoundStartedMessage(int currentRound) {
        LocaleService localeService = this.plugin.getService(LocaleService.class);

        boolean messageEnabled = localeService.getBoolean(GameMessagesLocaleImpl.MATCH_ROUND_STARTED_MESSAGE_ENABLED_BOOLEAN);
        if (!messageEnabled) {
            return;
        }

        List<String> format = localeService.getMessageList(GameMessagesLocaleImpl.MATCH_ROUND_STARTED_MESSAGE_FORMAT);
        format.forEach(message -> match.sendMessage(message
                .replace("{kit-name}", match.getKit().getName())
                .replace("{arena-name}", match.getArena().getName())
                .replace("{current-round}", String.valueOf(currentRound))
        ));
    }

    public void playSoundStarting() {
        this.match.playSound(Sound.NOTE_STICKS);
    }

    public void playSoundStarted() {
        this.match.playSound(Sound.FIREWORK_BLAST);
    }
}