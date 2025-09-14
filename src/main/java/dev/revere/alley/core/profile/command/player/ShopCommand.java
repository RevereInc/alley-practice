package dev.revere.alley.core.profile.command.player;

import dev.revere.alley.core.profile.menu.shop.ShopMenu;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;

/**
 * @author Remi
 * @project Alley
 * @date 6/2/2024
 */
public class ShopCommand extends BaseCommand {
    @CommandData(
            name = "shop",
            usage = "shop",
            description = "Open the server shop"
    )
    @Override
    public void onCommand(CommandArgs command) {
        new ShopMenu().openMenu(command.getPlayer());
    }
}