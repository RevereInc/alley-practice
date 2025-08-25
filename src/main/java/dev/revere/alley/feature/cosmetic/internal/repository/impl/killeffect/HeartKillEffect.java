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
@CosmeticData(type = CosmeticType.KILL_EFFECT, name = "Heart", description = "Spawn hearts at the opponent", permission = "heart", icon = Material.REDSTONE, slot = 15)
public class HeartKillEffect extends BaseKillEffect {
    @Override
    public void execute(Player player) {
        ParticleEffect.HEART.display(0.4f, 0.4f, 0.4f, 0.1f, 10, player.getLocation(), 20.0);
    }
}