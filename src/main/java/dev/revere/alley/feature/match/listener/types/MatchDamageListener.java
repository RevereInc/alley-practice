package dev.revere.alley.feature.match.listener.types;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.adapter.core.CoreAdapter;
import dev.revere.alley.common.reflect.ReflectionService;
import dev.revere.alley.common.reflect.internal.types.ActionBarReflectionServiceImpl;
import dev.revere.alley.common.text.CC;
import dev.revere.alley.common.text.Symbol;
import dev.revere.alley.core.profile.Profile;
import dev.revere.alley.core.profile.ProfileService;
import dev.revere.alley.core.profile.enums.ProfileState;
import dev.revere.alley.feature.combat.CombatService;
import dev.revere.alley.feature.kit.Kit;
import dev.revere.alley.feature.kit.setting.types.mechanic.KitSettingNoDamageImpl;
import dev.revere.alley.feature.kit.setting.types.mechanic.KitSettingNoFallDamageImpl;
import dev.revere.alley.feature.kit.setting.types.mode.KitSettingBoxing;
import dev.revere.alley.feature.kit.setting.types.mode.KitSettingHideAndSeek;
import dev.revere.alley.feature.kit.setting.types.mode.KitSettingSpleef;
import dev.revere.alley.feature.kit.setting.types.mode.KitSettingSumo;
import dev.revere.alley.feature.kit.setting.types.visual.KitSettingBowShotIndicator;
import dev.revere.alley.feature.kit.setting.types.visual.KitSettingHealthBar;
import dev.revere.alley.feature.match.Match;
import dev.revere.alley.feature.match.MatchState;
import dev.revere.alley.feature.match.internal.types.HideAndSeekMatch;
import dev.revere.alley.feature.match.model.GameParticipant;
import dev.revere.alley.feature.match.model.internal.MatchGamePlayer;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * @author Emmy
 * @project Alley
 * @since 08/02/2025
 */
public class MatchDamageListener implements Listener {
    @EventHandler
    private void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        ProfileService profileService = AlleyPlugin.getInstance().getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());

        if (profile.getState() == ProfileState.SPECTATING) event.setCancelled(true);
        if (profile.getState() == ProfileState.PLAYING) {
            Kit matchKit = profile.getMatch().getKit();

            if (matchKit.isSettingEnabled(KitSettingNoFallDamageImpl.class)
                    && event.getCause() == EntityDamageEvent.DamageCause.FALL) {
                event.setCancelled(true);
            }

            if (event.getCause() == EntityDamageEvent.DamageCause.FALL
                    && (matchKit.isSettingEnabled(KitSettingBoxing.class)
                    || matchKit.isSettingEnabled(KitSettingSumo.class)
                    || matchKit.isSettingEnabled(KitSettingSpleef.class))) {
                event.setCancelled(true);
            }

            if (profile.getMatch().getState() != MatchState.RUNNING) {
                event.setCancelled(true);
                return;
            }

            if (profile.getMatch().getGamePlayer(player).isDead()) {
                event.setCancelled(true);
                return;
            }

            if (matchKit.isSettingEnabled(KitSettingBoxing.class)
                    || matchKit.isSettingEnabled(KitSettingSumo.class)
                    || matchKit.isSettingEnabled(KitSettingSpleef.class)
                    || matchKit.isSettingEnabled(KitSettingNoDamageImpl.class)) {
                event.setDamage(0);
                player.setHealth(player.getMaxHealth());
                player.updateInventory();
            }
        }
    }

    @EventHandler
    private void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player damaged = (Player) event.getEntity();
        Player attacker;

        if (event.getDamager() instanceof Player) {
            attacker = (Player) event.getDamager();
        } else if (event.getDamager() instanceof Projectile) {
            Projectile projectile = (Projectile) event.getDamager();
            if (projectile.getShooter() instanceof Player) {
                attacker = (Player) projectile.getShooter();
            } else {
                return;
            }
        } else {
            return;
        }

        ProfileService profileService = AlleyPlugin.getInstance().getService(ProfileService.class);

        Profile damagedProfile = profileService.getProfile(damaged.getUniqueId());
        Profile attackerProfile = profileService.getProfile(attacker.getUniqueId());

        if (damagedProfile.getState() == ProfileState.SPECTATING || attackerProfile.getState() == ProfileState.SPECTATING) {
            event.setCancelled(true);
            return;
        }

        if (damagedProfile.getState() == ProfileState.PLAYING && attackerProfile.getState() == ProfileState.PLAYING) {
            Match match = damagedProfile.getMatch();
            if (match == null || attackerProfile.getMatch() != match) {
                event.setCancelled(true);
                return;
            }

            if (match.getState() != MatchState.RUNNING) {
                event.setCancelled(true);
                return;
            }

            if (match.getGamePlayer(damaged).isDead() || match.getGamePlayer(attacker).isDead()) {
                event.setCancelled(true);
                return;
            }

            if (match.getGamePlayer(attacker).isDead()) {
                event.setCancelled(true);
                return;
            }

            if (!attacker.getUniqueId().equals(damaged.getUniqueId()) && match.isInSameTeam(attacker, damaged)) {
                if (match.getKit().isSettingEnabled(KitSettingHideAndSeek.class)) {
                    HideAndSeekMatch matchHideAndSeek = (HideAndSeekMatch) attackerProfile.getMatch();
                    GameParticipant<MatchGamePlayer> seekers = matchHideAndSeek.getParticipantA();

                    boolean isSeeker = seekers.containsPlayer(attacker.getUniqueId());

                    if (matchHideAndSeek.getGameEndTask() == null && isSeeker) {
                        return;
                    }
                }

                event.setCancelled(true);
                return;
            }

            if (!attacker.getUniqueId().equals(damaged.getUniqueId())) {
                attackerProfile.getMatch().getGamePlayer(attacker).getData().handleAttack();
                damagedProfile.getMatch().getGamePlayer(damaged).getData().resetCombo();

                GameParticipant<MatchGamePlayer> participant = match.getParticipant(attacker);
                GameParticipant<MatchGamePlayer> opponent = match.getParticipant(damaged);

                if (participant != null && opponent != null) {
                    participant.setTeamHits(participant.getTeamHits() + 1);

                    if (match.getKit().isSettingEnabled(KitSettingBowShotIndicator.class) && event.getDamager() instanceof Arrow) {
                        double finalHealth = damaged.getHealth() - event.getFinalDamage();
                        finalHealth = Math.max(0, finalHealth);

                        if (finalHealth > 0) {
                            attacker.sendMessage(CC.translate(AlleyPlugin.getInstance().getService(CoreAdapter.class).getCore().getPlayerColor(damaged) + damaged.getName() + " &7&l" + Symbol.ARROW_R + " &6" + String.format("%.1f", finalHealth) + " &c" + Symbol.HEART));
                        }
                    }

                    if (match.getKit().isSettingEnabled(KitSettingBoxing.class)) {
                        int lowestPlayerCount = match.getParticipants().stream()
                                .mapToInt(p -> p.getPlayers().size())
                                .filter(size -> size > 0)
                                .min()
                                .orElse(1);

                        int requiredHits = lowestPlayerCount * 100;

                        if (participant.getTeamHits() >= requiredHits) {
                            opponent.getPlayers().forEach(matchGamePlayer -> {
                                match.handleDeath(matchGamePlayer.getTeamPlayer(), EntityDamageEvent.DamageCause.ENTITY_ATTACK);
                            });
                        }
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    private void onEntityDamageByEntityMonitor(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Profile profile = AlleyPlugin.getInstance().getService(ProfileService.class).getProfile(event.getEntity().getUniqueId());
            if (profile.getState() == ProfileState.SPECTATING) {
                event.setCancelled(true);
                return;
            }

            if (profile.getState() == ProfileState.PLAYING) {
                Player player = (Player) event.getEntity();
                Player attacker = (Player) event.getDamager();

                AlleyPlugin.getInstance().getService(CombatService.class).setLastAttacker(player, attacker);

                if (profile.getMatch().getKit().isSettingEnabled(KitSettingHealthBar.class)) {
                    AlleyPlugin.getInstance().getService(ReflectionService.class).getReflectionService(ActionBarReflectionServiceImpl.class).visualizeTargetHealth(attacker, player);
                }
            }
        }
    }
}