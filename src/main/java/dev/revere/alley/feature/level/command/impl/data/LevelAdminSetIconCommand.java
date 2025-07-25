package dev.revere.alley.feature.level.command.impl.data;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.feature.level.LevelService;
import dev.revere.alley.feature.level.data.LevelData;
import dev.revere.alley.util.chat.CC;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @since 26/05/2025
 */
public class LevelAdminSetIconCommand extends BaseCommand {
    @CommandData(name = "leveladmin.seticon", isAdminOnly = true, description = "Set the icon for a level")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&6Usage: &e/leveladmin seticon &6<levelName>"));
            return;
        }

        String levelName = args[0];
        LevelService levelService = this.plugin.getService(LevelService.class);
        LevelData level = levelService.getLevel(levelName);
        if (level == null) {
            player.sendMessage(CC.translate("&cNo level found with that name!"));
            return;
        }

        if (player.getItemInHand() == null || player.getItemInHand().getType() == Material.AIR) {
            player.sendMessage(CC.translate("&cYou must hold an item in your hand to set it as the level icon!"));
            return;
        }

        Material iconMaterial = player.getItemInHand().getType();
        int durability = player.getItemInHand().getDurability();
        level.setMaterial(iconMaterial);
        level.setDurability(durability);
        levelService.saveLevel(level);
        player.sendMessage(CC.translate("&aSuccessfully set the icon for level &6" + levelName + " &ato " + iconMaterial.name() + "&a!"));
    }
}