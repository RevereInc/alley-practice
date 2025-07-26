package dev.revere.alley.game.match.task;

import dev.revere.alley.Alley;
import dev.revere.alley.base.kit.setting.impl.mode.KitSettingRounds;
import dev.revere.alley.config.ConfigService;
import dev.revere.alley.game.match.Match;
import dev.revere.alley.game.match.enums.MatchState;
import dev.revere.alley.tool.reflection.ReflectionService;
import dev.revere.alley.tool.reflection.impl.TitleReflectionServiceImpl;
import dev.revere.alley.util.chat.CC;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author Remi
 * @project Alley
 * @date 5/25/2024
 */
@Getter
@Setter
public class MatchTask extends BukkitRunnable {
    private final Match match;
    private int stage;

    /**
     * Constructor for the MatchTask class.
     *
     * @param match The match.
     */
    public MatchTask(Match match) {
        this.match = match;
        this.stage = 6;
    }

    @Override
    public void run() {
        this.stage--;

        if (this.hasToEnd()) return;

        switch (this.match.getState()) {
            case STARTING:
                if (this.stage == 0) {
                    Alley.getInstance().getServer().getScheduler().runTask(Alley.getInstance(), this.match::handleRoundStart);

                    this.match.setState(MatchState.RUNNING);
                    this.match.sendMessage(CC.translate("&aMatch has started. Good luck!"));

                    this.sendTitleStarted();
                    this.playSoundStarted();
                    this.sendDisclaimer();
                } else {
                    this.match.sendMessage(CC.translate("&a" + this.stage + "..."));

                    this.sendTitleStarting();
                    this.playSoundStarting();
                }
                break;
            case RESTARTING_ROUND:
                if (this.stage == 0) {
                    Alley.getInstance().getServer().getScheduler().runTask(Alley.getInstance(), this.match::handleRoundStart);
                    this.match.setState(MatchState.RUNNING);

                    this.match.sendMessage(CC.translate("&aRound Started!"));
                    this.playSoundStarted();
                } else {
                    this.match.sendMessage(CC.translate("&a" + this.stage + "..."));
                    this.sendTitleStarting();
                    this.playSoundStarting();
                }
                break;
            case ENDING_ROUND:
                if (this.match.canStartRound()) {
                    this.match.setState(MatchState.RESTARTING_ROUND);
                    this.match.getRunnable().setStage(4);
                }
                break;
            case ENDING_MATCH:
                if (this.stage == 0) {
                    Alley.getInstance().getServer().getScheduler().runTask(Alley.getInstance(), this.match::endMatch);
                }
                break;
        }
    }

    /**
     * End the match if the time is up based on different kit settings.
     *
     * @return If the time limit has been reached.
     */
    private boolean hasToEnd() {
        long elapsedTime = System.currentTimeMillis() - match.getStartTime();
        if (this.match.getKit().isSettingEnabled(KitSettingRounds.class)) {
            return checkTime(elapsedTime, 900_000); // 15 minutes
        } else {
            return this.checkTime(elapsedTime, 1800_000); // 30 minutes (default)
        }
    }

    /**
     * Check if the time is up in the match.
     *
     * @param elapsedTime The elapsed time.
     * @param timeLimit   The time limit.
     * @return If the match ended.
     */
    private boolean checkTime(long elapsedTime, long timeLimit) {
        if (this.match.getState() == MatchState.RUNNING && elapsedTime >= timeLimit) {
            this.match.sendMessage(CC.translate("&cMatch has ended due to time limit!"));
            this.match.setState(MatchState.ENDING_MATCH);
            this.stage = 4;
            return true;
        }
        return false;
    }

    private void sendTitleStarted() {
        this.match.getParticipants().forEach(gameParticipant -> gameParticipant.getPlayers().forEach(matchGamePlayer -> {
            Alley.getInstance().getService(ReflectionService.class).getReflectionService(TitleReflectionServiceImpl.class).sendTitle(
                    matchGamePlayer.getTeamPlayer(),
                    "&6&lMatch started",
                    "&fGood Luck!"
            );
        }));
    }

    private void sendTitleStarting() {
        this.match.getParticipants().forEach(gameParticipant -> gameParticipant.getPlayers().forEach(matchGamePlayer -> {
            Alley.getInstance().getService(ReflectionService.class).getReflectionService(TitleReflectionServiceImpl.class).sendTitle(
                    matchGamePlayer.getTeamPlayer(),
                    "&6&lMatch",
                    "&fStarts in &6" + this.stage + "s",
                    0, 23, 20
            );
        }));
    }

    /**
     * Send the disclaimer to the participants.
     */
    private void sendDisclaimer() {
        FileConfiguration config = Alley.getInstance().getService(ConfigService.class).getMessagesConfig();
        if (config.getBoolean("match.started.kit-disclaimer.enabled")) {
            if (this.match.getKit().getDisclaimer() == null) {
                Alley.getInstance().getService(ConfigService.class).getMessagesConfig().getStringList("match.started.kit-disclaimer.not-set").forEach(message -> this.match.sendMessage(CC.translate(message)));
                return;
            }

            config.getStringList("match.started.kit-disclaimer.format").forEach(message -> this.match.sendMessage(CC.translate(message)
                    .replace("{kit-disclaimer}", CC.translate(this.match.getKit().getDisclaimer()))
                    .replace("{kit-name}", this.match.getKit().getName())
            ));
        }
    }

    private void playSoundStarting() {
        this.match.playSound(Sound.NOTE_STICKS);
    }

    private void playSoundStarted() {
        this.match.playSound(Sound.FIREWORK_BLAST);
    }
}