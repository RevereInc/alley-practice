package dev.revere.alley.feature.cosmetic.internal.repository.impl.killeffect;

import dev.revere.alley.common.ParticleEffect;
import dev.revere.alley.feature.cosmetic.annotation.CosmeticData;
import dev.revere.alley.feature.cosmetic.model.CosmeticType;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @since 02/04/2025
 */
@CosmeticData(type = CosmeticType.KILL_EFFECT, name = "Explosion", description = "Spawn explosion particles", permission = "explosion", icon = Material.TNT, slot = 13)
public class ExplosionKillEffect extends BaseKillEffect {
    @Override
    public void execute(Player player) {
        ParticleEffect.EXPLOSION_LARGE.display(0.5f, 0.5f, 0.5f, 1.0f, 12, player.getLocation(), 20.0);
    }
}