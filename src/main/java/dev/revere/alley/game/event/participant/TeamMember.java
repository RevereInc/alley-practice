package dev.revere.alley.game.event.participant;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * @author Emmy
 * @project alley-practice
 * @since 31/07/2025
 */
@Getter
@Setter
public class TeamMember {
    private final UUID uuid;
    private final String username;
    private final boolean isDead = false;

    /**
     * Constructor for the TeamMember class.
     *
     * @param player the player to create a team member from.
     */
    public TeamMember(Player player) {
        this.uuid = player.getUniqueId();
        this.username = player.getName();
    }
}
