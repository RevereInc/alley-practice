package dev.revere.alley.feature.tip;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.core.locale.LocaleService;
import dev.revere.alley.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Emmy
 * @project Alley
 * @since 25/04/2025
 */
@Getter
public class Tip {
    private final List<String> tips = new ArrayList<>();

    public Tip() {
        LocaleService localeService = AlleyPlugin.getInstance().getService(LocaleService.class);
        List<String> loadedTips = localeService.getStringListRaw(GlobalMessagesLocaleImpl.TIPS_LIST);
        this.tips.addAll(loadedTips);
    }

    /**
     * Returns a random tip from the list of tips.
     *
     * @return A random tip as a String.
     */
    public String getRandomTip() {
        if (this.tips.isEmpty()) {
            return "&cThe list of tips appears to be empty at the moment. Well at this point we can only apologize and hit you up with this tip: &ePractice makes perfect!";
        }

        return this.tips.get(ThreadLocalRandom.current().nextInt(this.tips.size()));
    }
}