package dev.revere.alley.feature.abilities.cooldown;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class AbilityCooldowns {
    private static HashMap<String, HashMap<UUID, Long>> cooldown;

    static {
        AbilityCooldowns.cooldown = new HashMap<>();
    }

    public static void createCooldown(final String k) {
        if (AbilityCooldowns.cooldown.containsKey(k)) {
            throw new IllegalArgumentException("Cooldown already exists.");
        }
        AbilityCooldowns.cooldown.put(k, new HashMap<>());
    }

    public static HashMap<UUID, Long> getCooldownMap(final String k) {
        if (AbilityCooldowns.cooldown.containsKey(k)) {
            return AbilityCooldowns.cooldown.get(k);
        }
        return null;
    }

    public static void addCooldown(final String k, final Player p, final int seconds) {
        if (!AbilityCooldowns.cooldown.containsKey(k)) {
            throw new IllegalArgumentException(k + " does not exist");
        }
        final long next = System.currentTimeMillis() + seconds * 1000L;
        AbilityCooldowns.cooldown.get(k).put(p.getUniqueId(), next);
    }

    public static boolean isOnCooldown(final String k, final Player p) {
        return AbilityCooldowns.cooldown.containsKey(k) && AbilityCooldowns.cooldown.get(k).containsKey(p.getUniqueId()) && System.currentTimeMillis() <= AbilityCooldowns.cooldown.get(k).get(p.getUniqueId());
    }

    public static int getCooldownForPlayerInt(final String k, final Player p) {
        return (int) (AbilityCooldowns.cooldown.get(k).get(p.getUniqueId()) - System.currentTimeMillis()) / 1000;
    }

    public static long getCooldownForPlayerLong(final String k, final Player p) {
        return AbilityCooldowns.cooldown.get(k).get(p.getUniqueId()) - System.currentTimeMillis();
    }

    public static void removeCooldown(final String k, final Player p) {
        if (!AbilityCooldowns.cooldown.containsKey(k)) {
            throw new IllegalArgumentException(k + " does not exist");
        }
        AbilityCooldowns.cooldown.get(k).remove(p.getUniqueId());
    }
}
