package dev.revere.alley.feature.party;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.common.text.CC;
import dev.revere.alley.feature.emoji.EmojiService;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Emmy
 * @project Alley
 * @date 21/05/2024 - 21:42
 */
@Getter
@Setter
public class Party {
    private Player leader;
    private PartyState state;
    private List<UUID> members;
    private List<UUID> bannedPlayers;

    /**
     * Constructor for the Party class.
     *
     * @param leader The leader of the party.
     */
    public Party(Player leader) {
        this.leader = leader;
        this.members = new ArrayList<>();
        this.members.add(leader.getUniqueId());
        this.bannedPlayers = new ArrayList<>();
        this.state = PartyState.PRIVATE;
    }

    /**
     * Sends a message to all party members.
     *
     * @param message The message to notify the party members of.
     */
    public void notifyParty(String message) {
        for (Map.Entry<String, String> entry : AlleyPlugin.getInstance().getService(EmojiService.class).getEmojis().entrySet()) {
            if (message.contains(entry.getKey())) {
                message = message.replace(entry.getKey(), entry.getValue());
            }
        }

        for (UUID member : members) {
            Player player = AlleyPlugin.getInstance().getServer().getPlayer(member);
            if (player != null) {
                player.sendMessage(CC.translate(message));
            }
        }
    }

    /**
     * Determines whether the specified player is the leader of the party.
     *
     * @param player The player to check.
     * @return True if the specified player is the leader of the party, false otherwise.
     */
    public boolean isLeader(Player player) {
        return leader == player;
    }

    /**
     * Checks if the party is private.
     *
     * @return True if the party is private, false otherwise.
     */
    public boolean isPrivate() {
        return this.state == PartyState.PRIVATE;
    }
}