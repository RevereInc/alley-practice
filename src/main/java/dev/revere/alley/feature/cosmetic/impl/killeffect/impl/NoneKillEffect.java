package dev.revere.alley.feature.cosmetic.impl.killeffect.impl;

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
@CosmeticData(type = CosmeticType.KILL_EFFECT, name = "None", description = "Remove your kill effect", icon = Material.BARRIER, slot = 10)
public class NoneKillEffect extends BaseCosmetic {
    @Override
    public void execute(Player player) {

    }
}
