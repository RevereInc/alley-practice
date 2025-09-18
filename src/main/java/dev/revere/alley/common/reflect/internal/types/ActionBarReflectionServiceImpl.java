package dev.revere.alley.common.reflect.internal.types;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.common.logger.Logger;
import dev.revere.alley.common.reflect.Reflection;
import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.locale.LocaleService;
import dev.revere.alley.core.locale.internal.impl.VisualsLocaleImpl;
import dev.revere.alley.core.profile.Profile;
import dev.revere.alley.core.profile.ProfileService;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
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
                }.runTaskLater(AlleyPlugin.getInstance(), durationSeconds * 20L);
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
        Profile victimProfile = AlleyPlugin.getInstance().getService(ProfileService.class).getProfile(victim.getUniqueId());
        this.sendMessage(killer, "&c&lKILL! &f" + victimProfile.getFancyName(), 3);
    }

    /**
     * Visualizes the target's health in the action bar for a player.
     *
     * @param player The player who will see the target's health.
     * @param target The player whose health will be visualized.
     */
    public void visualizeTargetHealth(Player player, Player target) {
        LocaleService localeService = AlleyPlugin.getInstance().getService(LocaleService.class);

        String message = localeService.getMessage(VisualsLocaleImpl.ACTIONBAR_HEALTH_INDICATOR_MESSAGE_FORMAT)
                .replace("{target}", target.getName())
                .replace("{name-color}", AlleyPlugin.getInstance().getService(ProfileService.class).getProfile(target.getUniqueId()).getNameColor().toString());

        String symbol = localeService.getMessage(VisualsLocaleImpl.ACTIONBAR_HEALTH_INDICATOR_SYMBOL_APPEARANCE);
        String fullColor = localeService.getMessage(VisualsLocaleImpl.ACTIONBAR_HEALTH_INDICATOR_SYMBOL_COLOR_FULL);
        String halfColor = localeService.getMessage(VisualsLocaleImpl.ACTIONBAR_HEALTH_INDICATOR_SYMBOL_COLOR_HALF);
        String emptyColor = localeService.getMessage(VisualsLocaleImpl.ACTIONBAR_HEALTH_INDICATOR_SYMBOL_COLOR_EMPTY);

        int maxHealth = (int) target.getMaxHealth() / 2;
        double rawHealth = target.getHealth() / 2;
        int currentHealth = (int) Math.ceil(rawHealth);

        StringBuilder healthBar = new StringBuilder();
        for (int i = 0; i < maxHealth; i++) {
            if (i < currentHealth) {
                healthBar.append(CC.translate(fullColor + symbol));
            } else if (i == currentHealth && rawHealth % 1 != 0) {
                healthBar.append(CC.translate(halfColor + symbol));
            } else {
                healthBar.append(CC.translate(emptyColor + symbol));
            }
        }

        String finalMessage = CC.translate(message.replace("{health-bar}", healthBar.toString()));
        this.sendMessage(player, finalMessage);
    }
}