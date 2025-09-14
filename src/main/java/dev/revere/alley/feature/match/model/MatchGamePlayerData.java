package dev.revere.alley.feature.match.model;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.core.locale.LocaleService;
import dev.revere.alley.core.locale.internal.impl.ServerLocaleImpl;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Remi
 * @project Alley
 * @date 5/27/2024
 */
@Getter
@Setter
public class MatchGamePlayerData {
    private int lives;
    private int score;
    private int kills;
    private int deaths;

    private int longestCombo;
    private int combo;

    private int hits;
    private int criticalHits;
    private int blockedHits;

    private int missedPotions;
    private int thrownPotions;

    private int wTaps;

    private BaseRaiderRole role;

    public MatchGamePlayerData() {
        this.lives = AlleyPlugin.getInstance().getService(LocaleService.class).getInt(ServerLocaleImpl.GAME_LIVES_PER_MATCH);
        this.score = 0;
    }

    /**
     * Method to handle an attack.
     */
    public void handleAttack() {
        this.hits++;
        this.combo++;

        if (this.combo > this.longestCombo) {
            this.longestCombo = this.combo;
        }
    }

    /**
     * Method to reset the combo.
     */
    public void resetCombo() {
        this.combo = 0;
    }

    public void incrementScore() {
        this.score++;
    }

    public void incrementKills() {
        this.kills++;
    }

    public void incrementDeaths() {
        this.deaths++;
    }

    public void incrementMissedPotions() {
        this.missedPotions++;
    }

    public void incrementThrownPotions() {
        this.thrownPotions++;
    }

    public void incrementCriticalHits() {
        this.criticalHits++;
    }

    public void incrementBlockedHits() {
        this.blockedHits++;
    }

    public void incrementWTaps() {
        this.wTaps++;
    }
}