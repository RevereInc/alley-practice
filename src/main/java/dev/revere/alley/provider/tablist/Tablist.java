package dev.revere.alley.provider.tablist;

import org.bukkit.entity.Player;

import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @date 07/09/2024 - 15:17
 */
public interface Tablist {

    List<String> getHeader(Player player);

    List<String> getFooter(Player player);

    void update(Player player);
}