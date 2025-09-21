package dev.revere.alley.feature.level.command.impl.manage;

import dev.revere.alley.common.text.CC;
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
public class LevelAdminViewCommand extends BaseCommand {
    @CommandData(
            name = "leveladmin.view",
            isAdminOnly = true,
            inGameOnly = false,
            usage = "leveladmin view <levelName>",
            description = "View level information"
    )
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (args.length < 1) {
            command.sendUsage();
            return;
        }

        String levelName = args[0];
        LevelService levelService = this.plugin.getService(LevelService.class);
        LevelData level = levelService.getLevel(levelName);
        if (level == null) {
            sender.sendMessage(this.getMessage(GlobalMessagesLocaleImpl.LEVEL_NOT_FOUND).replace("{level-name}", levelName));
            return;
        }

        Arrays.asList(
                "",
                "&6&lLevel Information:",
                " &f● &6Name: &e" + level.getName(),
                " &f● &6Display Name: &e" + level.getDisplayName(),
                " &f● &6Minimum Elo: &e" + level.getMinElo(),
                " &f● &6Maximum Elo: &e" + level.getMaxElo(),
                " &f● &6Material: &e" + level.getMaterial().name(),
                " &f● &6Durability: &e" + level.getDurability(),
                ""
        ).forEach(line -> sender.sendMessage(CC.translate(line)));
    }
}