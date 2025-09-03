package dev.revere.alley.feature.command.impl.other.troll;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.core.config.internal.locale.impl.ErrorLocale;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import dev.revere.alley.common.text.CC;
import net.minecraft.server.v1_8_R3.EntityBoat;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntity;
import net.minecraft.server.v1_8_R3.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Remi
 * @project alley-practice
 * @date 22/07/2025
 */
public class DonutCommand extends BaseCommand {
    private static final double DONUT_RADIUS = 2.5;
    private static final double TUBE_RADIUS = 0.5;
    private static final int MAIN_SEGMENTS = 250;
    private static final int TUBE_SEGMENTS = 250;
    private static int FAKE_ENTITY_ID_COUNTER = Integer.MAX_VALUE - 1_100_100;

    @CommandData(name = "donut", permission = "alley.command.troll.donut")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length == 0) {
            player.sendMessage(CC.translate("&cUsage: /donut <player>"));
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null || !target.isOnline()) {
            player.sendMessage(ErrorLocale.INVALID_PLAYER.getMessage());
            return;
        }

        spawnDonut(target);
        player.sendMessage(CC.translate("&aYou have successfully donut'd " + target.getName() + "!"));
    }

    private void spawnDonut(Player target) {
        Location location = target.getLocation();
        CraftPlayer craftPlayer = (CraftPlayer) target;
        WorldServer worldServer = (WorldServer) craftPlayer.getHandle().getWorld();

        List<Integer> fakeEntityIds = new ArrayList<>(MAIN_SEGMENTS * TUBE_SEGMENTS);

        for (int i = 0; i < MAIN_SEGMENTS; i++) {
            double theta = 2 * Math.PI * i / MAIN_SEGMENTS;
            double cosTheta = Math.cos(theta);
            double sinTheta = Math.sin(theta);

            for (int j = 0; j < TUBE_SEGMENTS; j++) {
                double phi = 2 * Math.PI * j / TUBE_SEGMENTS;
                double cosPhi = Math.cos(phi);
                double sinPhi = Math.sin(phi);

                double x = (DONUT_RADIUS + TUBE_RADIUS * cosPhi) * cosTheta;
                double y = TUBE_RADIUS * sinPhi;
                double z = (DONUT_RADIUS + TUBE_RADIUS * cosPhi) * sinTheta;

                EntityBoat fakeBoat = new EntityBoat(worldServer);
                fakeBoat.setPosition(location.getX() + x, location.getY() + y + 1.0f, location.getZ() + z);

                int fakeId = getNextFakeEntityId();
                fakeEntityIds.add(fakeId);

                PacketPlayOutSpawnEntity spawnPacket = new PacketPlayOutSpawnEntity(fakeBoat, 1, fakeId);
                craftPlayer.getHandle().playerConnection.sendPacket(spawnPacket);
            }
        }

        int [] idsToDestroy = fakeEntityIds.stream().mapToInt(Integer::intValue).toArray();
        PacketPlayOutEntityDestroy destroyPacket = new PacketPlayOutEntityDestroy(idsToDestroy);

        Bukkit.getScheduler().runTaskLater(AlleyPlugin.getInstance(), () -> {
            if (target.isOnline()) {
                CraftPlayer craftTarget = (CraftPlayer) target;
                craftTarget.getHandle().playerConnection.sendPacket(destroyPacket);
            }
        }, 1200L);
    }

    private int getNextFakeEntityId() {
        return FAKE_ENTITY_ID_COUNTER--;
    }
}
