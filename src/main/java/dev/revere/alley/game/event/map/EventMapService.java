package dev.revere.alley.game.event.map;

import dev.revere.alley.game.event.map.enums.EventMapType;
import dev.revere.alley.plugin.lifecycle.Service;

import java.util.List;

/**
 * @author Emmy
 * @project alley-practice
 * @since 29/07/2025
 */
public interface EventMapService extends Service {
    /**
     * Retrieves a list of all EventMaps.
     *
     * @return a list of EventMaps.
     */
    List<EventMap> getMaps();

    /**
     * Initializes the event maps.
     * This method should be called when the plugin is enabled.
     */
    void initializeMaps();

    /**
     * Retrieves an EventMap by its name.
     *
     * @param name the name of the map to retrieve.
     * @return the EventMap associated with the given name, or null if not found.
     */
    EventMap getMap(String name);

    /**
     * Retrieves an EventMap by its model.
     *
     * @param eventMap the EventMap to retrieve.
     * @return the EventMap associated with the given type, or null if not found.
     */
    EventMap getMap(EventMap eventMap);

    /**
     * Deletes an EventMap.
     *
     * @param map the EventMap to delete.
     */
    void deleteMap(EventMap map);

    /**
     * Saves an EventMap.
     *
     * @param map the EventMap to save.
     */
    void saveMap(EventMap map);

    /**
     * Creates a new EventMap with the specified name and type.
     *
     * @param name the name of the new map.
     * @param type the type of the new map.
     */
    void createMap(String name, EventMapType type);

    /**
     * Gets a random EventMap of the specified type.
     *
     * @param mapType the type of the EventMap to retrieve.
     * @return a random EventMap of the specified type, or null if no maps are available.
     */
    EventMap getRandomEventMap(EventMapType mapType);
}