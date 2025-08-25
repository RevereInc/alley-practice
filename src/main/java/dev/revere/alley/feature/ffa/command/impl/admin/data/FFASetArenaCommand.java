package dev.revere.alley.feature.ffa.command.impl.admin.data;

import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.config.internal.locale.impl.ArenaLocale;
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
            inGameOnly = false
    )
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (args.length < 1) {
            sender.sendMessage(CC.translate("&6Usage: &e/ffa setarena &6<arenaName>"));
            return;
        }

        String arenaName = args[0];
        ArenaService arenaService = this.plugin.getService(ArenaService.class);
        Arena arena = arenaService.getArenaByName(arenaName);
        if (arena == null) {
            sender.sendMessage(ArenaLocale.NOT_FOUND.getMessage().replace("{arena-name}", arenaName));
            return;
        }

        if (arena.getType() != ArenaType.FFA) {
            sender.sendMessage(CC.translate("&cYou can only set the arena for Free-For-All arenas!"));
            return;
        }

        KitService kitService = this.plugin.getService(KitService.class);
        Kit kit = kitService.getKit(arenaName);
        if (kit == null) {
            sender.sendMessage(CC.translate("&cA kit with that name does not exist!"));
            return;
        }

        if (!kit.isFfaEnabled()) {
            sender.sendMessage(CC.translate("&cFFA mode is not enabled for this kit!"));
            return;
        }

        kit.setFfaArenaName(arena.getName());
        kitService.saveKit(kit);
        this.plugin.getService(FFAService.class).reloadFFAKits();
        sender.sendMessage(CC.translate("&aFFA arena has been set for kit &6" + kit.getName() + "&a!"));
    }
}