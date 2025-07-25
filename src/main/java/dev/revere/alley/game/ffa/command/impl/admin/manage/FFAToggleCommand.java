package dev.revere.alley.game.ffa.command.impl.admin.manage;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
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
public class FFAToggleCommand extends BaseCommand {
    @CommandData(name = "ffa.toggle", isAdminOnly = true)
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&6Usage: &e/ffa toggle &6<kitName>"));
            return;
        }

        KitService kitService = this.plugin.getService(KitService.class);
        Kit kit = kitService.getKit(args[0]);
        if (kit == null) {
            player.sendMessage(CC.translate("&cA kit with that name does not exist!"));
            return;
        }

        FFAService ffaService = this.plugin.getService(FFAService.class);
        if (ffaService.isNotEligibleForFFA(kit)) {
            player.sendMessage(CC.translate("&cThis kit is not eligible for FFA due to the kit setting it has enabled!"));
            return;
        }

        kit.setFfaEnabled(!kit.isFfaEnabled());
        boolean ffaEnabled = kit.isFfaEnabled();

        kitService.saveKit(kit);
        ffaService.reloadFFAKits();
        player.sendMessage(CC.translate("&aFFA mode has been " + (ffaEnabled ? "&aenabled" : "&cdisabled") + " for kit &6" + kit.getName() + "&a!"));
        player.sendMessage(CC.translate("&7Additionally, all FFA matches have been reloaded."));
    }
}