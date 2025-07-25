package dev.revere.alley.feature.abilities;

import dev.revere.alley.api.constant.PluginConstant;
import dev.revere.alley.config.ConfigService;
import dev.revere.alley.plugin.AlleyContext;
import dev.revere.alley.plugin.annotation.Service;
import dev.revere.alley.tool.item.ItemBuilder;
import dev.revere.alley.tool.logger.Logger;
import dev.revere.alley.util.TaskUtil;
import dev.revere.alley.util.chat.CC;
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
            } catch (Exception e) {
                Logger.logException("Failed to instantiate ability: " + clazz.getName(), e);
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
            player.sendMessage(CC.translate(configService.getAbilityConfig().getString("RECEIVED_ABILITY")
                    .replace("%ABILITY%", abilityName)
                    .replace("%AMOUNT%", String.valueOf(amount))));
        } else {
            player.sendMessage(CC.translate(configService.getAbilityConfig().getString("RECEIVED_ABILITY")
                    .replace("%ABILITY%", abilityName)
                    .replace("%AMOUNT%", String.valueOf(amount))));
            sender.sendMessage(CC.translate(configService.getAbilityConfig().getString("GIVE_ABILITY")
                    .replace("%ABILITY%", abilityName)
                    .replace("%AMOUNT%", String.valueOf(amount))
                    .replace("%PLAYER%", player.getName())));
        }
    }

    @Override
    public void sendPlayerMessage(Player player, String abilityKey) {
        String displayName = getDisplayName(abilityKey);
        String cooldown = String.valueOf(getCooldown(abilityKey));

        configService.getAbilityConfig().getStringList(abilityKey + ".MESSAGE.PLAYER").forEach(
                message -> CC.message(player, message
                        .replace("%ABILITY%", displayName)
                        .replace("%COOLDOWN%", cooldown)));
    }

    @Override
    public void sendTargetMessage(Player target, Player player, String abilityKey) {
        String displayName = getDisplayName(abilityKey);

        configService.getAbilityConfig().getStringList(abilityKey + ".MESSAGE.TARGET").forEach(
                message -> CC.message(target, message
                        .replace("%ABILITY%", displayName)
                        .replace("%PLAYER%", player.getName())
                        .replace("%TARGET%", target.getName())));
    }

    @Override
    public void sendCooldownMessage(Player player, String abilityName, String cooldown) {
        CC.message(player, configService.getAbilityConfig().getString("STILL_ON_COOLDOWN")
                .replace("%ABILITY%", abilityName)
                .replace("%COOLDOWN%", cooldown));
    }

    @Override
    public void sendCooldownExpiredMessage(Player player, String abilityName, String ability) {
        TaskUtil.runLaterAsync(() ->
                CC.message(player, configService.getAbilityConfig().getString("COOLDOWN_EXPIRED")
                        .replace("%ABILITY%", abilityName)), getCooldown(ability) * 20L);
    }
}
