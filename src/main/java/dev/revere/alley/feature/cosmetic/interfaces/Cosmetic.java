package dev.revere.alley.feature.cosmetic.interfaces;

import org.bukkit.Material;

/**
 * @author Remi
 * @project Alley
 * @date 6/1/2024
 */
public interface Cosmetic {
    String getName();

    String getDescription();

    String getPermission();

    Material getIcon();

    int getPrice();

    int getSlot();
}