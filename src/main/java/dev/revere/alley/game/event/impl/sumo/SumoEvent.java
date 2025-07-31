package dev.revere.alley.game.event.impl.sumo;

import dev.revere.alley.Alley;
import dev.revere.alley.base.hotbar.HotbarService;
import dev.revere.alley.base.hotbar.enums.HotbarType;
import dev.revere.alley.base.spawn.SpawnService;
import dev.revere.alley.game.event.Event;
import dev.revere.alley.game.event.EventService;
import dev.revere.alley.game.event.enums.EventState;
import dev.revere.alley.game.event.enums.EventTeamSize;
import dev.revere.alley.game.event.enums.EventType;
import dev.revere.alley.game.event.impl.sumo.task.SumoGameTask;
import dev.revere.alley.game.event.impl.sumo.task.SumoRoundEndTask;
import dev.revere.alley.game.event.impl.sumo.task.SumoRoundStartTask;
import dev.revere.alley.game.event.map.EventMap;
import dev.revere.alley.game.event.participant.EventParticipant;
import dev.revere.alley.game.event.task.EventTask;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.ProfileService;
import dev.revere.alley.profile.enums.ProfileState;
import dev.revere.alley.tool.reflection.ReflectionService;
import dev.revere.alley.tool.reflection.impl.TitleReflectionServiceImpl;
import dev.revere.alley.util.PlayerUtil;
import dev.revere.alley.util.SoundUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * @author Emmy
 * @project alley-practice
 * @since 29/07/2025
 */
@Getter
public class SumoEvent extends Event {
    private EventParticipant participantA;
    private EventParticipant participantB;

    private final EventTeamSize teamSize;

    //TODO: methods for this event weren't made for duos and trios, so they need to be adjusted accordingly.

    /**
     * Constructor for the SumoEvent class.
     *
     * @param map       the event map.
     * @param host      the UUID of the host player.
     * @param teamSize  the size of the teams in the event.
     */
    public SumoEvent(EventMap map, UUID host, EventTeamSize teamSize) {
        super(EventType.SUMO, map, 100, host);
        this.participantA = null;
        this.participantB = null;
        this.teamSize = teamSize;

        //TODO: handle team size logic, e.g. if team size is SOLO, only allow 2 players, if DUO, allow 4 players, etc.
        // also if a player joins the event with an existing party, split the party into teams based on the team size.
    }

    /**
     * Start the event.
     */
    @Override
    public void startEvent() {
        SumoGameTask task = new SumoGameTask(this);
        this.setTask(task);

        Player player = Bukkit.getPlayer(this.getHost());
        if (player == null) {
            //should technically never happen, but just in case yk
            return;
        }

        Alley.getInstance().getService(ReflectionService.class).getReflectionService(TitleReflectionServiceImpl.class).sendTitle(
                player,
                "&a&lSUCCESS!",
                "&fYou've hosted a &6" + this.getEventType().getName() + " Event&f!",
                10, 30, 10
        );

        SoundUtil.playCustomSound(player, Sound.FIREWORK_TWINKLE, 1.0F, 1.0F);
    }

    @Override
    public void stopEvent() {
        this.getPlayers().forEach(this::finalizePlayer);

        this.getSpectators().forEach(spectator -> {
            Player player = Bukkit.getPlayer(spectator);
            if (player == null) return;
            super.removeSpectator(player);
        });

        this.getTask().cancel();
        Alley.getInstance().getService(EventService.class).terminateEvent();
    }

    /**
     * Handle a player joining the event.
     *
     * @param player to be handled.
     */
    @Override
    public void handleJoin(Player player, boolean notify) {
        Profile profile = Alley.getInstance().getService(ProfileService.class).getProfile(player.getUniqueId());

        EventParticipant participant = new EventParticipant(player);
        this.getParticipants().put(player.getUniqueId(), participant);

        if (notify) {
            this.notifyParticipants("&6" + player.getName() + " &ahas joined the &6event. &6(&a" + this.getRemainingPlayers().size() + "&6/&a" + this.getMaxPlayers() + "&6)");
        }

        PlayerUtil.reset(player, true, true);
        profile.setState(ProfileState.PLAYING_EVENT);
        profile.setEvent(this);

        player.teleport(this.getMap().getSpawn());
    }

    /**
     * Handle a player leaving the event.
     *
     * @param player to be handled.
     */
    @Override
    public void handleLeave(Player player, boolean notify) {
        Profile profile = Alley.getInstance().getService(ProfileService.class).getProfile(player.getUniqueId());
        this.getParticipants().remove(player.getUniqueId());

        if (notify) {
            if (this.getState().equals(EventState.PREPARING)) {
                this.notifyParticipants("&c" + player.getName() + " has left the event. (&f" + this.getRemainingPlayers().size() + "&c/&f" + this.getMaxPlayers() + "&c)");
            }
        }

        PlayerUtil.reset(player, true, true);
        profile.setState(ProfileState.LOBBY);
        profile.setEvent(null);

        Alley.getInstance().getService(SpawnService.class).teleportToSpawn(player);
        Alley.getInstance().getService(HotbarService.class).applyHotbarItems(player);
    }

    /**
     * Handle a player dying in the event.
     *
     * @param player to be handled.
     */
    @Override
    public void handleDeath(Player player) {
        EventParticipant losingParticipant = this.getParticipant(player.getUniqueId());
        losingParticipant.setAlive(false);

        EventParticipant winningParticipant = participantA.getUuid().equals(player.getUniqueId()) ? participantB : participantA;
        winningParticipant.setAlive(true);

        //TODO: tp to arena spawn / start spectating

        //TODO: get cosmetic death message
        this.notifyParticipants("&c" + winningParticipant.getUsername() + " &fkilled &c" + losingParticipant.getUsername() + "&c!");


        this.setState(EventState.ENDING_ROUND);

        EventTask task = new SumoRoundEndTask(this);
        this.setTask(task);
    }

    @Override
    public void handleNewRound() {
        this.setState(EventState.STARTING_ROUND);

        if (this.participantA == null || !participantA.isAlive()) {
            this.participantA = this.getRandomParticipant();
        } else {
            Player player = Bukkit.getPlayer(participantA.getUuid());
            PlayerUtil.reset(player, true, true);
            //TODO: teleport to event spawn
        }

        if (this.participantB == null || !participantB.isAlive()) {
            this.participantB = this.getRandomParticipant();
        } else {
            Player player = Bukkit.getPlayer(participantB.getUuid());
            PlayerUtil.reset(player, true, true);
            //TODO: teleport to event spawn
        }

        EventTask task = new SumoRoundStartTask(this);
        this.setTask(task);
    }

    @Override
    public boolean canEventEnd() {
        return (this.participantA.isEliminated()) || (this.participantB.isEliminated());
    }

    /**
     * Get a random participant besides participant A or B, based on who got eliminated.
     *
     * @return an eligible participant.
     */
    private EventParticipant getRandomParticipant() {
        List<EventParticipant> eligibleParticipants = this.getParticipants().values().stream()
                .filter(EventParticipant::isAlive)
                .filter(participant -> !participant.equals(this.participantA) && !participant.equals(this.participantB))
                .collect(Collectors.toList());

        if (eligibleParticipants.isEmpty()) {
            return null;
        }

        return eligibleParticipants.get(ThreadLocalRandom.current().nextInt(eligibleParticipants.size()));
    }

    /**
     * Handle the cooldown period of the event.
     *
     * @param playerA      the first player.
     * @param playerB      the second player.
     * @param denyMovement if movement should be denied.
     */
    public void handleCooldown(Player playerA, Player playerB, boolean denyMovement) {
        ProfileService profileService = Alley.getInstance().getService(ProfileService.class);
        Profile profileA = profileService.getProfile(playerA.getUniqueId());
        Profile profileB = profileService.getProfile(playerB.getUniqueId());

        if (denyMovement) {
            this.denyPlayerMovement(playerA, profileA);
            this.denyPlayerMovement(playerB, profileB);
        }
    }

    /**
     * Deny player movement by teleporting them to their respective positions.
     *
     * @param player   the player to deny movement.
     * @param profile  the profile of the player.
     */
    private void denyPlayerMovement(Player player, Profile profile) {
        Event event = profile.getEvent();

        SumoEvent sumoEvent = (SumoEvent) event;
        EventParticipant participantA = sumoEvent.getParticipantA();
        EventParticipant participantB = sumoEvent.getParticipantB();

        Location playerLocation = player.getLocation();
        Location locationA = sumoEvent.getMap().getPos1();
        Location locationB = sumoEvent.getMap().getPos2();

        if (participantA != null && player.getUniqueId().equals(participantA.getUuid())) {
            if (playerLocation.getBlockX() != locationA.getBlockX() || playerLocation.getBlockZ() != locationA.getBlockZ()) {
                player.teleport(new Location(locationA.getWorld(), locationA.getX(), playerLocation.getY(), locationA.getZ(), playerLocation.getYaw(), playerLocation.getPitch()));
            }
        } else if (participantB != null && player.getUniqueId().equals(participantB.getUuid())) {
            if (playerLocation.getBlockX() != locationB.getBlockX() || playerLocation.getBlockZ() != locationB.getBlockZ()) {
                player.teleport(new Location(locationB.getWorld(), locationB.getX(), playerLocation.getY(), locationB.getZ(), playerLocation.getYaw(), playerLocation.getPitch()));
            }
        }
    }
}