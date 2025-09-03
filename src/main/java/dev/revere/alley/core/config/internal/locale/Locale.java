package dev.revere.alley.core.config.internal.locale;

import java.util.Collections;
import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @since 03/03/2025
 */
public interface Locale {
    /**
     * Gets a String from the config.
     *
     * @return The message from the config.
     */
    String getMessage();

    /**
     * Gets a List from the config.
     *
     * @return The list from the config.
     */
    default List<String> getList() {
        return Collections.emptyList();
    }

    /**
     * Gets a boolean from the config.
     *
     * @return The boolean from the config.
     */
    default boolean getBoolean() {
        return false;
    }
}