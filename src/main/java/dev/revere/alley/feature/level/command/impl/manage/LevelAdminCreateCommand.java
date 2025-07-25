package dev.revere.alley.feature.level.command.impl.manage;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.feature.level.LevelService;
import dev.revere.alley.feature.level.data.LevelData;
import dev.revere.alley.util.chat.CC;
import org.bukkit.command.CommandSender;

/**
 * @author Emmy
 * @project Alley
 * @since 26/05/2025
 */
public class LevelAdminCreateCommand extends BaseCommand {
    @CommandData(name = "leveladmin.create", isAdminOnly = true, description = "Create a new level", inGameOnly = false)
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (args.length < 3) {
            sender.sendMessage(CC.translate("&6Usage: &e/leveladmin create &6<levelName> <minElo> <maxElo>"));
            return;
        }

        String levelName = args[0];
        LevelService levelService = this.plugin.getService(LevelService.class);
        LevelData level = levelService.getLevel(levelName);
        if (level != null) {
            sender.sendMessage(CC.translate("&cA level with that name already exists!"));
            return;
        }

        int minElo;
        try {
            minElo = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage(CC.translate("&cInvalid minimum Elo value!"));
            return;
        }

        int maxElo;
        try {
            maxElo = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            sender.sendMessage(CC.translate("&cInvalid maximum Elo value!"));
            return;
        }

        if (minElo >= maxElo) {
            sender.sendMessage(CC.translate("&cMinimum Elo must be less than maximum Elo!"));
            return;
        }

        levelService.createLevel(levelName, minElo, maxElo);
        sender.sendMessage(CC.translate("&aLevel &6" + levelName + " &acreated successfully with min Elo &6" + minElo + " &aand max Elo &6" + maxElo + "&a!"));
    }
}