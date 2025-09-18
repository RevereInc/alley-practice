package dev.revere.alley.feature.filter.internal;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.bootstrap.AlleyContext;
import dev.revere.alley.bootstrap.annotation.Service;
import dev.revere.alley.common.constants.PluginConstant;
import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.locale.LocaleService;
import dev.revere.alley.core.locale.internal.impl.SettingsLocaleImpl;
import dev.revere.alley.feature.filter.FilterService;
import lombok.Getter;
import org.bukkit.Bukkit;
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
    private final AlleyPlugin plugin;

    private final LocaleService localeService;
    private final PluginConstant pluginConstant;

    private final Set<String> filteredWords = new HashSet<>();
    private String notificationMessage;

    /**
     * DI Constructor for the FilterServiceImpl class.
     *
     * @param plugin         The main Alley plugin instance.
     * @param localeService  The locale service.
     * @param pluginConstant The plugin constant.
     */
    public FilterServiceImpl(AlleyPlugin plugin, LocaleService localeService, PluginConstant pluginConstant) {
        this.plugin = plugin;
        this.localeService = localeService;
        this.pluginConstant = pluginConstant;
    }

    @Override
    public void initialize(AlleyContext context) {
        this.notificationMessage = this.localeService.getMessage(SettingsLocaleImpl.PROFANITY_FILTER_STAFF_NOTIFICATION_FORMAT);
        this.loadFilteredWords();
    }

    private void loadFilteredWords() {
        List<String> words = this.localeService.getListRaw(SettingsLocaleImpl.PROFANITY_FILTER_FILTERED_WORDS_LIST);
        this.filteredWords.addAll(words);

        if (this.localeService.getBoolean(SettingsLocaleImpl.PROFANITY_FILTER_ADD_DEFAULT_WORDS_BOOLEAN)) {
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