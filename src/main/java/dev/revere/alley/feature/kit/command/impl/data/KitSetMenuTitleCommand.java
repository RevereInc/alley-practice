package dev.revere.alley.feature.kit.command.impl.data;

import dev.revere.alley.common.text.CC;
import dev.revere.alley.feature.kit.Kit;
import dev.revere.alley.feature.kit.KitService;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import dev.revere.alley.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

/**
 * @author Emmy
 * @project Alley
 * @since 25/06/2025
 */
public class KitSetMenuTitleCommand extends BaseCommand {
    @CommandData(
            name = "kit.setmenutitle",
            aliases = "kit.menutitle",
            isAdminOnly = true,
            inGameOnly = false,
            usage = "kit setmenutitle <kitName> <title>",
            description = "Set the menu title for a kit."
    )
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (args.length < 2) {
            command.sendUsage();
            return;
        }

        String kitName = args[0];
        KitService kitService = this.plugin.getService(KitService.class);
        Kit kit = kitService.getKit(kitName);
        if (kit == null) {
            sender.sendMessage(CC.translate(this.getMessage(GlobalMessagesLocaleImpl.KIT_NOT_FOUND)));
            return;
        }

        String title = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        kit.setMenuTitle(title);
        kitService.saveKit(kit);
        sender.sendMessage(CC.translate(this.getMessage(GlobalMessagesLocaleImpl.KIT_MENU_TITLE_SET).replace("{kit-name}", kit.getName()).replace("{title}", title)));
    }
}