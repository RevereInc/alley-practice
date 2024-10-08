package me.emmy.alley.kit.command;

import me.emmy.alley.api.command.Completer;
import me.emmy.alley.kit.command.impl.data.KitSetDescriptionCommand;
import me.emmy.alley.kit.command.impl.data.KitSetDisclaimerCommand;
import me.emmy.alley.kit.command.impl.data.KitSetDisplayNameCommand;
import me.emmy.alley.kit.command.impl.data.KitSetIconCommand;
import me.emmy.alley.kit.command.impl.data.inventory.KitGetInvCommand;
import me.emmy.alley.kit.command.impl.data.inventory.KitSetInvCommand;
import me.emmy.alley.kit.command.impl.data.slot.KitSetEditorSlotCommand;
import me.emmy.alley.kit.command.impl.data.slot.KitSetRankedSlotCommand;
import me.emmy.alley.kit.command.impl.data.slot.KitSetSlotAllCommand;
import me.emmy.alley.kit.command.impl.data.slot.KitSetUnrankedSlotCommand;
import me.emmy.alley.kit.command.impl.manage.KitCreateCommand;
import me.emmy.alley.kit.command.impl.manage.KitDeleteCommand;
import me.emmy.alley.kit.command.impl.manage.KitListCommand;
import me.emmy.alley.kit.command.impl.manage.KitViewCommand;
import me.emmy.alley.kit.command.impl.settings.KitSetSettingCommand;
import me.emmy.alley.kit.command.impl.settings.KitSettingsCommand;
import me.emmy.alley.kit.command.impl.storage.KitSaveAllCommand;
import me.emmy.alley.kit.command.impl.storage.KitSaveCommand;
import me.emmy.alley.util.chat.CC;
import me.emmy.alley.api.command.BaseCommand;
import me.emmy.alley.api.command.Command;
import me.emmy.alley.api.command.CommandArgs;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Emmy
 * @project Practice
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
        new KitViewCommand();
    }

    @Completer(name = "kit")
    public List<String> kitCompleter(CommandArgs command) {
        List<String> completion = new ArrayList<>();
        if (command.getArgs().length == 1 && command.getPlayer().hasPermission("alley.admin")) {
            completion.add("list");
            completion.add("saveall");
            completion.add("settings");
            completion.add("save");
            completion.add("view");
            completion.add("delete");
            completion.add("create");
            completion.add("seticon");
            completion.add("setinv");
            completion.add("getinv");
            completion.add("setdesc");
            completion.add("setdisclaimer");
            completion.add("seteditorslot");
            completion.add("setrankedslot");
            completion.add("setslotall");
            completion.add("setunrankedslot");
            completion.add("setsetting");
            completion.add("setdisplayname");
        }

        return completion;
    }

    @Command(name = "kit", permission = "alley.admin")
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        sender.sendMessage(" ");
        sender.sendMessage(CC.translate("&b&lKit Commands Help:"));
        sender.sendMessage(CC.translate(" &f● &b/kit list &7| View all kits"));
        sender.sendMessage(CC.translate(" &f● &b/kit saveall &7| Save all kits"));
        sender.sendMessage(CC.translate(" &f● &b/kit settings &7| View all kit settings"));
        sender.sendMessage(CC.translate(" &f● &b/kit save &8(&7kitName&8) &7| Save a kit"));
        sender.sendMessage(CC.translate(" &f● &b/kit view &8(&7kitName&8) &7| View a kit"));
        sender.sendMessage(CC.translate(" &f● &b/kit delete &8(&7kitName&8) &7| Delete a kit"));
        sender.sendMessage(CC.translate(" &f● &b/kit create &8(&7kitName&8) &7| Create a kit"));
        sender.sendMessage(CC.translate(" &f● &b/kit seticon &8(&7kitName&8) &7| Set icon of a kit"));
        sender.sendMessage(CC.translate(" &f● &b/kit setinv &8(&7kitName&8) &7| Set inventory of a kit"));
        sender.sendMessage(CC.translate(" &f● &b/kit getinv &8(&7kitName&8) &7| Get inventory of a kit"));
        sender.sendMessage(CC.translate(" &f● &b/kit setdesc &8(&7kitName&8) &8(&7description&8) &7| Set desc of a kit"));
        sender.sendMessage(CC.translate(" &f● &b/kit setdisclaimer &8(&7kitName&8) &8(&7disclaimer&8) &7| Set disclaimer of a kit"));
        sender.sendMessage(CC.translate(" &f● &b/kit seteditorslot &8(&7kitName&8) &8(&7slot&8) &7| Set editor menu slot"));
        sender.sendMessage(CC.translate(" &f● &b/kit setrankedslot &8(&7kitName&8) &8(&7slot&8) &7| Set ranked menu slot"));
        sender.sendMessage(CC.translate(" &f● &b/kit setunrankedslot &8(&7kitName&8) &8(&7slot&8) &7| Set unranked menu slot"));
        sender.sendMessage(CC.translate(" &f● &b/kit setslotall &8(&7kitName&8) &8(&7slot&8) &7| Set all menu slots"));
        sender.sendMessage(CC.translate(" &f● &b/kit setsetting &8(&7kitName&8) &8(&7setting&8) &8(&7enabled&8) &7| Set kit setting"));
        sender.sendMessage(CC.translate(" &f● &b/kit setdisplayname &8(&7kitName&8) &8(&7displayname&8) &7| Set display-name of a kit"));
        sender.sendMessage("");
    }
}