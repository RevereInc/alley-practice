package dev.revere.alley.feature.arena.command.impl.manage;

import dev.revere.alley.common.text.CC;
import dev.revere.alley.common.text.EnumFormatter;
import dev.revere.alley.core.locale.LocaleService;
import dev.revere.alley.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import dev.revere.alley.core.locale.internal.impl.SettingsLocaleImpl;
import dev.revere.alley.feature.arena.Arena;
import dev.revere.alley.feature.arena.ArenaService;
import dev.revere.alley.feature.arena.ArenaType;
import dev.revere.alley.feature.arena.internal.types.FreeForAllArena;
import dev.revere.alley.feature.arena.internal.types.SharedArena;
import dev.revere.alley.feature.arena.internal.types.StandAloneArena;
import dev.revere.alley.feature.arena.selection.ArenaSelection;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author Remi
 * @project Alley
 * @date 5/20/2024
 */
public class ArenaCreateCommand extends BaseCommand {
    @CommandData(
            name = "arena.create",
            isAdminOnly = true,
            usage = "arena create <arenaName> <type>",
            description = "Creates a new arena of the specified type"
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            command.sendUsage();
            return;
        }

        String arenaName = args[0];
        ArenaType arenaType = Arrays.stream(ArenaType.values())
                .filter(type -> type.name().equalsIgnoreCase(args[1]))
                .findFirst()
                .orElse(null);

        if (arenaType == null) {
            player.sendMessage(EnumFormatter.outputAvailableValues(ArenaType.class));
            return;
        }

        if (this.plugin.getService(ArenaService.class).getArenaByName(arenaName) != null) {
            player.sendMessage(this.getMessage(GlobalMessagesLocaleImpl.ARENA_ALREADY_EXISTS));
            return;
        }

        ArenaSelection arenaSelection = ArenaSelection.createSelection(player);
        if (!arenaSelection.hasSelection()) {
            player.sendMessage(this.getMessage(GlobalMessagesLocaleImpl.ARENA_NO_SELECTION));
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
        player.sendMessage(this.getMessage(GlobalMessagesLocaleImpl.ARENA_CREATED)
                .replace("{arena-name}", arenaName)
                .replace("{arena-type}", arenaType.name())
        );
    }

    /**
     * Get the default display name for the specified arena type.
     *
     * @param arenaType The type of the arena.
     * @return The default display name.
     */
    private String getDefaultDisplayName(ArenaType arenaType) {
        LocaleService localeService = this.plugin.getService(LocaleService.class);

        switch (arenaType) {
            case SHARED:
                return localeService.getMessage(SettingsLocaleImpl.CONFIG_ARENA_DEFAULT_DISPLAY_NAME_SHARED);
            case STANDALONE:
                return localeService.getMessage(SettingsLocaleImpl.CONFIG_ARENA_DEFAULT_DISPLAY_NAME_STANDALONE);
            case FFA:
                return localeService.getMessage(SettingsLocaleImpl.CONFIG_ARENA_DEFAULT_DISPLAY_NAME_FFA);
        }

        return null;
    }
}
