package dev.revere.alley.tool.reflection.impl;

import dev.revere.alley.tool.logger.Logger;
import dev.revere.alley.tool.reflection.Reflection;
import dev.revere.alley.util.TaskUtil;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Emmy
 * @project Alley
 * @since 02/04/2025
 */
public class DeathReflectionServiceImpl implements Reflection {
    private final int FAKE_ENTITY_ID;
    private final byte DEATH_STATUS;
    private final int VIEW_RADIUS;

    public DeathReflectionServiceImpl() {
        this.FAKE_ENTITY_ID = Integer.MAX_VALUE - 1;
        this.DEATH_STATUS = 3;
        this.VIEW_RADIUS = 64;
    }

    /**
     * Visualizes the death of a player by sending fake entity packets to nearby players.
     *
     * @param player The supposedly dying player.
     */
    public void animateDeath(Player player) {
        PacketPlayOutNamedEntitySpawn spawnPacket = this.createSpawnPacket(player);
        PacketPlayOutEntityStatus statusPacket = this.createStatusPacket();
        PacketPlayOutEntityMetadata metadataPacket = this.createMetadataPacket();

        Set<Player> playersInRange = this.getPlayersInRange(player);

        for (Player watcher : playersInRange) {
            this.sendPacket(watcher, spawnPacket);
            this.sendPacket(watcher, statusPacket);
            this.sendPacket(watcher, metadataPacket);
        }

        TaskUtil.runLater(() -> this.removeFakeEntities(playersInRange), 40L);
    }

    /**
     * Creates a spawn packet for the fake death entity.
     *
     * @param player The player whose death animation is being shown.
     * @return The spawn packet.
     */
    private PacketPlayOutNamedEntitySpawn createSpawnPacket(Player player) {
        PacketPlayOutNamedEntitySpawn spawnPacket = new PacketPlayOutNamedEntitySpawn((this.getCraftPlayer(player).getHandle()));
        this.setEntityId(spawnPacket);
        return spawnPacket;
    }

    /**
     * Creates a status packet for the fake death entity.
     *
     * @return The status packet indicating death.
     */
    private PacketPlayOutEntityStatus createStatusPacket() {
        PacketPlayOutEntityStatus statusPacket = new PacketPlayOutEntityStatus();
        this.setEntityId(statusPacket);
        this.setStatus(statusPacket);
        return statusPacket;
    }

    /**
     * Sets the entity ID for a packet using reflection from the IReflection interface.
     *
     * @param packet The packet to set the entity ID for.
     */
    private void setEntityId(Object packet) {
        try {
            Field field;
            if (packet instanceof PacketPlayOutNamedEntitySpawn) {
                field = this.getField(PacketPlayOutNamedEntitySpawn.class, "a");
                this.setField(field, packet, this.FAKE_ENTITY_ID);
            } else if (packet instanceof PacketPlayOutEntityStatus) {
                field = this.getField(PacketPlayOutEntityStatus.class, "a");
                this.setField(field, packet, this.FAKE_ENTITY_ID);
            }
        } catch (Exception exception) {
            Logger.logException("Failed to set entity ID for death animation packet.", exception);
        }
    }

    /**
     * Sets the status byte for the entity status packet using reflection from the IReflection interface.
     *
     * @param statusPacket The entity status packet.
     */
    private void setStatus(PacketPlayOutEntityStatus statusPacket) {
        try {
            Field field = this.getField(PacketPlayOutEntityStatus.class, "b");
            this.setField(field, statusPacket, this.DEATH_STATUS);
        } catch (Exception exception) {
            Logger.logException("Failed to set status for death animation packet.", exception);
        }
    }

    /**
     * Retrieves a set of players within a certain radius of the player.
     *
     * @param player The player to check around.
     * @return A set of nearby players.
     */
    private Set<Player> getPlayersInRange(Player player) {
        Set<Player> playersInRange = new HashSet<>();
        for (Entity entity : player.getNearbyEntities(this.VIEW_RADIUS, this.VIEW_RADIUS, this.VIEW_RADIUS)) {
            if (entity instanceof Player && !entity.getUniqueId().equals(player.getUniqueId())) {
                playersInRange.add((Player) entity);
            }
        }
        return playersInRange;
    }

    /**
     * Removes the fake death entity from players' views.
     *
     * @param players The players to remove the fake entity for.
     */
    private void removeFakeEntities(Set<Player> players) {
        PacketPlayOutEntityDestroy destroyPacket = new PacketPlayOutEntityDestroy(this.FAKE_ENTITY_ID);
        for (Player player : players) {
            this.sendPacket(player, destroyPacket);
        }
    }

    private PacketPlayOutEntityMetadata createMetadataPacket() {
        try {
            PacketPlayOutEntityMetadata metadataPacket = new PacketPlayOutEntityMetadata();
            Field entityIdField = this.getField(PacketPlayOutEntityMetadata.class, "a");
            Field dataWatcherField = this.getField(PacketPlayOutEntityMetadata.class, "b");

            this.setField(entityIdField, metadataPacket, this.FAKE_ENTITY_ID);

            DataWatcher dataWatcher = new DataWatcher(null);
            dataWatcher.a(6, 0.0F);

            List<DataWatcher.WatchableObject> items = dataWatcher.c();
            this.setField(dataWatcherField, metadataPacket, items);

            return metadataPacket;
        } catch (Exception exception) {
            Logger.logException("Failed to create metadata packet for death animation.", exception);
            return null;
        }
    }
}