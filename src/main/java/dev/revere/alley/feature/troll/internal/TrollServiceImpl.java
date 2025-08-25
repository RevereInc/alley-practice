package dev.revere.alley.feature.troll.internal;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.bootstrap.AlleyContext;
import dev.revere.alley.bootstrap.annotation.Service;
import dev.revere.alley.common.logger.Logger;
import dev.revere.alley.common.reflect.ReflectionService;
import dev.revere.alley.common.reflect.internal.types.DefaultReflectionImpl;
import dev.revere.alley.feature.troll.TrollService;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Emmy
 * @project alley-practice
 * @since 25/08/2025
 */
@Service(provides = TrollService.class, priority = 1010)
public class TrollServiceImpl implements TrollService {
    private static final double DONUT_RADIUS = 2.5;
    private static final double TUBE_RADIUS = 0.5;
    private static final int MAIN_SEGMENTS = 250;
    private static final int TUBE_SEGMENTS = 250;
    private static int FAKE_ENTITY_ID_COUNTER = Integer.MAX_VALUE - 1_100_100;

    private DefaultReflectionImpl reflection;

    @Override
    public void initialize(AlleyContext context) {
        this.reflection = context.getPlugin().getService(ReflectionService.class).getReflectionService(DefaultReflectionImpl.class);
    }

    @Override
    public void spawnDonut(Player target) {
        Location location = target.getLocation();
        WorldServer worldServer = (WorldServer) this.reflection.getCraftPlayer(target).getHandle().getWorld();

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

                int fakeId = this.getNextFakeEntityId();
                fakeEntityIds.add(fakeId);

                PacketPlayOutSpawnEntity spawnPacket = new PacketPlayOutSpawnEntity(fakeBoat, 1, fakeId);
                this.reflection.sendPacket(target, spawnPacket);
            }
        }

        int[] idsToDestroy = fakeEntityIds.stream().mapToInt(Integer::intValue).toArray();
        PacketPlayOutEntityDestroy destroyPacket = new PacketPlayOutEntityDestroy(idsToDestroy);

        AlleyPlugin.getInstance().getServer().getScheduler().runTaskLater(AlleyPlugin.getInstance(), () -> {
            if (target.isOnline()) {
                this.reflection.sendPacket(target, destroyPacket);
            }
        }, 1200L); // 60 seconds
    }

    @Override
    public void openDemoMenu(Player target) {
        try {
            PacketPlayOutGameStateChange packet = new PacketPlayOutGameStateChange(5, 0);
            this.reflection.sendPacket(target, packet);
        } catch (Exception exception) {
            Logger.error("An error occurred whilst trying to open demo menu for " + target.getName() + ": " + exception.getMessage());
        }
    }

    /**
     * Generates a unique fake entity ID for the donut boats.
     *
     * @return A unique fake entity ID.
     */
    private int getNextFakeEntityId() {
        return FAKE_ENTITY_ID_COUNTER--;
    }
}