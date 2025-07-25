package dev.revere.alley.feature.explosives.listener;

import dev.revere.alley.Alley;
import dev.revere.alley.base.cooldown.Cooldown;
import dev.revere.alley.base.cooldown.CooldownRepository;
import dev.revere.alley.base.cooldown.enums.CooldownType;
import dev.revere.alley.base.kit.setting.impl.mechanic.KitSettingExplosiveImpl;
import dev.revere.alley.feature.explosives.ExplosiveService;
import dev.revere.alley.game.match.Match;
import dev.revere.alley.profile.ProfileService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.enums.ProfileState;
import dev.revere.alley.util.chat.CC;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Remi
 * @project Alley
 * @since 24/06/2025
 */
public class ExplosiveListener implements Listener {
    private static final String PRACTICE_TNT_METADATA = "PRACTICE_TNT";

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        if (item == null) return;

        Material itemType = item.getType();
        Action action = event.getAction();

        if (itemType == Material.FIREBALL && (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)) {
            handleFireballUse(event);
        } else if (itemType == Material.TNT && action == Action.RIGHT_CLICK_BLOCK) {
            handleTntPlace(event);
        }
    }

    private void handleFireballUse(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        Profile profile = Alley.getInstance().getService(ProfileService.class).getProfile(player.getUniqueId());
        if (profile == null) return;
        if (profile.getState() != ProfileState.PLAYING) return;

        Match match = profile.getMatch();
        if (match == null) return;

        if (!match.getKit().isSettingEnabled(KitSettingExplosiveImpl.class)) return;

        ItemStack item = event.getItem();
        if (item == null || item.getType() != Material.FIREBALL) return;
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        event.setCancelled(true);

        CooldownType cooldownType = CooldownType.FIREBALL;
        CooldownRepository cooldownRepository = Alley.getInstance().getService(CooldownRepository.class);
        Optional<Cooldown> optionalCooldown = Optional.ofNullable(cooldownRepository.getCooldown(player.getUniqueId(), cooldownType));
        if (optionalCooldown.isPresent() && optionalCooldown.get().isActive()) {
            player.sendMessage(CC.translate("&cYou must wait " + optionalCooldown.get().remainingTimeInMinutes() + " &cbefore using another fireball."));
            return;
        }

        Cooldown cooldown = optionalCooldown.orElseGet(() -> {
            Cooldown newCooldown = new Cooldown(cooldownType, () -> player.sendMessage(CC.translate("&cYou can now use fireballs again.")));
            cooldownRepository.addCooldown(player.getUniqueId(), cooldownType, newCooldown);
            return newCooldown;
        });

        cooldown.resetCooldown();

        ExplosiveService explosiveService = Alley.getInstance().getService(ExplosiveService.class);
        Fireball fireball = player.launchProjectile(Fireball.class);
        fireball.setIsIncendiary(false);
        fireball.setYield(2.0F);
        fireball.setVelocity(player.getLocation().getDirection().normalize().multiply(explosiveService.getSpeed()));

        //SoundUtil.playCustomSound(player, Sound.GHAST_FIREBALL, 1.0f, 1.0f);

        if (player.getGameMode() == GameMode.CREATIVE) return;

        if (item.getAmount() > 1) {
            item.setAmount(item.getAmount() - 1);
        } else {
            player.setItemInHand(null);
        }
    }

    private void handleTntPlace(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block clickedBlock = event.getClickedBlock();
        if (clickedBlock == null) return;

        Profile profile = Alley.getInstance().getService(ProfileService.class).getProfile(player.getUniqueId());
        if (profile == null || profile.getState() != ProfileState.PLAYING) return;

        Match match = profile.getMatch();
        if (match == null || !match.getKit().isSettingEnabled(KitSettingExplosiveImpl.class)) return;

        event.setCancelled(true);

        if (player.getGameMode() != GameMode.CREATIVE) {
            ItemStack item = event.getItem();
            if (item.getAmount() > 1) {
                item.setAmount(item.getAmount() - 1);
            } else {
                player.getInventory().setItemInHand(null);
            }
            player.updateInventory();
        }

        Location tntLocation = clickedBlock.getRelative(event.getBlockFace()).getLocation().add(0.5, 0.0, 0.5);
        TNTPrimed tnt = (TNTPrimed) tntLocation.getWorld().spawnEntity(tntLocation, EntityType.PRIMED_TNT);

        tnt.setFuseTicks(Alley.getInstance().getService(ExplosiveService.class).getTntFuseTicks());
        tnt.setMetadata(PRACTICE_TNT_METADATA, new FixedMetadataValue(Alley.getInstance(), true));
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (!(event.getEntity() instanceof Fireball) || !(event.getEntity().getShooter() instanceof Player)) return;

        Fireball fireball = (Fireball) event.getEntity();
        Location explosionLocation = fireball.getLocation();

        pushNearbyTnt(explosionLocation, fireball);

        handleFireballExplosion(explosionLocation);
        applyPlayerKnockback(fireball, fireball.getLocation());
    }

    @EventHandler
    public void onExplosionPrime(ExplosionPrimeEvent event) {
        event.setCancelled(true);

        if (event.getEntityType() != EntityType.PRIMED_TNT || !event.getEntity().hasMetadata(PRACTICE_TNT_METADATA)) {
            return;
        }

        TNTPrimed tnt = (TNTPrimed) event.getEntity();
        Location explosionLocation = tnt.getLocation();

        handleCustomTntExplosion(tnt, explosionLocation);
    }

    @EventHandler
    public void onExplosionDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;

        EntityDamageEvent.DamageCause cause = event.getCause();
        if (cause != EntityDamageEvent.DamageCause.BLOCK_EXPLOSION && cause != EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) {
            return;
        }

        Player player = (Player) event.getEntity();
        Profile profile = Alley.getInstance().getService(ProfileService.class).getProfile(player.getUniqueId());

        if (profile != null && profile.getState() == ProfileState.PLAYING) {
            event.setDamage(0.0);
        }
    }

    /**
     * Applies configured knockback to all players within range of an explosion.
     * The knockback strength is scaled based on the player's distance from the explosion center.
     *
     * @param source            The entity causing the explosion (e.g., Fireball, TNTPrimed).
     * @param explosionLocation The center location of the explosion.
     */
    private void applyPlayerKnockback(Entity source, Location explosionLocation) {
        ExplosiveService explosiveService = Alley.getInstance().getService(ExplosiveService.class);
        double maxRange = explosiveService.getRange();
        double maxHorizontal = explosiveService.getHorizontal();
        double maxVertical = explosiveService.getVertical();

        source.getNearbyEntities(maxRange, maxRange, maxRange).forEach(entity -> {
            if (!(entity instanceof Player)) {
                return;
            }

            Player player = (Player) entity;
            Profile profile = Alley.getInstance().getService(ProfileService.class).getProfile(player.getUniqueId());
            if (profile == null || profile.getState() != ProfileState.PLAYING) {
                return;
            }

            double distance = player.getLocation().distance(explosionLocation);

            if (distance > maxRange) {
                return;
            }

            double falloff = (maxRange > 0) ? (1.0 - (distance / maxRange)) : 1.0;

            double scaledHorizontal = maxHorizontal * falloff;
            double scaledVertical = maxVertical * falloff;

            Vector knockbackDirection = player.getLocation().toVector().subtract(explosionLocation.toVector()).normalize();

            if (knockbackDirection.lengthSquared() == 0) {
                knockbackDirection.setY(1);
            }

            Vector finalVelocity = knockbackDirection.multiply(scaledHorizontal);
            finalVelocity.setY(scaledVertical);

            player.setVelocity(finalVelocity);
        });
    }

    /**
     * Pushes nearby primed TNTs away from a fireball's explosion.
     * The force of the push is inversely proportional to the distance from the explosion.
     *
     * @param explosionLocation The location of the fireball's explosion.
     */
    private void pushNearbyTnt(Location explosionLocation, Entity source) {
        double maxPushStrength = 1;
        double pushRadius = 6.0;

        for (Entity entity : explosionLocation.getWorld().getNearbyEntities(explosionLocation, pushRadius, pushRadius, pushRadius)) {
            if (entity.getUniqueId().equals(source.getUniqueId())) {
                continue;
            }

            if (entity instanceof TNTPrimed && entity.hasMetadata(PRACTICE_TNT_METADATA)) {
                TNTPrimed tnt = (TNTPrimed) entity;

                double distance = tnt.getLocation().distance(explosionLocation);

                if (distance < 0.01) {
                    continue;
                }

                double pushForce = maxPushStrength * (1 - (distance / pushRadius));

                if (pushForce <= 0) {
                    continue;
                }

                Vector pushDirection = tnt.getLocation().toVector().subtract(explosionLocation.toVector()).normalize();

                Vector finalVelocity = tnt.getVelocity().add(pushDirection.multiply(pushForce));
                tnt.setVelocity(finalVelocity);
            }
        }
    }

    /**
     * Handles the logic for a fireball explosion.
     * <p>
     * This explosion operates in a spherical radius of 3 blocks and specifically
     * targets and destroys only WOOL blocks. It also creates a cosmetic explosion
     * for sound and particle effects.
     *
     * @param explosionLocation The central Location where the fireball explosion occurs.
     */
    private void handleFireballExplosion(Location explosionLocation) {
        ExplosiveService explosiveService = Alley.getInstance().getService(ExplosiveService.class);
        double range = explosiveService.getExplosionRange();

        List<Block> blocksToBreak = new ArrayList<>();
        int radius = (int) Math.ceil(range);

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    Location blockLoc = explosionLocation.clone().add(x, y, z);
                    if (blockLoc.distance(explosionLocation) <= radius) {
                        Block block = blockLoc.getBlock();
                        if (block.getType() == Material.WOOL) {
                            blocksToBreak.add(block);
                        }
                    }
                }
            }
        }

        for (Block block : blocksToBreak) {
            block.setType(Material.AIR);
        }

        explosionLocation.getWorld().createExplosion(explosionLocation, 0F, false);
    }

    /**
     * Handles the logic for a custom TNT explosion.
     * <p>
     * This explosion operates in a spherical radius and specifically targets
     * and destroys only WOOD and ENDER_STONE blocks. It then creates a
     * purely cosmetic (zero-power) explosion for sounds and particle effects
     * and applies knockback to nearby players.
     *
     * @param tnt               The TNTPrimed entity that is exploding. This is used as a reference for applying player knockback.
     * @param explosionLocation The central Location where the explosion occurs.
     */
    private void handleCustomTntExplosion(TNTPrimed tnt, Location explosionLocation) {
        ExplosiveService explosiveService = Alley.getInstance().getService(ExplosiveService.class);
        double range = explosiveService.getExplosionRange();

        List<Block> blocksToBreak = new ArrayList<>();
        int radius = (int) Math.ceil(range);

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    Location blockLoc = explosionLocation.clone().add(x, y, z);
                    if (blockLoc.distance(explosionLocation) <= range) {
                        Block block = blockLoc.getBlock();
                        Material type = block.getType();

                        if (type == Material.WOOD || type == Material.ENDER_STONE || type == Material.WOOL) {
                            blocksToBreak.add(block);
                        }
                    }
                }
            }
        }

        for (Block block : blocksToBreak) {
            block.setType(Material.AIR);
        }

        explosionLocation.getWorld().createExplosion(explosionLocation, 0F, false);

        pushNearbyTnt(explosionLocation, tnt);

        applyPlayerKnockback(tnt, explosionLocation);
    }
}