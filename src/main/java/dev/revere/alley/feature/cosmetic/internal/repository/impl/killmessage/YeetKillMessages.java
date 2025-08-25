package dev.revere.alley.feature.cosmetic.internal.repository.impl.killmessage;

import dev.revere.alley.feature.cosmetic.annotation.CosmeticData;
import dev.revere.alley.feature.cosmetic.model.CosmeticType;
import org.bukkit.Material;

/**
 * @author Remi
 * @project alley-practice
 * @date 27/06/2025
 */
@CosmeticData(
        type = CosmeticType.KILL_MESSAGE,
        name = "Yeet Messages",
        description = "For when you don't just kill them, you yeet them.",
        icon = Material.PISTON_BASE,
        slot = 12,
        price = 750
)
public class YeetKillMessages extends KillMessagePack {
    @Override
    protected String getResourceFileName() {
        return "yeet_messages.yml";
    }
}