package dev.revere.alley.api.assemble;

import dev.revere.alley.api.assemble.events.AssembleBoardCreatedEvent;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class AssembleBoard {
    private final AssembleServiceImpl assembleServiceImpl;

    private final List<AssembleBoardEntry> entries;
    private final List<String> identifiers;

    private final UUID uuid;

    /**
     * Assemble Board.
     *
     * @param player   that the board belongs to.
     * @param assembleServiceImpl instance.
     */
    public AssembleBoard(Player player, AssembleServiceImpl assembleServiceImpl) {
        this.assembleServiceImpl = assembleServiceImpl;
        this.entries = new ArrayList<>();
        this.identifiers = new ArrayList<>();
        this.uuid = player.getUniqueId();
        this.setup(player);
    }

    /**
     * Get's a player's bukkit scoreboard.
     *
     * @return either existing scoreboard or new scoreboard.
     */
    public Scoreboard getScoreboard() {
        Player player = Bukkit.getPlayer(getUuid());
        if (this.assembleServiceImpl.isHook() || player.getScoreboard() != Bukkit.getScoreboardManager().getMainScoreboard()) {
            return player.getScoreboard();
        }

        return Bukkit.getScoreboardManager().getNewScoreboard();
    }

    /**
     * Get's the player's scoreboard objective.
     *
     * @return either existing objecting or new objective.
     */
    public Objective getObjective() {
        Scoreboard scoreboard = this.getScoreboard();
        if (scoreboard.getObjective("Assemble") == null) {
            Objective objective = scoreboard.registerNewObjective("Assemble", "dummy");
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
            objective.setDisplayName(getAssembleServiceImpl().getAdapter().getTitle(Bukkit.getPlayer(getUuid())));
            return objective;
        }

        return scoreboard.getObjective("Assemble");
    }

    /**
     * Setup the board for a player.
     *
     * @param player who's board to setup.
     */
    private void setup(Player player) {
        Scoreboard scoreboard = getScoreboard();
        player.setScoreboard(scoreboard);
        this.getObjective();

        if (this.assembleServiceImpl.isCallEvents()) {
            AssembleBoardCreatedEvent createdEvent = new AssembleBoardCreatedEvent(this);
            Bukkit.getPluginManager().callEvent(createdEvent);
        }
    }

    /**
     * Get the board entry at a specific position.
     *
     * @param pos to find entry.
     * @return entry if it isn't out of range.
     */
    public AssembleBoardEntry getEntryAtPosition(int pos) {
        return pos >= this.entries.size() ? null : this.entries.get(pos);
    }

    /**
     * Get the unique identifier for position in scoreboard.
     *
     * @param position for identifier.
     * @return unique identifier.
     */
    public String getUniqueIdentifier(int position) {
        String identifier = this.getRandomChatColor(position) + ChatColor.WHITE;

        while (this.identifiers.contains(identifier)) {
            identifier = identifier + this.getRandomChatColor(position) + ChatColor.WHITE;
        }

        if (identifier.length() > 16) {
            return this.getUniqueIdentifier(position);
        }

        this.identifiers.add(identifier);

        return identifier;
    }

    /**
     * Gets a ChatColor based off the position in the collection.
     *
     * @param position of entry.
     * @return ChatColor adjacent to position.
     */
    private String getRandomChatColor(int position) {
        return this.assembleServiceImpl.getChatColorCache()[position].toString();
    }
}