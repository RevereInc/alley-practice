package dev.revere.alley.core.profile;

import com.mongodb.client.MongoCollection;
import dev.revere.alley.bootstrap.lifecycle.Service;
import dev.revere.alley.core.database.model.DatabaseProfile;
import dev.revere.alley.feature.kit.Kit;
import org.bson.Document;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

/**
 * @author Remi
 * @project alley-practice
 * @date 2/07/2025
 */
public interface ProfileService extends Service {
    /**
     * Gets a player's profile by their UUID.
     * <p>
     * This method features lazy-loading: if the profile is not found in the cache,
     * it will be loaded from the database on-demand.
     *
     * @param uuid The UUID of the player.
     * @return The player's Profile object.
     */
    Profile getProfile(UUID uuid);

    /**
     * Gets the Data Access Object (DAO) responsible for database operations for profiles.
     * This is used internally to load and save individual profiles.
     *
     * @return The DatabaseProfile DAO instance.
     */
    DatabaseProfile getDatabaseProfile();

    /**
     * Gets the raw MongoDB collection for profiles.
     * <p>
     * Warning: Use with caution. Interacting with this collection directly bypasses
     * the caching and management logic of this service. It is intended for services
     * that need to perform complex, custom queries.
     *
     * @return The MongoCollection for profiles.
     */
    MongoCollection<Document> getCollection();

    /**
     * Gets the map of all currently cached profiles.
     *
     * @return A map of UUIDs to Profile objects.
     */
    Map<UUID, Profile> getProfiles();

    /**
     * Removes a profile from the in-memory cache.
     * <p>
     * This does not delete the profile from the database; it only removes it from the cache.
     *
     * @param uuid The UUID of the profile to remove.
     */
    void removeProfile(UUID uuid);

    /**
     * Resets the statistics for a target player and archives their old profile.
     *
     * @param player The staff member issuing the command.
     * @param target The UUID of the player whose stats are being reset.
     */
    void resetStats(Player player, UUID target);

    /**
     * Resets the inventory layout for a specific kit across all player profiles.
     *
     * @param kit The kit to reset the layout for.
     */
    void resetLayoutForKit(Kit kit);
}
