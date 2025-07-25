package dev.revere.alley.feature.filter;

import dev.revere.alley.Alley;
import dev.revere.alley.api.constant.PluginConstant;
import dev.revere.alley.config.ConfigService;
import dev.revere.alley.plugin.AlleyContext;
import dev.revere.alley.plugin.annotation.Service;
import dev.revere.alley.util.chat.CC;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Emmy
 * @project Alley
 * @since 27/04/2025
 */
@Getter
@Service(provides = FilterService.class, priority = 340)
public class FilterServiceImpl implements FilterService {
    private final Alley plugin;
    private final ConfigService configService;
    private final PluginConstant pluginConstant;

    private final Set<String> filteredWords = new HashSet<>();
    private String notificationMessage;

    /**
     * Constructor for DI.
     */
    public FilterServiceImpl(Alley plugin, ConfigService configService, PluginConstant pluginConstant) {
        this.plugin = plugin;
        this.configService = configService;
        this.pluginConstant = pluginConstant;
    }

    @Override
    public void initialize(AlleyContext context) {
        this.notificationMessage = this.configService.getSettingsConfig().getString("profanity-filter.staff-notification-format");
        this.loadFilteredWords();
    }

    private void loadFilteredWords() {
        FileConfiguration config = this.configService.getSettingsConfig();
        ConfigurationSection section = config.getConfigurationSection("profanity-filter");

        if (section != null) {
            this.filteredWords.addAll(section.getStringList("filtered-words"));
        }

        if (config.getBoolean("profanity-filter.add-default-words")) {
            this.filteredWords.addAll(this.getDefaultFilteredWords());
        }
    }

    /**
     * Censors words in the message based on the filtered words list.
     *
     * @param message The message to censor.
     * @return The censored message.
     */
    public String censorWords(String message) {
        String censoredMessage = message;
        String[] words = message.split(" ");

        for (String word : words) {
            String normalizedWord = this.normalize(word);
            if (this.filteredWords.contains(normalizedWord)) {
                censoredMessage = censoredMessage.replaceAll("(?i)\\b" + word + "\\b", "****");
            }
        }

        return censoredMessage;
    }

    @Override
    public boolean isProfanity(String message) {
        for (String word : message.split(" ")) {
            if (this.filteredWords.contains(this.normalize(word))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void notifyStaff(String message, Player offender) {
        String permission = this.pluginConstant.getAdminPermissionPrefix();
        String replacedMessage = this.notificationMessage
                .replace("{player}", offender.getName())
                .replace("{message}", message);

        this.plugin.getServer().getOnlinePlayers().stream()
                .filter(player -> player.hasPermission(permission))
                .forEach(player -> player.sendMessage(CC.translate(replacedMessage)));

        Bukkit.getConsoleSender().sendMessage(CC.translate(replacedMessage));
    }

    /**
     * Normalizes words or messages by replacing certain characters with their corresponding letters.
     * This ensures that words like "b1tch" or "sh!t" are properly normalized.
     *
     * @param word The word or message to normalize.
     * @return The normalized version of the input.
     */
    private String normalize(String word) {
        return word.toLowerCase()
                .replace("@", "a").replace("4", "a")
                .replace("0", "o")
                .replace("1", "i").replace("!", "i")
                .replace("3", "e")
                .replace("5", "s")
                .replace("7", "t")
                .replace("8", "b")
                .replace("!", "i")
                .replaceAll("\\p{Punct}", "")
                .trim();
    }

    private List<String> getDefaultFilteredWords() {
        return Arrays.asList(
                "fuck", "shit", "bitch", "asshole", "bastard", "dick", "pussy", "cunt",
                "fag", "faggot", "slut", "whore", "retard", "nigger", "nigga", "chink",
                "spic", "kike", "twat", "cock", "motherfucker", "douche", "dumbass", "dipshit",
                "jackass", "prick", "cocksucker", "fucker", "shithead", "anus", "arse", "bollocks",
                "bugger", "wanker", "tosser"

                // ^ all copilot completion by the way, not me.
        );
    }
}