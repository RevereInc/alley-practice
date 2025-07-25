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
public class DivisionDeleteCommand extends BaseCommand {
    @CommandData(name = "division.delete", isAdminOnly = true, usage = "division delete <name>")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&6Usage: &e/division delete &6<name>"));
            return;
        }

        String name = args[0];
        DivisionService divisionService = this.plugin.getService(DivisionService.class);
        Division division = divisionService.getDivision(name);
        if (division == null) {
            player.sendMessage(CC.translate("&cA division with that name does not exist."));
            return;
        }

        divisionService.deleteDivision(division.getName());
        player.sendMessage(CC.translate("&aSuccessfully deleted the division named &6" + name + "&a."));
    }
}
