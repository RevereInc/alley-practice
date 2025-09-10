package dev.revere.alley.feature.command.impl.main;

import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import dev.revere.alley.library.command.annotation.CompleterData;
import dev.revere.alley.common.constants.PluginConstant;
import dev.revere.alley.common.text.CC;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @date 19/04/2024 - 17:39
 */
public class AlleyCommand extends BaseCommand {
    @CompleterData(name = "alley")
    public List<String> alleyCompleter(CommandArgs command) {
        List<String> completion = new ArrayList<>();
        if (command.getArgs().length == 1 && command.getPlayer().hasPermission(this.getAdminPermission())) {
            completion.addAll(Arrays.asList(
                    "reload", "debug", "core"
            ));
        }

        return completion;
    }

    @CommandData(
            name = "alley",
            aliases = {"apractice", "aprac", "practice", "prac", "emmy", "remi", "revere"},
            inGameOnly = false,
            usage = "alley",
            description = "Displays information about the plugin."
    )
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();

        Arrays.asList(
                "",
                "     &6&lAlley Practice",
                "      &6&l│ &fAuthors: &6" + this.plugin.getDescription().getAuthors().toString().replace("[", "").replace("]", ""),
                "      &6&l│ &fVersion: &6" + this.plugin.getDescription().getVersion(),
                "",
                "     &6&lDescription:",
                "      &6&l│ &f" + this.plugin.getDescription().getDescription(),
                ""
        ).forEach(line -> sender.sendMessage(CC.translate(line)));

        if (sender.hasPermission(this.plugin.getService(PluginConstant.class).getAdminPermissionPrefix())) {
            Arrays.asList(
                    "     &6&lAdmin Help",
                    "      &6&l│ &f/alley reload &7- &6Reloads the bootstrap.",
                    "      &6&l│ &f/alley debug &7- &6Database Debugging.",
                    "      &6&l│ &f/alley core &7- &6Core Hook Info.",
                    ""
            ).forEach(line -> sender.sendMessage(CC.translate(line)));
        }
    }
}