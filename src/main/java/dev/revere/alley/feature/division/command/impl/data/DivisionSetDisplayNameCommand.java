package dev.revere.alley.feature.division.command.impl.data;

import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.locale.internal.impl.command.DivisionLocaleImpl;
import dev.revere.alley.feature.division.Division;
import dev.revere.alley.feature.division.DivisionService;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @since 28/01/2025
 */
public class DivisionSetDisplayNameCommand extends BaseCommand {
    @CommandData(
            name = "division.setdisplayname",
            isAdminOnly = true,
            usage = "division setdisplayname <name> <displayName>",
            description = "Set the display name of a division."
    )
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
            player.sendMessage(this.getMessage(DivisionLocaleImpl.NOT_FOUND).replace("{division-name}", args[0]));
            return;
        }

        String displayName = args[1];
        division.setDisplayName(displayName);
        divisionService.saveDivision(division);
        player.sendMessage(this.getMessage(DivisionLocaleImpl.DISPLAY_NAME_SET)
                .replace("{division-name}", division.getName())
                .replace("{display-name}", displayName)
        );
    }
}