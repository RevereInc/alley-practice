package dev.revere.alley;

import dev.revere.alley.base.arena.Arena;
import dev.revere.alley.base.arena.ArenaService;
import dev.revere.alley.base.kit.KitService;
import dev.revere.alley.base.kit.Kit;
import dev.revere.alley.profile.ProfileService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.data.ProfileData;
import dev.revere.alley.util.chat.CC;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * AlleyAPI – A central class providing easy access to Alley.
 * <p>
 * This class allows other developers to interact with the server functionalities of Alley,
 * such as registering custom code to be executed during plugin enable and disable,
 * and accessing player profiles.
 * </p>
 * <p>
 * Developers can use this class to easily hook into the lifecycle of the Alley plugin
 * and retrieve player profiles without having to directly interact with other parts of the code.
 * </p>
 *
 * @author Emmy
 * @project Alley
 * @since 22/04/2025
 */
@Getter
public class AlleyAPI {

    @Getter
    private static AlleyAPI instance;

    private final List<Runnable> onEnableCallbacks;
    private final List<Runnable> onDisableCallbacks;

    public AlleyAPI() {
        instance = this;

        this.onEnableCallbacks = new ArrayList<>();
        this.onDisableCallbacks = new ArrayList<>();
    }

    /**
     * Register custom code to be executed when Alley is enabled.
     * Developers can use this method to inject their code into the onEnable lifecycle of Alley.
     *
     * @param callback The code to execute on enable.
     */
    public void registerOnEnableCallback(Runnable callback) {
        this.onEnableCallbacks.add(callback);
    }

    /**
     * Register custom code to be executed when Alley is disabled.
     * Developers can use this method to inject their code into the onDisable lifecycle of Alley.
     *
     * @param callback The code to execute on disable.
     */
    public void registerOnDisableCallback(Runnable callback) {
        this.onDisableCallbacks.add(callback);
    }

    /**
     * Run all registered onEnable callbacks.
     * This method executes each registered callback when Alley is enabled.
     */
    public void runOnEnableCallbacks() {
        if (this.onEnableCallbacks.isEmpty()) {
            Bukkit.getConsoleSender().sendMessage(CC.translate("&f[&6AlleyAPI&f] No external code registered to be executed on enable."));
            return;
        }

        for (Runnable callback : this.onEnableCallbacks) {
            callback.run();
        }
    }

    /**
     * Run all registered onDisable callbacks.
     * This method executes each registered callback when Alley is disabled.
     */
    public void runOnDisableCallbacks() {
        if (this.onDisableCallbacks.isEmpty()) {
            Bukkit.getConsoleSender().sendMessage(CC.translate("&f[&6AlleyAPI&f] No external code registered to be executed on disable."));
            return;
        }

        for (Runnable callback : this.onDisableCallbacks) {
            callback.run();
        }
    }

    /**
     * Get the profile of a player using their UUID.
     * Profile contains all types of non-statistic related data for the player.
     * Such as; UUID, Username, Join date, etc.
     *
     * @param uuid The UUID of the player to retrieve the profile for.
     * @return The profile associated with the UUID.
     */
    public Profile getProfile(UUID uuid) {
        return Alley.getInstance().getService(ProfileService.class).getProfile(uuid);
    }

    /**
     * Get the profile data of a player using their UUID.
     * ProfileData contains all types of game related data for the player.
     * Such as; Ranked data, Unranked data, FFA data, Divisions, Titles, ELO, etc.
     *
     * @param uuid The UUID of the player to retrieve the profile data for.
     * @return The profile data associated with the UUID.
     */
    public ProfileData getProfileData(UUID uuid) {
        return Alley.getInstance().getService(ProfileService.class).getProfile(uuid).getProfileData();
    }

    /**
     * Get a kit by its name.
     * This method retrieves a kit from the Alley instance using its name.
     *
     * @param kitName The name of the kit to retrieve.
     * @return The Kit object associated with the given name, or null if not found.
     */
    public Kit getKit(String kitName) {
        return Alley.getInstance().getService(KitService.class).getKits().stream().filter(kit -> kit.getName().equalsIgnoreCase(kitName)).findFirst().orElse(null);
    }

    /**
     * Get an arena by its name.
     * This method retrieves an arena from the Alley instance using its name.
     *
     * @param arenaName The name of the arena to retrieve.
     * @return The AbstractArena object associated with the given name, or null if not found.
     */
    public Arena getArena(String arenaName) {
        return Alley.getInstance().getService(ArenaService.class).getArenas().stream().filter(arena -> arena.getName().equalsIgnoreCase(arenaName)).findFirst().orElse(null);
    }

    /**
     * Get a random arena for a specific kit.
     * This method retrieves a random arena from the Alley instance for the specified kit.
     *
     * @param kit The Kit object for which to retrieve a random arena.
     * @return A random AbstractArena object associated with the given kit.
     */
    public Arena getRandomArena(Kit kit) {
        return Alley.getInstance().getService(ArenaService.class).getRandomArena(kit);
    }
}