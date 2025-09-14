package dev.revere.alley.feature.division.command.impl.data;

import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.locale.internal.impl.command.DivisionLocaleImpl;
import dev.revere.alley.feature.division.Division;
import dev.revere.alley.feature.division.DivisionService;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * @author Emmy
 * @project Alley
 * @since 28/01/2025
 */
public class DivisionSetDescriptionCommand extends BaseCommand {
    @CommandData(
            name = "division.setdescription",
            isAdminOnly = true,
            usage = "division setdescription <name> <description>",
            description = "Set the description of a division."
    )
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
            player.sendMessage(this.getMessage(DivisionLocaleImpl.NOT_FOUND).replace("{division-name}", args[0]));
            return;
        }

        String description = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        division.setDescription(description);
        divisionService.saveDivision(division);
        player.sendMessage(this.getMessage(DivisionLocaleImpl.DESCRIPTION_SET)
                .replace("{division-name}", division.getDisplayName())
                .replace("{description}", description)
        );
    }
}