package dev.revere.alley.base.nametag;

import dev.revere.alley.Alley;
import dev.revere.alley.plugin.AlleyContext;
import dev.revere.alley.plugin.annotation.Service;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Remi
 * @project alley-practice
 * @date 27/06/2025
 */
@Getter
@Service(provides = NametagService.class, priority = 390)
public class NametagServiceImpl implements NametagService, Listener {
    private final Alley plugin;

    private final Map<UUID, NametagPerspective> playerPerspectives = new ConcurrentHashMap<>();
    private final NametagRegistry nametagRegistry;

    /**
     * Constructor for DI.
     */
    public NametagServiceImpl(Alley plugin) {
        this.plugin = plugin;
        this.nametagRegistry = new NametagRegistry(this);
    }

    @Override
    public void initialize(AlleyContext context) {
        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    /**
     * This is the main method to call when a player's state changes (e.g., joining/leaving a match).
     * It triggers a full, two-way re-evaluation of nametags.
     *
     * @param player The player whose state has changed.
     */
    public void updatePlayerState(Player player) {
        if (player == null) return;

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            NametagPerspective changedPlayerPerspective = playerPerspectives.get(player.getUniqueId());

            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (changedPlayerPerspective != null) {
                    changedPlayerPerspective.updateNametagFor(onlinePlayer);
                }

                NametagPerspective otherPlayerPerspective = playerPerspectives.get(onlinePlayer.getUniqueId());
                if (otherPlayerPerspective != null) {
                    otherPlayerPerspective.updateNametagFor(player);
                }
            }
        });
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        NametagPerspective newPerspective = new NametagPerspective(this, player, this.nametagRegistry);
        playerPerspectives.put(player.getUniqueId(), newPerspective);

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            nametagRegistry.sendAllAdapters(player);
            updatePlayerState(player);
        });
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        playerPerspectives.remove(event.getPlayer().getUniqueId());
        nametagRegistry.cleanupPlayer(event.getPlayer());
    }
}
