package dev.revere.alley.feature.hotbar.command;

import dev.revere.alley.common.text.CC;
import dev.revere.alley.common.text.ClickableUtil;
import dev.revere.alley.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project alley-practice
 * @since 21/07/2025
 */
public class HotbarCommand extends BaseCommand {
    @CommandData(
            name = "hotbar",
            aliases = "hotbar.help",
            isAdminOnly = true,
            inGameOnly = false,
            usage = "hotbar help <page>",
            description = "View all hotbar commands."
    )
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();
        int page = 1;

        if (args.length > 0) {
            try {
                page = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                sender.sendMessage(this.getString(GlobalMessagesLocaleImpl.ERROR_INVALID_PAGE_NUMBER).replace("{input}", args[0]));
            }
        }

        if (page > this.pages.length || page < 1) {
            sender.sendMessage(this.getString(GlobalMessagesLocaleImpl.ERROR_NO_MORE_PAGES_AVAILABLE)
                    .replace("{input}", String.valueOf(page))
                    .replace("{max-pages}", String.valueOf(pages.length))
            );
            return;
        }

        sender.sendMessage("");
        sender.sendMessage(CC.translate("&6&lHotbar Commands &8(&7Page &f" + page + "&7/&f" + this.pages.length + "&8)"));
        for (String string : this.pages[page - 1]) {
            sender.sendMessage(CC.translate(string));
        }

        sender.sendMessage("");

        if (sender instanceof Player) {
            Player player = (Player) sender;
            ClickableUtil.sendPageNavigation(player, page, this.pages.length, "/hotbar", false, true);
        }
    }

    private final String[][] pages = {
            {
                    " &f◆ &6/hotbar list &7| List all hotbar items.",
                    " &f◆ &6/hotbar create &8(&7name&8) &8(&7type&8) &7| Create a new hotbar item.",
                    " &f◆ &6/hotbar delete &8(&7name&8) &7| Delete a hotbar item.",
                    " &f◆ &6/hotbar refresh &7| Refresh the hotbar item cache.",
                    " &f◆ &6/hotbar view &8(&7name&8) &7| View a hotbar item.",
            },
            {
                    " &f◆ &6/hotbar displayname &8(&7name&8) &8(&7displayName&8) &7| Set the display name of a hotbar item.",
                    " &f◆ &6/hotbar material &8(&7name&8) &7| Set the material of a hotbar item.",
                    " &f◆ &6/hotbar addlore &8(&7name&8) &8(&7lore...&8) &7| Add lore lines to a hotbar item.",
                    " &f◆ &6/hotbar removelore &8(&7name&8) &8(&7lineNumber&8) &7| Remove a lore line from a hotbar item.",
            },
            {
                    " &f◆ &6/hotbar toggletype &8(&7name&8) &8(&7type&8) &7| Toggle the type of a hotbar item.",
                    " &f◆ &6/hotbar slot &8(&7name&8) &8(&7type&8) &8(&7slot&8) &7| Set the slot of a hotbar item.",
                    " &f◆ &6/hotbar command &8(&7name&8) &8(&7command&8) &7| Set the command action of a hotbar item.",
                    " &f◆ &6/hotbar menu &8(&7name&8) &8(&7menuName&8) &7| Set the menu action name of a hotbar item.",
            },
            {
                    " &f◆ &6/hotbar giveall&7| Give a hotbar item to all online players.",
                    " &f◆ &6/hotbar give &8(&7name&8) &7| Give a hotbar item to a player.",
                    " &f◆ &6/hotbar save &8(&7name&8) &7| Save changes to a hotbar item.",
                    " &f◆ &6/hotbar saveall &7| Save all hotbar items to the config.",
            }
    };
}