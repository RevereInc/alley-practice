package dev.revere.alley.feature.ability;

import com.google.common.collect.Maps;
import dev.revere.alley.Alley;
import dev.revere.alley.feature.ability.data.AbilityData;
import dev.revere.alley.feature.ability.listener.AbilityListener;
import dev.revere.alley.tool.logger.Logger;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

/**
 * @author Remi
 * @project alley-practice
 * @date 13/06/2025
 */
@Getter
public class AbilityService {
    protected final Alley plugin;
    protected final AbilityHandler handler;

    private final Map<String, AbilityData> abilities = Maps.newConcurrentMap();

    // todo: multiple actions per trigger type

    public AbilityService(Alley plugin) {
        this.plugin = plugin;
        this.handler = new AbilityHandler();

        loadAbilities();
    }

    public void loadAbilities() {
        abilities.clear();

        FileConfiguration config = this.plugin.getConfigService().getAbilitiesConfig();

        ConfigurationSection abilitiesSection = config.getConfigurationSection("abilities");
        if (abilitiesSection == null) return;

        for (String abilityKey : abilitiesSection.getKeys(false)) {
            ConfigurationSection abilitySection = abilitiesSection.getConfigurationSection(abilityKey);
            if (abilitySection == null) continue;

            try {
                AbilityData abilityData = new AbilityData(abilityKey, abilitySection);
                this.abilities.put(abilityKey.toUpperCase(), abilityData);

                if (abilityData.hasActions()) {
                    AbilityListener listener = new AbilityListener(abilityData, handler);
                    listener.register();
                }
            } catch (Exception e) {
                Logger.logException("Failed to load ability: " + abilityKey, e);
                continue;
            }
        }
    }

    public Optional<AbilityData> getAbility(String key) {
        if (key == null || key.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(abilities.get(key.toUpperCase()));
    }

    public Collection<AbilityData> getAbilities() {
        return Collections.unmodifiableCollection(abilities.values());
    }
}
