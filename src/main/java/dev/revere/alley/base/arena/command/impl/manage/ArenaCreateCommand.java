package dev.revere.alley.base.arena.command.impl.manage;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.base.arena.Arena;
import dev.revere.alley.base.arena.ArenaService;
import dev.revere.alley.base.arena.enums.ArenaType;
import dev.revere.alley.base.arena.impl.FreeForAllArena;
import dev.revere.alley.base.arena.impl.SharedArena;
import dev.revere.alley.base.arena.impl.StandAloneArena;
import dev.revere.alley.base.arena.selection.ArenaSelection;
import dev.revere.alley.config.ConfigService;
import dev.revere.alley.config.locale.impl.ArenaLocale;
import dev.revere.alley.util.chat.CC;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author Remi
 * @project Alley
 * @date 5/20/2024
 */
public class ArenaCreateCommand extends BaseCommand {

    @CommandData(name = "arena.create", isAdminOnly = true)
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            player.sendMessage(CC.translate("&6Usage: &e/arena create &6<arenaName> <type>"));
            return;
        }

        String arenaName = args[0];
        ArenaType arenaType = Arrays.stream(ArenaType.values())
                .filter(type -> type.name().equalsIgnoreCase(args[1]))
                .findFirst()
                .orElse(null);

        if (arenaType == null) {
            player.sendMessage(CC.translate("&cInvalid arena type! Valid types: SHARED, STANDALONE, FFA"));
            return;
        }

        if (this.plugin.getService(ArenaService.class).getArenaByName(arenaName) != null) {
            player.sendMessage(ArenaLocale.ALREADY_EXISTS.getMessage());
            return;
        }

        ArenaSelection arenaSelection = ArenaSelection.createSelection(player);
        if (!arenaSelection.hasSelection()) {
            player.sendMessage(CC.translate("&cYou must select the minimum and maximum locations for the arena."));
            return;
        }

        Arena arena;
        switch (arenaType) {
            case SHARED:
                arena = new SharedArena(arenaName, arenaSelection.getMinimum(), arenaSelection.getMaximum());
                break;
            case STANDALONE:
                arena = new StandAloneArena(arenaName, arenaSelection.getMinimum(), arenaSelection.getMaximum(), null, null, 7, 70);
                break;
            case FFA:
                arena = new FreeForAllArena(arenaName, arenaSelection.getMinimum(), arenaSelection.getMaximum());
                break;
            default:
                return;
        }

        arena.setDisplayName(Objects.requireNonNull(this.getDefaultDisplayName(arenaType)).replace("{arena-name}", arenaName));

        arena.createArena();
        player.sendMessage(ArenaLocale.CREATED.getMessage().replace("{arena-name}", arenaName).replace("{arena-type}", arenaType.name()));
    }

    /**
     * Get the default display name for the specified arena type.
     *
     * @param arenaType The type of the arena.
     * @return The default display name.
     */
    private String getDefaultDisplayName(ArenaType arenaType) {
        FileConfiguration config = this.plugin.getService(ConfigService.class).getSettingsConfig();

        switch (arenaType) {
            case SHARED:
                return config.getString("arena.default-display-name.shared");
            case STANDALONE:
                return config.getString("arena.default-display-name.standalone");
            case FFA:
                return config.getString("arena.default-display-name.ffa");
        }

        return null;
    }
}
