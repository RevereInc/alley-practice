package dev.revere.alley.feature.emoji;

import dev.revere.alley.config.ConfigService;
import dev.revere.alley.plugin.AlleyContext;
import dev.revere.alley.plugin.annotation.Service;
import dev.revere.alley.feature.emoji.enums.EmojiType;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author Emmy
 * @project Alley
 * @date 10/11/2024 - 09:41
 */
@Getter
@Service(provides = EmojiService.class, priority = 420)
public class EmojiServiceImpl implements EmojiService {
    private final ConfigService configService;

    private final Map<String, String> emojis = new HashMap<>();
    private boolean enabled = false;

    public EmojiServiceImpl(ConfigService configService) {
        this.configService = configService;
    }

    @Override
    public void initialize(AlleyContext context) {
        FileConfiguration settingsConfig = this.configService.getSettingsConfig();
        this.enabled = settingsConfig.getBoolean("essentials.emojis", false);
        if (!enabled) return;

        for (EmojiType value : EmojiType.values()) {
            this.emojis.put(value.getIdentifier(), value.getFormat());
        }
    }

    @Override
    public Map<String, String> getEmojis() {
        return Collections.unmodifiableMap(this.emojis);
    }

    @Override
    public Optional<String> getEmojiFormat(String identifier) {
        return Optional.ofNullable(this.emojis.get(identifier));
    }
}