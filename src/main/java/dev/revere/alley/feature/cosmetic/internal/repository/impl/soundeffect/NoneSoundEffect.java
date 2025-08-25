package dev.revere.alley.feature.cosmetic.internal.repository.impl.soundeffect;

import dev.revere.alley.feature.cosmetic.annotation.CosmeticData;
import dev.revere.alley.feature.cosmetic.model.CosmeticType;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 01/06/2024
 */
@CosmeticData(type = CosmeticType.SOUND_EFFECT, name = "None", description = "Remove your sound effect", icon = Material.BARRIER, slot = 10)
public class NoneSoundEffect extends BaseSoundEffect {
    @Override
    public void execute(Player player) {

    }
}
