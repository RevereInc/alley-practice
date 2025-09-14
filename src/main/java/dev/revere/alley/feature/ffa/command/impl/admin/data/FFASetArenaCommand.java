package dev.revere.alley.feature.ffa.command.impl.admin.data;

import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.locale.internal.impl.command.ArenaLocaleImpl;
import dev.revere.alley.core.locale.internal.impl.command.FFALocaleImpl;
import dev.revere.alley.core.locale.internal.impl.command.KitLocaleImpl;
import dev.revere.alley.feature.arena.Arena;
import dev.revere.alley.feature.arena.ArenaService;
import dev.revere.alley.feature.arena.ArenaType;
import dev.revere.alley.feature.ffa.FFAService;
import dev.revere.alley.feature.kit.Kit;
import dev.revere.alley.feature.kit.KitService;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import org.bukkit.command.CommandSender;

/**
 * @author Emmy
 * @project alley-practice
 * @since 25/07/2025
 */
public class FFASetArenaCommand extends BaseCommand {
    @CommandData(
            name = "ffa.setarena",
            isAdminOnly = true,
            inGameOnly = false,
            usage = "ffa setarena <kitName> <arenaName>",
            description = "Set the FFA arena for a kit."
    )
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (args.length < 2) {
            sender.sendMessage(CC.translate("&6Usage: &e/ffa setarena &6<kitName> <arenaName>"));
            return;
        }

        KitService kitService = this.plugin.getService(KitService.class);
        Kit kit = kitService.getKit(args[0]);
        if (kit == null) {
            sender.sendMessage(this.getMessage(KitLocaleImpl.NOT_FOUND));
            return;
        }

        String arenaName = args[1];
        ArenaService arenaService = this.plugin.getService(ArenaService.class);
        Arena arena = arenaService.getArenaByName(arenaName);
        if (arena == null) {
            sender.sendMessage(this.getMessage(ArenaLocaleImpl.NOT_FOUND).replace("{arena-name}", arenaName));
            return;
        }

        if (arena.getType() != ArenaType.FFA) {
            sender.sendMessage(this.getMessage(FFALocaleImpl.CAN_ONLY_SETUP_IN_FFA_ARENA));
            return;
        }

        if (!kit.isFfaEnabled()) {
            sender.sendMessage(this.getMessage(FFALocaleImpl.DISABLED).replace("{kit-name}", kit.getName()));
            return;
        }

        kit.setFfaArenaName(arena.getName());
        kitService.saveKit(kit);
        this.plugin.getService(FFAService.class).reloadFFAKits();
        sender.sendMessage(this.getMessage(FFALocaleImpl.ARENA_SET)
                .replace("{kit-name}", kit.getName())
                .replace("{arena-name}", arena.getName())
        );
    }
}