package dev.revere.alley.feature.division.command.impl.data;

import dev.revere.alley.core.config.internal.locale.impl.DivisionLocale;
import dev.revere.alley.core.config.internal.locale.impl.ErrorLocale;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import dev.revere.alley.feature.division.Division;
import dev.revere.alley.feature.division.DivisionService;
import dev.revere.alley.common.text.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @since 28/01/2025
 */
public class DivisionSetIconCommand extends BaseCommand {
    @CommandData(name = "division.seticon", isAdminOnly = true, usage = "division seticon <name>")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&6Usage: &e/division seticon &6<name>"));
            return;
        }

        DivisionService divisionService = this.plugin.getService(DivisionService.class);
        Division division = divisionService.getDivision(args[0]);
        if (division == null) {
            player.sendMessage(DivisionLocale.NOT_FOUND.getMessage().replace("{division-name}", args[0]));
            return;
        }

        if (player.getItemInHand() == null) {
            player.sendMessage(ErrorLocale.MUST_HOLD_ITEM.getMessage());
            return;
        }

        division.setIcon(player.getItemInHand().getType());
        division.setDurability(player.getItemInHand().getDurability());
        divisionService.saveDivision(division);
        player.sendMessage(DivisionLocale.ICON_SET.getMessage()
                .replace("{division-name}", division.getDisplayName())
                .replace("{item-type}", player.getItemInHand().getType().name())
                .replace("{item-durability}", String.valueOf(player.getItemInHand().getDurability()))
        );
    }
}