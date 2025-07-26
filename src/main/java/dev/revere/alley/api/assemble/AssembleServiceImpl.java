package dev.revere.alley.api.assemble;

import dev.revere.alley.Alley;
import dev.revere.alley.api.assemble.enums.AssembleStyle;
import dev.revere.alley.api.assemble.events.AssembleBoardCreateEvent;
import dev.revere.alley.api.assemble.events.AssembleBoardDestroyEvent;
import dev.revere.alley.api.assemble.listener.AssembleListener;
import dev.revere.alley.config.ConfigService;
import dev.revere.alley.plugin.AlleyContext;
import dev.revere.alley.plugin.annotation.Service;
import dev.revere.alley.profile.ProfileService;
import dev.revere.alley.tool.animation.AnimationService;
import dev.revere.alley.tool.logger.Logger;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Setter
@Service(provides = AssembleService.class, priority = 320)
public class AssembleServiceImpl implements AssembleService {
    private final Alley plugin;
    private final AnimationService animationService;
    private final ProfileService profileService;
    private final ConfigService configService;

    private final Map<UUID, AssembleBoard> boards = new ConcurrentHashMap<>();
    private final ChatColor[] chatColorCache = ChatColor.values();

    private AssembleAdapter adapter;
    private AssembleThread thread;
    private AssembleListener listeners;
    private AssembleStyle assembleStyle = AssembleStyle.MODERN;

    private boolean hook;
    private boolean debugMode = true;
    private boolean callEvents = true;
    private long ticks = 2;

    public AssembleServiceImpl(Alley plugin, AnimationService animationService, ProfileService profileService, ConfigService configService) {
        this.plugin = plugin;
        this.animationService = animationService;
        this.profileService = profileService;
        this.configService = configService;
    }

    @Override
    public void initialize(AlleyContext context) {
        this.adapter = new AssembleAdapterImpl(animationService, profileService, configService);

        this.listeners = new AssembleListener(this);
        this.plugin.getServer().getPluginManager().registerEvents(this.listeners, this.plugin);

        for (Player player : this.plugin.getServer().getOnlinePlayers()) {
            if (this.callEvents) {
                AssembleBoardCreateEvent createEvent = new AssembleBoardCreateEvent(player);
                Bukkit.getPluginManager().callEvent(createEvent);
                if (createEvent.isCancelled()) continue;
            }
            this.boards.putIfAbsent(player.getUniqueId(), new AssembleBoard(player, this));
        }

        this.thread = new AssembleThread(this);
    }

    @Override
    public void shutdown(AlleyContext context) {
        if (this.thread != null) {
            this.thread.interrupt();
            this.thread = null;
        }

        if (this.listeners != null) {
            HandlerList.unregisterAll(this.listeners);
            this.listeners = null;
        }

        for (UUID uuid : this.boards.keySet()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null && player.isOnline()) {
                player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
            }
        }
        this.boards.clear();
        Logger.info("Assemble scoreboard has been shut down.");
    }

    @Override
    public void createBoard(Player player) {
        if (isCallEvents()) {
            AssembleBoardCreateEvent createEvent = new AssembleBoardCreateEvent(player);
            Bukkit.getPluginManager().callEvent(createEvent);
            if (createEvent.isCancelled()) {
                return;
            }
        }
        this.boards.put(player.getUniqueId(), new AssembleBoard(player, this));
    }

    @Override
    public void removeBoard(Player player) {
        if (isCallEvents()) {
            AssembleBoardDestroyEvent destroyEvent = new AssembleBoardDestroyEvent(player);
            Bukkit.getPluginManager().callEvent(destroyEvent);
            if (destroyEvent.isCancelled()) {
                return;
            }
        }
        this.boards.remove(player.getUniqueId());
        if (player.getScoreboard() != null) {
            player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
        }
    }

    @Override
    public Map<UUID, AssembleBoard> getBoards() {
        return Collections.unmodifiableMap(this.boards);
    }
}