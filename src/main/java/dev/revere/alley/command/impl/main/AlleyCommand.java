package dev.revere.alley.command.impl.main;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.api.command.annotation.CompleterData;
import dev.revere.alley.api.constant.PluginConstant;
import dev.revere.alley.util.chat.CC;
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
    @SuppressWarnings("unused")
    @CompleterData(name = "alley")
    public List<String> alleyCompleter(CommandArgs command) {
        List<String> completion = new ArrayList<>();
        if (command.getArgs().length == 1 && command.getPlayer().hasPermission(this.getAdminPermission())) {
            completion.addAll(Arrays.asList(
                    "reload", "debug", "server"
            ));
        }

        return completion;
    }

    @CommandData(name = "alley", aliases = {"apractice", "aprac", "practice", "prac", "emmy", "remi", "revere"}, inGameOnly = false)
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();

        Arrays.asList(
                "",
                "     &6&lAlley Practice",
                "      &f│ Authors: &6" + this.plugin.getDescription().getAuthors().toString().replace("[", "").replace("]", ""),
                "      &f│ Version: &6" + this.plugin.getDescription().getVersion(),
                "",
                "     &6&lDescription:",
                "      &f│ " + this.plugin.getDescription().getDescription(),
                ""
        ).forEach(line -> sender.sendMessage(CC.translate(line)));

        if (sender.hasPermission(this.plugin.getService(PluginConstant.class).getAdminPermissionPrefix())) {
            Arrays.asList(
                    "     &6&lAdmin Help",
                    "      &f│ /alley reload &7- &6Reloads the plugin.",
                    "      &f│ /alley debug &7- &6Database Debugging.",
                    "      &f│ /alley server &7- &6Core Hook Info.",
                    ""
            ).forEach(line -> sender.sendMessage(CC.translate(line)));
        }
    }
}