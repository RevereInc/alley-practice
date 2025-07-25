package dev.revere.alley.game.match.impl;

import dev.revere.alley.base.arena.Arena;
import dev.revere.alley.base.kit.Kit;
import dev.revere.alley.base.queue.Queue;
import dev.revere.alley.game.match.Match;
import dev.revere.alley.game.match.player.impl.MatchGamePlayerImpl;
import dev.revere.alley.game.match.player.participant.GameParticipant;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @since 25/04/2025
 */
public class FFAMatch extends Match {
    private final List<GameParticipant<MatchGamePlayerImpl>> participants;
    private GameParticipant<MatchGamePlayerImpl> winner;

    /**
     * Constructor for the MatchFFAImpl class.
     *
     * @param queue        The queue associated with this match.
     * @param kit          The kit used in this match.
     * @param arena        The arena where the match takes place.
     * @param participants The list of participants in the match.
     */
    public FFAMatch(Queue queue, Kit kit, Arena arena, List<GameParticipant<MatchGamePlayerImpl>> participants) {
        super(queue, kit, arena, false);
        this.participants = new ArrayList<>(participants);
    }

    @Override
    public void setupPlayer(Player player) {
        super.setupPlayer(player);

        Location spawn = this.getArena().getPos1();
        player.teleport(spawn);
    }

    @Override
    public void handleRespawn(Player player) {
        player.spigot().respawn();

        //this.addSpectator(player);

        player.teleport(this.getArena().getCenter());
    }

    @Override
    public void handleDisconnect(Player player) {

    }

    @Override
    public List<GameParticipant<MatchGamePlayerImpl>> getParticipants() {
        return participants;
    }

    @Override
    public boolean canStartRound() {
        return participants.stream().noneMatch(GameParticipant::isAllDead);
    }

    @Override
    public boolean canEndRound() {
        long aliveCount = participants.stream().filter(p -> !p.isAllDead()).count();
        return aliveCount <= 1;
    }

    @Override
    public boolean canEndMatch() {
        return this.canEndRound();
    }

    @Override
    public void handleDeathItemDrop(Player player, PlayerDeathEvent event) {
        event.getDrops().clear();
    }

    @Override
    public void handleRoundEnd() {
        super.handleRoundEnd();

        this.participants.stream()
                .filter(participant -> !participant.isAllDead())
                .findFirst()
                .ifPresent(remaining -> {
                    this.winner = remaining;
                    this.winner.getLeader().setEliminated(true);

                    // temporarily, couldnt be asked to mess with clickables again

                    this.sendMessage("Winner: " + this.winner.getLeader().getUsername());

                    String losers = this.participants.stream()
                            .filter(participant -> participant != this.winner)
                            .map(GameParticipant::getLeader)
                            .map(MatchGamePlayerImpl::getUsername)
                            .reduce((a, b) -> a + ", " + b)
                            .orElse("None");

                    this.sendMessage("Losers: " + losers);
                })
        ;
    }
}
