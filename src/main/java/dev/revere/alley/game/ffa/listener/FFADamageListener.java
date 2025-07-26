package dev.revere.alley.game.ffa.listener;

import dev.revere.alley.Alley;
import dev.revere.alley.base.combat.CombatService;
import dev.revere.alley.base.kit.setting.impl.visual.KitSettingBowShotIndicator;
import dev.revere.alley.base.kit.setting.impl.visual.KitSettingHealthBar;
import dev.revere.alley.game.ffa.cuboid.FFASpawnService;
import dev.revere.alley.profile.ProfileService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.enums.ProfileState;
import dev.revere.alley.tool.reflection.ReflectionService;
import dev.revere.alley.tool.reflection.impl.ActionBarReflectionServiceImpl;
import dev.revere.alley.util.chat.CC;
import dev.revere.alley.util.chat.Symbol;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 * @author Emmy
 * @project alley-practice
 * @since 16/07/2025
 */
public class FFADamageListener implements Listener {
    /**
     * Handles the EntityDamageByEntityEvent.
     * The event is cancelled if the player is in the FFA state and tries to damage another player.
     *
     * @param event The EntityDamageByEntityEvent
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityDamageByEntityMonitor(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player) || !(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();
        Player attacker = (Player) event.getDamager();

        ProfileService profileService = Alley.getInstance().getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());
        if (profile.getState() != ProfileState.FFA) return;

        CombatService combatService = Alley.getInstance().getService(CombatService.class);
        combatService.setLastAttacker(player, attacker);

        if (profile.getFfaMatch().getKit().isSettingEnabled(KitSettingHealthBar.class)) {
            Alley.getInstance().getService(ReflectionService.class).getReflectionService(ActionBarReflectionServiceImpl.class).visualizeTargetHealth(attacker, player);
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Player attacker;

        if (event.getDamager() instanceof Player) {
            attacker = (Player) event.getDamager();
        } else if (event.getDamager() instanceof Projectile) {
            if (((Projectile) event.getDamager()).getShooter() instanceof Player) {
                attacker = (Player) ((Projectile) event.getDamager()).getShooter();
            } else {
                return;
            }
        } else {
            return;
        }

        if (!(event.getEntity() instanceof Player)) return;
        Player victim = (Player) event.getEntity();

        ProfileService profileService = Alley.getInstance().getService(ProfileService.class);
        Profile profile = profileService.getProfile(victim.getUniqueId());
        if (profile.getState() != ProfileState.FFA) {
            return;
        }

        if (victim != attacker) {
            if (profile.getFfaMatch().getKit().isSettingEnabled(KitSettingBowShotIndicator.class) && event.getDamager() instanceof Arrow) {
                double finalHealth = victim.getHealth() - event.getFinalDamage();
                finalHealth = Math.max(0, finalHealth);

                if (finalHealth > 0) {
                    attacker.sendMessage(CC.translate(profile.getNameColor() + victim.getName() + " &7&l" + Symbol.ARROW_R + " &6" + String.format("%.1f", finalHealth) + " &c" + Symbol.HEART));
                }
            }
        }

        FFASpawnService ffaSpawnService = Alley.getInstance().getService(FFASpawnService.class);
        if ((ffaSpawnService.getCuboid().isIn(victim) && ffaSpawnService.getCuboid().isIn(attacker)) || (!ffaSpawnService.getCuboid().isIn(victim) && ffaSpawnService.getCuboid().isIn(attacker)) || (ffaSpawnService.getCuboid().isIn(victim) && !ffaSpawnService.getCuboid().isIn(attacker))) {
            CombatService combatService = Alley.getInstance().getService(CombatService.class);
            if (combatService.isPlayerInCombat(victim.getUniqueId()) && combatService.isPlayerInCombat(attacker.getUniqueId())) {
                return;
            }

            event.setCancelled(true);
            return;
        }

        if (victim != attacker) {
            CombatService combatService = Alley.getInstance().getService(CombatService.class);
            combatService.setLastAttacker(victim, attacker);
        }
    }
}