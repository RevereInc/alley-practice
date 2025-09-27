package dev.revere.alley.common.text;

import dev.revere.alley.AlleyPlugin;
import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @date 25/05/2024 - 12:41
 */
@UtilityClass
public class CC {
    public final String MENU_BAR;
    public final String PREFIX;
    public final String ERROR_PREFIX;
    public final String WARNING_PREFIX;

    static {
        MENU_BAR = translate("&8&m----------------------");
        PREFIX = translate("&f[&6" + AlleyPlugin.getInstance().getDescription().getName() + "&f] &r");
        ERROR_PREFIX = translate("&c[&4" + AlleyPlugin.getInstance().getDescription().getName() + "&c] &r");
        WARNING_PREFIX = translate("&f[&c" + AlleyPlugin.getInstance().getDescription().getName() + "&f] &r");
    }

    /**
     * Translate a string to a colored string.
     *
     * @param string The string to translate.
     * @return The translated string.
     */
    public String translate(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    /**
     * Translate a list of strings to a colored list of strings.
     *
     * @param string The list of strings to translate.
     * @return The translated list of strings.
     */
    public List<String> translateList(List<String> string) {
        List<String> list = new ArrayList<>();

        for (String line : string) {
            list.add(ChatColor.translateAlternateColorCodes('&', line));
        }

        return list;
    }

    /**
     * Translate an array of strings to a colored list of strings.
     *
     * @param string The array of strings to translate.
     * @return The translated list of strings.
     */
    public List<String> translateArray(String[] string) {
        List<String> list = new ArrayList<>();

        for (String line : string) {
            if (line != null) {
                list.add(ChatColor.translateAlternateColorCodes('&', line));
            }
        }

        return list;
    }

    public static void sender(CommandSender sender, String in) {
        sender.sendMessage(translate(in));
    }

    public static void message(Player player, String in) {
        player.sendMessage(translate(in));
    }
}