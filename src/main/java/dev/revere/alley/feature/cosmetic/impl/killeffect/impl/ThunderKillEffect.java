package dev.revere.alley.feature.cosmetic.impl.killeffect.impl;

import dev.revere.alley.feature.cosmetic.BaseCosmetic;
import dev.revere.alley.feature.cosmetic.CosmeticType;
import dev.revere.alley.feature.cosmetic.annotation.CosmeticData;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 01/06/2024
 */
@CosmeticData(type = CosmeticType.KILL_EFFECT, name = "Thunder", description = "Spawn a lighting bolt at the opponent", permission = "thunder", icon = Material.STICK, slot = 11)
public class ThunderKillEffect extends BaseCosmetic {

    @Override
    public void execute(Player player) {
        Location location = player.getLocation();
        location.getWorld().strikeLightningEffect(location);
    }
}
