package dev.revere.alley.base.kit.data;

import dev.revere.alley.base.kit.Kit;
import dev.revere.alley.game.match.player.enums.BaseRaiderRole;
import lombok.Getter;

/**
 * @author Emmy
 * @project Alley
 * @since 16/06/2025
 */
@Getter
public class BaseRaidingKitData {
    private Kit raiderKit;
    private Kit trapperKit;

    public BaseRaidingKitData() {
        this.raiderKit = null;
        this.trapperKit = null;
    }

    /**
     * Sets the kit for a specific raider role.
     *
     * @param kit  The kit to set.
     * @param role The role of the raider (RAIDER or TRAPPER).
     * @throws IllegalArgumentException if the role is not recognized.
     */
    public void setKit(Kit kit, BaseRaiderRole role) {
        if (role == BaseRaiderRole.RAIDER) {
            this.raiderKit = kit;
        } else if (role == BaseRaiderRole.TRAPPER) {
            this.trapperKit = kit;
        } else {
            throw new IllegalArgumentException("Invalid role: " + role);
        }
    }

    /**
     * Gets the kit for a specific raider role.
     *
     * @param role The role of the raider (RAIDER or TRAPPER).
     * @return The kit associated with the specified role.
     * @throws IllegalArgumentException if the role is not recognized.
     */
    public Kit getKitAssociatedWithRole(BaseRaiderRole role) {
        if (role == BaseRaiderRole.RAIDER) {
            return this.raiderKit;
        } else if (role == BaseRaiderRole.TRAPPER) {
            return this.trapperKit;
        } else {
            throw new IllegalArgumentException("Invalid role: " + role);
        }
    }
}