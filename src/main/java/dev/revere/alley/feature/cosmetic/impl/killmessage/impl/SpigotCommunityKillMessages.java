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
        name = "Spigot Community Messages",
        description = "A dive into the spigot community.",
        icon = Material.GOLD_NUGGET,
        slot = 14,
        price = 750
)
public class SpigotCommunityKillMessages extends KillMessagePack {
    @Override
    protected String getResourceFileName() {
        return "spigot_community_messages.yml";
    }
}