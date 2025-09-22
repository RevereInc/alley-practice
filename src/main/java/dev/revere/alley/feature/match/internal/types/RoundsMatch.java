package dev.revere.alley.feature.match.internal.types;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.common.ListenerUtil;
import dev.revere.alley.common.PlayerUtil;
import dev.revere.alley.core.locale.LocaleService;
import dev.revere.alley.core.locale.internal.impl.VisualsLocaleImpl;
import dev.revere.alley.core.locale.internal.impl.message.GameMessagesLocaleImpl;
import dev.revere.alley.feature.arena.Arena;
import dev.revere.alley.feature.combat.CombatService;
import dev.revere.alley.feature.kit.Kit;
import dev.revere.alley.feature.kit.setting.types.mode.KitSettingBridges;
import dev.revere.alley.feature.kit.setting.types.mode.KitSettingStickFight;
import dev.revere.alley.feature.match.MatchState;
import dev.revere.alley.feature.match.model.GameParticipant;
import dev.revere.alley.feature.match.model.TeamGameParticipant;
import dev.revere.alley.feature.match.model.internal.MatchGamePlayer;
import dev.revere.alley.feature.queue.Queue;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.util.Vector;

import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @since 08/02/2025
 */
@Getter
public class RoundsMatch extends DefaultMatch {
    private final GameParticipant<MatchGamePlayer> participantA;
    private final GameParticipant<MatchGamePlayer> participantB;

    private GameParticipant<MatchGamePlayer> winner;
    private GameParticipant<MatchGamePlayer> loser;

    private final int rounds;
    private int currentRound;

    @Setter
    private String scorer;
    private Player fallenPlayer;

    /**
     * Constructor for the MatchRoundsImpl class.
     *
     * @param queue        The queue of the match.
     * @param kit          The kit of the match.
     * @param arena        The arena of the match.
     * @param ranked       Whether the match is ranked or not.
     * @param participantA The first participant.
     * @param participantB The second participant.
     * @param rounds       The amount of rounds the match will have.
     */
    public RoundsMatch(Queue queue, Kit kit, Arena arena, boolean ranked, GameParticipant<MatchGamePlayer> participantA, GameParticipant<MatchGamePlayer> participantB, int rounds) {
        super(queue, kit, arena, ranked, participantA, participantB);
        this.participantA = participantA;
        this.participantB = participantB;
        this.rounds = rounds;
        this.scorer = "Unknown";
    }

    @Override
    public void handleRoundEnd() {
        this.winner = this.participantA.isAllDead() ? this.participantB : this.participantA;
        this.winner.getLeader().getData().incrementScore();
        this.loser = this.participantA.isAllDead() ? this.participantA : this.participantB;
        this.currentRound++;

        this.broadcastPlayerScoreMessage(this.winner, this.loser, this.scorer);

        if (this.getKit().isSettingEnabled(KitSettingStickFight.class)) {
            if (this.canEndMatch()) {
                this.removePlacedBlocks();
                this.setEndTime(System.currentTimeMillis());
                this.setState(MatchState.ENDING_MATCH);
                this.getRunnable().setStage(4);
                super.handleRoundEnd();
            } else {
                this.removePlacedBlocks();
                this.handleRespawn(this.fallenPlayer);
                this.setState(MatchState.ENDING_ROUND);

                this.getParticipants().forEach(participant -> participant.getPlayers().forEach(playerParticipant -> {
                    Player player1 = playerParticipant.getTeamPlayer();
                    player1.setVelocity(new Vector(0, 0, 0));
                    playerParticipant.setDead(false);

                    super.setupPlayer(player1);
                }));
            }
        } else {
            if (this.canEndMatch()) {
                super.handleRoundEnd();
            } else {
                if (!getKit().isSettingEnabled(KitSettingBridges.class)) {
                    this.removePlacedBlocks();
                }
                this.setState(MatchState.ENDING_ROUND);

                this.getParticipants().forEach(participant -> participant.getPlayers().forEach(playerParticipant -> {
                    Player player = playerParticipant.getTeamPlayer();
                    player.setVelocity(new Vector(0, 0, 0));
                    playerParticipant.setDead(false);

                    super.setupPlayer(player);
                }));
            }
        }
    }

    @Override
    public void handleDeath(Player player, EntityDamageEvent.DamageCause cause) {
        GameParticipant<MatchGamePlayer> participant = this.participantA.containsPlayer(player.getUniqueId())
                ? this.participantA
                : this.participantB;
        participant.getLeader().getData().incrementDeaths();

        this.fallenPlayer = player;

        if (this.getKit().isSettingEnabled(KitSettingStickFight.class)) {
            Player lastAttacker = AlleyPlugin.getInstance().getService(CombatService.class).getLastAttacker(player);
            if (lastAttacker == null) {
                GameParticipant<MatchGamePlayer> opponent = this.participantA.containsPlayer(player.getUniqueId())
                        ? this.participantB
                        : this.participantA;

                this.setScorer(opponent.getLeader().getUsername());
            } else {
                this.setScorer(lastAttacker.getName());
            }

            if (this.participantA.containsPlayer(player.getUniqueId())) {
                participant = this.participantA;
            } else {
                participant = this.participantB;
            }

            if (participant instanceof TeamGameParticipant<?>) {
                TeamGameParticipant<MatchGamePlayer> team = (TeamGameParticipant<MatchGamePlayer>) participant;
                MatchGamePlayer gamePlayer = team.getPlayers().stream()
                        .filter(gamePlayer1 -> gamePlayer1.getUuid().equals(player.getUniqueId()))
                        .findFirst()
                        .orElse(null);

                if (gamePlayer != null) {
                    team.getPlayers().forEach(matchGamePlayer -> {
                        matchGamePlayer.getData().incrementDeaths();
                        matchGamePlayer.setDead(true);
                    });
                    this.handleRoundEnd();
                }
            } else {
                MatchGamePlayer gamePlayer = participant.getLeader();
                gamePlayer.getData().incrementDeaths();
                gamePlayer.setDead(true);
                this.handleRoundEnd();
            }
            return;
        }

        super.handleDeath(player, cause);
    }

    @Override
    public void handleParticipant(Player player, MatchGamePlayer gamePlayer) {
        GameParticipant<MatchGamePlayer> participant = this.participantA.containsPlayer(player.getUniqueId())
                ? this.participantA
                : this.participantB;
        if (participant.getLeader().getData().getScore() == this.rounds) {
            GameParticipant<MatchGamePlayer> opponent = participant == this.participantA ? this.participantB : this.participantA;
            opponent.getLeader().setEliminated(true);
        }
    }

    @Override
    public void handleRespawn(Player player) {
        player.spigot().respawn();
        PlayerUtil.reset(player, false, true);

        Location spawnLocation = getParticipants().get(0).containsPlayer(player.getUniqueId()) ? this.getArena().getPos1() : this.getArena().getPos2();
        ListenerUtil.teleportAndClearSpawn(player, spawnLocation);

        this.giveLoadout(player, this.getKit());
        this.applyColorKit(player);
    }

    @Override
    public boolean canStartRound() {
        return this.participantA.getLeader().getData().getScore() < this.rounds && this.participantB.getLeader().getData().getScore() < this.rounds;
    }

    @Override
    public boolean canEndRound() {
        return (this.participantA.isAllDead() || this.participantB.isAllDead())
                || (this.participantA.getAllPlayers().stream().allMatch(MatchGamePlayer::isDisconnected)
                || this.participantB.getAllPlayers().stream().allMatch(MatchGamePlayer::isDisconnected));
    }

    @Override
    public boolean canEndMatch() {
        return (this.participantA.getLeader().getData().getScore() == this.rounds || this.participantB.getLeader().getData().getScore() == this.rounds)
                || (this.participantA.getAllPlayers().stream().allMatch(MatchGamePlayer::isDisconnected)
                || this.participantB.getAllPlayers().stream().allMatch(MatchGamePlayer::isDisconnected));
    }

    /**
     * Broadcasts a message to all players in the match when a player scores.
     *
     * @param winner The player who scored.
     * @param loser  The player who was scored on.
     * @param scorer The name of the player who scored.
     */
    public void broadcastPlayerScoreMessage(GameParticipant<MatchGamePlayer> winner, GameParticipant<MatchGamePlayer> loser, String scorer) {
        LocaleService localeService = AlleyPlugin.getInstance().getService(LocaleService.class);

        boolean messageEnabled = localeService.getBoolean(GameMessagesLocaleImpl.MATCH_SCORED_MESSAGE_ENABLED_BOOLEAN);
        if (messageEnabled) {
            List<String> message;
            if (this.isTeamMatch()) {
                message = localeService.getMessageList(GameMessagesLocaleImpl.MATCH_SCORED_MESSAGE_SOLO_FORMAT);
            } else {
                message = localeService.getMessageList(GameMessagesLocaleImpl.MATCH_SCORED_MESSAGE_TEAM_FORMAT);
            }

            message.forEach(line -> this.notifyAll(line
                    .replace("{scorer}", scorer)
                    .replace("{winner}", winner.getLeader().getUsername())
                    .replace("{winner-color}", String.valueOf(this.getTeamColor(winner)))
                    .replace("{winner-goals}", String.valueOf(winner.getLeader().getData().getScore()))
                    .replace("{loser}", loser.getLeader().getUsername())
                    .replace("{loser-color}", String.valueOf(this.getTeamColor(loser)))
                    .replace("{loser-goals}", String.valueOf(loser.getLeader().getData().getScore()))
                    .replace("{current-score}", String.valueOf(winner.getLeader().getData().getScore()))
                    .replace("{max-rounds}", String.valueOf(this.rounds))
            ));
        }

        String header = localeService.getMessage(VisualsLocaleImpl.TITLE_TEAM_SCORED_HEADER).replace("{scorer}", scorer);
        String footer = localeService.getMessage(VisualsLocaleImpl.TITLE_TEAM_SCORED_FOOTER)
                .replace("{current-score}", String.valueOf(winner.getLeader().getData().getScore()))
                .replace("{max-rounds}", String.valueOf(this.rounds));
        int fadeIn = localeService.getInt(VisualsLocaleImpl.TITLE_TEAM_SCORED_FADE_IN);
        int stay = localeService.getInt(VisualsLocaleImpl.TITLE_TEAM_SCORED_STAY);
        int fadeOut = localeService.getInt(VisualsLocaleImpl.TITLE_TEAM_SCORED_FADEOUT);

        this.sendTitle(header, footer, fadeIn, stay, fadeOut, true);
    }
}
