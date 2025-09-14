package dev.revere.alley.feature.explosives.internal;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.bootstrap.AlleyContext;
import dev.revere.alley.bootstrap.annotation.Service;
import dev.revere.alley.core.config.ConfigService;
import dev.revere.alley.core.locale.LocaleService;
import dev.revere.alley.core.locale.internal.impl.ServerLocaleImpl;
import dev.revere.alley.feature.explosives.ExplosiveService;
import dev.revere.alley.feature.explosives.listener.ExplosiveListener;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Emmy
 * @project Alley
 * @since 11/06/2025
 */
@Setter
@Getter
@Service(provides = ExplosiveService.class, priority = 370)
public class ExplosiveServiceImpl implements ExplosiveService {
    private final AlleyPlugin plugin;
    private final ConfigService configService;
    private final LocaleService localeService;

    private boolean enabled;

    private double horizontalFireballKnockback;
    private double verticalFireballKnockback;
    private double fireballExplosionRange;
    private double fireballThrowSpeed;

    private double tntExplosionRange;
    private int tntFuseTicks;

    /**
     * DI Constructor for the ExplosiveServiceImpl class.
     *
     * @param plugin        The main AlleyPlugin instance.
     * @param configService The configuration service.
     * @param localeService The locale service.
     */
    public ExplosiveServiceImpl(AlleyPlugin plugin, ConfigService configService, LocaleService localeService) {
        this.plugin = plugin;
        this.configService = configService;
        this.localeService = localeService;
    }

    @Override
    public void initialize(AlleyContext context) {
        this.enabled = this.localeService.getBoolean(ServerLocaleImpl.EXPLOSIVE_ENABLED);

        if (this.enabled) {
            this.assignValues();
            this.registerListener();
        }
    }

    private void assignValues() {
        this.horizontalFireballKnockback = this.localeService.getDouble(ServerLocaleImpl.EXPLOSIVE_FIREBALL_HORIZONTAL_KB_VALUE);
        this.verticalFireballKnockback = this.localeService.getDouble(ServerLocaleImpl.EXPLOSIVE_FIREBALL_VERTICAL_KB_VALUE);
        this.fireballExplosionRange = this.localeService.getDouble(ServerLocaleImpl.EXPLOSIVE_FIREBALL_EXPLOSION_RANGE_VALUE);
        this.fireballThrowSpeed = this.localeService.getDouble(ServerLocaleImpl.EXPLOSIVE_FIREBALL_THROW_SPEED_VALUE);

        this.tntFuseTicks = this.localeService.getInt(ServerLocaleImpl.EXPLOSIVE_TNT_FUSE_TICKS_VALUE);
        this.tntExplosionRange = this.localeService.getDouble(ServerLocaleImpl.EXPLOSIVE_TNT_EXPLOSION_RANGE_VALUE);
    }

    @Override
    public void save() {
        this.localeService.setBoolean(ServerLocaleImpl.EXPLOSIVE_ENABLED, this.enabled);

        this.localeService.setDouble(ServerLocaleImpl.EXPLOSIVE_FIREBALL_HORIZONTAL_KB_VALUE, this.horizontalFireballKnockback);
        this.localeService.setDouble(ServerLocaleImpl.EXPLOSIVE_FIREBALL_VERTICAL_KB_VALUE, this.verticalFireballKnockback);
        this.localeService.setDouble(ServerLocaleImpl.EXPLOSIVE_FIREBALL_EXPLOSION_RANGE_VALUE, this.fireballExplosionRange);
        this.localeService.setDouble(ServerLocaleImpl.EXPLOSIVE_FIREBALL_THROW_SPEED_VALUE, this.fireballThrowSpeed);

        this.localeService.setInt(ServerLocaleImpl.EXPLOSIVE_TNT_FUSE_TICKS_VALUE, this.tntFuseTicks);
        this.localeService.setDouble(ServerLocaleImpl.EXPLOSIVE_TNT_EXPLOSION_RANGE_VALUE, this.tntExplosionRange);
    }

    private void registerListener() {
        this.plugin.getServer().getPluginManager().registerEvents(new ExplosiveListener(), this.plugin);
    }
}
