package dev.revere.alley.feature.cosmetic.command.impl.player;

import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import dev.revere.alley.feature.cosmetic.menu.CosmeticsMenu;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 6/1/2024
 */
public class CosmeticsCommand extends BaseCommand {
    @CommandData(name = "cosmetics")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        new CosmeticsMenu().openMenu(player);
    }
}