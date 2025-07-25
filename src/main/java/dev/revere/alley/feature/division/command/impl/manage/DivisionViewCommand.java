package dev.revere.alley.feature.division.command.impl.manage;

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
    @CommandData(name = "division.view", isAdminOnly = true, usage = "division view <name>")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&6Usage: &e/division view &6<name>"));
            return;
        }

        DivisionService divisionService = this.plugin.getService(DivisionService.class);
        Division division = divisionService.getDivision(args[0]);
        if (division == null) {
            player.sendMessage(CC.translate("&cA division with that name does not exist."));
            return;
        }

        Arrays.asList(
                "",
                "&6&lDivision &f(" + division.getDisplayName() + ")",
                " &f● &6Name: &f" + division.getDisplayName(),
                " &f● &6Tiers: &f" + division.getTiers().size(),
                " &f● &6Description: &f" + division.getDescription(),
                " &f● &6Required Wins: &f" + division.getTiers().get(0).getRequiredWins(),
                ""
        ).forEach(line -> player.sendMessage(CC.translate(line)));
    }
}