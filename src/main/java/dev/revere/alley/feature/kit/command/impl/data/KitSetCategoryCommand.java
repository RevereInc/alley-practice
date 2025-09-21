package dev.revere.alley.feature.kit.command.impl.data;

import dev.revere.alley.common.text.CC;
import dev.revere.alley.common.text.EnumFormatter;
import dev.revere.alley.feature.kit.Kit;
import dev.revere.alley.feature.kit.KitCategory;
import dev.revere.alley.feature.kit.KitService;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import dev.revere.alley.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import org.bukkit.command.CommandSender;

/**
 * @author Emmy
 * @project Alley
 * @since 01/05/2025
 */
public class KitSetCategoryCommand extends BaseCommand {
    @CommandData(
            name = "kit.setcategory",
            isAdminOnly = true,
            inGameOnly = false,
            usage = "kit setcategory <kitName> <category>",
            description = "Set the category of a kit."
    )
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (command.length() < 2) {
            command.sendUsage();
            return;
        }

        KitService kitService = this.plugin.getService(KitService.class);
        Kit kit = kitService.getKit(args[0]);
        if (kit == null) {
            sender.sendMessage(this.getMessage(GlobalMessagesLocaleImpl.KIT_NOT_FOUND));
            return;
        }

        KitCategory category;

        try {
            category = KitCategory.valueOf(args[1].toUpperCase());
        } catch (IllegalArgumentException e) {
            sender.sendMessage(EnumFormatter.outputAvailableValues(KitCategory.class));
            return;
        }

        kit.setCategory(category);
        kitService.saveKit(kit);
        sender.sendMessage(CC.translate(this.getMessage(GlobalMessagesLocaleImpl.KIT_CATEGORY_SET).replace("{kit-name}", kit.getName()).replace("{category}", category.getName())));
    }
}
