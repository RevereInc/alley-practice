package dev.revere.alley.feature.emoji;

import dev.revere.alley.plugin.lifecycle.Service;

import java.util.Map;
import java.util.Optional;

/**
 * @author Remi
 * @project alley-practice
 * @date 3/07/2025
 */
public interface EmojiService extends Service {
    /**
     * Gets the map containing all emoji identifiers and their corresponding formats.
     * @return An unmodifiable map of emojis.
     */
    Map<String, String> getEmojis();

    /**
     * Gets the format for a specific emoji by its identifier (e.g., ":)").
     * @param identifier The emoji identifier string.
     * @return An Optional containing the format if found.
     */
    Optional<String> getEmojiFormat(String identifier);
}