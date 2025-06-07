package dev.revere.alley.base.kit.command;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.api.command.annotation.CompleterData;
import dev.revere.alley.base.kit.command.impl.data.*;
import dev.revere.alley.base.kit.command.impl.data.inventory.KitGetInvCommand;
import dev.revere.alley.base.kit.command.impl.data.inventory.KitSetInvCommand;
import dev.revere.alley.base.kit.command.impl.data.slot.*;
import dev.revere.alley.base.kit.command.impl.manage.*;
import dev.revere.alley.base.kit.command.impl.settings.KitSetSettingCommand;
import dev.revere.alley.base.kit.command.impl.settings.KitSettingsCommand;
import dev.revere.alley.base.kit.command.impl.storage.KitSaveAllCommand;
import dev.revere.alley.base.kit.command.impl.storage.KitSaveCommand;
import dev.revere.alley.util.chat.CC;
import dev.revere.alley.util.chat.ClickableUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @date 28/04/2024 - 21:58
 */
public class KitCommand extends BaseCommand {

    public KitCommand() {
        new KitSaveCommand();
        new KitSaveAllCommand();
        new KitCreateCommand();
        new KitDeleteCommand();
        new KitListCommand();
        new KitGetInvCommand();
        new KitSetInvCommand();
        new KitSetDescriptionCommand();
        new KitSetDisclaimerCommand();
        new KitSetDisplayNameCommand();
        new KitSetEditorSlotCommand();
        new KitSetRankedSlotCommand();
        new KitSetSlotAllCommand();
        new KitSetUnrankedSlotCommand();
        new KitSetSettingCommand();
        new KitSettingsCommand();
        new KitSetIconCommand();
        new KitSetPotionCommand();
        new KitViewCommand();
        new KitViewSettingsCommand();
        new KitSetupFFACommand();
        new KitToggleFFACommand();
        new KitSetFfaSlotCommand();
        new KitSetExtraSlotCommand();
        new KitSetCategoryCommand();
        new KitSetEditableCommand();
    }

    @SuppressWarnings("unused")
    @CompleterData(name = "kit")
    public List<String> kitCompleter(CommandArgs command) {
        List<String> completion = new ArrayList<>();
        if (command.getArgs().length == 1 && command.getPlayer().hasPermission("alley.admin")) {
            completion.addAll(Arrays.asList(
                    "list", "saveall", "settings", "save", "view", "viewsettings", "delete", "create",
                    "seticon", "setinv", "getinv", "setdesc", "setdisclaimer", "seteditorslot",
                    "setrankedslot", "setslotall", "setunrankedslot", "setffaslot", "setsetting",
                    "setdisplayname", "setpotion", "toggleffa", "setupffa", "setcategory", "seteditable"
            ));
        }

        return completion;
    }

    @CommandData(name = "kit", isAdminOnly = true, inGameOnly = false)
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();
        int page = 1;

        if (args.length > 0) {
            try {
                page = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                sender.sendMessage(CC.translate("&cInvalid page number."));
            }
        }

        if (page > this.pages.length || page < 1) {
            sender.sendMessage(CC.translate("&cNo more pages available."));
            return;
        }

        sender.sendMessage("");
        sender.sendMessage(CC.translate("&b&lKit Commands &8(&7Page &f" + page + "&7/&f" + this.pages.length + "&8)"));
        for (String string : this.pages[page - 1]) {
            sender.sendMessage(CC.translate(string));
        }
        sender.sendMessage("");

        if (sender instanceof Player) {
            Player player = (Player) sender;
            ClickableUtil.sendPageNavigation(player, page, this.pages.length, "/kit", false, true);
        }
    }

    private final String[][] pages = {
            {
                    " &f● &b/kit list &7| View all kits",
                    " &f● &b/kit saveall &7| Save all kits",
                    " &f● &b/kit settings &7| View all kit settings",
                    " &f● &b/kit save &8(&7kitName&8) &7| Save a kit",
                    " &f● &b/kit view &8(&7kitName&8) &7| View a kit",
                    " &f● &b/kit viewsettings &8(&7kitName&8) &7| View kit settings",
                    " &f● &b/kit delete &8(&7kitName&8) &7| Delete a kit",
                    " &f● &b/kit create &8(&7kitName&8) &7| Create a kit",
                    " &f● &b/kit toggleffa &8(&7kitName&8) &7| Toggle ffa kit",
                    " &f● &b/kit setupffa &8(&7kitName&8) &7| Setup ffa kit",
            },
            {
                    " &f● &b/kit seticon &8(&7kitName&8) &7| Set icon of a kit",
                    " &f● &b/kit setinv &8(&7kitName&8) &7| Set inventory of a kit",
                    " &f● &b/kit getinv &8(&7kitName&8) &7| Get inventory of a kit",
                    " &f● &b/kit setdesc &8(&7kitName&8) &8(&7description&8) &7| Set desc of a kit",
                    " &f● &b/kit setsetting &8(&7kitName&8) &8(&7setting&8) &8(&7enabled&8) &7| Set kit setting",
                    " &f● &b/kit setdisclaimer &8(&7kitName&8) &8(&7disclaimer&8) &7| Set disclaimer",
                    " &f● &b/kit setdisplayname &8(&7kitName&8) &8(&7displayname&8) &7| Set display-name of a kit",
                    " &f● &b/kit setpotion &8(&7kitName&8) &7| Set potion effects of a kit",
                    " &f● &b/kit setcategory &8(&7kitName&8) &8(&7category&8) &7| Set category of a kit",
            },
            {
                    " &f● &b/kit setslotall &8(&7kitName&8) &8(&7slot&8) &7| Set all menu slots",
                    " &f● &b/kit seteditorslot &8(&7kitName&8) &8(&7slot&8) &7| Set editor menu slot",
                    " &f● &b/kit setrankedslot &8(&7kitName&8) &8(&7slot&8) &7| Set ranked menu slot",
                    " &f● &b/kit setunrankedslot &8(&7kitName&8) &8(&7slot&8) &7| Set unranked menu slot",
                    " &f● &b/kit setffaslot &8(&7kitName&8) &8(&7slot&8) &7| Set ffa menu slot",
            }
    };
}