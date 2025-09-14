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
     * Method to retrieve a raw list of strings from a specified configuration file without translation.
     *
     * @param entry The locale entry containing the configuration name and string path.
     * @return The raw list of configuration strings, or a default error message if not found or empty.
     */
    List<String> getListRaw(LocaleEntry entry);

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

    /**
     * Method to set or update a message in the specified configuration file.
     *
     * @param entry   The locale entry containing the configuration name and string path.
     * @param message The new message to set in the configuration.
     */
    void setMessage(LocaleEntry entry, String message);

    /**
     * Method to set or update a list of messages in the specified configuration file.
     *
     * @param entry    The locale entry containing the configuration name and string path.
     * @param messages The new list of messages to set in the configuration.
     */
    void setList(LocaleEntry entry, List<String> messages);

    /**
     * Method to set or update an integer value in the specified configuration file.
     *
     * @param entry The locale entry containing the configuration name and string path.
     * @param value The new integer value to set in the configuration.
     */
    void setInt(LocaleEntry entry, int value);

    /**
     * Method to set or update a double value in the specified configuration file.
     *
     * @param entry The locale entry containing the configuration name and string path.
     * @param value The new double value to set in the configuration.
     */
    void setDouble(LocaleEntry entry, double value);

    /**
     * Method to set or update a boolean value in the specified configuration file.
     *
     * @param entry The locale entry containing the configuration name and string path.
     * @param value The new boolean value to set in the configuration.
     */
    void setBoolean(LocaleEntry entry, boolean value);
}