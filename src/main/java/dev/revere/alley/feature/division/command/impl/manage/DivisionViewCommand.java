package dev.revere.alley.feature.division.command.impl.manage;

import dev.revere.alley.Alley;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.feature.division.Division;
import dev.revere.alley.feature.division.DivisionService;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * @author Emmy
 * @project Alley
 * @since 26/01/2025
 */
public class DivisionViewCommand extends BaseCommand {
    @CommandData(name = "division.view", permission = "alley.admin", usage = "division view <name>")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();
        
        if (args.length < 1) {
            player.sendMessage(CC.translate("&6Usage: &e/division view &b<name>"));
            return;
        }
        
        DivisionService divisionService = Alley.getInstance().getDivisionService();
        Division division = divisionService.getDivision(args[0]);
        if (division == null) {
            player.sendMessage(CC.translate("&cA division with that name does not exist."));
            return;
        }

        Arrays.asList(
                "",
                "&b&lDivision &f(" + division.getDisplayName() + ")",
                " &f● &bName: &f" + division.getDisplayName(),
                " &f● &bTiers: &f" + division.getTiers().size(),
                " &f● &bDescription: &f" + division.getDescription(),
                " &f● &bRequired Wins: &f" + division.getTiers().get(0).getRequiredWins(),
                ""
        ).forEach(line -> player.sendMessage(CC.translate(line)));
    }
}