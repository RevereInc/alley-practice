package dev.revere.alley.feature.cosmetic.impl.killmessage;

import dev.revere.alley.feature.cosmetic.impl.killmessage.impl.*;
import dev.revere.alley.feature.cosmetic.BaseCosmeticRepository;

/**
 * @author Remi
 * @project alley-practice
 * @date 27/06/2025
 */
public class KillMessageRepository extends BaseCosmeticRepository<KillMessagePack> {
    public KillMessageRepository() {
        this.registerCosmetic(NoneKillMessages.class);
        this.registerCosmetic(SaltyKillMessages.class);
        this.registerCosmetic(YeetKillMessages.class);
        this.registerCosmetic(NerdKillMessages.class);
        this.registerCosmetic(SpigotCommunityKillMessages.class);
    }
}