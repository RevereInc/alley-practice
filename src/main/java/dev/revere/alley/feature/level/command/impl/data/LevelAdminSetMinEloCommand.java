package dev.revere.alley.feature.level.command.impl.data;

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
public class LevelAdminSetMinEloCommand extends BaseCommand {
    @CommandData(name = "leveladmin.setminelo", isAdminOnly = true, description = "Set the minimum Elo for a level", inGameOnly = false)
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (args.length < 2) {
            sender.sendMessage(CC.translate("&6Usage: &e/leveladmin setminelo &6<levelName> <minElo>"));
            return;
        }

        String levelName = args[0];
        LevelService levelService = this.plugin.getService(LevelService.class);
        LevelData level = levelService.getLevel(levelName);
        if (level == null) {
            sender.sendMessage(CC.translate("&cA level with that name does not exist!"));
            return;
        }

        int minElo;
        try {
            minElo = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage(CC.translate("&cInvalid minimum Elo value! It must be a number."));
            return;
        }

        if (minElo < 0) {
            sender.sendMessage(CC.translate("&cMinimum Elo cannot be negative!"));
            return;
        }

        level.setMinElo(minElo);
        levelService.saveLevel(level);
        sender.sendMessage(CC.translate("&aMinimum Elo for level &6" + levelName + " &aset to &6" + minElo + "&a!"));
    }
}