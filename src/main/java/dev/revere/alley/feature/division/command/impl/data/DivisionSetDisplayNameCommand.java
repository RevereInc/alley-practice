package dev.revere.alley.feature.division.command.impl.data;

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
 * @since 28/01/2025
 */
public class DivisionSetDisplayNameCommand extends BaseCommand {
    @CommandData(name = "division.setdisplayname", isAdminOnly = true, usage = "division setdisplayname <name> <displayName>")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            player.sendMessage(CC.translate("&6Usage: &e/division setdisplayname &6<name> <displayName>"));
            return;
        }

        DivisionService divisionService = this.plugin.getService(DivisionService.class);
        Division division = divisionService.getDivision(args[0]);
        if (division == null) {
            player.sendMessage(CC.translate("&cA division with that name does not exist."));
            return;
        }

        String displayName = args[1];
        division.setDisplayName(displayName);
        divisionService.saveDivision(division);
        player.sendMessage(CC.translate("&aSuccessfully set the display name of the division &6" + division.getName() + " &ato &6" + displayName + "&a."));
    }
}