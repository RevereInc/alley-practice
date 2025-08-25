package dev.revere.alley.feature.spawn.command;

import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.config.ConfigService;
import dev.revere.alley.feature.spawn.SpawnService;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 29/04/2024 - 18:41
 */
public class SetSpawnCommand extends BaseCommand {

    @Override
    @CommandData(name = "setspawn", isAdminOnly = true)
    public void onCommand(CommandArgs cmd) {
        Player player = (Player) cmd.getSender();
        Location location = player.getLocation();
        this.plugin.getService(SpawnService.class).updateSpawnLocation(location);

        String message = this.plugin.getService(ConfigService.class).getMessagesConfig().getString("spawn.spawn-set");
        message = message.replace("{world}", location.getWorld().getName())
                .replace("{x}", String.format("%.2f", location.getX()))
                .replace("{y}", String.format("%.2f", location.getY()))
                .replace("{z}", String.format("%.2f", location.getZ()))
                .replace("{yaw}", String.format("%.2f", location.getYaw()))
                .replace("{pitch}", String.format("%.2f", location.getPitch()));

        player.sendMessage(CC.translate(message));
    }
}