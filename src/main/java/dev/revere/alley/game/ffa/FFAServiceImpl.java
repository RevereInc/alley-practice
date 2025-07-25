package dev.revere.alley.game.ffa;

import dev.revere.alley.base.arena.Arena;
import dev.revere.alley.base.arena.ArenaService;
import dev.revere.alley.base.kit.KitService;
import dev.revere.alley.base.kit.Kit;
import dev.revere.alley.base.kit.setting.impl.mechanic.KitSettingBuildImpl;
import dev.revere.alley.base.kit.setting.impl.mode.KitSettingBoxing;
import dev.revere.alley.plugin.AlleyContext;
import dev.revere.alley.plugin.annotation.Service;
import dev.revere.alley.game.ffa.impl.DefaultFFAMatch;
import dev.revere.alley.tool.logger.Logger;
import dev.revere.alley.util.chat.CC;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Emmy
 * @project Alley
 * @since 11/04/2025
 */
@Getter
@Service(provides = FFAService.class, priority = 130)
public class FFAServiceImpl implements FFAService {
    private final KitService kitService;
    private final ArenaService arenaService;

    private final List<FFAMatch> matches = new ArrayList<>();
    private final List<Kit> ffaKits = new ArrayList<>();
    private final int defaultPlayerSize = 20;

    /**
     * Constructor for DI.
     */
    public FFAServiceImpl(KitService kitService, ArenaService arenaService) {
        this.kitService = kitService;
        this.arenaService = arenaService;
    }

    @Override
    public void initialize(AlleyContext context) {
        this.ffaKits.addAll(this.kitService.getKits().stream().filter(Kit::isFfaEnabled).collect(Collectors.toList()));
        this.initializeMatches();
    }

    @Override
    public void shutdown(AlleyContext context) {
        this.matches.forEach(match -> match.getPlayers().forEach(ffaPlayer -> {
            Player player = ffaPlayer.getPlayer();
            if (player != null) {
                match.leave(player);
                player.sendMessage(CC.translate("&cThe FFA arena is closing due to a server shutdown."));
            }
        }));
        this.matches.clear();
        Logger.info("Cleaned up all FFA matches.");
    }

    /**
     * Load all FFA matches
     */
    public void initializeMatches() {
        for (Kit kit : this.ffaKits) {
            Arena arena = this.arenaService.getArenaByName(kit.getFfaArenaName());
            if (arena == null) {
                Logger.error("Kit " + kit.getName() + " has no FFA arena set. Please set the FFA arena in the kit settings.");
                continue;
            }

            if (kit.getMaxFfaPlayers() <= 0) {
                kit.setMaxFfaPlayers(this.defaultPlayerSize);
                Logger.error("FFA match for kit " + kit.getName() + " has a max player size of 0. Setting to default of " + this.defaultPlayerSize + " players.");
            }

            this.createFFAMatch(arena, kit, kit.getMaxFfaPlayers());
        }
    }

    @Override
    public void createFFAMatch(Arena arena, Kit kit, int maxPlayers) {
        DefaultFFAMatch match = new DefaultFFAMatch(kit.getName(), arena, kit, maxPlayers);
        this.matches.add(match);
    }

    @Override
    public Optional<FFAMatch> getMatchByPlayer(Player player) {
        return this.matches.stream().filter(match -> match.getPlayers().contains(match.getGameFFAPlayer(player))).findFirst();
    }

    @Override
    public FFAMatch getFFAMatch(String kitName) {
        return this.matches.stream()
                .filter(match -> match.getKit().getName().equalsIgnoreCase(kitName))
                .findFirst()
                .orElse(null);
    }

    @Override
    public FFAMatch getFFAMatch(Player player) {
        return this.getMatchByPlayer(player).orElse(null);
    }

    @Override
    public void reloadFFAKits() {
        this.shutdown(null);

        this.ffaKits.clear();
        this.ffaKits.addAll(this.kitService.getKits().stream().filter(Kit::isFfaEnabled).collect(Collectors.toList()));
        this.initializeMatches();
    }

    @Override
    public boolean isNotEligibleForFFA(Kit kit) {
        return kit.isSettingEnabled(KitSettingBuildImpl.class) || kit.isSettingEnabled(KitSettingBoxing.class);
    }
}