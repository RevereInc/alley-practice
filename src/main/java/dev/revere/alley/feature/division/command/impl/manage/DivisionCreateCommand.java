package dev.revere.alley.feature.division.command.impl.manage;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.feature.division.Division;
import dev.revere.alley.feature.division.DivisionService;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @since 26/01/2025
 */
public class DivisionCreateCommand extends BaseCommand {
    @CommandData(name = "division.create", isAdminOnly = true, usage = "division.create <name> <requiredWins>")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            player.sendMessage(CC.translate("&6Usage: &e/division create &6<name> <requiredWins>"));
            return;
        }

        String name = args[0];
        int requiredWins;
        try {
            requiredWins = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            player.sendMessage(CC.translate("&cInvalid number."));
            return;
        }

        DivisionService divisionService = this.plugin.getService(DivisionService.class);
        Division division = divisionService.getDivision(name);
        if (division != null) {
            player.sendMessage(CC.translate("&cA division with that name already exists."));
            return;
        }

        divisionService.createDivision(name, requiredWins);
        player.sendMessage(CC.translate("&aSuccessfully created a new division named &6" + name + "&a with &6" + requiredWins + " &awins."));
    }
}
