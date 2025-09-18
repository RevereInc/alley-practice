package dev.revere.alley.visual.tablist.task;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.common.logger.Logger;
import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.locale.LocaleService;
import dev.revere.alley.core.locale.internal.impl.VisualsLocaleImpl;
import dev.revere.alley.core.profile.ProfileService;
import dev.revere.alley.visual.tablist.TablistAdapter;
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
public class TablistImpl implements TablistAdapter {
    protected final AlleyPlugin plugin;

    /**
     * Constructor for the TablistVisualizer class.
     *
     * @param plugin The Alley bootstrap instance.
     */
    public TablistImpl(AlleyPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> getHeader(Player player) {
        return this.plugin.getService(LocaleService.class).getMessageList(VisualsLocaleImpl.TAB_LIST_HEADER);
    }

    @Override
    public List<String> getFooter(Player player) {
        return this.plugin.getService(LocaleService.class).getMessageList(VisualsLocaleImpl.TAB_LIST_FOOTER);
    }

    @Override
    public void update(Player player) {
        if (AlleyPlugin.getInstance().getService(ProfileService.class).getProfile(player.getUniqueId()).getProfileData().getSettingData().isTablistEnabled()) {
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