package dev.revere.alley.feature.cosmetic.internal.repository.impl.soundeffect;

import dev.revere.alley.feature.cosmetic.annotation.CosmeticData;
import dev.revere.alley.feature.cosmetic.model.CosmeticType;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 01/06/2024
 */
@CosmeticData(type = CosmeticType.SOUND_EFFECT, name = "Step", description = "Play step sound upon kill", permission = "step", icon = Material.REDSTONE, slot = 11)
public class StepSoundEffect extends BaseSoundEffect {
    @Override
    public void execute(Player player) {
        player.playEffect(player.getLocation(), Effect.STEP_SOUND, Material.REDSTONE_BLOCK);
    }
}
