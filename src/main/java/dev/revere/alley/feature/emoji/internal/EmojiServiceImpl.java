package dev.revere.alley.feature.emoji.internal;

import dev.revere.alley.bootstrap.AlleyContext;
import dev.revere.alley.bootstrap.annotation.Service;
import dev.revere.alley.core.locale.LocaleService;
import dev.revere.alley.core.locale.internal.impl.ServerLocaleImpl;
import dev.revere.alley.feature.emoji.EmojiService;
import dev.revere.alley.feature.emoji.EmojiType;
import lombok.Getter;

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
    private final LocaleService localeService;

    private final Map<String, String> emojis = new HashMap<>();
    private boolean enabled = false;

    /**
     * DI Constructor for the EmojiServiceImpl class.
     *
     * @param localeService The locale service.
     */
    public EmojiServiceImpl(LocaleService localeService) {
        this.localeService = localeService;
    }

    @Override
    public void initialize(AlleyContext context) {
        this.enabled = this.localeService.getBoolean(ServerLocaleImpl.ESSENTIAL_EMOJI_FEATURE_BOOLEAN);
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