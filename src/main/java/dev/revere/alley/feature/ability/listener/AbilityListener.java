package dev.revere.alley.feature.ability.listener;

import dev.revere.alley.Alley;
import dev.revere.alley.feature.ability.AbilityHandler;
import dev.revere.alley.feature.ability.AbilityService;
import dev.revere.alley.feature.ability.data.AbilityData;
import dev.revere.alley.tool.logger.Logger;
import jdk.jpackage.internal.Log;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * @author Remi
 * @project alley-practice
 * @date 13/06/2025
 */
public class AbilityListener implements Listener {
    private final AbilityData abilityData;
    private final AbilityHandler abilityHandler;

    public AbilityListener(AbilityData abilityData, AbilityHandler abilityHandler) {
        this.abilityData = abilityData;
        this.abilityHandler = abilityHandler;
    }

    public void register() {
        Bukkit.getPluginManager().registerEvents(this, Alley.getInstance());
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!isAbilityItem(event.getItem())) {
            return;
        }

        Player player = event.getPlayer();
        Action action = event.getAction();

        if ((action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) && abilityData.getLeftClickAction() != null) {
            handleAbilityUse(player, abilityData.getLeftClickAction(), event);
        }

        if ((action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) && abilityData.getRightClickAction() != null) {
            handleAbilityUse(player, abilityData.getRightClickAction(), event);
        }
    }

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        if (!(event.getEntity().getShooter() instanceof Player)) {
            return;
        }

        Player shooter = (Player) event.getEntity().getShooter();
        if (!isAbilityItem(shooter.getItemInHand())) {
            return;
        }

        if (abilityData.getProjectileHitAction() != null) {
            event.getEntity().setMetadata(abilityData.getKey(), new FixedMetadataValue(Alley.getInstance(), true));
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player target = (Player) event.getEntity();

        if (event.getDamager() instanceof Projectile) {
            Projectile projectile = (Projectile) event.getDamager();
            if (!(projectile.getShooter() instanceof Player)) {
                return;
            }

            Player shooter = (Player) projectile.getShooter();
            if (projectile.hasMetadata(abilityData.getKey()) && abilityData.getProjectileHitAction() != null) {
                handleAbilityHit(shooter, target, abilityData.getProjectileHitAction());
            }
        }

        if (event.getDamager() instanceof Player) {
            Player damager = (Player) event.getDamager();
            if (isAbilityItem(damager.getItemInHand()) && abilityData.getDamageAction() != null) {
                handleAbilityHit(damager, target, abilityData.getDamageAction());
            }
        }
    }

    private void handleAbilityUse(Player player, AbilityData.AbilityAction action, PlayerInteractEvent event) {
        if (!abilityData.isEnabled()) {
            return;
        }

        if (abilityHandler.hasCooldown(player, abilityData.getKey())) {
            String remainingTime = abilityHandler.getCooldownRemaining(player, abilityData.getKey());
            player.sendMessage("You cannot use this ability yet. Cooldown remaining: " + remainingTime);
            event.setCancelled(true);
            return;
        }

        if (abilityData.getCooldown() > 0) {
            abilityHandler.applyCooldown(player, abilityData.getKey(), abilityData.getCooldown() * 1000L);

            Bukkit.getScheduler().runTaskLater(Alley.getInstance(), () -> {
                player.sendMessage("Your ability cooldown has expired: " + abilityData.getDisplayName());
            }, abilityData.getCooldown() * 20L);
        }

        executeAbilityAction(player, null, action);

        player.sendMessage("You used the ability: " + abilityData.getDisplayName());
    }

    private void handleAbilityHit(Player player, Player target, AbilityData.AbilityAction action) {
        if (!abilityData.isEnabled()) {
            return;
        }

        executeAbilityAction(player, target, action);

        player.sendMessage("You hit " + (target != null ? target.getName() : "an entity") + " with the ability: " + abilityData.getDisplayName());
        if (target != null) {
            target.sendMessage("You were hit by " + player.getName() + " with the ability: " + abilityData.getDisplayName());
        }
    }

    private void executeAbilityAction(Player player, Player target, AbilityData.AbilityAction action) {
        switch (action.getType()) {
            case "DAMAGE": {
                if (target != null && action.getDamage() > 0) {
                    target.damage(action.getDamage(), player);
                }
                break;
            }
            case "SWITCH_POSITIONS": {
                if (target != null && action.isSwitchPositions()) {
                    Location playerLocation = player.getLocation().clone();
                    Location targetLocation = target.getLocation().clone();
                    player.teleport(targetLocation);
                    target.teleport(playerLocation);
                }
                break;
            }
            case "TELEPORT": {
                if (action.getTeleportDistance() > 0) {
                    Location targetLocation = target.getLocation();
                    Location playerLocation = player.getLocation();
                    if (playerLocation.distance(targetLocation) <= action.getTeleportDistance()) {
                        player.teleport(targetLocation);
                    }
                }
                break;
            }
            case "EFFECTS": {
                applyEffects(player, target, action);
                break;
            }
            case "COMMANDS": {
                executeCommands(player, target, action);
            }
        }
    }

    private void applyEffects(Player player, Player target, AbilityData.AbilityAction action) {
        for (String effectName : action.getEffects()) {
            try {
                PotionEffectType effectType = PotionEffectType.getByName(effectName);
                if (effectType != null) {
                    PotionEffect effect = effectType.createEffect(action.getEffectDuration(), action.getEffectAmplifier());
                    if (target != null) {
                        target.addPotionEffect(effect);
                    } else {
                        player.addPotionEffect(effect);
                    }
                }
            } catch (Exception e) {
                Logger.logException("Invalid potion effect type: " + effectName + " for ability: " + abilityData.getKey() + ". ", e);
            }
        }
    }

    private void executeCommands(Player player, Player target, AbilityData.AbilityAction action) {
        for (String command : action.getCommands()) {
            String processedCommand = command
                    .replace("{player}", player.getName())
                    .replace("{target}", target != null ? target.getName() : "")
                    .replace("{ability}", abilityData.getDisplayName());

            if (processedCommand.startsWith("CONSOLE")) {
                String consoleCommand = processedCommand.substring("CONSOLE ".length());
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), consoleCommand);
            } else {
                player.performCommand(processedCommand);
            }
        }
    }

    private boolean isAbilityItem(ItemStack item) {
        if (item == null || !item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) {
            return false;
        }

        return item.getItemMeta().getDisplayName().equalsIgnoreCase(abilityData.getDisplayName()) && item.getType() == abilityData.getMaterial();
    }
}
