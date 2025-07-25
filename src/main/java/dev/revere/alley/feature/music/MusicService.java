package dev.revere.alley.feature.music;

import dev.revere.alley.feature.music.enums.MusicDisc;
import dev.revere.alley.plugin.lifecycle.Service;
import dev.revere.alley.profile.Profile;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * @author Emmy
 * @project alley-practice
 * @since 19/07/2025
 */
public interface MusicService extends Service {
    /**
     * Starts a music session for a player.
     * This will first stop any currently playing music for the player. A random song
     * from the player's selected preferences will then be played if their lobby music setting is enabled.
     * The audio is played client-side and appears to emanate from the spawn location.
     *
     * @param player The player to start the music for.
     */
    void startMusic(Player player);

    /**
     * Fully stops a player's music session.
     * This is a "hard stop" that halts the audio, cancels any associated tracking tasks,
     * and completely removes the player's session from memory.
     *
     * @param player The player whose music session should be stopped.
     */
    void stopMusic(Player player);

    /**
     * Retrieves a list of all available music discs defined in the system.
     *
     * @return An immutable list of {@link MusicDisc} representing all available music discs.
     */
    List<MusicDisc> getMusicDiscs();

    /**
     * Selects a random music disc from the entire pool of available discs.
     *
     * @return A random {@link MusicDisc} value.
     */
    MusicDisc getRandomMusicDisc();

    /**
     * Retrieves the set of music discs a player has selected in their profile.
     * This method safely converts disc names stored in the profile to {@link MusicDisc} objects.
     *
     * @param profile The player's profile containing their music preferences.
     * @return A non-null set of {@link MusicDisc} values representing the selected discs.
     */
    Set<MusicDisc> getSelectedMusicDiscs(Profile profile);

    /**
     * Selects a random music disc from a player's personal list of selected discs.
     * If the player has not selected any discs, this will fall back to selecting a
     * random disc from the global pool.
     *
     * @param profile The player's profile.
     * @return A random {@link MusicDisc} from the player's selection.
     */
    MusicDisc getRandomSelectedMusicDisc(Profile profile);

    /**
     * Retrieves the current music session state for a specific player.
     *
     * @param playerUuid The UUID of the player.
     * @return An {@link Optional} containing the {@link MusicSession} if one is active for the player, otherwise empty.
     */
    Optional<MusicSession> getMusicState(UUID playerUuid);
}