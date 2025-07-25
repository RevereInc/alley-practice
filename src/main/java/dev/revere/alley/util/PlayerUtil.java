package dev.revere.alley.util;

import dev.revere.alley.Alley;
import dev.revere.alley.profile.ProfileService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.enums.ProfileState;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author Emmy
 * @project Alley
 * @date 29/04/2024 - 18:53
 */
@UtilityClass
public class PlayerUtil {
    /**
     * Reset a player's state to default values.
     *
     * @param player         the player to reset.
     * @param closeInventory whether to close the player's inventory after resetting.
     */
    public void reset(Player player, boolean closeInventory, boolean resetHealth) {
        if (resetHealth) {
            player.setMaxHealth(20.0D);
            player.setHealth(player.getMaxHealth());
        }
        player.setSaturation(5.0F);
        player.setFallDistance(0.0F);
        player.setFoodLevel(20);
        player.setFireTicks(0);
        player.setMaximumNoDamageTicks(20);

        if (canFly(player)) {
            player.setAllowFlight(true);
            player.setFlying(true);
        } else {
            player.setAllowFlight(false);
            player.setFlying(false);
        }

        player.setExp(0.0F);
        player.setLevel(0);

        player.setGameMode(GameMode.SURVIVAL);

        player.getInventory().setArmorContents(new ItemStack[4]);
        player.getInventory().setContents(new ItemStack[36]);

        player.getActivePotionEffects().forEach(effect -> player.removePotionEffect(effect.getType()));
        player.updateInventory();

        // Clears visuals from the player's model
        ((CraftPlayer) player).getHandle().getDataWatcher().watch(9, (byte) 0);

        if (closeInventory) {
            player.closeInventory();
        }
    }

    /**
     * Starts flying for the player if they have the required permission.
     *
     * @param player the player to start flying.
     */
    public boolean canFly(Player player) {
        Profile profile = Alley.getInstance().getService(ProfileService.class).getProfile(player.getUniqueId());
        return (profile.getState() == ProfileState.LOBBY || profile.getState() == ProfileState.WAITING) && player.hasPermission("alley.donator.fly");
    }

    public static void decrement(Player player) {
        ItemStack itemStack = player.getItemInHand();
        if (itemStack.getAmount() <= 1) player.setItemInHand(new ItemStack(Material.AIR, 1));
        else itemStack.setAmount(itemStack.getAmount() - 1);
        player.updateInventory();
    }

    /**
     * Get an offline player by their name
     *
     * @param name the name of the player
     * @return the offline player
     */
    public OfflinePlayer getOfflinePlayerByName(String name) {
        for (OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
            if (offlinePlayer.getName().equalsIgnoreCase(name)) {
                return offlinePlayer;
            }
        }
        return null;
    }
}