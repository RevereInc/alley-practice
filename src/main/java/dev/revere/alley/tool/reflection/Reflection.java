package dev.revere.alley.tool.reflection;

import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

/**
 * @author Emmy
 * @project Alley
 * @since 03/04/2025
 */
public interface Reflection {
    /**
     * Gets the CraftPlayer instance from a Player.
     * This method must be implemented by any class implementing IReflection.
     *
     * @param player The Player instance.
     * @return The CraftPlayer instance.
     */
    default CraftPlayer getCraftPlayer(Player player) {
        return (CraftPlayer) player;
    }

    /**
     * Get a field from a class using reflection.
     *
     * @param clazz     the class to search in
     * @param fieldName the field's name
     * @return the field object
     * @throws NoSuchFieldException if the field doesn't exist
     */
    default Field getField(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field;
    }

    /**
     * Set the value of a field via reflection.
     *
     * @param field    the field to set
     * @param instance the object instance
     * @param value    the value to set
     * @throws IllegalAccessException if the field is not accessible
     */
    default void setField(Field field, Object instance, Object value) throws IllegalAccessException {
        field.set(instance, value);
    }

    /**
     * Sends a packet to a player via reflection.
     *
     * @param player the player to send the packet to
     * @param packet the packet to send
     */
    default void sendPacket(Player player, Packet<?> packet) {
        this.getCraftPlayer(player).getHandle().playerConnection.sendPacket(packet);
    }
}