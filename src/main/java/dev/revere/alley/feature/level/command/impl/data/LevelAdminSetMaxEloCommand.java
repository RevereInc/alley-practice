package dev.revere.alley.feature.level.command.impl.data;

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
public class LevelAdminSetMaxEloCommand extends BaseCommand {
    @CommandData(
            name = "leveladmin.setmaxelo",
            isAdminOnly = true,
            inGameOnly = false,
            usage = "leveladmin setmaxelo <levelName> <maxElo>",
            description = "Set the maximum Elo for a level"
    )
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (args.length < 2) {
            command.sendUsage();
            return;
        }

        String levelName = args[0];
        LevelService levelService = this.plugin.getService(LevelService.class);
        LevelData level = levelService.getLevel(levelName);
        if (level == null) {
            sender.sendMessage(this.getString(GlobalMessagesLocaleImpl.LEVEL_NOT_FOUND).replace("{level-name}", levelName));
            return;
        }

        int maxElo;
        try {
            maxElo = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage(this.getString(GlobalMessagesLocaleImpl.ERROR_INVALID_NUMBER).replace("{input}", args[1]));
            return;
        }

        if (maxElo <= level.getMinElo()) {
            sender.sendMessage(this.getString(GlobalMessagesLocaleImpl.LEVEL_MAX_ELO_MUST_BE_GREATER_THAN_MIN).replace("{min-elo}", String.valueOf(level.getMinElo())));
            return;
        }

        level.setMaxElo(maxElo);
        levelService.saveLevel(level);

        sender.sendMessage(this.getString(GlobalMessagesLocaleImpl.LEVEL_MAX_ELO_SET)
                .replace("{level-name}", levelName)
                .replace("{max-elo}", String.valueOf(maxElo))
        );
    }
}