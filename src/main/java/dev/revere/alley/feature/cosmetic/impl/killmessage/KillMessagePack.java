package dev.revere.alley.feature.cosmetic.impl.killmessage;

import dev.revere.alley.Alley;
import dev.revere.alley.config.ConfigService;
import dev.revere.alley.feature.cosmetic.BaseCosmetic;
import dev.revere.alley.tool.logger.Logger;
import dev.revere.alley.util.chat.CC;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * @author Remi
 * @project alley-practice
 * @date 27/06/2025
 */
public abstract class KillMessagePack extends BaseCosmetic {
    private final Map<EntityDamageEvent.DamageCause, List<String>> messagesByCause = new EnumMap<>(EntityDamageEvent.DamageCause.class);
    private List<String> genericMessages = Collections.emptyList();

    public KillMessagePack() {
        super();
        this.loadMessages();
    }

    /**
     * Concrete classes must implement this to specify which .yml file to load.
     *
     * @return The name of the resource file (e.g., "salty_messages.yml").
     */
    protected abstract String getResourceFileName();

    private void loadMessages() {
        String fileName = getResourceFileName();
        if (fileName == null || fileName.isEmpty()) {
            Logger.error("Kill message pack tried to load with empty or null file name. Aborting loading.");
            return;
        }

        String configPath = "cosmetics/messages/" + fileName;

        ConfigService configService = Alley.getInstance().getService(ConfigService.class);
        if (configService == null) {
            Logger.error("ConfigService is null when loading " + fileName + ". Service not available!");
            return;
        }

        FileConfiguration config = configService.getConfig(configPath);
        if (config == null) {
            Logger.error("Could not load kill message config: " + configPath);
            Logger.error("Make sure the file is added to ConfigService.configFileNames array!");
            return;
        }

        try {
            for (String key : config.getKeys(false)) {
                try {
                    EntityDamageEvent.DamageCause cause = EntityDamageEvent.DamageCause.valueOf(key.toUpperCase());
                    this.messagesByCause.put(cause, config.getStringList(key));
                } catch (IllegalArgumentException e) {
                    if (key.equalsIgnoreCase("GENERIC")) {
                        this.genericMessages = config.getStringList(key);
                    } else {
                        Logger.error("Unknown damage cause in " + fileName + ": " + key);
                    }
                }
            }
        } catch (Exception e) {
            Logger.logException("Failed to load kill message pack: " + fileName, e);
        }
    }

    /**
     * Gets a random message for a given damage cause, with fallback to GENERIC.
     *
     * @param cause The cause of death.
     * @return A random message string, or null if none are available.
     */
    public String getRandomMessage(EntityDamageEvent.DamageCause cause) {
        List<String> messageList = messagesByCause.get(cause);

        if (messageList == null || messageList.isEmpty()) {
            messageList = genericMessages;
        }

        if (messageList == null || messageList.isEmpty()) {
            return null;
        }

        return messageList.get(ThreadLocalRandom.current().nextInt(messageList.size()));
    }

    /**
     * Gathers all message strings from all categories into a single list.
     */
    public List<String> getDisplayableMessages() {
        List<String> allMessages = messagesByCause.values().stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toCollection(ArrayList::new));
        allMessages.addAll(genericMessages);

        return Collections.unmodifiableList(allMessages);
    }

    @Override
    public List<String> getDisplayLore() {
        List<String> lore = new ArrayList<>();
        List<String> displayable = getDisplayableMessages();
        if (displayable.isEmpty()) {
            lore.add("&7" + this.getDescription());
        } else {
            for (String message : displayable) {
                lore.add(CC.translate("&f- &6" + message.replace("{victim}", "victim").replace("{killer}", "killer")));
            }
        }
        return lore;
    }

    @Override
    public void execute(Player player) {

    }
}
