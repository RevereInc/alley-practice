package dev.revere.alley.game.event.impl.sumo.task;

import dev.revere.alley.Alley;
import dev.revere.alley.game.event.Event;
import dev.revere.alley.game.event.enums.EventState;
import dev.revere.alley.game.event.impl.sumo.SumoEvent;
import dev.revere.alley.game.event.participant.EventParticipant;
import dev.revere.alley.game.event.task.BaseEventTask;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.ProfileService;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project alley-practice
 * @since 29/07/2025
 */
public class SumoRoundStartTask extends BaseEventTask {
    /**
     * Constructor for the SumoRoundStartTask class.
     *
     * @param event the event .
     */
    public SumoRoundStartTask(Event event) {
        super(event, EventState.STARTING_ROUND);
    }

    @Override
    public void handleTaskImpl() {
        Event event = this.getEvent();
        SumoEvent sumoEvent = (SumoEvent) event;

        if (this.getStage() >= 3) {
            event.setEventTask(null);
            event.setRoundStart(System.currentTimeMillis());
            event.setState(EventState.RUNNING_ROUND);

            this.applyCooldownPhrase(sumoEvent, false);
        } else {
            this.applyCooldownPhrase(sumoEvent, true);
            event.notifyParticipants("&a" + (3 - this.getStage()) + "...");
        }
    }

    /**
     * Applies the cooldown phrase to all team members of both participants.
     *
     * @param event        the SumoEvent instance.
     * @param denyMovement whether to deny movement during cooldown.
     */
    private void applyCooldownPhrase(SumoEvent event, boolean denyMovement) {
        EventParticipant participantA = event.getParticipantA();
        EventParticipant participantB = event.getParticipantB();

        Player playerA = event.getPlayer(participantA);
        Player playerB = event.getPlayer(participantB);

        this.handleCooldown(playerA, denyMovement);
        this.handleCooldown(playerB, denyMovement);
    }

    /**
     * Handle the cooldown period of the event.
     *
     * @param player       the player to handle cooldown for.
     * @param denyMovement if movement should be denied.
     */
    public void handleCooldown(Player player, boolean denyMovement) {
        ProfileService profileService = Alley.getInstance().getService(ProfileService.class);
        Profile profileA = profileService.getProfile(player.getUniqueId());

        if (denyMovement) {
            this.denyPlayerMovement(player, profileA);
        }
    }

    /**
     * Deny player movement by teleporting them to their respective positions.
     *
     * @param player  the player to deny movement.
     * @param profile the profile of the player.
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