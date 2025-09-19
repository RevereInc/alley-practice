package dev.revere.alley.feature.level.command.impl.manage;

import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import dev.revere.alley.feature.level.LevelService;
import dev.revere.alley.feature.level.data.LevelData;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import org.bukkit.command.CommandSender;

/**
 * @author Emmy
 * @project Alley
 * @since 26/05/2025
 */
public class LevelAdminCreateCommand extends BaseCommand {
    @CommandData(
            name = "leveladmin.create",
            isAdminOnly = true,
            inGameOnly = false,
            usage = "leveladmin create <levelName> <minElo> <maxElo>",
            description = "Create a new level"
    )
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
            sender.sendMessage(this.getMessage(GlobalMessagesLocaleImpl.LEVEL_ALREADY_EXISTS).replace("{level-name}", levelName));
            return;
        }

        int minElo;
        try {
            minElo = Integer.parseInt(args[1]);
        } catch (NumberFormatException exception) {
            sender.sendMessage(this.getMessage(GlobalMessagesLocaleImpl.ERROR_INVALID_NUMBER).replace("{input}", args[1]));
            return;
        }

        int maxElo;
        try {
            maxElo = Integer.parseInt(args[2]);
        } catch (NumberFormatException exception) {
            sender.sendMessage(this.getMessage(GlobalMessagesLocaleImpl.ERROR_INVALID_NUMBER).replace("{input}", args[2]));
            return;
        }

        if (minElo >= maxElo) {
            sender.sendMessage(this.getMessage(GlobalMessagesLocaleImpl.LEVEL_MINIMUM_ELO_MUST_BE_LESS_THAN_MAXIMUM));
            return;
        }

        levelService.createLevel(levelName, minElo, maxElo);

        sender.sendMessage(this.getMessage(GlobalMessagesLocaleImpl.LEVEL_CREATED)
                .replace("{level-name}", levelName)
                .replace("{min-elo}", String.valueOf(minElo))
                .replace("{max-elo}", String.valueOf(maxElo))
        );
    }
}