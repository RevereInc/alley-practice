package dev.revere.alley.feature.division.command.impl.manage;

import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import dev.revere.alley.feature.division.Division;
import dev.revere.alley.feature.division.DivisionService;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @since 26/01/2025
 */
public class DivisionCreateCommand extends BaseCommand {
    @CommandData(
            name = "division.create",
            isAdminOnly = true,
            usage = "division.create <name> <requiredWins>",
            description = "Create a new division."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            command.sendUsage();
            return;
        }

        String name = args[0];
        int requiredWins;
        try {
            requiredWins = Integer.parseInt(args[1]);
        } catch (NumberFormatException exception) {
            player.sendMessage(this.getMessage(GlobalMessagesLocaleImpl.ERROR_INVALID_NUMBER).replace("{input}", args[1]));
            return;
        }

        DivisionService divisionService = this.plugin.getService(DivisionService.class);
        Division division = divisionService.getDivision(name);
        if (division != null) {
            player.sendMessage(this.getMessage(GlobalMessagesLocaleImpl.DIVISION_ALREADY_EXISTS).replace("{division-name}", name));
            return;
        }

        divisionService.createDivision(name, requiredWins);
        player.sendMessage(this.getMessage(GlobalMessagesLocaleImpl.DIVISION_CREATED)
                .replace("{division-name}", name)
                .replace("{required-wins}", String.valueOf(requiredWins))
        );
    }
}