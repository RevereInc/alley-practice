package dev.revere.alley.feature.emoji.enums;

import dev.revere.alley.util.chat.Symbol;
import lombok.Getter;

/**
 * @author Emmy
 * @project Alley
 * @date 22/01/2025 - 21:16
 */
@Getter
public enum EmojiType {
    HEART(":heart:", "§4§l" + Symbol.HEART + "§r"),
    SHRUG(":shrug:", "§c¯\\_(ツ)_/¯"),
    UWU(":uwu:", "§d(✧ω✧)"),
    PEACE(":peace:", "§6✌"),
    SLOTH(":sloth:", "§6(・⊝・)"),
    CUTIE(":cutie:", "§d(｡♥‿♥｡)"),
    WINK(":wink:", "§5(^_-)"),
    CRY(":cry:", "§b(ಥ_ಥ)"),
    DOG(":dog:", "§6(◕ᴥ◕)"),
    O7(":o7:", "§eヽ(^◇^*)/"),
    GIMME(":gimme:", "§b༼つ◕_◕༽つ"),
    YES(":yes:", "§a(✿◠‿◠)"),
    NO(":no:", "§c(◕︵◕)"),
    NUH_UH(":nuh_uh:", "§c(¬_¬)"),
    YUH_UH(":yuh_uh:", "§a(¬‿¬)"),

    ;

    private final String identifier;
    private final String format;

    /**
     * Constructor for EnumEmojiType class.
     *
     * @param identifier the identifier of the emoji
     * @param format     the format of the emoji
     */
    EmojiType(String identifier, String format) {
        this.identifier = identifier;
        this.format = format;
    }
}