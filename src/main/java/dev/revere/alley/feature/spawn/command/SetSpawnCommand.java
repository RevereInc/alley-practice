package dev.revere.alley.feature.spawn.command;

import dev.revere.alley.feature.spawn.SpawnService;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import dev.revere.alley.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Objects;

/**
 * @author Emmy
 * @project Alley
 * @date 29/04/2024 - 18:41
 */
public class SetSpawnCommand extends BaseCommand {
    @CommandData(
            name = "setspawn",
            isAdminOnly = true,
            usage = "setspawn",
            description = "Set the server spawn point."
    )
    @Override
    public void onCommand(CommandArgs cmd) {
        Player player = (Player) cmd.getSender();

        Location location = player.getLocation();
        this.plugin.getService(SpawnService.class).updateSpawnLocation(location);

        player.sendMessage(this.getMessage(GlobalMessagesLocaleImpl.SPAWN_SET)
                .replace("{world}", Objects.requireNonNull(location.getWorld()).getName())
                .replace("{x}", String.format("%.2f", location.getX()))
                .replace("{y}", String.format("%.2f", location.getY()))
                .replace("{z}", String.format("%.2f", location.getZ()))
                .replace("{yaw}", String.format("%.2f", location.getYaw()))
                .replace("{pitch}", String.format("%.2f", location.getPitch()))
        );
    }
}