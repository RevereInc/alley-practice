package dev.revere.alley.feature.ability.data;

import dev.revere.alley.util.chat.CC;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Remi
 * @project alley-practice
 * @date 13/06/2025
 */
@Getter
public class AbilityData {
    private final String key;
    private final String displayName;
    private final List<String> description;
    private final Material material;
    private final int data;
    private final int cooldown;
    private final boolean enabled;

    private final AbilityAction leftClickAction;
    private final AbilityAction rightClickAction;
    private final AbilityAction projectileHitAction;
    private final AbilityAction damageAction;

    private final List<String> playerMessages;
    private final List<String> targetMessages;

    private final int maxDistance;
    private final double damage;
    private final List<String> effects;
    private final int effectDuration;
    private final int effectAmplifier;

    public AbilityData(String key, ConfigurationSection section) {
        this.key = key.toUpperCase();
        this.enabled = section.getBoolean("ENABLED", true);

        ConfigurationSection iconSection = section.getConfigurationSection("ICON");
        if (iconSection == null) {
            this.material = Material.STONE;
            this.data = 0;
            this.description = null;
            this.displayName = key;
        } else {
            this.material = Material.valueOf(iconSection.getString("MATERIAL", "STONE").toUpperCase());
            this.data = iconSection.getInt("DATA", 0);
            this.description = iconSection.getStringList("DESCRIPTION").stream().map(CC::translate).collect(Collectors.toList());
            this.displayName = CC.translate(iconSection.getString("DISPLAY_NAME", key));
        }

        this.cooldown = section.getInt("COOLDOWN", 0);

        this.leftClickAction = loadAction(section, "LEFT_CLICK");
        this.rightClickAction = loadAction(section, "RIGHT_CLICK");
        this.projectileHitAction = loadAction(section, "PROJECTILE_HIT");
        this.damageAction = loadAction(section, "DAMAGE");

        ConfigurationSection messagesSection = section.getConfigurationSection("MESSAGES");
        if (messagesSection == null) {
            this.playerMessages = null;
            this.targetMessages = null;
        } else {
            this.playerMessages = messagesSection.getStringList("PLAYER").stream().map(CC::translate).collect(Collectors.toList());
            this.targetMessages = messagesSection.getStringList("TARGET").stream().map(CC::translate).collect(Collectors.toList());
        }

        this.maxDistance = section.getInt("MAX_DISTANCE", 100);
        this.damage = section.getDouble("DAMAGE", 0.0);
        this.effects = section.getStringList("EFFECTS");
        this.effectDuration = section.getInt("EFFECT_DURATION", 20);
        this.effectAmplifier = section.getInt("EFFECT_AMPLIFIER", 0);
    }

    private AbilityAction loadAction(ConfigurationSection section, String actionType) {
        ConfigurationSection actionSection = section.getConfigurationSection("ACTIONS." + actionType);
        if (actionSection == null || !actionSection.getBoolean("ENABLED", false)) return null;

        return new AbilityAction(
                actionSection.getString("TYPE", "NONE"),
                actionSection.getDouble("DAMAGE", 0.0),
                actionSection.getInt("TELEPORT_DISTANCE", 0),
                actionSection.getStringList("EFFECTS"),
                actionSection.getInt("EFFECT_DURATION", 20),
                actionSection.getInt("EFFECT_AMPLIFIER", 0),
                actionSection.getBoolean("SWITCH_POSITIONS", false),
                actionSection.getBoolean("LAUNCH_PROJECTILE", false),
                actionSection.getString("PROJECTILE_TYPE", "SNOWBALL").toUpperCase(),
                actionSection.getStringList("COMMANDS")
        );
    }

    public boolean hasActions() {
        return leftClickAction != null || rightClickAction != null || projectileHitAction != null || damageAction != null;
    }

    @Getter
    public static class AbilityAction {
        private final String type;
        private final double damage;
        private final int teleportDistance;
        private final List<String> effects;
        private final int effectDuration;
        private final int effectAmplifier;
        private final boolean switchPositions;
        private final boolean launchProjectile;
        private final String projectileType;
        private final List<String> commands;

        public AbilityAction(String type, double damage, int teleportDistance, List<String> effects, int effectDuration, int effectAmplifier, boolean switchPositions, boolean launchProjectile, String projectileType, List<String> commands) {
            this.type = type.toUpperCase();
            this.damage = damage;
            this.teleportDistance = teleportDistance;
            this.effects = effects;
            this.effectDuration = effectDuration;
            this.effectAmplifier = effectAmplifier;
            this.switchPositions = switchPositions;
            this.launchProjectile = launchProjectile;
            this.projectileType = projectileType.toUpperCase();
            this.commands = commands;
        }
    }
}
