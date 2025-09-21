package dev.revere.alley.feature.kit.command.impl.data;

import dev.revere.alley.common.text.CC;
import dev.revere.alley.feature.kit.Kit;
import dev.revere.alley.feature.kit.KitService;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import dev.revere.alley.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * @author Emmy
 * @project Alley
 * @date 28/04/2024 - 22:46
 */
public class KitSetDisplayNameCommand extends BaseCommand {
    @CommandData(
            name = "kit.displayname",
            aliases = "kit.setdisplayname",
            isAdminOnly = true,
            usage = "kit displayname <kitName> <displayName>",
            description = "Set the display name of a kit."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (command.length() < 2) {
            command.sendUsage();
            return;
        }

        KitService kitService = this.plugin.getService(KitService.class);
        Kit kit = kitService.getKit(args[0]);
        if (kit == null) {
            player.sendMessage(CC.translate(this.getMessage(GlobalMessagesLocaleImpl.KIT_NOT_FOUND)));
            return;
        }

        String displayName = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        kit.setDisplayName(displayName);
        this.plugin.getService(KitService.class).saveKit(kit);
        player.sendMessage(CC.translate(this.getMessage(GlobalMessagesLocaleImpl.KIT_DISPLAYNAME_SET)).replace("{kit-name}", kit.getName()).replace("{display-name}", displayName));
    }
}