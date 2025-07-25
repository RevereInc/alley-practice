package dev.revere.alley.base.nametag;

import dev.revere.alley.tool.logger.Logger;
import dev.revere.alley.tool.reflection.Reflection;
import dev.revere.alley.tool.reflection.impl.instance.DefaultReflectionImpl;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardTeam;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author Remi
 * @project alley-practice
 * @date 27/06/2025
 */
@Getter
public class NametagAdapter {
    private final NametagServiceImpl engine;
    private final String name;
    private final String prefix;
    private final String suffix;
    private final NametagVisibility visibility;
    private final Reflection reflection = DefaultReflectionImpl.INSTANCE;

    public NametagAdapter(NametagServiceImpl engine, String name, String prefix, String suffix, NametagVisibility visibility) {
        this.engine = engine;
        this.name = name;
        this.prefix = prefix;
        this.suffix = suffix;
        this.visibility = visibility;
    }

    /**
     * Checks if this adapter represents the same style as a NametagView.
     *
     * @param view The view to compare against.
     * @return True if the prefix and suffix match.
     */
    public boolean represents(NametagView view) {
        return this.prefix.equals(view.getPrefix()) && this.suffix.equals(view.getSuffix());
    }

    /**
     * Sends the team creation packet to a specific player.
     *
     * @param player The player to send the packet to.
     */
    public void sendCreationPacket(Player player) {
        createPacket(0).sendToPlayer(player);
    }

    /**
     * Adds a player to this team for a specific viewer.
     *
     * @param player The player to add to the team.
     * @param viewer The player who needs to see this change.
     */
    public void addPlayer(Player player, Player viewer) {
        createPacket(3, player.getName()).sendToPlayer(viewer);
    }

    /**
     * Removes a player from this team for a specific viewer.
     *
     * @param player The player to remove from the team.
     * @param viewer The player who needs to see this change.
     */
    public void removePlayer(Player player, Player viewer) {
        createPacket(4, player.getName()).sendToPlayer(viewer);
    }

    @SuppressWarnings("unchecked")
    private PacketWrapper createPacket(int mode, String... players) {
        PacketPlayOutScoreboardTeam packet = new PacketPlayOutScoreboardTeam();
        try {
            reflection.getField(PacketPlayOutScoreboardTeam.class, "a").set(packet, name);
            reflection.getField(PacketPlayOutScoreboardTeam.class, "h").set(packet, mode);
            if (mode == 0 || mode == 2) {
                reflection.getField(PacketPlayOutScoreboardTeam.class, "b").set(packet, name);
                reflection.getField(PacketPlayOutScoreboardTeam.class, "c").set(packet, prefix);
                reflection.getField(PacketPlayOutScoreboardTeam.class, "d").set(packet, suffix);
                reflection.getField(PacketPlayOutScoreboardTeam.class, "i").set(packet, 1);
                reflection.getField(PacketPlayOutScoreboardTeam.class, "e").set(packet, visibility.getValue());
            }
            if (mode == 0 || mode == 3 || mode == 4) {
                ((Collection<String>) reflection.getField(PacketPlayOutScoreboardTeam.class, "g").get(packet)).addAll(Arrays.asList(players));
            }
        } catch (Exception e) {
            Logger.logException("Failed to create nametag packet", e);
        }
        return new PacketWrapper(packet, reflection);
    }

    /**
     * A simple, private static wrapper to make sending packets cleaner.
     */
    private static final class PacketWrapper {
        private final PacketPlayOutScoreboardTeam packet;
        private final Reflection reflectionService;

        public PacketWrapper(PacketPlayOutScoreboardTeam packet, Reflection reflectionService) {
            this.packet = packet;
            this.reflectionService = reflectionService;
        }

        void sendToPlayer(Player player) {
            if (player != null && player.isOnline()) {
                reflectionService.sendPacket(player, packet);
            }
        }
    }
}