package dev.revere.alley.profile.data.impl;

import dev.revere.alley.Alley;
import dev.revere.alley.feature.cosmetic.BaseCosmetic;
import dev.revere.alley.feature.cosmetic.CosmeticType;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Remi
 * @project Alley
 * @date 6/1/2024
 */
@Getter
@Setter
public class ProfileCosmeticData {
    protected final Alley plugin = Alley.getInstance();
    private String selectedKillEffect;
    private String selectedSoundEffect;
    private String selectedProjectileTrail;
    private String selectedKillMessage;

    public ProfileCosmeticData() {
        this.selectedKillEffect = "None";
        this.selectedSoundEffect = "None";
        this.selectedProjectileTrail = "None";
        this.selectedKillMessage = "None";
    }

    /**
     * Sets the active cosmetic for the correct category using its type.
     *
     * @param cosmetic The cosmetic object to select.
     */
    public void setSelected(BaseCosmetic cosmetic) {
        if (cosmetic == null) return;

        switch (cosmetic.getType()) {
            case KILL_EFFECT:
                this.selectedKillEffect = cosmetic.getName();
                break;
            case SOUND_EFFECT:
                this.selectedSoundEffect = cosmetic.getName();
                break;
            case PROJECTILE_TRAIL:
                this.selectedProjectileTrail = cosmetic.getName();
                break;
            case KILL_MESSAGE:
                this.selectedKillMessage = cosmetic.getName();
                break;
        }
    }

    /**
     * Gets the name of the selected cosmetic for a given type.
     *
     * @param type The CosmeticType category to check.
     * @return The name of the selected cosmetic.
     */
    public String getSelected(CosmeticType type) {
        switch (type) {
            case KILL_EFFECT:
                return this.selectedKillEffect;
            case SOUND_EFFECT:
                return this.selectedSoundEffect;
            case PROJECTILE_TRAIL:
                return this.selectedProjectileTrail;
            case KILL_MESSAGE:
                return this.selectedKillMessage;
            default:
                return "None";
        }
    }

    /**
     * Checks if a specific cosmetic is currently selected.
     *
     * @param cosmetic The cosmetic to check.
     * @return true if it is the currently selected cosmetic for its type.
     */
    public boolean isSelected(BaseCosmetic cosmetic) {
        if (cosmetic == null) return false;
        String selectedName = getSelected(cosmetic.getType());
        return cosmetic.getName().equals(selectedName);
    }
}