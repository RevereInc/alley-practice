package dev.revere.alley.feature.ffa.listener;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.common.geom.Cuboid;
import dev.revere.alley.core.locale.LocaleService;
import dev.revere.alley.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import dev.revere.alley.core.profile.Profile;
import dev.revere.alley.core.profile.ProfileService;
import dev.revere.alley.core.profile.enums.ProfileState;
import dev.revere.alley.feature.combat.CombatService;
import dev.revere.alley.feature.ffa.FFAService;
import dev.revere.alley.feature.ffa.FFAState;
import dev.revere.alley.feature.ffa.spawn.FFASpawnService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Emmy
 * @project Alley
 * @since 25/01/2025
 */
public class FFACuboidListener implements Listener {
    private final Map<UUID, Boolean> playerStates = new HashMap<>();

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Cuboid cuboid = AlleyPlugin.getInstance().getService(FFASpawnService.class).getCuboid();
        if (cuboid == null) {
            return;
        }

        Player player = event.getPlayer();
        if (event.getFrom().getBlock().equals(event.getTo().getBlock())) {
            return;
        }

        ProfileService profileService = AlleyPlugin.getInstance().getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());
        if (profile == null || profile.getState() != ProfileState.FFA) {
            return;
        }

        FFAService ffaService = AlleyPlugin.getInstance().getService(FFAService.class);
        CombatService combatService = AlleyPlugin.getInstance().getService(CombatService.class);
        LocaleService localeService = AlleyPlugin.getInstance().getService(LocaleService.class);

        if (ffaService.getFFAMatch(player) == null || !ffaService.getMatchByPlayer(player).isPresent()) {
            return;
        }

        UUID playerId = player.getUniqueId();

        boolean isInCuboid = cuboid.isIn(player);
        boolean wasInCuboid = this.playerStates.getOrDefault(playerId, true);

        if (isInCuboid != wasInCuboid) {
            if (isInCuboid) {
                if (combatService.isPlayerInCombat(playerId)) return;
                player.sendMessage(localeService.getString(GlobalMessagesLocaleImpl.FFA_SPAWN_ENTERED));
                ffaService.getMatchByPlayer(player).ifPresent(match -> match.getGameFFAPlayer(player).setState(FFAState.SPAWN));
            } else {
                player.sendMessage(localeService.getString(GlobalMessagesLocaleImpl.FFA_SPAWN_LEFT));
                ffaService.getMatchByPlayer(player).ifPresent(match -> match.getGameFFAPlayer(player).setState(FFAState.FIGHTING));
            }

            this.playerStates.put(playerId, isInCuboid);
        }
    }
}