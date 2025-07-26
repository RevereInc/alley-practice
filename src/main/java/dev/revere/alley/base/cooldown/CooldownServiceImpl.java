package dev.revere.alley.base.cooldown;

import dev.revere.alley.base.cooldown.enums.CooldownType;
import dev.revere.alley.plugin.annotation.Service;
import dev.revere.alley.tool.triple.impl.MutableTriple;
import lombok.Getter;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Remi
 * @project Alley
 * @date 5/27/2024
 */
@Getter
@Service(provides = CooldownService.class, priority = 200)
public class CooldownServiceImpl implements CooldownService {
    private final List<MutableTriple<UUID, CooldownType, Cooldown>> cooldowns = new CopyOnWriteArrayList<>();

    @Override
    public List<MutableTriple<UUID, CooldownType, Cooldown>> getCooldowns() {
        return cooldowns;
    }

    @Override
    public void addCooldown(UUID uuid, CooldownType type, Cooldown cooldown) {
        this.cooldowns.removeIf(triple -> triple.getA().equals(uuid) && triple.getB().equals(type));
        this.cooldowns.add(new MutableTriple<>(uuid, type, cooldown));
    }

    @Override
    public Cooldown getCooldown(UUID uuid, CooldownType type) {
        return this.cooldowns.stream()
                .filter(triple -> triple.getA().equals(uuid) && triple.getB().equals(type))
                .map(MutableTriple::getC)
                .findFirst()
                .orElse(null);
    }

    @Override
    public void removeCooldown(UUID uuid, CooldownType type) {
        this.cooldowns.removeIf(triple -> triple.getA().equals(uuid) && triple.getB().equals(type));
    }
}