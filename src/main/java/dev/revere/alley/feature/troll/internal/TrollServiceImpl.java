package dev.revere.alley.feature.troll.internal;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.bootstrap.AlleyContext;
import dev.revere.alley.bootstrap.annotation.Service;
import dev.revere.alley.common.reflect.Reflection;
import dev.revere.alley.common.reflect.internal.types.DefaultReflectionImpl;
import dev.revere.alley.common.text.CC;
import dev.revere.alley.feature.troll.TrollService;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.EntityBoat;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutGameStateChange;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntity;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.material.Directional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Emmy
 * @project alley-practice
 * @since 01/09/2025
 */
@Getter
@Service(provides = TrollService.class, priority = 500)
public class TrollServiceImpl implements TrollService {
    private static int FAKE_ENTITY_ID_COUNTER = Integer.MAX_VALUE - 1_100_100;
    private final MonkeyRegistry monkeyRegistry = new MonkeyRegistry();

    private final AlleyPlugin plugin;
    private Reflection reflection;

    /**
     * DI Constructor for the TrollServiceImpl class.
     *
     * @param plugin The AlleyPlugin instance.
     */
    public TrollServiceImpl(AlleyPlugin plugin) {
        this.plugin = plugin;
    }

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

        final int MAIN_SEGMENTS = 250;
        final int TUBE_SEGMENTS = 250;

        final double DONUT_RADIUS = 2.5;
        final double TUBE_RADIUS = 0.5;

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
            this.plugin.getServer().getScheduler().runTaskLater(this.plugin, () -> {
                for (Location point : trappedLocations) {
                    if (point.getBlock().getType() == material) {
                        point.getBlock().setType(Material.AIR);
                    }
                }
            }, ticks);
        }
    }

    @Override
    public void trapMonkey(Player trapper, Player target) {
        Location location = trapper.getLocation();
        target.teleport(location);

        Material material = Material.IRON_FENCE;

        int radius = 1;
        int height = 3;

        List<Location> cageLocations = this.generateCube(location, radius, height);
        for (Location blockLocation : cageLocations) {
            if (blockLocation.getBlock().getType() == Material.AIR) {
                blockLocation.getBlock().setType(material);
            }
        }

        this.setTopAndBottom(location, material);

        Location signLocation = location.clone().add(0, 3, radius + 1);
        signLocation.getBlock().setType(Material.WALL_SIGN);

        this.plugin.getServer().getScheduler().runTaskLater(this.plugin, () -> {
            BlockState blockState = signLocation.getBlock().getState();
            this.customizeMonkeySign(trapper, target, blockState, signLocation);
        }, 1L);

        trapper.teleport(trapper.getLocation().add(6, 0, 0));
        this.broadcastMonkeyCapturedMessage(trapper, target);
    }

    /**
     * Customizes the sign placed above the monkey cage with specific text and orientation.
     *
     * @param trapper      The player who set the trap.
     * @param target       The player who is trapped.
     * @param blockState   The BlockState of the sign to be customized.
     * @param signLocation The location of the sign.
     */
    private void customizeMonkeySign(Player trapper, Player target, BlockState blockState, Location signLocation) {
        if (blockState instanceof Sign) {
            Sign sign = (Sign) blockState;

            BlockFace facing = trapper.getLocation().getBlock().getFace(signLocation.getBlock());
            ((Directional) sign.getData()).setFacingDirection(facing != null ? facing : BlockFace.SOUTH);

            sign.setLine(0, "------------------");
            sign.setLine(1, CC.translate("&4&lMONKEY ALERT"));
            sign.setLine(2, CC.translate(target.getName()));
            sign.setLine(3, "------------------");
            sign.update();
        }
    }

    /**
     * Broadcasts a message to all online players indicating that a monkey has been captured.
     *
     * @param trapper The player who captured the monkey.
     * @param target  The player who was captured.
     */
    private void broadcastMonkeyCapturedMessage(Player trapper, Player target) {
        Arrays.asList(
                "",
                "&6&lMonkey Captured!",
                "",
                " &6│ &fTarget: &e" + target.getName(),
                " &6│ &fCaptured By: &e" + trapper.getName(),
                "",
                " &7Reason to this is because",
                " &7we've detected them to be",
                " &7a monkey, and monkeys belong in zoos.",
                ""
        ).forEach(line -> this.plugin.getServer().getOnlinePlayers().forEach(onlinePlayer -> onlinePlayer.sendMessage(CC.translate(line))));
    }

    /**
     * Sets the top and bottom layers of the cage to a different material for added security.
     *
     * @param location The center location of the cage.
     * @param material The material used for the cage walls.
     */
    private void setTopAndBottom(Location location, Material material) {
        int radius = 1;

        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                Location blockLocation = location.clone().add(x, 3, z);
                if (blockLocation.getBlock().getType() == material) {
                    blockLocation.getBlock().setType(Material.IRON_BLOCK);
                }
            }
        }

        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                Location blockLocation = location.clone().add(x, -1, z);
                if (blockLocation.getBlock().getType() == material) {
                    blockLocation.getBlock().setType(Material.IRON_BLOCK);
                }
            }
        }
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

        this.plugin.getServer().getScheduler().runTaskLater(this.plugin, () -> {
            if (target.isOnline()) {
                this.reflection.sendPacket(target, destroyPacket);
            }
        }, duration);
    }

    @Override
    public int getNextFakeEntityId() {
        return FAKE_ENTITY_ID_COUNTER--;
    }

    @Getter
    public static class MonkeyRegistry {
        private final List<String> monkeys = new ArrayList<>();

        public MonkeyRegistry() {
            this.monkeys.addAll(Arrays.asList(
                    // Tier 1 monkey
                    "7ee45f81-0efe-43fa-af9d-1376f7ae217c", // Insurant
                    "74e89738-6c9e-4f59-83ef-d365849e6049", // ziue
                    "9cc24825-2856-464d-b5c0-c1bb8612d831", // yczu
                    "59abc548-0beb-498e-9e33-37407ab8ecbd", // subclasses
                    "a7809777-e88a-4f21-a59d-738543d7204a", // dischargers
                    "8d68a09a-0e94-41bc-8441-d84010240b52", // adducer
                    "57e3fa53-de35-4e49-9d63-3c2c83afe6ae", // Residentiary
                    "2ac73c29-aaf5-49f9-b9eb-d846e599f931", // terned
                    "585f6c28-45cd-4259-bff5-77eaf5455d1b", // overfeel
                    "1df2ed62-e7be-4357-8a29-83e866b93ba8", // schillings
                    "20e4f919-05b9-4feb-bdf1-fc6e29ca7b5f", // miscounts
                    "2bc6b7fc-5c11-403d-a832-e9306997b563", // skirting
                    "8aa90679-8266-40ba-964e-adcfeafd12c4", // Pavonia

                    // Tier 2 monkey
                    "a5c88983-d880-4fe9-a59b-1f31f2b1d9cb", // kickrocks19
                    "31126861-ed38-43e0-b7df-9bcc25800427"  // pipework
            ));
        }
    }
}