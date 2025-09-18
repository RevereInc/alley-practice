package dev.revere.alley.feature.division.command.impl.manage;

import dev.revere.alley.common.text.CC;
import dev.revere.alley.feature.division.Division;
import dev.revere.alley.feature.division.DivisionService;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import dev.revere.alley.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @since 26/01/2025
 */
public class DivisionDeleteCommand extends BaseCommand {
    @CommandData(
            name = "division.delete",
            isAdminOnly = true,
            usage = "division delete <name>",
            description = "Delete a division."
    )
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
            player.sendMessage(this.getMessage(GlobalMessagesLocaleImpl.DIVISION_NOT_FOUND));
            return;
        }

        divisionService.deleteDivision(division.getName());
        player.sendMessage(this.getMessage(GlobalMessagesLocaleImpl.DIVISION_DELETED).replace("{division-name}", division.getName()));
    }
}