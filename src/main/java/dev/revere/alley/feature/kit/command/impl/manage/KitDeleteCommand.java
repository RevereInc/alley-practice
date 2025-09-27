package dev.revere.alley.feature.kit.command.impl.manage;

import dev.revere.alley.common.reflect.ReflectionService;
import dev.revere.alley.common.reflect.internal.types.ActionBarReflectionServiceImpl;
import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import dev.revere.alley.feature.arena.ArenaService;
import dev.revere.alley.feature.kit.Kit;
import dev.revere.alley.feature.kit.KitService;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 20/05/2024
 */
public class KitDeleteCommand extends BaseCommand {
    @CommandData(
            name = "kit.delete",
            isAdminOnly = true,
            usage = "kit delete <kitName>",
            description = "Delete a kit from the server"
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            command.sendUsage();
            return;
        }

        String kitName = args[0];
        KitService kitService = this.plugin.getService(KitService.class);
        Kit kit = kitService.getKit(kitName);
        if (kit == null) {
            player.sendMessage(CC.translate(this.getString(GlobalMessagesLocaleImpl.KIT_NOT_FOUND)));
            return;
        }

        kitService.deleteKit(kit);
        this.plugin.getService(ArenaService.class).getArenas().forEach(arena -> {
            if (arena.getKits().contains(kitName)) {
                arena.getKits().remove(kitName);
                arena.saveArena();
            }
        });

        String message = this.getString(GlobalMessagesLocaleImpl.KIT_DELETED).replace("{kit-name}", kitName);

        player.sendMessage(message);
        this.plugin.getService(ReflectionService.class).getReflectionService(ActionBarReflectionServiceImpl.class).sendMessage(player, message, 5);
        player.sendMessage("");
        player.sendMessage(CC.translate("&7Do not forget to reload the queues by using &c&l/queue reload&7."));
        player.sendMessage(CC.translate("&7Additionally, the kit has been removed from all arenas it was added to."));
        player.sendMessage("");
    }
}