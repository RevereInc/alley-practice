package dev.revere.alley.feature.troll.internal;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.bootstrap.AlleyContext;
import dev.revere.alley.bootstrap.annotation.Service;
import dev.revere.alley.common.reflect.Reflection;
import dev.revere.alley.common.reflect.internal.types.DefaultReflectionImpl;
import dev.revere.alley.feature.troll.TrollService;
import net.minecraft.server.v1_8_R3.EntityBoat;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutGameStateChange;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntity;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Emmy
 * @project alley-practice
 * @since 01/09/2025
 */
@Service(provides = TrollService.class, priority = 500)
public class TrollServiceImpl implements TrollService {
    private static int FAKE_ENTITY_ID_COUNTER = Integer.MAX_VALUE - 1_100_100;

    private static final int MAIN_SEGMENTS = 250;
    private static final int TUBE_SEGMENTS = 250;

    private static final double DONUT_RADIUS = 2.5;
    private static final double TUBE_RADIUS = 0.5;

    private Reflection reflection;

    @Override
    public void initialize(AlleyContext context) {
        this.reflection = DefaultReflectionImpl.INSTANCE;
    }

    @Override
    public void openDemoMenu(Player target) {
        PacketPlayOutGameStateChange packet = new PacketPlayOutGameStateChange(5, 0);
        this.reflection.sendPacket(target, packet);
    }

    @Override
    public void spawnDonut(Player target) {
        Location location = target.getLocation();
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

                EntityBoat fakeBoat = new EntityBoat(this.reflection.getCraftPlayer(target).getHandle().getWorld());
                fakeBoat.setPosition(location.getX() + x, location.getY() + y + 1.0f, location.getZ() + z);

                int fakeId = this.getNextFakeEntityId();
                fakeEntityIds.add(fakeId);

                PacketPlayOutSpawnEntity spawnPacket = new PacketPlayOutSpawnEntity(fakeBoat, 1, fakeId);
                this.reflection.sendPacket(target, spawnPacket);
            }
        }

        this.scheduleDestroyPacket(target, fakeEntityIds, 1200L);
    }

    @Override
    public void trapPlayer(Player trapper, Player target, long durationMillis) {
        target.teleport(trapper.getLocation());

        Location location = target.getLocation();
        Material material = Material.BARRIER;

        int radius = 2;
        int height = 5;

        List<Location> trappedLocations = this.generateCube(location, radius, height);
        for (Location blockLocation : trappedLocations) {
            if (blockLocation.getBlock().getType() == Material.AIR) {
                blockLocation.getBlock().setType(material);
            }
        }

        if (durationMillis > 0) {
            long ticks = durationMillis / 50L;
            AlleyPlugin.getInstance().getServer().getScheduler().runTaskLater(AlleyPlugin.getInstance(), () -> {
                for (Location point : trappedLocations) {
                    if (point.getBlock().getType() == material) {
                        point.getBlock().setType(Material.AIR);
                    }
                }
            }, ticks);
        }
    }

    @Override
    public List<Location> generateCube(Location location, int radius, int height) {
        List<Location> cubeLocations = new ArrayList<>();
        World world = location.getWorld();
        if (world == null) return cubeLocations;

        int px = location.getBlockX();
        int py = location.getBlockY();
        int pz = location.getBlockZ();

        for (int x = px - radius; x <= px + radius; x++) {
            for (int y = py - 1; y <= py + height; y++) {
                for (int z = pz - radius; z <= pz + radius; z++) {
                    if (x == px - radius || x == px + radius || y == py - 1 || y == py + height || z == pz - radius || z == pz + radius) {
                        Location blockLocation = new Location(world, x, y, z);
                        cubeLocations.add(blockLocation);
                    }
                }
            }
        }
        return cubeLocations;
    }

    /**
     * Schedules a task to destroy fake entities after a specified duration.
     *
     * @param target        The player to whom the destroy packets will be sent.
     * @param fakeEntityIds The list of fake entity IDs to be destroyed.
     * @param duration      The delay in ticks before sending the destroy packets.
     */
    private void scheduleDestroyPacket(Player target, List<Integer> fakeEntityIds, long duration) {
        int[] idsToDestroy = fakeEntityIds.stream().mapToInt(Integer::intValue).toArray();
        PacketPlayOutEntityDestroy destroyPacket = new PacketPlayOutEntityDestroy(idsToDestroy);

        AlleyPlugin.getInstance().getServer().getScheduler().runTaskLater(AlleyPlugin.getInstance(), () -> {
            if (target.isOnline()) {
                this.reflection.sendPacket(target, destroyPacket);
            }
        }, duration);
    }

    @Override
    public int getNextFakeEntityId() {
        return FAKE_ENTITY_ID_COUNTER--;
    }
}