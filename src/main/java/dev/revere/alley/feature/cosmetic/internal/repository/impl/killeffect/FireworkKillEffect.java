package dev.revere.alley.feature.cosmetic.internal.repository.impl.killeffect;

import dev.revere.alley.feature.cosmetic.annotation.CosmeticData;
import dev.revere.alley.feature.cosmetic.model.CosmeticType;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.stream.IntStream;

/**
 * @author Emmy
 * @project Alley
 * @since 02/04/2025
 */
@CosmeticData(type = CosmeticType.KILL_EFFECT, name = "Firework", description = "Spawn a firework at the opponent", permission = "firework", icon = Material.FIREWORK, slot = 14)
public class FireworkKillEffect extends BaseKillEffect {
    @Override
    public void execute(Player player) {
        IntStream.range(0, 3).forEach(i -> player.getWorld().spawnEntity(player.getLocation(), EntityType.FIREWORK));
    }
}