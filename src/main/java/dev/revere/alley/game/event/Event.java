package dev.revere.alley.game.event;

import dev.revere.alley.Alley;
import dev.revere.alley.base.hotbar.HotbarService;
import dev.revere.alley.base.spawn.SpawnService;
import dev.revere.alley.game.event.enums.EventState;
import dev.revere.alley.game.event.enums.EventType;
import dev.revere.alley.game.event.map.EventMap;
import dev.revere.alley.game.event.participant.EventParticipant;
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
    private EventState state = EventState.PREPARING;
    private final List<UUID> spectators = new ArrayList<>();
    private EventCountdown countdown;
    private EventType eventType;
    private EventTask task;
    private long roundStart;
    private final UUID host;
    private EventMap map;
    private int totalPlayers;
    private int maxPlayers;

    /**
     * Constructor for the Event class.
     *
     * @param eventType  the type of the event.
     * @param map        the map of the event.
     * @param maxPlayers the maximum amount of players allowed in the event.
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

    /**
     * Check if an event can end based on the players who are alive.
     *
     * @return false if players are alive, else true.
     */
    public boolean canEventEnd() {
        for (EventParticipant participant : this.participants.values()) {
            if (participant.isAlive()) {
                return false;
            }
        }

        return true;
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

        for (EventParticipant eventParticipant : this.participants.values()) {
            Player player = this.plugin.getServer().getPlayer(eventParticipant.getUuid());
            if (player != null) players.add(player);
        }

        return players;
    }

    /**
     * Gets a participant of the event by their unique ID.
     *
     * @param uuid the uuid of the player.
     * @return the EventParticipant object.
     */
    public EventParticipant getParticipant(UUID uuid) {
        return this.participants.get(uuid);
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
     * Sends a message to all participants in the event.
     *
     * @param message the message to send.
     */
    public void notifyParticipants(String message) {
        this.participants.keySet().forEach(uuid -> {
            Player player = this.plugin.getServer().getPlayer(uuid);
            if (player != null && player.isOnline()) {
                player.sendMessage(CC.translate(message));
            }
        });
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
        String hostName = this.plugin.getServer().getOfflinePlayer(this.host).getName();
        int maxPlayers = this.getMaxPlayers();
        String eventName = type.getName();
        int remainingPlayers = this.getRemainingPlayers().size();

        List<String> messages = Arrays.asList(
                "&6&l" + eventName,
                " &f• &6Host: &f" + hostName,
                " &f• &6Players: &f" + remainingPlayers + "&7/&f" + maxPlayers,
                " &f• &6Map: &f" + this.map.getName()
                //" &f• &6Required Players: &f" + this.minPlayers
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
                            " &a(Click to join)",
                            "/event join",
                            "&aClick to join the &6" + eventName + " &aevent."
                    );

            ClickableUtil.broadcastWithClickable(messages, textComponent, player);
        });
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
        PlayerUtil.reset(player, true, true);

        this.plugin.getService(SpawnService.class).teleportToSpawn(player);
        this.plugin.getService(HotbarService.class).applyHotbarItems(player);
    }
}