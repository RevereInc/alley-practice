package dev.revere.alley.core.locale;


import dev.revere.alley.bootstrap.lifecycle.Service;

import java.util.List;

/**
 * @author Emmy
 * @project alley-practice
 * @since 09/09/2025
 */
public interface LocaleService extends Service {
    /**
     * Method to retrieve and translate a string from a specified configuration file.
     *
     * @param entry The locale entry containing the configuration name and string path.
     * @return The translated configuration string, or a default error message if not found.
     */
    String getMessage(LocaleEntry entry);

    /**
     * Method to retrieve and translate a list of strings from a specified configuration file.
     *
     * @param entry The locale entry containing the configuration name and string path.
     * @return The translated list of configuration strings, or a default error message if not found or empty.
     */
    List<String> getMessageList(LocaleEntry entry);

    /**
     * Method to retrieve an integer value from a specified configuration file.
     *
     * @param entry The locale entry containing the configuration name and string path.
     * @return The integer value from the configuration.
     */
    int getInt(LocaleEntry entry);

    /**
     * Method to retrieve a double value from a specified configuration file.
     *
     * @param entry The locale entry containing the configuration name and string path.
     * @return The double value from the configuration.
     */
    double getDouble(LocaleEntry entry);

    /**
     * Method to retrieve a boolean value from a specified configuration file.
     *
     * @param entry The locale entry containing the configuration name and string path.
     * @return The boolean value from the configuration.
     */
    boolean getBoolean(LocaleEntry entry);
}