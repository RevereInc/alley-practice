package dev.revere.alley.feature.division.command.impl.data;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.api.command.annotation.CompleterData;
import dev.revere.alley.feature.division.Division;
import dev.revere.alley.feature.division.DivisionService;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @since 28/01/2025
 */
public class DivisionSetWinsCommand extends BaseCommand {

    @CompleterData(name = "division.setwins")
    public List<String> DivisionSetWinsCompleter(CommandArgs command) {
        List<String> completion = new ArrayList<>();
        if (command.getArgs().length == 1 && command.getPlayer().hasPermission(this.getAdminPermission())) {
            this.plugin.getService(DivisionService.class).getDivisions().forEach(division -> completion.add(division.getName()));
        } else if (command.getArgs().length == 2 && command.getPlayer().hasPermission(this.getAdminPermission())) {
            Division division = this.plugin.getService(DivisionService.class).getDivision(command.getArgs()[0]);
            if (division != null) {
                division.getTiers().forEach(tier -> completion.add(tier.getName()));
            }
        }

        return completion;
    }

    @CommandData(name = "division.setwins", isAdminOnly = true, usage = "division setwins <name> <tier> <wins>")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 3) {
            player.sendMessage(CC.translate("&6Usage: &e/division setwins &6<name> <tier> <wins>"));
            return;
        }

        DivisionService divisionService = this.plugin.getService(DivisionService.class);
        Division division = divisionService.getDivision(args[0]);
        if (division == null) {
            player.sendMessage(CC.translate("&cA division with that name does not exist."));
            return;
        }

        String tier = args[1];
        if (division.getTier(tier) == null) {
            player.sendMessage(CC.translate("&cThe " + division.getDisplayName() + " division does not have a tier named " + tier + "."));
            return;
        }

        int wins;
        try {
            wins = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            player.sendMessage(CC.translate("&cInvalid number."));
            return;
        }

        if (wins < 0) {
            player.sendMessage(CC.translate("&cThe number of wins can't be 0."));
            return;
        }

        division.getTier(tier).setRequiredWins(wins);
        divisionService.saveDivision(division);
        player.sendMessage(CC.translate("&aSuccessfully set the required wins for the " + division.getDisplayName() + " division's " + tier + " tier to " + wins + "."));
    }
}