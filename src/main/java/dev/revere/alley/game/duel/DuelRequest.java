package dev.revere.alley.game.duel;

import dev.revere.alley.base.arena.Arena;
import dev.revere.alley.base.kit.Kit;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 17/10/2024 - 20:04
 */
@Getter
@Setter
public class DuelRequest {
    private final Player sender;
    private final Player target;

    private Kit kit;

    private Arena arena;

    private final long expireTime;
    private boolean party;

    /**
     * Instantiates a new Duel request.
     *
     * @param sender the sender
     * @param target the target
     * @param kit    the kit
     * @param arena  the arena
     */
    public DuelRequest(Player sender, Player target, Kit kit, Arena arena, boolean party) {
        this.sender = sender;
        this.target = target;
        this.kit = kit;
        this.arena = arena;
        this.party = party;
        this.expireTime = System.currentTimeMillis() + 30000L;
    }

    /**
     * Check if the duel request has expired.
     *
     * @return true if the duel request has expired, false otherwise
     */
    public boolean hasExpired() {
        return System.currentTimeMillis() > expireTime;
    }

    /**
     * Get the remaining time until the duel request expires.
     *
     * @return the remaining time until the duel request expires
     */
    public long getRemainingTime() {
        return expireTime - System.currentTimeMillis();
    }

    /**
     * Get the remaining time formatted as a string.
     *
     * @return the remaining time formatted as a string
     */
    public String getRemainingTimeFormatted() {
        long seconds = getRemainingTime() / 1000;
        long minutes = seconds / 60;
        return String.format("%02d:%02d", minutes, seconds % 60);
    }
}
