package dev.revere.alley.feature.ffa.command.impl.admin.manage;

import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.locale.internal.impl.ErrorLocaleImpl;
import dev.revere.alley.core.locale.internal.impl.command.ArenaLocaleImpl;
import dev.revere.alley.core.locale.internal.impl.command.FFALocaleImpl;
import dev.revere.alley.core.locale.internal.impl.command.KitLocaleImpl;
import dev.revere.alley.feature.arena.Arena;
import dev.revere.alley.feature.arena.ArenaService;
import dev.revere.alley.feature.arena.ArenaType;
import dev.revere.alley.feature.ffa.FFAService;
import dev.revere.alley.feature.kit.Kit;
import dev.revere.alley.feature.kit.KitService;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @since 11/04/2025
 */
public class FFASetupCommand extends BaseCommand {
    @CommandData(
            name = "ffa.setup",
            isAdminOnly = true,
            usage = "ffa setup <kitName> <arenaName> <maxPlayers> <menu-slot>",
            description = "Setup a new FFA match."
    )
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
            player.sendMessage(this.getMessage(KitLocaleImpl.NOT_FOUND));
            return;
        }

        if (kit.isFfaEnabled()) {
            player.sendMessage(this.getMessage(FFALocaleImpl.ALREADY_EXISTS).replace("{ffa-name}", kit.getName()));
            return;
        }

        Arena arena = this.plugin.getService(ArenaService.class).getArenaByName(args[1]);
        if (arena == null) {
            player.sendMessage(this.getMessage(ArenaLocaleImpl.NOT_FOUND).replace("{arena-name}", args[1]));
            return;
        }

        if (arena.getType() != ArenaType.FFA) {
            player.sendMessage(this.getMessage(FFALocaleImpl.CAN_ONLY_SETUP_IN_FFA_ARENA));
            return;
        }

        int maxPlayers;
        try {
            maxPlayers = Integer.parseInt(args[2]);
        } catch (NumberFormatException exception) {
            player.sendMessage(this.getMessage(ErrorLocaleImpl.INVALID_NUMBER).replace("{input}", args[2]));
            return;
        }

        int menuSlot;
        try {
            menuSlot = Integer.parseInt(args[3]);
        } catch (NumberFormatException exception) {
            player.sendMessage(this.getMessage(ErrorLocaleImpl.INVALID_NUMBER).replace("{input}", args[2]));
            return;
        }

        FFAService ffaService = this.plugin.getService(FFAService.class);
        if (ffaService.isNotEligibleForFFA(kit)) {
            player.sendMessage(this.getMessage(FFALocaleImpl.KIT_NOT_ELIGIBLE));
            return;
        }

        kit.setFfaEnabled(true);
        kit.setFfaSlot(menuSlot);
        kit.setFfaArenaName(arena.getName());
        kit.setMaxFfaPlayers(maxPlayers);
        ffaService.createFFAMatch(arena, kit, maxPlayers);
        kitService.saveKit(kit);

        player.sendMessage(this.getMessage(FFALocaleImpl.MATCH_CREATED)
                .replace("kit-name", kit.getName())
                .replace("arena-name", arena.getName())
                .replace("max-players", String.valueOf(maxPlayers))
                .replace("menu-slot", String.valueOf(menuSlot))
        );
    }
}