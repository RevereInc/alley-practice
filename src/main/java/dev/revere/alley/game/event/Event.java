package dev.revere.alley.game.event;

import dev.revere.alley.Alley;
import dev.revere.alley.base.hotbar.HotbarService;
import dev.revere.alley.base.spawn.SpawnService;
import dev.revere.alley.game.event.enums.EventState;
import dev.revere.alley.game.event.enums.EventType;
import dev.revere.alley.game.event.impl.sumo.SumoEvent;
import dev.revere.alley.game.event.map.EventMap;
import dev.revere.alley.game.event.participant.EventParticipant;
import dev.revere.alley.game.event.participant.TeamMember;
import dev.revere.alley.game.event.task.EventTask;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.ProfileService;
import dev.revere.alley.profile.enums.ProfileState;
import dev.revere.alley.util.PlayerUtil;
import dev.revere.alley.util.TimeUtil;
import dev.revere.alley.util.chat.CC;
import dev.revere.alley.util.chat.ClickableUtil;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Emmy
 * @project alley-practice
 * @since 29/07/2025
 */
@Getter
@Setter
public abstract class Event {
    private final Alley plugin = Alley.getInstance();

    private final ConcurrentHashMap<UUID, EventParticipant> participants = new ConcurrentHashMap<>();
    private final List<UUID> spectators = new ArrayList<>();

    private EventState state = EventState.PREPARING;
    private EventCountdown countdown;
    private EventType eventType;
    private EventTask task;

    private EventMap map;
    private final UUID host;

    private long roundStart;

    private int maxPlayers;
    private int totalPlayers;

    /**
     * Constructor for the Event class.
     *
     * @param eventType  the type of the event.
     * @param map        the map for the event.
     * @param maxPlayers the maximum number of players allowed in the event.
     * @param hostUUID   the UUID of the host player.
     */
    public Event(EventType eventType, EventMap map, int maxPlayers, UUID hostUUID) {
        this.eventType = eventType;
        this.map = map;
        this.host = hostUUID;
        this.maxPlayers = maxPlayers;
    }

    public abstract void startEvent();

    public abstract void stopEvent();

    public abstract void handleJoin(Player player, boolean notify);

    public abstract void handleLeave(Player player, boolean notify);

    public abstract void handleDeath(Player player);

    public abstract void handleNewRound();

    public abstract boolean canEventEnd();

    /**
     * Gets the list of remaining players who are still alive in the event.
     *
     * @return a list of players who are still alive.
     */
    public List<Player> getRemainingPlayers() {
        List<Player> remainingPlayers = new ArrayList<>();

        this.participants.values().forEach(participant -> {
            participant.getTeamMembers().forEach(teamMember -> {
                if (!teamMember.isDead()) {
                    Player player = this.plugin.getServer().getPlayer(teamMember.getUuid());
                    if (player != null && player.isOnline()) {
                        remainingPlayers.add(player);
                    }
                }
            });
        });

        return remainingPlayers;
    }

    /**
     * Gets the list of players who are still alive in the event.
     *
     * @return a list of players who are still alive.
     */
    public List<Player> getPlayers() {
        List<Player> players = new ArrayList<>();

        this.participants.values().forEach(participant -> participant.getTeamMembers().forEach(teamMember -> {
            if (!teamMember.isDead()) {
                Player player = this.plugin.getServer().getPlayer(teamMember.getUuid());
                if (player != null && player.isOnline()) {
                    players.add(player);
                }
            }
        }));

        return players;
    }

    /**
     * Gets the participant object by the member's UUID.
     *
     * @param memberUuid the UUID of the team member.
     * @return the EventParticipant object if found, otherwise null.
     */
    public EventParticipant getParticipant(UUID memberUuid) {
        return participants.values().stream()
                .filter(participant ->
                        participant.getTeamMembers().stream()
                                .map(TeamMember::getUuid)
                                .anyMatch(uuid -> uuid.equals(memberUuid))
                )
                .findFirst()
                .orElse(null);
    }

    /**
     * Gets a player object by the EventParticipant object.
     *
     * @param participant the participant of an event.
     * @return the player object of the participant.
     */
    public Player getParticipantPlayer(EventParticipant participant) {
        return this.plugin.getServer().getPlayer(participant.getUuid());
    }

    /**
     * Gets the team members of a specific participant in the event.
     *
     * @param participant the participant whose team members are to be retrieved.
     * @return a list of players who are team members of the participant.
     */
    public List<Player> getTeamMembers(EventParticipant participant) {
        List<Player> teamMembers = new ArrayList<>();
        participant.getTeamMembers().forEach(teamMember -> {
            Player player = this.plugin.getServer().getPlayer(teamMember.getUuid());
            if (player != null && player.isOnline()) {
                teamMembers.add(player);
            }
        });
        return teamMembers;
    }

    /**
     * Sends a message to all participants in the event.
     *
     * @param message the message to send.
     */
    public void notifyParticipants(String message) {
        this.participants.values().forEach(participant -> participant.getTeamMembers().forEach(
                teamMember -> {
                    Player player = this.plugin.getServer().getPlayer(teamMember.getUuid());
                    if (player != null && player.isOnline()) {
                        player.sendMessage(CC.translate(message));
                    }
                }
        ));
    }

    /**
     * Notifies the participants that there aren't enough players to start the specific event.
     *
     * @param type the event type.
     */
    public void sendNotEnoughPlayersMessage(EventType type) {
        Arrays.asList(
                "",
                "&6&l[&a" + type.getName() + "&6&l] &cNot enough players to start the event.",
                ""
        ).forEach(this::notifyParticipants);
    }

    /**
     * Broadcasts the event to all online players across the server.
     *
     * @param type the type of the event.
     */
    public void broadcastEvent(EventType type) {
        Profile hostProfile = this.plugin.getService(ProfileService.class).getProfile(this.host);
        String hostName = hostProfile.getFancyName();
        int maxPlayers = this.getMaxPlayers();
        String eventName = this.buildEventName(type);
        int remainingPlayers = this.getRemainingPlayers().size();

        List<String> messages = Arrays.asList(
                "&6&l" + eventName + " Event",
                " &6│ &fHost: &6" + hostName,
                " &6│ &fPlayers: &6" + remainingPlayers + "&7/&6" + maxPlayers,
                " &6│ &fMap: &6" + this.map.getName()
        );

        this.plugin.getServer().getOnlinePlayers().forEach(player -> {
            boolean hasJoined = this.getParticipants().containsKey(player.getUniqueId());

            TextComponent textComponent = hasJoined
                    ?
                    ClickableUtil.createComponent(
                            " &a✔ Joined",
                            "/event info",
                            "&aClick to view the details of the &6" + eventName + " &aevent.")
                    :
                    ClickableUtil.createComponent(
                            " &a&l(CLICK TO JOIN)",
                            "/event join",
                            "&aClick to join the &6" + eventName + " &aevent."
                    );

            ClickableUtil.broadcastWithClickable(messages, textComponent, player);
        });
    }

    /**
     * Builds the event name based on the type and team size if applicable.
     *
     * @param type the type of the event.
     * @return the formatted event name.
     */
    private String buildEventName(EventType type) {
        String eventName;
        if (this instanceof SumoEvent) {
            SumoEvent sumoEvent = (SumoEvent) this;
            eventName = type.getName() + " " + sumoEvent.getTeamSize().getDisplayName();
        } else {
            eventName = type.getName();
        }
        return eventName;
    }

    /**
     * Change the current event task.
     *
     * @param task the task to be set.
     */
    public void setTask(EventTask task) {
        this.task = task;
        if (this.task != null) {
            this.task.runTaskTimer(this.plugin, 0L, 20L);
        }
    }

    /**
     * Gets the duration of the round.
     *
     * @return the duration of the round.
     */
    public String getDuration() {
        switch (this.state) {
            case STARTING_ROUND:
                return "00:00";
            case RUNNING_ROUND:
                return TimeUtil.millisToTimer(System.currentTimeMillis() - this.roundStart);
            default:
                return "Ending";
        }
    }

    /**
     * Reset the player and send them back to the lobby.
     *
     * @param player the player to reset.
     */
    public void finalizePlayer(Player player) {
        Profile profile = Alley.getInstance().getService(ProfileService.class).getProfile(player.getUniqueId());
        profile.setState(ProfileState.LOBBY);
        profile.setEvent(null);

        Alley.getInstance().getService(HotbarService.class).applyHotbarItems(player);
        Alley.getInstance().getService(SpawnService.class).teleportToSpawn(player);
    }

    /**
     * Adds a spectator to the event.
     *
     * @param player the player to be added.
     */
    public void addSpectator(Player player) {
        Profile profile = this.plugin.getService(ProfileService.class).getProfile(player.getUniqueId());
        profile.setState(ProfileState.SPECTATING);
        profile.setEvent(this);

        this.spectators.add(player.getUniqueId());
        PlayerUtil.reset(player, true, true);

        player.teleport(this.getMap().getCenter());
    }

    /**
     * Removes a spectator from the event.
     *
     * @param player the player to be removed.
     */
    public void removeSpectator(Player player) {
        Profile profile = this.plugin.getService(ProfileService.class).getProfile(player.getUniqueId());
        profile.setState(ProfileState.LOBBY);
        profile.setEvent(null);

        this.spectators.remove(player.getUniqueId());

        this.plugin.getService(SpawnService.class).teleportToSpawn(player);
        this.plugin.getService(HotbarService.class).applyHotbarItems(player);
    }
}