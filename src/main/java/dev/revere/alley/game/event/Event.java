package dev.revere.alley.game.event;

import dev.revere.alley.Alley;
import dev.revere.alley.base.hotbar.HotbarService;
import dev.revere.alley.base.spawn.SpawnService;
import dev.revere.alley.game.event.enums.EventState;
import dev.revere.alley.game.event.enums.EventType;
import dev.revere.alley.game.event.impl.sumo.SumoEvent;
import dev.revere.alley.game.event.map.EventMap;
import dev.revere.alley.game.event.participant.EventParticipant;
import dev.revere.alley.game.event.task.BaseEventTask;
import dev.revere.alley.game.event.task.impl.EventGameTask;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.ProfileService;
import dev.revere.alley.profile.enums.ProfileState;
import dev.revere.alley.tool.reflection.ReflectionService;
import dev.revere.alley.tool.reflection.impl.TitleReflectionServiceImpl;
import dev.revere.alley.util.PlayerUtil;
import dev.revere.alley.util.SoundUtil;
import dev.revere.alley.util.TimeUtil;
import dev.revere.alley.util.chat.CC;
import dev.revere.alley.util.chat.ClickableUtil;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.*;
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

    private final Map<UUID, EventParticipant> participants = new ConcurrentHashMap<>();
    private final List<UUID> spectators = new ArrayList<>();

    private EventState state = EventState.PREPARING;
    private EventCountdown countdown;
    private EventType eventType;
    private BaseEventTask task;
    private EventMap map;

    private final UUID host;

    private long roundStart;

    private int maxPlayers;

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

    public abstract void handleDeath(Player player);

    /**
     * Starts the event by initializing the game task and notifying the host.
     */
    public void startEvent() {
        EventGameTask task = new EventGameTask(this);
        this.setEventTask(task);

        Player player = Bukkit.getPlayer(this.getHost());
        if (player == null) return;

        this.plugin.getService(ReflectionService.class).getReflectionService(TitleReflectionServiceImpl.class).sendTitle(
                player,
                "&a&lSUCCESS!",
                "&fYou've hosted a &6" + this.getEventType().getName() + " Event&f!",
                10, 30, 10
        );

        SoundUtil.playCustomSound(player, Sound.FIREWORK_TWINKLE, 1.0F, 1.0F);
    }

    /**
     * Stops the event and finalizes all players and spectators.
     */
    public void stopEvent() {
        this.getPlayers().forEach(this::finalizePlayer);

        this.getSpectators().forEach(spectator -> {
            Player player = Bukkit.getPlayer(spectator);
            if (player == null) return;
            this.removeSpectator(player);
        });

        this.getTask().cancel();
        this.plugin.getService(EventService.class).terminateEvent();
    }

    /**
     * Handles a player joining the event.
     *
     * @param player the player who is joining.
     * @param notify whether to notify other participants about the join.
     */
    public void handleJoin(Player player, boolean notify) {
        Profile profile = this.plugin.getService(ProfileService.class).getProfile(player.getUniqueId());

        EventParticipant participant = new EventParticipant(player);
        this.getParticipants().put(player.getUniqueId(), participant);

        if (notify) {
            this.notifyParticipants("&6" + player.getName() + " &ahas joined the event. &6(&a" + this.getRemainingPlayers().size() + "&6/&a" + this.getMaxPlayers() + "&6)");
        }

        profile.setState(ProfileState.PLAYING_EVENT);
        profile.setEvent(this);

        PlayerUtil.reset(player, true, true);
        player.teleport(this.getMap().getSpawn());
        this.plugin.getService(HotbarService.class).applyHotbarItems(player);
    }

    /**
     * Handles a player leaving the event.
     *
     * @param player the player who is leaving.
     * @param notify whether to notify other participants about the leave.
     */
    public void handleLeave(Player player, boolean notify) {
        Profile profile = this.plugin.getService(ProfileService.class).getProfile(player.getUniqueId());
        this.getParticipants().remove(player.getUniqueId());

        if (notify) {
            if (this.getState().equals(EventState.PREPARING)) {
                this.notifyParticipants("&c" + player.getName() + " has left the event. (&f" + this.getRemainingPlayers().size() + "&c/&f" + this.getMaxPlayers() + "&c)");
            }
        }

        profile.setState(ProfileState.LOBBY);
        profile.setEvent(null);

        this.plugin.getService(SpawnService.class).teleportToSpawn(player);
        this.plugin.getService(HotbarService.class).applyHotbarItems(player);
    }

    /**
     * Handles the start of a round in the event.
     * This method sets the state to STARTING_ROUND.
     */
    public void handleRoundStart() {
        this.setState(EventState.STARTING_ROUND);
    }

    /**
     * Checks if the event can end based on the number of remaining players.
     *
     * @return true if the event can end, false otherwise.
     */
    public boolean canEventEnd() {
        return this.getRemainingPlayers().size() <= 1;
    }

    /**
     * Method to be called upon round start.
     *
     * @param player the player to initialize.
     */
    public void initializePlayer(Player player) {
        PlayerUtil.reset(player, true, true);
    }

    /**
     * Reset the player and send them back to the lobby.
     *
     * @param player the player to reset.
     */
    public void finalizePlayer(Player player) {
        Profile profile = this.plugin.getService(ProfileService.class).getProfile(player.getUniqueId());
        profile.setState(ProfileState.LOBBY);
        profile.setEvent(null);

        this.plugin.getService(HotbarService.class).applyHotbarItems(player);
        this.plugin.getService(SpawnService.class).teleportToSpawn(player);
    }

    /**
     * Gets the list of remaining players who are still alive in the event.
     *
     * @return a list of players who are still alive.
     */
    public List<Player> getRemainingPlayers() {
        List<Player> remainingPlayers = new ArrayList<>();

        this.participants.forEach((uuid, participant) -> {
            if (participant.isAlive()) {
                Player player = this.plugin.getServer().getPlayer(uuid);
                if (player != null && player.isOnline()) {
                    remainingPlayers.add(player);
                }
            }
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

        this.participants.forEach((uuid, participant) -> {
            Player player = this.plugin.getServer().getPlayer(uuid);
            if (player != null && player.isOnline()) {
                players.add(player);
            }
        });

        return players;
    }

    /**
     * Gets a player object by the EventParticipant object.
     *
     * @param participant the participant of an event.
     * @return the player object of the participant.
     */
    public Player getPlayer(EventParticipant participant) {
        return this.plugin.getServer().getPlayer(participant.getUuid());
    }

    /**
     * Gets the EventParticipant object by the player's UUID.
     *
     * @param uuid the UUID of the player.
     * @return the EventParticipant object associated with the player, or null if not found.
     */
    public EventParticipant getParticipant(UUID uuid) {
        return this.participants.get(uuid);
    }

    /**
     * Sends a message to all participants in the event.
     *
     * @param message the message to send.
     */
    public void notifyParticipants(String message) {
        this.participants.values().forEach(participant -> {
            Player player = this.getPlayer(participant);
            if (player != null && player.isOnline()) {
                player.sendMessage(CC.translate(message));
            }
        });
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

    /**
     * Change the current event task.
     *
     * @param task the task to be set.
     */
    public void setEventTask(BaseEventTask task) {
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
     * Notifies the participants that there aren't enough players to start the specific event.
     *
     * @param type the event type.
     */
    public void sendNotEnoughPlayersMessage(EventType type) {
        Arrays.asList(
                "",
                "&7[&6" + type.getName() + "&7] &cNot enough players to start the event.",
                ""
        ).forEach(this::notifyParticipants);
    }
}