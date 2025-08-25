package dev.revere.alley.common;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.common.reflect.ReflectionService;
import dev.revere.alley.common.reflect.internal.types.DefaultReflectionImpl;
import dev.revere.alley.core.profile.Profile;
import dev.revere.alley.core.profile.ProfileService;
import dev.revere.alley.core.profile.enums.ProfileState;
import dev.revere.alley.feature.cosmetic.CosmeticService;
import dev.revere.alley.feature.cosmetic.internal.repository.SuitRepository;
import dev.revere.alley.feature.cosmetic.internal.repository.impl.suit.BaseSuit;
import dev.revere.alley.feature.cosmetic.model.CosmeticType;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author Emmy
 * @project Alley
 * @date 29/04/2024 - 18:53
 */
@UtilityClass
public class PlayerUtil {
    private final AlleyPlugin plugin = AlleyPlugin.getInstance();

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

        clearVisuals(player);

        if (inLobby(player)) {
            equipSelectedSuit(player);
        }

        player.updateInventory();

        if (closeInventory) {
            player.closeInventory();
        }
    }

    /**
     * Clears any visual effects on the player's model.
     *
     * @param player the player to clear visuals for.
     */
    private void clearVisuals(Player player) {
        DefaultReflectionImpl reflection = plugin.getService(ReflectionService.class).getReflectionService(DefaultReflectionImpl.class);
        reflection.getCraftPlayer(player).getHandle().getDataWatcher().watch(9, (byte) 0);
    }

    /**
     * Starts flying for the player if they have the required permission.
     *
     * @param player the player to start flying.
     */
    public boolean canFly(Player player) {
        return inLobby(player) && player.hasPermission("alley.donator.fly");
    }

    /**
     * Checks if the player is in the lobby or waiting state.
     *
     * @param player the player to check.
     * @return true if the player is in the lobby or waiting state, false otherwise.
     */
    public boolean inLobby(Player player) {
        Profile profile = plugin.getService(ProfileService.class).getProfile(player.getUniqueId());
        return profile != null && (profile.getState() == ProfileState.LOBBY || profile.getState() == ProfileState.WAITING);
    }

    /**
     * Equip the selected suit for the player if they have one selected.
     *
     * @param player the player to equip the suit for.
     */
    private void equipSelectedSuit(Player player) {
        ProfileService profileService = plugin.getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());
        if (profile == null) {
            return;
        }

        CosmeticService cosmeticService = plugin.getService(CosmeticService.class);
        if (cosmeticService == null) {
            return;
        }

        String selectedSuitName = profile.getProfileData().getCosmeticData().getSelectedSuit();
        if (selectedSuitName == null || selectedSuitName.equalsIgnoreCase("None")) {
            return;
        }

        SuitRepository suitRepository = cosmeticService.getRepository(CosmeticType.SUIT, SuitRepository.class);
        if (suitRepository == null) {
            return;
        }

        BaseSuit suit = suitRepository.getCosmetic(selectedSuitName);
        if (suit == null) {
            return;
        }

        suit.equip(player);
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