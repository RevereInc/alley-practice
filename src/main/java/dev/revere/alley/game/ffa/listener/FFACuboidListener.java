package dev.revere.alley.game.ffa.listener;

import dev.revere.alley.Alley;
import dev.revere.alley.base.combat.CombatService;
import dev.revere.alley.game.ffa.FFAService;
import dev.revere.alley.game.ffa.cuboid.FFASpawnService;
import dev.revere.alley.game.ffa.enums.FFAState;
import dev.revere.alley.profile.ProfileService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.enums.ProfileState;
import dev.revere.alley.tool.cuboid.Cuboid;
import dev.revere.alley.util.chat.CC;
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
        Cuboid cuboid = Alley.getInstance().getService(FFASpawnService.class).getCuboid();
        if (cuboid == null) {
            return;
        }

        Player player = event.getPlayer();
        if (event.getFrom().getBlock().equals(event.getTo().getBlock())) {
            return;
        }

        ProfileService profileService = Alley.getInstance().getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());
        if (profile == null || profile.getState() != ProfileState.FFA) {
            return;
        }

        FFAService ffaService = Alley.getInstance().getService(FFAService.class);
        CombatService combatService = Alley.getInstance().getService(CombatService.class);

        if (ffaService.getFFAMatch(player) == null || !ffaService.getMatchByPlayer(player).isPresent()) {
            return;
        }

        UUID playerId = player.getUniqueId();

        boolean isInCuboid = cuboid.isIn(player);
        boolean wasInCuboid = this.playerStates.getOrDefault(playerId, true);

        if (isInCuboid != wasInCuboid) {
            if (isInCuboid) {
                if (combatService.isPlayerInCombat(playerId)) return;
                player.sendMessage(CC.translate("&aYou have entered the FFA spawn area."));
                ffaService.getMatchByPlayer(player).ifPresent(match -> match.getGameFFAPlayer(player).setState(FFAState.SPAWN));
            } else {
                player.sendMessage(CC.translate("&cYou have left the FFA spawn area."));
                ffaService.getMatchByPlayer(player).ifPresent(match -> match.getGameFFAPlayer(player).setState(FFAState.FIGHTING));
            }

            this.playerStates.put(playerId, isInCuboid);
        }
    }
}