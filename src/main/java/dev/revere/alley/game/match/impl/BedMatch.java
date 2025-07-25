package dev.revere.alley.game.match.impl;

import dev.revere.alley.base.arena.Arena;
import dev.revere.alley.base.kit.Kit;
import dev.revere.alley.base.queue.Queue;
import dev.revere.alley.game.match.player.impl.MatchGamePlayerImpl;
import dev.revere.alley.game.match.player.participant.GameParticipant;
import dev.revere.alley.game.match.player.participant.TeamGameParticipant;
import dev.revere.alley.util.ListenerUtil;
import dev.revere.alley.util.PlayerUtil;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * @author Emmy
 * @project Alley
 * @since 21/05/2025
 */
@Getter
public class BedMatch extends DefaultMatch {
    private final GameParticipant<MatchGamePlayerImpl> participantA;
    private final GameParticipant<MatchGamePlayerImpl> participantB;

    /**
     * Constructor for the MatchBedImpl class.
     *
     * @param queue        The queue of the match.
     * @param kit          The kit of the match.
     * @param arena        The arena of the match.
     * @param ranked       Whether the match is ranked or not.
     * @param participantA The first participant.
     * @param participantB The second participant.
     */
    public BedMatch(Queue queue, Kit kit, Arena arena, boolean ranked, GameParticipant<MatchGamePlayerImpl> participantA, GameParticipant<MatchGamePlayerImpl> participantB) {
        super(queue, kit, arena, ranked, participantA, participantB);
        this.participantA = participantA;
        this.participantB = participantB;
    }

    @Override
    public boolean canEndRound() {
        return (this.participantA.isAllEliminated() || this.participantB.isAllEliminated())
                || (this.participantA.getAllPlayers().stream().allMatch(MatchGamePlayerImpl::isDisconnected)
                || this.participantB.getAllPlayers().stream().allMatch(MatchGamePlayerImpl::isDisconnected));
    }

    @Override
    public boolean canStartRound() {
        return !this.participantA.isAllEliminated() && !this.participantB.isAllEliminated();
    }

    @Override
    public void handleParticipant(Player player, MatchGamePlayerImpl gamePlayer) {
        GameParticipant<MatchGamePlayerImpl> gameParticipant = this.getParticipantA().containsPlayer(player.getUniqueId())
                ? this.getParticipantA()
                : this.getParticipantB();

        if (gameParticipant.isBedBroken()) {
            GameParticipant<MatchGamePlayerImpl> participant = getParticipant(player);
            if (participant == null) {
                return;
            }

            gamePlayer.setEliminated(true);
        }

        super.handleParticipant(player, gamePlayer);
    }

    @Override
    public void handleDeathItemDrop(Player player, PlayerDeathEvent event) {
        GameParticipant<MatchGamePlayerImpl> participant = this.participantA.containsPlayer(player.getUniqueId())
                ? this.participantA
                : this.participantB;

        if (participant.isBedBroken()) {
            ListenerUtil.clearDroppedItemsOnDeath(event, player);
            return;
        }
        super.handleDeathItemDrop(player, event);
    }

    @Override
    protected boolean shouldHandleRegularRespawn(Player player) {
        return false;
    }

    @Override
    public void handleRespawn(Player player) {
        PlayerUtil.reset(player, true, true);

        Location spawnLocation = this.getParticipants().get(0).containsPlayer(player.getUniqueId()) ? this.getArena().getPos1() : this.getArena().getPos2();
        ListenerUtil.teleportAndClearSpawn(player, spawnLocation);

        this.giveLoadout(player, this.getKit());
        this.applyColorKit(player);
    }

    /**
     * Alerts the participants about the bed destruction.
     *
     * @param breaker  The player who broke the bed.
     * @param opponent The opponent whose bed was destroyed.
     */
    public void alertBedDestruction(Player breaker, GameParticipant<MatchGamePlayerImpl> opponent) {
        String destructionMessage = "&6&lBED DESTRUCTION!";
        String subMessage = getBreakMessage(breaker, opponent);

        this.sendMessage(
                Arrays.asList(
                        "",
                        destructionMessage,
                        subMessage,
                        ""
                )
        );

        this.sendTitle(
                destructionMessage,
                subMessage
        );

        this.playSound(Sound.ENDERDRAGON_GROWL);
    }

    private @NotNull String getBreakMessage(Player breaker, GameParticipant<MatchGamePlayerImpl> opponent) {
        String subMessage;
        if (opponent instanceof TeamGameParticipant) {
            String opponentTeamName;
            String color;

            if (this.getParticipantA() == opponent) {
                opponentTeamName = "&9Blue Team";
                color = "&c";
            } else {
                opponentTeamName = "&cRed Team";
                color = "&9";
            }

            subMessage = color + breaker.getName() + " &7has destroyed the bed of " + opponentTeamName + "&7!";
        } else {
            subMessage = " &c" + breaker.getName() + " &7has destroyed the bed of &9" + opponent.getLeader().getUsername() + "&7!";
        }
        return subMessage;
    }

    /**
     * Checks if a block is near a bed.
     *
     * @param block The block to check.
     * @return true if the block is near a bed, false otherwise.
     */
    public boolean isNearBed(Block block) {
        Location center = block.getLocation();
        for (int x = -8; x <= 1; x++) {
            for (int y = -8; y <= 1; y++) {
                for (int z = -8; z <= 1; z++) {
                    Block relativeBlock = new Location(block.getWorld(), center.getX() + x, center.getY() + y, center.getZ() + z).getBlock();
                    if (relativeBlock.getType() == Material.BED_BLOCK) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
