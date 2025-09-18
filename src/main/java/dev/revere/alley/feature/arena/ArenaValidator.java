package dev.revere.alley.feature.arena;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.common.text.CC;
import dev.revere.alley.feature.arena.internal.types.StandAloneArena;
import dev.revere.alley.feature.kit.Kit;
import dev.revere.alley.feature.kit.KitService;
import dev.revere.alley.feature.kit.setting.types.mode.KitSettingBridges;
import dev.revere.alley.feature.kit.setting.types.mode.KitSettingRounds;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project alley-practice
 * @since 07/09/2025
 */
public class ArenaValidator {
    /**
     * Validates if an arena is fully configured before enabling or disabling it.
     * Sends appropriate messages to the player if validation fails.
     *
     * @param player the player attempting to enable/disable the arena
     * @param arena  the arena to be validated
     * @return true if the arena is fully configured, false otherwise
     */
    public boolean isEligible(Player player, Arena arena) {
        if (arena.getMinimum() == null || arena.getMaximum() == null) {
            player.sendMessage(CC.translate("&cYou must set both the minimum and maximum points for this arena before toggling! (/arena wand)"));
            return false;
        }

        if (arena.getPos1() == null || arena.getPos2() == null) {
            player.sendMessage(CC.translate("&cYou must set both spawn positions for this arena before toggling! (/arena setspawn)"));
            return false;
        }

        if (arena.getCenter() == null) {
            player.sendMessage(CC.translate("&cYou must set the center point for this arena before toggling! (/arena setcenter)"));
            return false;
        }

        if (arena.getKits().isEmpty()) {
            player.sendMessage(CC.translate("&cYou must add at least one kit to this arena before toggling!"));
            return false;
        }

        KitService kitService = AlleyPlugin.getInstance().getService(KitService.class);
        for (String kitName : arena.getKits()) {
            Kit kit = kitService.getKit(kitName);
            if (kit == null) {
                player.sendMessage(CC.translate("&cThe kit &6" + kitName + " &cassigned to this arena does not exist! Please remove it before toggling."));
                return false;
            }

            if (arena.getType() == ArenaType.STANDALONE) {
                StandAloneArena standAloneArena = (StandAloneArena) arena;
                if ((kit.isSettingEnabled(KitSettingRounds.class) || kit.isSettingEnabled(KitSettingBridges.class)) && (standAloneArena.getTeam1Portal() == null || standAloneArena.getTeam2Portal() == null)) {
                    player.sendMessage(CC.translate("&cYou must set both team portals for this arena before toggling! (/arena setportal)"));
                    return false;
                }
            }
        }

        return true;
    }
}