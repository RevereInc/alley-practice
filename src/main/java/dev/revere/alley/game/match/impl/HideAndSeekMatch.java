package dev.revere.alley.game.match.impl;

import dev.revere.alley.base.arena.Arena;
import dev.revere.alley.base.arena.ArenaService;
import dev.revere.alley.base.kit.Kit;
import dev.revere.alley.base.queue.Queue;
import dev.revere.alley.game.match.player.GamePlayer;
import dev.revere.alley.game.match.player.impl.MatchGamePlayerImpl;
import dev.revere.alley.game.match.player.participant.GameParticipant;
import dev.revere.alley.game.match.player.participant.TeamGameParticipant;
import dev.revere.alley.util.ListenerUtil;
import dev.revere.alley.util.PlayerUtil;
import dev.revere.alley.util.chat.CC;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Remi
 * @project alley-practice
 * @date 22/07/2025
 */
@Getter
public class HideAndSeekMatch extends DefaultMatch {
    private final GameParticipant<MatchGamePlayerImpl> participantA;
    private final GameParticipant<MatchGamePlayerImpl> participantB;

    private BukkitTask seekerReleaseTask;
    private BukkitTask gameEndTask;
    private boolean timeExpired = false;

    private final int hidingTimeSeconds = 180;
    private final int gameTimeSeconds = 600;
    private final int hiderHealthHearts = 3;

    private final ArenaService arenaService = plugin.getService(ArenaService.class);
    private final Arena intermissionArena = arenaService.getArenaByName("IntermissionArena");

    /**
     * Constructor for the MatchRegularImpl class.
     *
     * @param queue        The queue of the match.
     * @param kit          The kit of the match.
     * @param arena        The arena of the match.
     * @param ranked       Whether the match is ranked or not.
     * @param participantA The first participant.
     * @param participantB The second participant.
     */
    public HideAndSeekMatch(Queue queue, Kit kit, Arena arena, boolean ranked, GameParticipant<MatchGamePlayerImpl> participantA, GameParticipant<MatchGamePlayerImpl> participantB) {
        super(queue, kit, arena, ranked, participantA, participantB);

        if (arena == null) {
            throw new IllegalArgumentException("IntermissionArena cannot be null for MatchHideAndSeekImpl");
        }

        List<MatchGamePlayerImpl> allPlayers = new ArrayList<>();
        allPlayers.addAll(participantA.getPlayers());
        allPlayers.addAll(participantB.getPlayers());
        Collections.shuffle(allPlayers);

        List<MatchGamePlayerImpl> seekers = new ArrayList<>();
        List<MatchGamePlayerImpl> hiders = new ArrayList<>();

        int seekerCount = Math.max(1, (int) Math.ceil(allPlayers.size() * 0.2));
        for (int i = 0; i < allPlayers.size(); i++) {
            MatchGamePlayerImpl player = allPlayers.get(i);
            if (i < seekerCount) {
                seekers.add(player);
            } else {
                hiders.add(player);
            }
        }

        this.participantA = new TeamGameParticipant<>(seekers.get(0));
        this.participantB = new TeamGameParticipant<>(hiders.get(0));

        for (int i = 1; i < seekers.size(); i++) {
            this.participantA.addPlayer(seekers.get(i));
        }

        for (int i = 1; i < hiders.size(); i++) {
            this.participantB.addPlayer(hiders.get(i));
        }
    }

    @Override
    public void setupPlayer(Player player) {
        super.setupPlayer(player);

        if (getParticipantB().containsPlayer(player.getUniqueId())) {
            player.setMaxHealth(hiderHealthHearts * 2.0);
            player.setHealth(player.getMaxHealth());
        } else {
            ListenerUtil.teleportAndClearSpawn(player, intermissionArena.getPos1());
        }
    }

    @Override
    public void denyPlayerMovement(List<GameParticipant<MatchGamePlayerImpl>> participants) {
        GameParticipant<?> hiders = this.getParticipantB();
        Location hidersSpawn = this.getArena().getPos2();

        if (hiders == null || hidersSpawn == null) return;

        for (GamePlayer gamePlayer : hiders.getPlayers()) {
            Player participantPlayer = gamePlayer.getTeamPlayer();
            if (participantPlayer != null) {
                this.teleportBackIfMoved(participantPlayer, hidersSpawn);
            }
        }
    }

    @Override
    public void startMatch() {
        super.startMatch();

        int preMatchCountdownSeconds = 5;

        long totalDelayTicks = (hidingTimeSeconds + preMatchCountdownSeconds) * 20L;

        this.seekerReleaseTask = plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            sendTitle("&c&lSeekers Released!", "&7The hunt has begun!");
            playSound(Sound.ENDERDRAGON_GROWL);

            getParticipantA().getPlayers().forEach(seeker -> {
                Player p = plugin.getServer().getPlayer(seeker.getUuid());
                if (p != null) {
                    p.teleport(getArena().getPos2());
                }
            });

            this.gameEndTask = plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                this.timeExpired = true;

                new ArrayList<>(getParticipantA().getPlayers()).forEach(seeker -> {
                    Player p = Bukkit.getPlayer(seeker.getUuid());
                    if (p != null && !seeker.isDead()) {
                        handleDeath(p, EntityDamageEvent.DamageCause.CUSTOM);
                    }
                });
            }, gameTimeSeconds * 20L);

        }, totalDelayTicks);
    }

    @Override
    public void handleDeath(Player player, EntityDamageEvent.DamageCause cause) {
        GameParticipant<MatchGamePlayerImpl> participant = getParticipant(player);
        if (participant == getParticipantA()) {
            if (this.timeExpired) {
                super.handleDeath(player, cause);
            } else {
                if (gameEndTask != null) {
                    sendMessage(CC.translate("&c&lDEATH! &fSeeker &c" + player.getName() + " &fhas respawned."));
                }
                this.plugin.getServer().getScheduler().runTaskLater(this.plugin, () -> handleRespawn(player), 1L);
            }
        } else if (participant == getParticipantB()) {
            super.handleDeath(player, cause);
        }
    }

    @Override
    public void handleRespawn(Player player) {
        player.spigot().respawn();
        PlayerUtil.reset(player, true, false);

        if (gameEndTask == null) {
            ListenerUtil.teleportAndClearSpawn(player, intermissionArena.getPos1());
        } else {
            ListenerUtil.teleportAndClearSpawn(player, getArena().getPos2());
        }

        giveLoadout(player, getKit());
    }

    @Override
    public void endMatch() {
        if (this.seekerReleaseTask != null) {
            this.seekerReleaseTask.cancel();
        }

        if (this.gameEndTask != null) {
            this.gameEndTask.cancel();
        }
        super.endMatch();
    }

    @Override
    public boolean canEndRound() {
        return super.canEndRound() || this.timeExpired;
    }

    @Override
    public List<GameParticipant<MatchGamePlayerImpl>> getParticipants() {
        return Arrays.asList(this.participantA, this.participantB);
    }

    @Override
    public GameParticipant<MatchGamePlayerImpl> getParticipantA() {
        return participantA;
    }

    @Override
    public GameParticipant<MatchGamePlayerImpl> getParticipantB() {
        return participantB;
    }
}
