package dev.revere.alley.adapter.core;

import dev.revere.alley.Alley;
import dev.revere.alley.plugin.AlleyContext;
import dev.revere.alley.plugin.annotation.Service;
import dev.revere.alley.adapter.core.enums.CoreType;
import dev.revere.alley.adapter.core.impl.AquaCoreImpl;
import dev.revere.alley.adapter.core.impl.DefaultCoreImpl;
import dev.revere.alley.adapter.core.impl.HeliumCoreImpl;
import dev.revere.alley.adapter.core.impl.PhoenixCoreImpl;
import lombok.Getter;
import me.activated.core.plugin.AquaCoreAPI;
import services.plasma.helium.api.HeliumAPI;
import xyz.refinedev.phoenix.SharedAPI;

/**
 * @author Emmy
 * @project Alley
 * @since 26/04/2025
 */
@Getter
@Service(provides = CoreAdapter.class, priority = 60)
public class CoreAdapterImpl implements CoreAdapter {

    private final Alley plugin;
    private Core core;

    /**
     * Constructor for DI. Receives the main plugin instance.
     */
    public CoreAdapterImpl(Alley plugin) {
        this.plugin = plugin;
    }

    @Override
    public void initialize(AlleyContext context) {
        this.core = this.determineCore();
    }

    @Override
    public Core getCore() {
        if (this.core == null) {
            throw new IllegalStateException("CoreAdapter has not been initialized yet.");
        }
        return this.core;
    }

    /**
     * Determines the server implementation to use based on the enabled plugins.
     *
     * @return The selected server implementation.
     */
    private Core determineCore() {
        Core selectedCore = new DefaultCoreImpl(this.plugin);

        for (CoreType coreType : CoreType.values()) {
            if (this.plugin.getServer().getPluginManager().isPluginEnabled(coreType.getPluginName())) {
                switch (coreType) {
                    case PHOENIX:
                        selectedCore = new PhoenixCoreImpl(SharedAPI.getInstance());
                        break;
                    case AQUA:
                        selectedCore = new AquaCoreImpl(AquaCoreAPI.INSTANCE, this.plugin);
                        break;
                    case HELIUM:
                        selectedCore = new HeliumCoreImpl(HeliumAPI.INSTANCE);
                        break;
                }
                break;
            }
        }
        return selectedCore;
    }
}