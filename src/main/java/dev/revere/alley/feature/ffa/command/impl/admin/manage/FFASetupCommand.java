package dev.revere.alley.feature.ffa.command.impl.admin.manage;

import dev.revere.alley.core.config.internal.locale.impl.ArenaLocale;
import dev.revere.alley.core.config.internal.locale.impl.ErrorLocale;
import dev.revere.alley.core.config.internal.locale.impl.FFALocale;
import dev.revere.alley.core.config.internal.locale.impl.KitLocale;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import dev.revere.alley.feature.arena.Arena;
import dev.revere.alley.feature.arena.ArenaService;
import dev.revere.alley.feature.arena.ArenaType;
import dev.revere.alley.feature.kit.KitService;
import dev.revere.alley.feature.kit.Kit;
import dev.revere.alley.feature.ffa.FFAService;
import dev.revere.alley.common.text.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @since 11/04/2025
 */
public class FFASetupCommand extends BaseCommand {
    @CommandData(name = "ffa.setup", isAdminOnly = true)
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 4) {
            player.sendMessage(CC.translate("&6Usage: &e/ffa setup &6<kitName> <arenaName> <maxPlayers> <menu-slot>"));
            return;
        }

        KitService kitService = this.plugin.getService(KitService.class);
        Kit kit = kitService.getKit(args[0]);
        if (kit == null) {
            player.sendMessage(KitLocale.KIT_NOT_FOUND.getMessage());
            return;
        }

        if (kit.isFfaEnabled()) {
            player.sendMessage(FFALocale.ALREADY_EXISTS.getMessage().replace("{ffa-name}", kit.getName()));
            return;
        }

        Arena arena = this.plugin.getService(ArenaService.class).getArenaByName(args[1]);
        if (arena == null) {
            player.sendMessage(ArenaLocale.NOT_FOUND.getMessage().replace("{arena-name}", args[1]));
            return;
        }

        if (arena.getType() != ArenaType.FFA) {
            //TODO: Locale
            player.sendMessage(CC.translate("&cYou can only set up FFA matches in FFA arenas!"));
            return;
        }

        int maxPlayers;
        try {
            maxPlayers = Integer.parseInt(args[2]);
        } catch (NumberFormatException exception) {
            player.sendMessage(ErrorLocale.INVALID_NUMBER.getMessage().replace("{input}", args[2]));
            return;
        }

        int menuSlot;
        try {
            menuSlot = Integer.parseInt(args[3]);
        } catch (NumberFormatException exception) {
            player.sendMessage(ErrorLocale.INVALID_NUMBER.getMessage().replace("{input}", args[2]));
            return;
        }

        FFAService ffaService = this.plugin.getService(FFAService.class);
        if (ffaService.isNotEligibleForFFA(kit)) {
            player.sendMessage(FFALocale.KIT_NOT_ELIGIBLE.getMessage());
            return;
        }

        kit.setFfaEnabled(true);
        kit.setFfaSlot(menuSlot);
        kit.setFfaArenaName(arena.getName());
        kit.setMaxFfaPlayers(maxPlayers);
        ffaService.createFFAMatch(arena, kit, maxPlayers);
        kitService.saveKit(kit);

        player.sendMessage(FFALocale.MATCH_CREATED.getMessage()
                .replace("kit-name", kit.getName())
                .replace("arena-name", arena.getName())
                .replace("max-players", String.valueOf(maxPlayers))
                .replace("menu-slot", String.valueOf(menuSlot))
        );
    }
}