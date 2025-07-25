package dev.revere.alley.feature.cosmetic.impl.killmessage.impl;

import dev.revere.alley.feature.cosmetic.CosmeticType;
import dev.revere.alley.feature.cosmetic.annotation.CosmeticData;
import dev.revere.alley.feature.cosmetic.impl.killmessage.KillMessagePack;
import org.bukkit.Material;

/**
 * @author Remi
 * @project alley-practice
 * @date 27/06/2025
 */
@CosmeticData(
        type = CosmeticType.KILL_MESSAGE,
        name = "Salty Messages",
        description = "A pack of messages to taunt your opponents.",
        icon = Material.BOOK_AND_QUILL,
        slot = 11,
        price = 750
)
public class SaltyKillMessages extends KillMessagePack {
    @Override
    protected String getResourceFileName() {
        return "salty_messages.yml";
    }
}