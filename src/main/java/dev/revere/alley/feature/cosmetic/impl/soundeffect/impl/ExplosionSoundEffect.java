package dev.revere.alley.feature.cosmetic.impl.soundeffect.impl;

import dev.revere.alley.feature.cosmetic.BaseCosmetic;
import dev.revere.alley.feature.cosmetic.CosmeticType;
import dev.revere.alley.feature.cosmetic.annotation.CosmeticData;
import dev.revere.alley.util.SoundUtil;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @since 02/04/2025
 */
@CosmeticData(type = CosmeticType.SOUND_EFFECT, name = "Explosion", description = "Play an explosion sound", permission = "explosion", icon = Material.TNT, slot = 12)
public class ExplosionSoundEffect extends BaseCosmetic {

    @Override
    public void execute(Player player) {
        SoundUtil.playCustomSound(player, Sound.EXPLODE, 1.0f, 1.0f);
    }
}
