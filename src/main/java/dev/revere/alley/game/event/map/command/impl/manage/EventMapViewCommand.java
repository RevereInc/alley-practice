package dev.revere.alley.game.event.map.command.impl.manage;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.game.event.map.EventMap;
import dev.revere.alley.game.event.map.EventMapService;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project alley-practice
 * @since 29/07/2025
 */
public class EventMapViewCommand extends BaseCommand {
    @CommandData(
            name = "eventmap.view",
            isAdminOnly = true,
            usage = "eventmap view <name>",
            description = "View an event map."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&6Usage: &e/eventmap view &6<name>"));
            return;
        }

        EventMapService eventMapService = this.plugin.getService(EventMapService.class);
        EventMap eventMap = eventMapService.getMap(args[0]);
        if (eventMap == null) {
            player.sendMessage(CC.translate("&cAn event map with that name does not exist."));
            return;
        }

        player.sendMessage("");
        player.sendMessage(CC.translate("&6&lEvent Map &f(&6" + eventMap.getName() + "&f)"));
        player.sendMessage(CC.translate(" &f● &6Name: &f" + eventMap.getName()));
        player.sendMessage(CC.translate(" &f● &6Display Name: &f" + eventMap.getDisplayName()));
        player.sendMessage(CC.translate(" &f● &6Spawn: &f" + (eventMap.getSpawn() != null ? eventMap.getSpawn().getBlockX() + ", " + eventMap.getSpawn().getBlockY() + ", " + eventMap.getSpawn().getBlockZ() : "&cNot set")));
        player.sendMessage(CC.translate(" &f● &6Center: &f" + (eventMap.getCenter() != null ? eventMap.getCenter().getBlockX() + ", " + eventMap.getCenter().getBlockY() + ", " + eventMap.getCenter().getBlockZ() : "&cNot set")));
        player.sendMessage(CC.translate(" &f● &6Pos1: &f" + (eventMap.getPos1() != null ? eventMap.getPos1().getBlockX() + ", " + eventMap.getPos1().getBlockY() + ", " + eventMap.getPos1().getBlockZ() : "&cNot set")));
        player.sendMessage(CC.translate(" &f● &6Pos2: &f" + (eventMap.getPos2() != null ? eventMap.getPos2().getBlockX() + ", " + eventMap.getPos2().getBlockY() + ", " + eventMap.getPos2().getBlockZ() : "&cNot set")));
        player.sendMessage(CC.translate(" &f● &6Type: &f" + (eventMap.getType() != null ? eventMap.getType().name() : "&cNot set")));
        player.sendMessage("");
    }
}