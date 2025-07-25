package dev.revere.alley.adapter.core.impl;

import dev.revere.alley.Alley;
import dev.revere.alley.api.constant.PluginConstant;
import dev.revere.alley.adapter.core.Core;
import dev.revere.alley.adapter.core.enums.CoreType;
import dev.revere.alley.util.chat.CC;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @since 26/04/2025
 */
public class DefaultCoreImpl implements Core {
    protected final Alley plugin;
    protected final String adminPermission;

    /**
     * Constructor for the DefaultCoreImpl class.
     *
     * @param plugin The Alley plugin instance.
     */
    public DefaultCoreImpl(Alley plugin) {
        this.plugin = plugin;
        this.adminPermission = Alley.getInstance().getService(PluginConstant.class).getAdminPermissionPrefix();
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