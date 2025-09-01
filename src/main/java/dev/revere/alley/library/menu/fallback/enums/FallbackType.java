package dev.revere.alley.library.menu.fallback.enums;

import lombok.Getter;

/**
 * @author Emmy
 * @project alley-practice
 * @since 29/07/2025
 */
@Getter
public enum FallbackType {
    HOTBAR_CONFIG_MENU_STRING("The provided menu is invalid")

    ;

    private final String readableMessage;

    /**
     * Constructor for the FallbackType enum.
     *
     * @param readableMessage the message that describes the fallback type.
     */
    FallbackType(String readableMessage) {
        this.readableMessage = readableMessage;
    }
}