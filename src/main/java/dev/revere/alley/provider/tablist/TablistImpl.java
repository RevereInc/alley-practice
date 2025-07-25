package dev.revere.alley.provider.tablist;

import dev.revere.alley.Alley;
import dev.revere.alley.config.ConfigService;
import dev.revere.alley.profile.ProfileService;
import dev.revere.alley.tool.logger.Logger;
import dev.revere.alley.util.chat.CC;
import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Emmy
 * @project Alley
 * @date 07/09/2024 - 15:16
 */
public class TablistImpl implements Tablist {
    protected final Alley plugin;

    /**
     * Constructor for the TablistVisualizer class.
     *
     * @param plugin The Alley plugin instance.
     */
    public TablistImpl(Alley plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> getHeader(Player player) {
        return Alley.getInstance().getService(ConfigService.class).getTabListConfig().getStringList("tablist.header");
    }

    @Override
    public List<String> getFooter(Player player) {
        return Alley.getInstance().getService(ConfigService.class).getTabListConfig().getStringList("tablist.footer");
    }

    @Override
    public void update(Player player) {
        if (Alley.getInstance().getService(ProfileService.class).getProfile(player.getUniqueId()).getProfileData().getSettingData().isTablistEnabled()) {
            List<String> headerLines = getHeader(player).stream()
                    .map(CC::translate)
                    .collect(Collectors.toList());

            List<String> footerLines = getFooter(player).stream()
                    .map(CC::translate)
                    .collect(Collectors.toList());

            String headerText = String.join("\n", headerLines);
            String footerText = String.join("\n", footerLines);

            PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
            try {
                Field headerField = packet.getClass().getDeclaredField("a");
                headerField.setAccessible(true);
                headerField.set(packet, new ChatComponentText(headerText));

                Field footerField = packet.getClass().getDeclaredField("b");
                footerField.setAccessible(true);
                footerField.set(packet, new ChatComponentText(footerText));

                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
            } catch (Exception e) {
                Logger.error("Failed to update tablist for " + player.getName());
            }
        } else {
            PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
            try {
                Field headerField = packet.getClass().getDeclaredField("a");
                headerField.setAccessible(true);
                headerField.set(packet, new ChatComponentText(""));

                Field footerField = packet.getClass().getDeclaredField("b");
                footerField.setAccessible(true);
                footerField.set(packet, new ChatComponentText(""));

                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
            } catch (Exception e) {
                Logger.error("Failed to update tablist for " + player.getName());
            }
        }
    }
}