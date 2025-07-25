package dev.revere.alley.game.ffa.command.impl.admin.manage;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.base.arena.Arena;
import dev.revere.alley.base.arena.ArenaService;
import dev.revere.alley.base.arena.enums.ArenaType;
import dev.revere.alley.base.kit.KitService;
import dev.revere.alley.base.kit.Kit;
import dev.revere.alley.game.ffa.FFAService;
import dev.revere.alley.util.chat.CC;
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
            player.sendMessage(CC.translate("&cA kit with that name does not exist!"));
            return;
        }

        if (kit.isFfaEnabled()) {
            player.sendMessage(CC.translate("&cThis kit is already set up for FFA!"));
            return;
        }

        Arena arena = this.plugin.getService(ArenaService.class).getArenaByName(args[1]);
        if (arena == null) {
            player.sendMessage(CC.translate("&cAn arena with that name does not exist!"));
            return;
        }

        if (arena.getType() != ArenaType.FFA) {
            player.sendMessage(CC.translate("&cYou can only set up FFA matches in FFA arenas!"));
            return;
        }

        int maxPlayers;
        try {
            maxPlayers = Integer.parseInt(args[2]);
        } catch (NumberFormatException exception) {
            player.sendMessage(CC.translate("&cThe max players must be a number."));
            return;
        }

        int menuSlot;
        try {
            menuSlot = Integer.parseInt(args[3]);
        } catch (NumberFormatException exception) {
            player.sendMessage(CC.translate("&cThe menu slot must be a number."));
            return;
        }

        FFAService ffaService = this.plugin.getService(FFAService.class);
        if (ffaService.isNotEligibleForFFA(kit)) {
            player.sendMessage(CC.translate("&cThis kit is not eligible for FFA due to the kit setting it has enabled!"));
            return;
        }

        kit.setFfaEnabled(true);
        kit.setFfaSlot(menuSlot);
        kit.setFfaArenaName(arena.getName());
        kit.setMaxFfaPlayers(maxPlayers);
        ffaService.createFFAMatch(arena, kit, maxPlayers);
        kitService.saveKit(kit);

        //this.plugin.getService(IProfileService.class).loadProfiles();
        player.sendMessage(CC.translate("&aFFA match has been created with the kit &6" + kit.getName() + "&a!"));
    }
}
