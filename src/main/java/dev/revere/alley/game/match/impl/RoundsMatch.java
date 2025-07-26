package dev.revere.alley.game.match.impl;

import dev.revere.alley.Alley;
import dev.revere.alley.base.arena.Arena;
import dev.revere.alley.base.combat.CombatService;
import dev.revere.alley.base.kit.Kit;
import dev.revere.alley.base.kit.setting.impl.mode.KitSettingBridges;
import dev.revere.alley.base.kit.setting.impl.mode.KitSettingStickFight;
import dev.revere.alley.base.queue.Queue;
import dev.revere.alley.config.ConfigService;
import dev.revere.alley.game.match.enums.MatchState;
import dev.revere.alley.game.match.player.impl.MatchGamePlayerImpl;
import dev.revere.alley.game.match.player.participant.GameParticipant;
import dev.revere.alley.game.match.player.participant.TeamGameParticipant;
import dev.revere.alley.tool.reflection.ReflectionService;
import dev.revere.alley.tool.reflection.impl.TitleReflectionServiceImpl;
import dev.revere.alley.util.ListenerUtil;
import dev.revere.alley.util.PlayerUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.util.Vector;

/**
 * @author Emmy
 * @project Alley
 * @since 08/02/2025
 */
@Getter
public class RoundsMatch extends DefaultMatch {
    private final GameParticipant<MatchGamePlayerImpl> participantA;
    private final GameParticipant<MatchGamePlayerImpl> participantB;

    private GameParticipant<MatchGamePlayerImpl> winner;
    private GameParticipant<MatchGamePlayerImpl> loser;

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
    public RoundsMatch(Queue queue, Kit kit, Arena arena, boolean ranked, GameParticipant<MatchGamePlayerImpl> participantA, GameParticipant<MatchGamePlayerImpl> participantB, int rounds) {
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
        GameParticipant<MatchGamePlayerImpl> participant = this.participantA.containsPlayer(player.getUniqueId())
                ? this.participantA
                : this.participantB;
        participant.getLeader().getData().incrementDeaths();

        this.fallenPlayer = player;

        if (this.getKit().isSettingEnabled(KitSettingStickFight.class)) {
            Player lastAttacker = Alley.getInstance().getService(CombatService.class).getLastAttacker(player);
            if (lastAttacker == null) {
                GameParticipant<MatchGamePlayerImpl> opponent = this.participantA.containsPlayer(player.getUniqueId())
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
                TeamGameParticipant<MatchGamePlayerImpl> team = (TeamGameParticipant<MatchGamePlayerImpl>) participant;
                MatchGamePlayerImpl gamePlayer = team.getPlayers().stream()
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
                MatchGamePlayerImpl gamePlayer = participant.getLeader();
                gamePlayer.getData().incrementDeaths();
                gamePlayer.setDead(true);
                this.handleRoundEnd();
            }
            return;
        }

        super.handleDeath(player, cause);
    }

    @Override
    public void handleParticipant(Player player, MatchGamePlayerImpl gamePlayer) {
        GameParticipant<MatchGamePlayerImpl> participant = this.participantA.containsPlayer(player.getUniqueId())
                ? this.participantA
                : this.participantB;
        if (participant.getLeader().getData().getScore() == this.rounds) {
            GameParticipant<MatchGamePlayerImpl> opponent = participant == this.participantA ? this.participantB : this.participantA;
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
                || (this.participantA.getAllPlayers().stream().allMatch(MatchGamePlayerImpl::isDisconnected)
                || this.participantB.getAllPlayers().stream().allMatch(MatchGamePlayerImpl::isDisconnected));
    }

    @Override
    public boolean canEndMatch() {
        return (this.participantA.getLeader().getData().getScore() == this.rounds || this.participantB.getLeader().getData().getScore() == this.rounds)
                || (this.participantA.getAllPlayers().stream().allMatch(MatchGamePlayerImpl::isDisconnected)
                || this.participantB.getAllPlayers().stream().allMatch(MatchGamePlayerImpl::isDisconnected));
    }

    /**
     * Broadcasts a message to all players in the match when a player scores.
     *
     * @param winner The player who scored.
     * @param loser  The player who was scored on.
     * @param scorer The name of the player who scored.
     */
    public void broadcastPlayerScoreMessage(GameParticipant<MatchGamePlayerImpl> winner, GameParticipant<MatchGamePlayerImpl> loser, String scorer) {
        ChatColor teamWinnerColor = this.getTeamColor(winner);
        ChatColor teamLoserColor = this.getTeamColor(loser);

        String configPath = this.isTeamMatch() ? "match.scored.format.team" : "match.scored.format.solo";

        FileConfiguration config = Alley.getInstance().getService(ConfigService.class).getMessagesConfig();
        if (config.getBoolean("match.scored.enabled")) {
            for (String message : config.getStringList(configPath)) {
                this.notifyAll(message
                        .replace("{scorer}", scorer)
                        .replace("{winner}", winner.getLeader().getUsername())
                        .replace("{winner-color}", teamWinnerColor.toString())
                        .replace("{winner-goals}", String.valueOf(winner.getLeader().getData().getScore()))
                        .replace("{loser}", loser.getLeader().getUsername())
                        .replace("{loser-color}", teamLoserColor.toString())
                        .replace("{loser-goals}", String.valueOf(loser.getLeader().getData().getScore()))
                );
            }
        }

        this.getParticipants().forEach(gameParticipant -> gameParticipant.getPlayers().forEach(uuid -> {
            Player player = this.plugin.getServer().getPlayer(uuid.getUuid());
            Alley.getInstance().getService(ReflectionService.class).getReflectionService(TitleReflectionServiceImpl.class).sendTitle(
                    player,
                    teamWinnerColor.toString() + scorer + " &fhas scored!",
                    "&f" + winner.getLeader().getData().getScore() + " &7/&f " + this.rounds
            );
        }));
    }
}
