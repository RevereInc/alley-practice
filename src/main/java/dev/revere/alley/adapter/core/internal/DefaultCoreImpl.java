package dev.revere.alley.adapter.core.internal;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.adapter.core.Core;
import dev.revere.alley.adapter.core.CoreType;
import dev.revere.alley.common.constants.PluginConstant;
import dev.revere.alley.common.text.CC;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @since 26/04/2025
 */
public class DefaultCoreImpl implements Core {
    protected final AlleyPlugin plugin;
    protected final String adminPermission;

    /**
     * Constructor for the DefaultCoreImpl class.
     *
     * @param plugin The Alley plugin instance.
     */
    public DefaultCoreImpl(AlleyPlugin plugin) {
        this.plugin = plugin;
        this.adminPermission = AlleyPlugin.getInstance().getService(PluginConstant.class).getAdminPermissionPrefix();
    }

    @Override
    public CoreType getType() {
        return CoreType.DEFAULT;
    }

    @Override
    public ChatColor getPlayerColor(Player player) {
        if (player == null) {
            return ChatColor.WHITE;
        }

        if (player.isOp()) {
            return ChatColor.DARK_RED;
        } else if (player.hasPermission(this.adminPermission)) {
            return ChatColor.RED;
        }

        return ChatColor.GREEN;
    }

    @Override
    public String getRankPrefix(Player player) {
        if (player == null) {
            return CC.translate("&a");
        }

        if (player.isOp()) {
            return CC.translate("&7[&4&oOwner&7] &4");
        } else if (player.hasPermission(this.adminPermission)) {
            return CC.translate("&7[&c&oAdmin&7] &c");
        }

        return CC.translate("&a");
    }

    @Override
    public String getRankName(Player player) {
        if (player == null) {
            return "Default";
        }

        if (player.isOp()) {
            return "Owner";
        } else if (player.hasPermission(this.adminPermission)) {
            return "Admin";
        }

        return "Default";
    }

    @Override
    public String getRankSuffix(Player player) {
        return "";
    }

    @Override
    public ChatColor getRankColor(Player player) {
        if (player == null) {
            return ChatColor.WHITE;
        }

        if (player.isOp()) {
            return ChatColor.DARK_RED;
        } else if (player.hasPermission(this.adminPermission)) {
            return ChatColor.RED;
        }

        return ChatColor.GREEN;
    }

    @Override
    public String getTagPrefix(Player player) {
        return "";
    }

    @Override
    public ChatColor getTagColor(Player player) {
        return ChatColor.WHITE;
    }
}