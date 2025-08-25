package dev.revere.alley.feature.cosmetic.internal.repository.impl.soundeffect;

import dev.revere.alley.common.SoundUtil;
import dev.revere.alley.feature.cosmetic.annotation.CosmeticData;
import dev.revere.alley.feature.cosmetic.model.CosmeticType;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @since 02/04/2025
 */
@CosmeticData(type = CosmeticType.SOUND_EFFECT, name = "Explosion", description = "Play an explosion sound", permission = "explosion", icon = Material.TNT, slot = 12)
public class ExplosionSoundEffect extends BaseSoundEffect {
    @Override
    public void execute(Player player) {
        SoundUtil.playCustomSound(player, Sound.EXPLODE, 1.0f, 1.0f);
    }
}
