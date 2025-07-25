package dev.revere.alley.base.combat;

import dev.revere.alley.plugin.annotation.Service;
import dev.revere.alley.profile.ProfileService;
import dev.revere.alley.profile.enums.ProfileState;
import dev.revere.alley.util.TimeUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Emmy
 * @project Alley
 * @since 07/03/2025
 */
@Getter
@Service(provides = CombatService.class, priority = 270)
public class CombatServiceImpl implements CombatService {
    private final ProfileService profileService;

    private final Map<UUID, Combat> combatMap = new ConcurrentHashMap<>();
    private final long ffaExpirationTime;
    private final long defaultExpirationTime;

    /**
     * Constructor for DI.
     */
    public CombatServiceImpl(ProfileService profileService) {
        this.profileService = profileService;
        this.ffaExpirationTime = 15 * 1000L; // 15 seconds
        this.defaultExpirationTime = 5 * 1000L; // 5 seconds
    }

    @Override
    public Map<UUID, Combat> getCombatMap() {
        return Collections.unmodifiableMap(this.combatMap);
    }

    @Override
    public void setLastAttacker(Player victim, Player attacker) {
        ProfileState victimState = this.profileService.getProfile(victim.getUniqueId()).getState();

        long expirationTime = (victimState == ProfileState.FFA) ? this.ffaExpirationTime : this.defaultExpirationTime;
        long currentTime = System.currentTimeMillis();

        tagPlayer(victim, attacker, currentTime, expirationTime);
        tagPlayer(attacker, victim, currentTime, expirationTime);
    }

    @Override
    public Player getLastAttacker(Player victim) {
        if (isTagExpired(victim)) {
            return null;
        }
        Combat combat = this.combatMap.get(victim.getUniqueId());
        return combat != null ? Bukkit.getPlayer(combat.getAttackerUUID()) : null;
    }

    @Override
    public void removeLastAttacker(Player player, boolean removeBoth) {
        Combat combat = this.combatMap.get(player.getUniqueId());
        if (combat != null) {
            this.combatMap.remove(player.getUniqueId());

            if (removeBoth) {
                UUID victimUUID = combat.getAttackerUUID();
                Combat victimCombat = this.combatMap.get(victimUUID);
                if (victimCombat != null) {
                    this.combatMap.remove(victimUUID);
                }
            }
        }
    }

    @Override
    public boolean isPlayerInCombat(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        return player != null && !isTagExpired(player);
    }

    @Override
    public boolean isExpired(Player player) {
        Combat combat = this.combatMap.get(player.getUniqueId());
        if (combat == null) return true;

        long attackTime = combat.getAttackTimestamp();
        long expirationTime = combat.getExpirationTime();

        if (System.currentTimeMillis() - attackTime > expirationTime) {
            this.removeLastAttacker(player, false);
            return true;
        }

        return false;
    }

    @Override
    public long getRemainingTime(Player victim) {
        Combat combat = this.combatMap.get(victim.getUniqueId());
        if (combat == null || isTagExpired(victim)) {
            return 0;
        }
        long attackTime = combat.getAttackTimestamp();
        long expirationTime = combat.getExpirationTime();
        return (attackTime + expirationTime) - System.currentTimeMillis();
    }

    @Override
    public String getRemainingTimeFormatted(Player victim) {
        long remaining = getRemainingTime(victim);
        return (remaining > 0) ? TimeUtil.millisToSecondsTimer(remaining) + "s" : "0.0s";
    }

    @Override
    public void resetCombatLog(Player player) {
        removeCombatTag(player);
    }

    /**
     * A clear helper method to tag one player with another.
     */
    private void tagPlayer(Player playerToTag, Player newAttacker, long currentTime, long expirationTime) {
        Combat combat = this.combatMap.get(playerToTag.getUniqueId());
        if (combat == null) {
            this.combatMap.put(playerToTag.getUniqueId(), new Combat(newAttacker.getUniqueId(), currentTime, expirationTime));
        } else {
            combat.setAttackerUUID(newAttacker.getUniqueId());
            combat.setAttackTimestamp(currentTime);
            combat.setExpirationTime(expirationTime);
        }
    }

    private boolean isTagExpired(Player player) {
        if (player == null) return true;

        Combat combat = this.combatMap.get(player.getUniqueId());
        if (combat == null) return true;

        long elapsedTime = System.currentTimeMillis() - combat.getAttackTimestamp();
        if (elapsedTime > combat.getExpirationTime()) {
            this.combatMap.remove(player.getUniqueId());
            return true;
        }
        return false;
    }

    private void removeCombatTag(Player player) {
        this.combatMap.remove(player.getUniqueId());
    }
}