package dev.revere.alley.feature.level.command.impl.data;

import dev.revere.alley.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import dev.revere.alley.feature.level.LevelService;
import dev.revere.alley.feature.level.data.LevelData;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

/**
 * @author Emmy
 * @project Alley
 * @since 26/05/2025
 */
public class LevelAdminSetDisplayNameCommand extends BaseCommand {
    @CommandData(
            name = "leveladmin.setdisplayname",
            isAdminOnly = true,
            usage = "leveladmin setdisplayname <levelName> <displayName>",
            description = "Set the display name of a level",
            inGameOnly = false
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

        String displayName = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        level.setDisplayName(displayName);
        levelService.saveLevel(level);

        sender.sendMessage(this.getString(GlobalMessagesLocaleImpl.LEVEL_DISPLAY_NAME_SET)
                .replace("{level-name}", levelName)
                .replace("{display-name}", displayName)
        );
    }
}