package dev.revere.alley.tool.reflection.impl;

import dev.revere.alley.Alley;
import dev.revere.alley.tool.reflection.Reflection;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.PacketPlayOutBlockBreakAnimation;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @since 25/04/2025
 */
public class BlockAnimationReflectionServiceImpl implements Reflection {
    /**
     * Animates a block being broken over time (0 → 9).
     *
     * @param block       The block to animate.
     * @param animationId A unique ID for this animation.
     * @param duration    Duration in ticks (e.g. 40 = 2 seconds).
     * @param players     List of players to send the animation to.
     */
    public void sendBreakAnimationSequence(List<Player> players, Block block, int animationId, int duration) {
        int maxStage = 9;
        int interval = duration / (maxStage + 1);

        new BukkitRunnable() {
            int stage = 0;

            @Override
            public void run() {
                if (this.stage > maxStage || block.getType().equals(Material.AIR)) {
                    this.cancel();
                    return;
                }

                players.forEach(
                        player -> sendBreakAnimation(player, block, animationId, this.stage)
                );

                this.stage++;
            }
        }.runTaskTimer(Alley.getInstance(), 0L, interval);
    }

    /**
     * Sends a block break animation to all online players.
     *
     * @param block       The block to animate.
     * @param animationId Unique animation ID.
     * @param stage       Break stage (0-9).
     */
    public void sendBreakAnimation(Player player, Block block, int animationId, int stage) {
        PacketPlayOutBlockBreakAnimation packet = new PacketPlayOutBlockBreakAnimation(
                animationId,
                new BlockPosition(block.getX(), block.getY(), block.getZ()),
                stage
        );

        this.sendPacket(player, packet);
    }
}