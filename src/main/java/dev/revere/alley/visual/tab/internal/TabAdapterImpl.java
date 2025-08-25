package dev.revere.alley.visual.tab.internal;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.common.logger.Logger;
import dev.revere.alley.common.reflect.ReflectionService;
import dev.revere.alley.common.reflect.internal.types.DefaultReflectionImpl;
import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.config.ConfigService;
import dev.revere.alley.core.profile.ProfileService;
import dev.revere.alley.visual.tab.TabAdapter;
import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Emmy
 * @project Alley
 * @date 07/09/2024 - 15:16
 */
public class TabAdapterImpl implements TabAdapter {
    protected final AlleyPlugin plugin;

    /**
     * Constructor for the TabAdapterImpl class.
     *
     * @param plugin The Alley plugin instance.
     */
    public TabAdapterImpl(AlleyPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> getHeader(Player player) {
        return this.plugin.getService(ConfigService.class).getTabListConfig().getStringList("tablist.header");
    }

    @Override
    public List<String> getFooter(Player player) {
        return this.plugin.getService(ConfigService.class).getTabListConfig().getStringList("tablist.footer");
    }

    @Override
    public void update(Player player) {
        DefaultReflectionImpl reflection = this.plugin.getService(ReflectionService.class).getReflectionService(DefaultReflectionImpl.class);

        boolean enabled = this.plugin.getService(ProfileService.class).getProfile(player.getUniqueId()).getProfileData().getSettingData().isTablistEnabled();

        List<String> headerLines = enabled ? this.getHeader(player).stream().map(CC::translate).collect(Collectors.toList()) : Collections.singletonList("");
        List<String> footerLines = enabled ? this.getFooter(player).stream().map(CC::translate).collect(Collectors.toList()) : Collections.singletonList("");

        String headerText = String.join("\n", headerLines);
        String footerText = String.join("\n", footerLines);

        try {
            PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();

            Field headerField = reflection.getField(packet.getClass(), "a");
            Field footerField = reflection.getField(packet.getClass(), "b");

            reflection.setField(headerField, packet, new ChatComponentText(headerText));
            reflection.setField(footerField, packet, new ChatComponentText(footerText));

            reflection.sendPacket(player, packet);
        } catch (Exception exception) {
            Logger.logException("Failed to update tablist for " + player.getName(), exception);
        }
    }
}