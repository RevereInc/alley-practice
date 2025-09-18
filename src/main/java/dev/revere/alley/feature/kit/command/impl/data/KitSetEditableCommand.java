package dev.revere.alley.feature.kit.command.impl.data;

import dev.revere.alley.common.text.CC;
import dev.revere.alley.feature.kit.Kit;
import dev.revere.alley.feature.kit.KitService;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import dev.revere.alley.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import org.bukkit.command.CommandSender;

/**
 * @author Emmy
 * @project Alley
 * @since 04/05/2025
 */
public class KitSetEditableCommand extends BaseCommand {
    @CommandData(
            name = "kit.seteditable",
            isAdminOnly = true,
            inGameOnly = false,
            usage = "kit seteditable <name> <true/false>",
            description = "Set whether a kit is editable or not."
    )
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (args.length < 1) {
            sender.sendMessage(CC.translate("&6Usage: &e/kit seteditable &6<name> <true/false>"));
            return;
        }

        String kitName = args[0];
        KitService kitService = this.plugin.getService(KitService.class);
        Kit kit = kitService.getKit(kitName);
        if (kit == null) {
            sender.sendMessage(CC.translate(this.getMessage(GlobalMessagesLocaleImpl.KIT_NOT_FOUND)));
            return;
        }

        boolean editable;
        try {
            editable = Boolean.parseBoolean(args[1]);
        } catch (Exception exception) {
            sender.sendMessage(CC.translate("&cInvalid value for editable! Use true or false."));
            return;
        }

        kit.setEditable(editable);
        kitService.saveKit(kit);
        sender.sendMessage(CC.translate(this.getMessage(GlobalMessagesLocaleImpl.KIT_SET_EDITABLE)
                .replace("{kit-name}", kit.getName())
                .replace("{editable}", String.valueOf(editable))));
    }
}