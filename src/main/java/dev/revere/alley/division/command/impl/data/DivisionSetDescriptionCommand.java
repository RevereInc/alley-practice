package dev.revere.alley.division.command.impl.data;

import dev.revere.alley.Alley;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.Command;
import dev.revere.alley.division.Division;
import dev.revere.alley.division.DivisionRepository;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * @author Emmy
 * @project Alley
 * @since 28/01/2025
 */
public class DivisionSetDescriptionCommand extends BaseCommand {
    @Command(name = "division.setdescription", permission = "alley.admin", usage = "division setdescription <name> <description>")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();
        
        if (args.length < 2) {
            player.sendMessage(CC.translate("&6Usage: &e/division setdescription &b<name> <description>"));
            return;
        }

        DivisionRepository divisionRepository = Alley.getInstance().getDivisionRepository();
        Division division = divisionRepository.getDivision(args[0]);
        if (division == null) {
            player.sendMessage(CC.translate("&cA division with that name does not exist."));
            return;
        }
        
        String description = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        division.setDescription(description);
        divisionRepository.saveDivision(division);
        player.sendMessage(CC.translate("&aSuccessfully set the description of the division named &b" + division.getDisplayName() + "&a to &b" + description + "&a."));
    }
}