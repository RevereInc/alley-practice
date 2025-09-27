package dev.revere.alley.feature.arena;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.core.locale.LocaleService;
import dev.revere.alley.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
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
        LocaleService localeService = AlleyPlugin.getInstance().getService(LocaleService.class);

        if (arena.getMinimum() == null || arena.getMaximum() == null) {
            player.sendMessage(localeService.getString(GlobalMessagesLocaleImpl.ARENA_NO_SELECTION));
            return false;
        }

        if (arena.getPos1() == null || arena.getPos2() == null) {
            player.sendMessage(localeService.getString(GlobalMessagesLocaleImpl.ARENA_SPAWN_NOT_SET));
            return false;
        }

        if (arena.getCenter() == null) {
            player.sendMessage(localeService.getString(GlobalMessagesLocaleImpl.ARENA_CENTER_NOT_SET));
            return false;
        }

        if (arena.getKits().isEmpty()) {
            player.sendMessage(localeService.getString(GlobalMessagesLocaleImpl.ARENA_MUST_ADD_KIT));
            return false;
        }

        KitService kitService = AlleyPlugin.getInstance().getService(KitService.class);
        for (String kitName : arena.getKits()) {
            Kit kit = kitService.getKit(kitName);
            if (kit == null) {
                player.sendMessage(localeService.getString(GlobalMessagesLocaleImpl.ARENA_ASSIGNED_KIT_NULL).replace("{kit-name}", kitName));
                return false;
            }

            if (arena.getType() == ArenaType.STANDALONE) {
                StandAloneArena standAloneArena = (StandAloneArena) arena;
                if ((kit.isSettingEnabled(KitSettingRounds.class) || kit.isSettingEnabled(KitSettingBridges.class)) && (standAloneArena.getTeam1Portal() == null || standAloneArena.getTeam2Portal() == null)) {
                    player.sendMessage(localeService.getString(GlobalMessagesLocaleImpl.ARENA_STANDALONE_PORTALS_NOT_SET));
                    return false;
                }
            }
        }

        return true;
    }
}