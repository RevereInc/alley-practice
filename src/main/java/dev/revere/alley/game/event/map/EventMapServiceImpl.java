package dev.revere.alley.game.event.map;

import dev.revere.alley.config.ConfigService;
import dev.revere.alley.game.event.map.enums.EventMapType;
import dev.revere.alley.plugin.AlleyContext;
import dev.revere.alley.plugin.annotation.Service;
import dev.revere.alley.tool.serializer.Serializer;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * @author Emmy
 * @project alley-practice
 * @since 29/07/2025
 */
@Getter
@Service(provides = EventMapService.class, priority = 450)
public class EventMapServiceImpl implements EventMapService {
    private final ConfigService configService;

    private final List<EventMap> maps = new ArrayList<>();

    /**
     * DI Constructor for the EventMapServiceImpl class.
     *
     * @param configService the config service instance.
     */
    public EventMapServiceImpl(ConfigService configService) {
        this.configService = configService;
    }

    @Override
    public void initialize(AlleyContext context) {
        this.initializeMaps();
    }

    @Override
    public void shutdown(AlleyContext context) {
        this.maps.forEach(this::saveMap);
    }

    /**
     * Loads maps from the configuration file into the repository.
     */
    @Override
    public void initializeMaps() {
        FileConfiguration config = configService.getEventMapsConfig();
        if (config.contains("event-maps")) {
            ConfigurationSection configurationSection = config.getConfigurationSection("event-maps");
            if (configurationSection == null || configurationSection.getKeys(false).isEmpty()) {
                return;
            }

            for (String mapName : configurationSection.getKeys(false)) {
                String path = "event-maps." + mapName;

                EventMap map = new EventMap(mapName);

                map.setDisplayName(config.getString(path + ".display-name", "&6" + mapName));

                if (config.contains(path + ".spawn")) {
                    map.setSpawn(Serializer.deserializeLocation(config.getString(path + ".spawn")));
                }

                if (config.contains(path + ".center")) {
                    map.setCenter(Serializer.deserializeLocation(config.getString(path + ".center")));
                }

                if (config.contains(path + ".pos1")) {
                    map.setPos1(Serializer.deserializeLocation(config.getString(path + ".pos1")));
                }

                if (config.contains(path + ".pos2")) {
                    map.setPos2(Serializer.deserializeLocation(config.getString(path + ".pos2")));
                }

                if (config.contains(path + ".type")) {
                    map.setType(EventMapType.valueOf(config.getString(path + ".type")));
                }

                this.maps.add(map);
            }
        }
    }

    /**
     * Checks if a map exists in the repository.
     *
     * @param name the name of the map to check.
     * @return if the map exists, else false.
     */
    @Override
    public EventMap getMap(String name) {
        return this.maps.stream().filter(map -> map.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    /**
     * Checks if a map exists in the repository.
     *
     * @param map the name of the map to check.
     * @return if the map exists, else false.
     */
    @Override
    public EventMap getMap(EventMap map) {
        return this.maps.stream().filter(m -> m.equals(map)).findFirst().orElse(null);
    }

    /**
     * Deletes an event map and removes it from the configuration.
     *
     * @param map the map to delete.
     */
    @Override
    public void deleteMap(EventMap map) {
        FileConfiguration config = configService.getEventMapsConfig();
        this.maps.remove(map);

        config.set("event-maps." + map.getName(), null);

        configService.saveConfig(configService.getConfigFile("storage/event-maps.yml"), config);
    }

    @Override
    public void saveMap(EventMap map) {
        FileConfiguration config = configService.getEventMapsConfig();
        String path = "event-maps." + map.getName();

        config.set(path + ".display-name", map.getDisplayName());
        config.set(path + ".spawn", Serializer.serializeLocation(map.getSpawn()));
        config.set(path + ".center", Serializer.serializeLocation(map.getCenter()));
        config.set(path + ".pos1", Serializer.serializeLocation(map.getPos1()));
        config.set(path + ".pos2", Serializer.serializeLocation(map.getPos2()));
        config.set(path + ".type", map.getType().name());

        configService.saveConfig(configService.getConfigFile("storage/event-maps.yml"), config);
    }

    @Override
    public void createMap(String name, EventMapType type) {
        EventMap map = new EventMap(name);
        this.maps.add(map);
        map.setType(type);
        this.saveMap(map);
    }

    @Override
    public EventMap getRandomEventMap(EventMapType mapType) {
        List<EventMap> filteredMaps = this.maps.stream()
                .filter(map -> map.getType().equals(mapType))
                .collect(Collectors.toList());

        if (filteredMaps.isEmpty()) {
            return null;
        }

        return filteredMaps.get(ThreadLocalRandom.current().nextInt(filteredMaps.size()));
    }
}