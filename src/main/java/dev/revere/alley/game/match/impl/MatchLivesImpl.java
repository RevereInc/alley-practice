package dev.revere.alley.game.match.impl;

import dev.revere.alley.base.arena.AbstractArena;
import dev.revere.alley.base.kit.Kit;
import dev.revere.alley.base.queue.Queue;
import dev.revere.alley.game.match.player.data.MatchGamePlayerData;
import dev.revere.alley.game.match.player.impl.MatchGamePlayerImpl;
import dev.revere.alley.game.match.player.participant.GameParticipant;
import dev.revere.alley.util.PlayerUtil;
import dev.revere.alley.util.TaskUtil;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 5/21/2024
 */
@Getter
public class MatchLivesImpl extends MatchRegularImpl {
    private final GameParticipant<MatchGamePlayerImpl> participantA;
    private final GameParticipant<MatchGamePlayerImpl> participantB;

    private GameParticipant<MatchGamePlayerImpl> winner;
    private GameParticipant<MatchGamePlayerImpl> loser;

    /**
     * Constructor for the MatchLivesImpl class.
     *
     * @param queue        The queue of the match.
     * @param kit          The kit of the match.
     * @param arena        The arena of the match.
     * @param ranked       Whether the match is ranked.
     * @param participantA The first participant.
     * @param participantB The second participant.
     */
    public MatchLivesImpl(Queue queue, Kit kit, AbstractArena arena, boolean ranked, GameParticipant<MatchGamePlayerImpl> participantA, GameParticipant<MatchGamePlayerImpl> participantB) {
        super(queue, kit, arena, ranked, participantA, participantB);
        this.participantA = participantA;
        this.participantB = participantB;
    }

    @Override
    public boolean canStartRound() {
        return participantA.getPlayer().getData().getLives() > 0 && participantB.getPlayer().getData().getLives() > 0;
    }

    @Override
    public boolean canEndRound() {
        return participantA.isAllDead() || participantB.isAllDead();
    }

    @Override
    public boolean canEndMatch() {
        return participantA.getPlayer().getData().getLives() <= 0 || participantB.getPlayer().getData().getLives() <= 0;
    }

    /**
     * Reduces the lives of a participant by one.
     *
     * @param participant The participant whose lives are to be reduced.
     */
    public void reduceLife(GameParticipant<MatchGamePlayerImpl> participant) {
        MatchGamePlayerData data = participant.getPlayer().getData();
        data.setLives(data.getLives() - 1);
        if (data.getLives() <= 0) {
            this.determineWinnerAndLoser();
        }
    }

    @Override
    public void handleDeath(Player player) {
        GameParticipant<MatchGamePlayerImpl> participant = this.participantA.containsPlayer(player.getUniqueId()) ? this.participantA : this.participantB;
        this.reduceLife(participant);

        if (participant.getPlayer().getData().getLives() > 0) {
            TaskUtil.runTaskLater(() -> this.startRespawnProcess(player), 5L);
        } else {
            super.handleDeath(player);
        }
    }

    /**
     * Determines the winner and loser of the match.
     */
    private void determineWinnerAndLoser() {
        if (this.participantA.getPlayer().getData().getLives() <= 0) {
            this.winner = this.participantB;
            this.loser = this.participantA;
        } else if (this.participantB.getPlayer().getData().getLives() <= 0) {
            this.winner = this.participantA;
            this.loser = this.participantB;
        }
    }

    @Override
    public void handleRespawn(Player player) {
        PlayerUtil.reset(player, true);

        Location spawnLocation = this.getParticipants().get(0).containsPlayer(player.getUniqueId()) ? this.getArena().getPos1() : this.getArena().getPos2();
        player.teleport(spawnLocation);

        this.giveLoadout(player, this.getKit());
        this.applyWoolAndArmorColor(player);

        this.notifyParticipants("&b" + player.getName() + " &ahas respawned");
        this.notifySpectators("&b" + player.getName() + " &ahas respawned");
    }

    @Override
    public void handleDisconnect(Player player) {
        super.handleDeath(player);
    }
}