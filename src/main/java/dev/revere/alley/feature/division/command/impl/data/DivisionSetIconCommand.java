package dev.revere.alley.feature.division.command.impl.data;

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
 * @since 28/01/2025
 */
public class DivisionSetIconCommand extends BaseCommand {
    @CommandData(
            name = "division.seticon",
            isAdminOnly = true,
            usage = "division seticon <name>",
            description = "Set the icon of a division to the item in your hand."
    )
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
            player.sendMessage(this.getMessage(GlobalMessagesLocaleImpl.DIVISION_NOT_FOUND).replace("{division-name}", args[0]));
            return;
        }

        if (player.getItemInHand() == null) {
            player.sendMessage(this.getMessage(GlobalMessagesLocaleImpl.ERROR_YOU_MUST_HOLD_ITEM));
            return;
        }

        division.setIcon(player.getItemInHand().getType());
        division.setDurability(player.getItemInHand().getDurability());
        divisionService.saveDivision(division);
        player.sendMessage(this.getMessage(GlobalMessagesLocaleImpl.DIVISION_ICON_SET)
                .replace("{division-name}", division.getDisplayName())
                .replace("{item-type}", player.getItemInHand().getType().name())
                .replace("{item-durability}", String.valueOf(player.getItemInHand().getDurability()))
        );
    }
}