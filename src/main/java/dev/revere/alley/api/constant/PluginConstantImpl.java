package dev.revere.alley.api.constant;

import dev.revere.alley.Alley;
import dev.revere.alley.plugin.AlleyContext;
import dev.revere.alley.plugin.annotation.Service;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginDescriptionFile;
import org.reflections.Reflections;

import java.util.List;

/**
 * This class holds constants related to a plugin.
 * It allows for easy access to specific plugin constants and configurations.
 *
 * @author Emmy
 * @project Alley
 * @since 03/04/2025
 */
@Getter
@Service(provides = PluginConstant.class, priority = 0)
public class PluginConstantImpl implements PluginConstant {
    private final Alley plugin;

    private String name;
    private String version;
    private String description;
    private List<String> authors;
    private String spigotVersion;
    private ChatColor mainColor;
    private String packageDirectory;
    private String adminPermissionPrefix;
    private String permissionLackMessage;
    private Reflections reflections;

    /**
     * Constructor for the PluginConstant class.
     *
     * @param plugin The Alley plugin instance.
     */
    public PluginConstantImpl(Alley plugin) {
        this.plugin = plugin;
    }

    @Override
    public void initialize(AlleyContext context) {
        PluginDescriptionFile pluginDescription = plugin.getDescription();

        this.name = pluginDescription.getName();
        this.version = pluginDescription.getVersion();
        this.authors = pluginDescription.getAuthors();
        this.description = pluginDescription.getDescription();
        this.spigotVersion = this.getBukkitVersionExact();

        this.mainColor = ChatColor.GOLD;
        this.packageDirectory = "dev.revere.alley";

        this.adminPermissionPrefix = this.name + ".admin";
        this.permissionLackMessage = ChatColor.RED + "No permission.";

        this.reflections = new Reflections(this.packageDirectory);
    }

    /**
     * Gets the exact Bukkit version of the server (e.g., "1.8.8").
     * @return The exact Bukkit version string.
     */
    private String getBukkitVersionExact() {
        String serverVersion = this.plugin.getServer().getVersion();
        int mcIndex = serverVersion.indexOf("MC: ");
        if (mcIndex != -1) {
            int endIndex = serverVersion.indexOf(")", mcIndex);
            if (endIndex != -1) {
                return serverVersion.substring(mcIndex + 4, endIndex);
            }
        }
        return "Unknown";
    }
}