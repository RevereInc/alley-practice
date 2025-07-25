package dev.revere.alley.adapter.knockback;

import dev.revere.alley.Alley;
import dev.revere.alley.plugin.AlleyContext;
import dev.revere.alley.plugin.annotation.Service;
import dev.revere.alley.adapter.knockback.enums.KnockbackType;
import dev.revere.alley.adapter.knockback.impl.DefaultKnockbackImpl;
import dev.revere.alley.adapter.knockback.impl.ZoneKnockbackImpl;
import lombok.Getter;

/**
 * @author Emmy
 * @project Alley
 * @since 26/04/2025
 */
@Getter
@Service(provides = KnockbackAdapter.class, priority = 50)
public class KnockbackAdapterImpl implements KnockbackAdapter {
    private final Alley plugin;
    private Knockback knockbackImplementation;

    /**
     * Constructor for DI. Receives the main plugin instance.
     */
    public KnockbackAdapterImpl(Alley plugin) {
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