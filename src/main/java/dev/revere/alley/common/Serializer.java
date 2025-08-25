package dev.revere.alley.common;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.common.logger.Logger;
import lombok.experimental.UtilityClass;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Utility class for serializing and deserializing various objects such as Locations, ItemStacks, PotionEffects, etc...
 * Mainly used for saving to and retrieving from databases or YAML files.
 *
 * @author Emmy
 * @project Alley
 * @date 25/08/2025
 */
@UtilityClass
public class Serializer {
    private final String SEPARATOR = ":";

    /**
     * Serialize a location to a string.
     *
     * @param location The location to serialize.
     * @return The serialized location.
     */
    public String serializeLocation(Location location) {
        if (location == null) {
            return "null";
        }

        return Objects.requireNonNull(location.getWorld()).getName() + SEPARATOR +
                location.getX() + SEPARATOR + location.getY() + SEPARATOR + location.getZ() + SEPARATOR +
                location.getYaw() + SEPARATOR + location.getPitch();
    }

    /**
     * Deserialize a location from a string.
     *
     * @param source The source to deserialize.
     * @return The deserialized location.
     */
    public Location deserializeLocation(String source) {
        if (source == null || source.equalsIgnoreCase("null")) {
            return null;
        }

        String[] split = source.split(SEPARATOR);
        World world = AlleyPlugin.getInstance().getServer().getWorld(split[0]);

        if (world == null) {
            return null;
        }

        return new Location(
                world,
                Double.parseDouble(split[1]),
                Double.parseDouble(split[2]),
                Double.parseDouble(split[3]),
                Float.parseFloat(split[4]),
                Float.parseFloat(split[5])
        );
    }

    /**
     * Serialize an ItemStack array to a Base64 string
     *
     * @param items the ItemStack array to serialize
     * @return the serialized ItemStack array
     */
    public String serializeItemStack(ItemStack[] items) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            dataOutput.writeInt(items.length);
            for (ItemStack item : items) {
                dataOutput.writeObject(item);
            }

            dataOutput.close();
            return Base64.getEncoder().encodeToString(outputStream.toByteArray());
        } catch (Exception exception) {
            Logger.logException("Failed to serialize item stacks.", exception);
            return "";
        }
    }

    /**
     * Deserialize an ItemStack array from a Base64 string
     *
     * @param data the Base64 string to deserialize
     * @return the deserialized ItemStack array
     */
    public ItemStack[] deserializeItemStack(String data) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64.getDecoder().decode(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);

            ItemStack[] items = new ItemStack[dataInput.readInt()];
            for (int i = 0; i < items.length; i++) {
                items[i] = (ItemStack) dataInput.readObject();
            }

            dataInput.close();
            return items;
        } catch (Exception exception) {
            Logger.logException("Failed to deserialize item stacks.", exception);
            return new ItemStack[0];
        }
    }

    /**
     * Serialize a list of potion effects.
     *
     * @param potionEffects The potion effects.
     * @return The serialized potion effects.
     */
    public List<String> serializePotionEffects(List<PotionEffect> potionEffects) {
        return potionEffects.stream()
                .map(effect -> effect.getType().getName() + SEPARATOR + effect.getDuration() + SEPARATOR + effect.getAmplifier())
                .collect(Collectors.toList());
    }

    /**
     * Deserialize a list of potion effects.
     *
     * @param serializedEffects The serialized potion effects.
     * @return The potion effects.
     */
    public List<PotionEffect> deserializePotionEffects(List<String> serializedEffects) {
        return serializedEffects.stream()
                .map(s -> {
                    String[] parts = s.split(SEPARATOR);
                    if (parts.length < 3) return null;
                    PotionEffectType type = PotionEffectType.getByName(parts[0]);
                    int duration = Integer.parseInt(parts[1]);
                    int amplifier = Integer.parseInt(parts[2]);
                    return type != null ? new PotionEffect(type, duration, amplifier) : null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}