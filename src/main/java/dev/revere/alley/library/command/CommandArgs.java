package dev.revere.alley.library.command;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.library.command.annotation.CommandData;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;

/**
 * Command Framework - CommandArgs <br>
 * This class is passed to the command methods and contains various utilities as
 * well as the command info.
 *
 * @author minnymin3
 */
@Getter
public class CommandArgs {
    private final CommandSender sender;
    private final Command command;
    private final String label;
    private final String[] args;

    protected CommandArgs(CommandSender sender, Command command, String label, String[] args, int subCommand) {
        String[] modArgs = new String[args.length - subCommand];
        if (args.length - subCommand >= 0) System.arraycopy(args, subCommand, modArgs, 0, args.length - subCommand);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(label);

        for (int x = 0; x < subCommand; x++) {
            stringBuilder.append(".").append(args[x]);
        }

        String cmdLabel = stringBuilder.toString();

        this.sender = sender;
        this.command = command;
        this.label = cmdLabel;
        this.args = modArgs;
    }

    public String getArgs(int index) {
        return args[index];
    }

    public int length() {
        return args.length;
    }

    public boolean isPlayer() {
        return sender instanceof Player;
    }

    public Player getPlayer() {
        if (sender instanceof Player) {
            return (Player) sender;
        } else {
            return null;
        }
    }

    /**
     * Formats the command's usage string with color codes.
     * Formatting: <required> = AQUA, [optional] = GRAY, command/label = YELLOW, "Usage:" = GOLD
     *
     * @return The command's usage string with color codes.
     */
    public String getUsage() {

        //TODO: maybe this should be configurable through config too?

        CommandFramework commandFramework = AlleyPlugin.getInstance().getService(CommandFramework.class);
        Method method = commandFramework.getCommandMap().get(this.label).getKey();
        CommandData commandData = method.getAnnotation(CommandData.class);

        // access annotation data because sub commands will inherit the parent command's usage, which is usually like "help <page>", ruining the entire point of this method
        String rawUsage = commandData.usage();

        StringBuilder formattedUsage = new StringBuilder();

        // start with "Usage: /command"
        formattedUsage.append(ChatColor.GOLD).append("Usage: ").append(ChatColor.YELLOW).append("/").append(label);
        //if formattedUsage contains a dot, split by dot and take the first part
        if (label.contains(".")) {
            formattedUsage = new StringBuilder(formattedUsage.toString().split("\\.")[0]);
        }

        // then append the rest of the usage
        String[] parts = rawUsage.split(" ");

        //the rest should be self-explanatory :D
        for (int i = 1; i < parts.length; i++) {
            String part = parts[i];

            if (part.startsWith("[") && part.endsWith("]")) {
                formattedUsage.append(" ").append(ChatColor.GRAY).append(part);
            } else if (part.startsWith("<") && part.endsWith(">")) {
                formattedUsage.append(" ").append(ChatColor.AQUA).append(part);
            } else {
                formattedUsage.append(" ").append(ChatColor.YELLOW).append(part);
            }
        }

        return formattedUsage.toString();
    }

    public void sendUsage() {
        sender.sendMessage(this.getUsage());
    }
}