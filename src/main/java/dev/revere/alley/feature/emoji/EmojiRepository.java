package dev.revere.alley.feature.emoji;

import dev.revere.alley.feature.emoji.enums.EnumEmojiType;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Emmy
 * @project Alley
 * @date 10/11/2024 - 09:41
 */
@Getter
public class EmojiRepository {
    private final Map<String, String> emojis;

    public EmojiRepository() {
        this.emojis = new HashMap<>();

        for (EnumEmojiType value : EnumEmojiType.values()) {
            this.emojis.put(value.getIdentifier(), value.getFormat());
        }
    }
}