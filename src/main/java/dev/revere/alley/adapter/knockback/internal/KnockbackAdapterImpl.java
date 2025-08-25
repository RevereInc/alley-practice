package dev.revere.alley.adapter.knockback.internal;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.adapter.knockback.Knockback;
import dev.revere.alley.adapter.knockback.KnockbackAdapter;
import dev.revere.alley.adapter.knockback.KnockbackType;
import dev.revere.alley.bootstrap.AlleyContext;
import dev.revere.alley.bootstrap.annotation.Service;
import lombok.Getter;

/**
 * @author Emmy
 * @project Alley
 * @since 26/04/2025
 */
@Getter
@Service(provides = KnockbackAdapter.class, priority = 50)
public class KnockbackAdapterImpl implements KnockbackAdapter {
    private final AlleyPlugin plugin;
    private Knockback knockbackImplementation;

    /**
     * DI Constructor for the KnockbackAdapterImpl class.
     *
     * @param plugin The Alley plugin instance.
     */
    public KnockbackAdapterImpl(AlleyPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void initialize(AlleyContext context) {
        this.knockbackImplementation = this.determineKnockback();
    }

    @Override
    public Knockback getKnockbackImplementation() {
        if (this.knockbackImplementation == null) {
            throw new IllegalStateException("KnockbackAdapter has not been initialized yet.");
        }
        return this.knockbackImplementation;
    }

    /**
     * Determines the knockback implementation to use based on the server's name.
     *
     * @return The selected knockback implementation.
     */
    private Knockback determineKnockback() {
        Knockback selectedImplementation = new DefaultKnockbackImpl();

        for (KnockbackType kbType : KnockbackType.values()) {
            if (this.plugin.getServer().getName().equalsIgnoreCase(kbType.getSpigotName())) {
                switch (kbType) {
                    case ZONE:
                        selectedImplementation = new ZoneKnockbackImpl();
                        break;
                }
                break;
            }
        }

        return selectedImplementation;
    }
}