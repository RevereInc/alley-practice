package dev.revere.alley.feature.main.command.impl;

import dev.revere.alley.feature.main.menu.AlleyMainPanelMenu;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;

/**
 * @author Emmy
 * @project alley-practice
 * @since 28/09/2025
 */
public class AlleyPanelCommand extends BaseCommand {
    @CommandData(
            name = "alley.panel",
            aliases = {"alley.admin", "alleymenu", "alley.help", "alleyadmin"},
            isAdminOnly = true,
            usage = "alley panel",
            description = "Open the Alley admin panel."
    )
    @Override
    public void onCommand(CommandArgs command) {
        new AlleyMainPanelMenu().openMenu(command.getPlayer());
    }
}