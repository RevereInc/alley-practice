package dev.revere.alley.feature.abilities;

import dev.revere.alley.plugin.lifecycle.Service;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Set;

/**
 * @author Remi
 * @project alley-practice
 * @date 2/07/2025
 */
public interface AbilityService extends Service {
    /**
     * Retrieves a specific ability instance by its class.
     *
     * @param abilityClass The class of the ability to get.
     * @param <T>          The type of the ability.
     * @return The singleton instance of the ability, or null if not found.
     */
    <T extends Ability> T getAbility(Class<T> abilityClass);

    /**
     * Creates an ItemStack for a given ability, configured with its name, lore, and material.
     *
     * @param abilityKey The key of the ability in the config (e.g., "GUARDIAN_ANGEL").
     * @param amount     The amount for the ItemStack.
     * @return The configured ItemStack for the ability.
     */
    ItemStack getAbilityItem(String abilityKey, int amount);

    /**
     * Gets the configured display name for an ability.
     *
     * @param abilityKey The key of the ability in the config.
     * @return The formatted display name.
     */
    String getDisplayName(String abilityKey);

    /**
     * Gets the configured description lore for an ability.
     *
     * @param abilityKey The key of the ability in the config.
     * @return A list of lore strings.
     */
    List<String> getDescription(String abilityKey);

    /**
     * Gets a set of all ability keys from the configuration.
     *
     * @return A set of ability config keys.
     */
    Set<String> getAbilityKeys();

    /**
     * Gives a player a specified amount of an ability item.
     */
    void giveAbility(CommandSender sender, Player player, String key, String abilityName, int amount);

    /**
     * Sends the configured "used ability" message to a player.
     */
    void sendPlayerMessage(Player player, String abilityKey);

    /**
     * Sends the configured "hit by ability" message to a target player.
     */
    void sendTargetMessage(Player target, Player player, String abilityKey);

    /**
     * Sends the "cooldown active" message to a player.
     */
    void sendCooldownMessage(Player player, String abilityName, String cooldown);

    /**
     * Schedules and sends the "cooldown expired" message to a player.
     */
    void sendCooldownExpiredMessage(Player player, String abilityName, String abilityKey);
}