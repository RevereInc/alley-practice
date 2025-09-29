package dev.revere.alley.common;

import dev.revere.alley.AlleyPlugin;
import lombok.experimental.UtilityClass;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @since 08/02/2025
 */
@UtilityClass
public class ListenerUtil {
    /**
     * After 5 seconds, clears the dropped items on death via a BukkitRunnable.
     *
     * @param event      The event.
     * @param deadPlayer The dead player.
     */
    public void clearDroppedItemsOnDeath(PlayerDeathEvent event, Player deadPlayer) {
        List<Item> droppedItems = new ArrayList<>();
        for (ItemStack drop : event.getDrops()) {
            if (drop != null && drop.getType() != Material.AIR) {
                droppedItems.add(deadPlayer.getWorld().dropItemNaturally(deadPlayer.getLocation(), drop));
            }
        }
        event.getDrops().clear();

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Item item : droppedItems) {
                    if (item != null && item.isValid()) {
                        item.remove();
                    }
                }
            }
        }.runTaskLater(AlleyPlugin.getInstance(), 100L);
    }

    /**
     * After 5 seconds, clears the dropped items on regular item drop via a BukkitRunnable.
     *
     * @param item The dropped item.
     */
    public void clearDroppedItemsOnRegularItemDrop(Item item) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (item != null && item.isValid()) {
                    item.remove();
                }
            }
        }.runTaskLater(AlleyPlugin.getInstance(), 100L);
    }

    /**
     * Checks if the player is not stepping on a pressure plate.
     *
     * @param block The block you are standing on.
     * @return true if the player is stepping on a pressure plate, false otherwise.
     */
    public boolean notSteppingOnPlate(Block block) {
        if (block == null) {
            return false;
        }

        Material type = block.getType();
        return !pressurePlates.contains(type);
    }

    public boolean checkSteppingOnGoldPressurePlate(Block block) {
        if (block == null) {
            return false;
        }

        Material type = block.getType();
        return type == Material.GOLD_PLATE;
    }

    public boolean checkSteppingOnIronPressurePlate(Block block) {
        if (block == null) {
            return false;
        }

        Material type = block.getType();
        return type == Material.IRON_PLATE;
    }

    public void teleportAndClearSpawn(Player player, Location spawnLocation) {
        for (int i = 0; i <= 2; i++) {
            Block block = spawnLocation.clone().add(0, i, 0).getBlock();
            if (block.getType() != Material.AIR) {
                block.setType(Material.AIR);
            }
        }
        player.teleport(spawnLocation);
    }

    public void teleportAndClearSpawn(Player player, Location spawnLocation, boolean teleport) {
        for (int i = 0; i <= 2; i++) {
            Block block = spawnLocation.clone().add(0, i, 0).getBlock();
            if (block.getType() != Material.AIR) {
                block.setType(Material.AIR);
            }
        }

        if (teleport) {
            player.teleport(spawnLocation);
        }
    }

    /**
     * List of pressure plate materials.
     */
    private final List<Material> pressurePlates = Arrays.asList(
            Material.WOOD_PLATE,
            Material.STONE_PLATE,
            Material.IRON_PLATE,
            Material.GOLD_PLATE
    );

    /**
     * Checks if the material is a door or gate.
     *
     * @param material The material to check.
     * @return true if the material is a door or gate, false otherwise.
     */
    public boolean isInteractiveBlock(Material material) {
        return interactiveBlocks.contains(material);
    }

    /**
     * List of door and gate materials.
     */
    private final List<Material> interactiveBlocks = Arrays.asList(
            Material.WOODEN_DOOR,
            Material.SPRUCE_DOOR,
            Material.BIRCH_DOOR,
            Material.JUNGLE_DOOR,
            Material.ACACIA_DOOR,
            Material.DARK_OAK_DOOR,

            Material.FENCE_GATE,
            Material.SPRUCE_FENCE_GATE,
            Material.BIRCH_FENCE_GATE,
            Material.JUNGLE_FENCE_GATE,
            Material.ACACIA_FENCE_GATE,
            Material.DARK_OAK_FENCE_GATE,

            Material.TRAP_DOOR,
            Material.IRON_TRAPDOOR,

            Material.CHEST,
            Material.ENDER_CHEST,
            Material.TRAPPED_CHEST,

            Material.HOPPER,
            Material.HOPPER_MINECART
    );

    /**
     * Checks if the material is a bed fight protected block.
     *
     * @param material The material to check.
     * @return true if the material is a bed fight protected block, false otherwise.
     */
    public boolean isBedFightProtectedBlock(Material material) {
        return bedFightProtectedBlocks.contains(material);
    }

    /**
     * List of bed fight protected block materials.
     */
    private final List<Material> bedFightProtectedBlocks = Arrays.asList(
            Material.ENDER_STONE,
            Material.WOOD,
            Material.WOOL,
            Material.BED_BLOCK
    );

    /**
     * Checks if the material is a bed fight protected block.
     *
     * @param material The material to check.
     * @return true if the material is a bed fight protected block, false otherwise.
     */
    public boolean isSword(Material material) {
        return swords.contains(material);
    }

    /**
     * List of bed fight protected block materials.
     */
    private final List<Material> swords = Arrays.asList(
            Material.DIAMOND_SWORD,
            Material.GOLD_SWORD,
            Material.IRON_SWORD,
            Material.STONE_SWORD,
            Material.WOOD_SWORD
    );
}