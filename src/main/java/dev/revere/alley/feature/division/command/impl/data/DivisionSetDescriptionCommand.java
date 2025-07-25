package dev.revere.alley.feature.division.command.impl.data;

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
 * @since 28/01/2025
 */
public class DivisionSetDescriptionCommand extends BaseCommand {
    @CommandData(name = "division.setdescription", isAdminOnly = true, usage = "division setdescription <name> <description>")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            player.sendMessage(CC.translate("&6Usage: &e/division setdescription &6<name> <description>"));
            return;
        }

        DivisionService divisionService = this.plugin.getService(DivisionService.class);
        Division division = divisionService.getDivision(args[0]);
        if (division == null) {
            player.sendMessage(CC.translate("&cA division with that name does not exist."));
            return;
        }

        String description = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        division.setDescription(description);
        divisionService.saveDivision(division);
        player.sendMessage(CC.translate("&aSuccessfully set the description of the division named &6" + division.getDisplayName() + "&a to &6" + description + "&a."));
    }
}