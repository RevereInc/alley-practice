package dev.revere.alley.base.nametag;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Remi
 * @project alley-practice
 * @date 27/06/2025
 */
@Getter
public class NametagRegistry {
    private final Cache<String, NametagAdapter> adapterCache;
    private final NametagServiceImpl service;
    private final AtomicInteger teamIdCounter = new AtomicInteger(0);

    public NametagRegistry(NametagServiceImpl service) {
        this.service = service;
        this.adapterCache = CacheBuilder.newBuilder()
                .expireAfterAccess(10, TimeUnit.MINUTES)
                .build();
    }

    /**
     * Gets or creates a NametagAdapter for a given style.
     *
     * @param view The nametag view.
     * @return The cached or newly created NametagAdapter.
     */
    public NametagAdapter getAdapter(NametagView view) {
        String key = view.getPrefix() + "|" + view.getSuffix() + "|" + view.getVisibility().name();
        try {
            return adapterCache.get(key, () -> {
                String teamName = "nt" + teamIdCounter.getAndIncrement();
                return new NametagAdapter(service, teamName, view.getPrefix(), view.getSuffix(), view.getVisibility());
            });
        } catch (Exception e) {
            throw new RuntimeException("Failed to load nametag adapter from cache", e);
        }
    }

    /**
     * Sends creation packets for all active adapters to a specific player.
     *
     * @param player The player to receive the packets.
     */
    public void sendAllAdapters(Player player) {
        for (NametagAdapter adapter : adapterCache.asMap().values()) {
            adapter.sendCreationPacket(player);
        }
    }

    /**
     * Cleans up a player's data from all perspectives when they quit.
     *
     * @param player The player who quit.
     */
    public void cleanupPlayer(Player player) {
        service.getPlayerPerspectives().values().forEach(p -> p.getDisplayedAdapters().remove(player.getUniqueId()));
    }
}