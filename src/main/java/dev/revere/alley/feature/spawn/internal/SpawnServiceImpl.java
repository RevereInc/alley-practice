package dev.revere.alley.feature.spawn.internal;

import dev.revere.alley.bootstrap.AlleyContext;
import dev.revere.alley.bootstrap.annotation.Service;
import dev.revere.alley.common.PlayerUtil;
import dev.revere.alley.common.logger.Logger;
import dev.revere.alley.common.serializer.Serializer;
import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.locale.LocaleService;
import dev.revere.alley.core.locale.internal.impl.ServerLocaleImpl;
import dev.revere.alley.feature.spawn.SpawnService;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 17/05/2024 - 17:47
 */
@Getter
@Service(provides = SpawnService.class, priority = 240)
public class SpawnServiceImpl implements SpawnService {
    private final LocaleService localeService;

    private Location location;

    /**
     * DI Constructor for the SpawnServiceImpl class.
     *
     * @param localeService The locale service.
     */
    public SpawnServiceImpl(LocaleService localeService) {
        this.localeService = localeService;
    }

    @Override
    public void initialize(AlleyContext context) {
        this.loadSpawnLocation();
    }

    private void loadSpawnLocation() {
        Location location = Serializer.deserializeLocation(this.localeService.getMessage(ServerLocaleImpl.LOCATION_SPAWN));
        if (location == null) {
            Logger.error("Spawn location is null.");
            return;
        }

        this.location = new Location(location.getWorld(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }

    @Override
    public void updateSpawnLocation(Location location) {
        if (location == null) return;

        this.location = location;
        this.localeService.setMessage(ServerLocaleImpl.LOCATION_SPAWN, Serializer.serializeLocation(location));
    }

    @Override
    public void teleportToSpawn(Player player) {
        if (this.location == null) {
            Logger.error("Cannot teleport " + player.getName() + " to spawn: Spawn location is not set.");
            player.sendMessage(CC.translate("&cThe server spawn is not set. Please notify an administrator."));
            return;
        }

        player.teleport(this.location);
        PlayerUtil.reset(player, false, true);
    }
}