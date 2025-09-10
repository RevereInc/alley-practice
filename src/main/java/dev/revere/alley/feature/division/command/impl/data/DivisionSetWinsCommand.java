package dev.revere.alley.feature.division.command.impl.data;

import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.locale.internal.types.DivisionLocaleImpl;
import dev.revere.alley.core.locale.internal.types.ErrorLocaleImpl;
import dev.revere.alley.feature.division.Division;
import dev.revere.alley.feature.division.DivisionService;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import dev.revere.alley.library.command.annotation.CompleterData;
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

    @CommandData(
            name = "division.setwins",
            isAdminOnly = true,
            usage = "division setwins <name> <tier> <wins>",
            description = "Set the required wins for a division tier."
    )
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
            player.sendMessage(this.getMessage(DivisionLocaleImpl.NOT_FOUND).replace("{division-name}", args[0]));
            return;
        }

        String tier = args[1];
        if (division.getTier(tier) == null) {
            player.sendMessage(this.getMessage(DivisionLocaleImpl.TIER_NOT_FOUND).replace("{division-name}", division.getDisplayName()).replace("{tier-name}", tier));
            return;
        }

        int wins;
        try {
            wins = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            player.sendMessage(this.getMessage(ErrorLocaleImpl.INVALID_NUMBER).replace("{input}", args[2]));
            return;
        }

        if (wins < 0) {
            //TODO: Locale
            player.sendMessage(CC.translate("&cThe number of wins can't be 0."));
            return;
        }

        division.getTier(tier).setRequiredWins(wins);
        divisionService.saveDivision(division);
        player.sendMessage(this.getMessage(DivisionLocaleImpl.WINS_SET)
                .replace("{division-name}", division.getName())
                .replace("{tier-name}", tier)
                .replace("{required-wins}", String.valueOf(wins))
        );
    }
}