package dev.revere.alley.feature.cosmetic.impl.soundeffect.impl;

import dev.revere.alley.feature.cosmetic.BaseCosmetic;
import dev.revere.alley.feature.cosmetic.CosmeticType;
import dev.revere.alley.feature.cosmetic.annotation.CosmeticData;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 01/06/2024
 */
@CosmeticData(type = CosmeticType.SOUND_EFFECT, name = "None", description = "Remove your sound effect", icon = Material.BARRIER, slot = 10)
public class NoneSoundEffect extends BaseCosmetic {
    @Override
    public void execute(Player player) {

    }
}
