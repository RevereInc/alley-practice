package dev.revere.alley.tool.reflection.impl;

import dev.revere.alley.Alley;
import dev.revere.alley.config.ConfigService;
import dev.revere.alley.profile.ProfileService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.tool.logger.Logger;
import dev.revere.alley.tool.reflection.Reflection;
import dev.revere.alley.util.chat.CC;
import dev.revere.alley.util.chat.Symbol;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author Emmy
 * @project Alley
 * @since 03/04/2025
 */
public class ActionBarReflectionServiceImpl implements Reflection {
    /**
     * Method to send an action bar message to a player in a specific interval.
     *
     * @param player          The player.
     * @param message         The message.
     * @param durationSeconds The duration to show the message (in seconds).
     */
    public void sendMessage(Player player, String message, int durationSeconds) {
        try {
            IChatBaseComponent chatBaseComponent = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + CC.translate(message) + "\"}");
            PacketPlayOutChat packet = new PacketPlayOutChat(chatBaseComponent, (byte) 2);
            this.sendPacket(player, packet);

            if (durationSeconds > 0) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        IChatBaseComponent clearChatBaseComponent = IChatBaseComponent.ChatSerializer.a("{\"text\": \"\"}");
                        PacketPlayOutChat clearPacket = new PacketPlayOutChat(clearChatBaseComponent, (byte) 2);
                        sendPacket(player, clearPacket);
                    }
                }.runTaskLater(Alley.getInstance(), durationSeconds * 20L);
            }
        } catch (Exception exception) {
            Logger.logException("An error occurred while trying to send an action bar message to " + player.getName(), exception);
        }
    }

    /**
     * Method to send an action bar message to a player.
     *
     * @param player  The player to send the message to.
     * @param message The message to send.
     */
    public void sendMessage(Player player, String message) {
        try {
            IChatBaseComponent chatBaseComponent = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + CC.translate(message) + "\"}");
            PacketPlayOutChat packet = new PacketPlayOutChat(chatBaseComponent, (byte) 2);
            this.sendPacket(player, packet);
        } catch (Exception exception) {
            Logger.logException("An error occurred while trying to send an action bar message to " + player.getName(), exception);
        }
    }

    /**
     * Sends a death message to the killer.
     *
     * @param killer The player who killed the victim.
     * @param victim The player who died.
     */
    public void sendDeathMessage(Player killer, Player victim) {
        Profile victimProfile = Alley.getInstance().getService(ProfileService.class).getProfile(victim.getUniqueId());
        this.sendMessage(killer, "&c&lKILL! &f" + victimProfile.getFancyName(), 3);
    }

    /**
     * Visualizes the target's health in the action bar for a player.
     *
     * @param player The player who will see the target's health.
     * @param target The player whose health will be visualized.
     */
    public void visualizeTargetHealth(Player player, Player target) {
        FileConfiguration config = Alley.getInstance().getService(ConfigService.class).getVisualsConfig();
        String path = "game.health-bar";

        String symbol = config.getString(path + ".symbol.appearance", Symbol.HEART);
        String fullColor = config.getString(path + ".symbol.color.full", "&a&l");
        String emptyColor = config.getString(path + ".symbol.color.empty", "&7&l");

        boolean roundUp = config.getBoolean(path + ".round-up-health", true);

        int maxHealth = (int) target.getMaxHealth() / 2;
        double rawHealth = target.getHealth() / 2;
        int currentHealth = roundUp ? (int) Math.ceil(rawHealth) : (int) Math.floor(rawHealth);

        StringBuilder healthBar = new StringBuilder();
        for (int i = 0; i < maxHealth; i++) {
            if (i < currentHealth) {
                healthBar.append(CC.translate(fullColor + symbol));
            } else {
                healthBar.append(CC.translate(emptyColor + symbol));
            }
        }

        ChatColor nameColor = Alley.getInstance().getService(ProfileService.class).getProfile(target.getUniqueId()).getNameColor();

        String template = config.getString(path + ".message-format", "&6{name-color}{target} &f{health-bar}");
        String message = CC.translate(
                template
                        .replace("{target}", target.getName())
                        .replace("{name-color}", nameColor.toString())
                        .replace("{health-bar}", healthBar.toString())
        );

        this.sendMessage(player, message);
    }
}