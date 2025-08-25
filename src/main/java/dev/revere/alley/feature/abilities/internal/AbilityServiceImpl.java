package dev.revere.alley.feature.abilities.internal;

import dev.revere.alley.bootstrap.AlleyContext;
import dev.revere.alley.bootstrap.annotation.Service;
import dev.revere.alley.common.TaskUtil;
import dev.revere.alley.common.constants.PluginConstant;
import dev.revere.alley.common.item.ItemBuilder;
import dev.revere.alley.common.logger.Logger;
import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.config.ConfigService;
import dev.revere.alley.feature.abilities.Ability;
import dev.revere.alley.feature.abilities.AbilityService;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.reflections.Reflections;

import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Service(provides = AbilityService.class, priority = 380)
public class AbilityServiceImpl implements AbilityService {
    private final ConfigService configService;
    private final PluginConstant pluginConstant;

    private final Set<Ability> abilities = new HashSet<>();

    public AbilityServiceImpl(ConfigService configService, PluginConstant pluginConstant) {
        this.configService = configService;
        this.pluginConstant = pluginConstant;
    }

    @Override
    public void initialize(AlleyContext context) {
        this.registerAbilities();

        Ability.getAbilities().forEach(Ability::register);
    }

    private void registerAbilities() {
        Reflections reflections = this.pluginConstant.getReflections();

        for (Class<? extends Ability> clazz : reflections.getSubTypesOf(Ability.class)) {
            if (clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers())) {
                continue;
            }
            try {
                Ability instance = clazz.getDeclaredConstructor().newInstance();
                this.abilities.add(instance);
            } catch (Exception exception) {
                Logger.logException("Failed to instantiate ability: " + clazz.getName(), exception);
            }
        }
    }

    @Override
    public <T extends Ability> T getAbility(Class<T> abilityClass) {
        return this.abilities.stream()
                .filter(abilityClass::isInstance)
                .map(abilityClass::cast)
                .findFirst()
                .orElse(null);
    }

    @Override
    public ItemStack getAbilityItem(String abilityKey, int amount) {
        return new ItemBuilder(getMaterial(abilityKey))
                .amount(amount)
                .durability(getData(abilityKey))
                .name(getDisplayName(abilityKey))
                .lore(getDescription(abilityKey))
                .build();
    }

    @Override
    public String getDisplayName(String abilityKey) {
        return configService.getAbilityConfig().getString(abilityKey + ".ICON.DISPLAYNAME");
    }

    @Override
    public List<String> getDescription(String abilityKey) {
        return configService.getAbilityConfig().getStringList(abilityKey + ".ICON.DESCRIPTION");
    }

    public Material getMaterial(String ability) {
        return Material.valueOf(configService.getAbilityConfig().getString(ability + ".ICON.MATERIAL"));
    }

    public int getData(String ability) {
        return configService.getAbilityConfig().getInt(ability + ".ICON.DATA");
    }

    public int getCooldown(String ability) {
        return configService.getAbilityConfig().getInt(ability + ".COOLDOWN");
    }

    @Override
    public Set<String> getAbilityKeys() {
        return configService.getAbilityConfig().getConfigurationSection("").getKeys(false);
    }

    @Override
    public void giveAbility(CommandSender sender, Player player, String key, String abilityName, int amount) {
        player.getInventory().addItem(this.getAbilityItem(key, amount));

        if (player == sender) {
            player.sendMessage(CC.translate(configService.getAbilityConfig().getString("RECEIVED_ABILITY").replace("%ABILITY%", abilityName).replace("%AMOUNT%", String.valueOf(amount))));
        } else {
            player.sendMessage(CC.translate(configService.getAbilityConfig().getString("RECEIVED_ABILITY").replace("%ABILITY%", abilityName).replace("%AMOUNT%", String.valueOf(amount))));
            sender.sendMessage(CC.translate(configService.getAbilityConfig().getString("GIVE_ABILITY").replace("%ABILITY%", abilityName).replace("%AMOUNT%", String.valueOf(amount)).replace("%PLAYER%", player.getName())));
        }
    }

    @Override
    public void sendPlayerMessage(Player player, String abilityKey) {
        String displayName = this.getDisplayName(abilityKey);
        String cooldown = String.valueOf(getCooldown(abilityKey));

        this.configService.getAbilityConfig().getStringList(abilityKey + ".MESSAGE.PLAYER").forEach(
                message -> Logger.info(message
                        .replace("%ABILITY%", displayName)
                        .replace("%COOLDOWN%", cooldown))
        );
    }

    @Override
    public void sendTargetMessage(Player target, Player player, String abilityKey) {
        String displayName = this.getDisplayName(abilityKey);
        this.configService.getAbilityConfig().getStringList(abilityKey + ".MESSAGE.TARGET").forEach(
                message -> player.sendMessage(message.replace("%ABILITY%", displayName).replace("%PLAYER%", player.getName()).replace("%TARGET%", target.getName()))
        );
    }

    @Override
    public void sendCooldownMessage(Player player, String abilityName, String cooldown) {
        this.configService.getAbilityConfig().getStringList("STILL_ON_COOLDOWN").forEach(
                message -> player.sendMessage(message.replace("%ABILITY%", abilityName).replace("%COOLDOWN%", cooldown))
        );

    }

    @Override
    public void sendCooldownExpiredMessage(Player player, String abilityName, String ability) {
        TaskUtil.runLaterAsync(() -> this.configService.getAbilityConfig().getStringList("COOLDOWN_EXPIRED").forEach(message ->
                player.sendMessage(message.replace("%ABILITY%", abilityName))), getCooldown(ability) * 20L)
        ;
    }
}
