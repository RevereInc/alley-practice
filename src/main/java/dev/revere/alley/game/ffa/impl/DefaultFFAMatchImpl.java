package dev.revere.alley.game.ffa.impl;

import dev.revere.alley.Alley;
import dev.revere.alley.feature.arena.AbstractArena;
import dev.revere.alley.feature.hotbar.enums.EnumHotbarType;
import dev.revere.alley.feature.kit.Kit;
import dev.revere.alley.game.ffa.AbstractFFAMatch;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.enums.EnumProfileState;
import dev.revere.alley.reflection.impl.ActionBarReflectionService;
import dev.revere.alley.util.PlayerUtil;
import dev.revere.alley.util.chat.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 5/27/2024
 */
public class DefaultFFAMatchImpl extends AbstractFFAMatch {
    /**
     * Constructor for the DefaultFFAMatchImpl class
     *
     * @param name       The name of the match
     * @param arena      The arena the match is being played in
     * @param kit        The kit the players are using
     * @param maxPlayers The maximum amount of players allowed in the match
     */
    public DefaultFFAMatchImpl(String name, AbstractArena arena, Kit kit, int maxPlayers) {
        super(name, arena, kit, maxPlayers);
    }

    /**
     * Join a player to the FFA match
     *
     * @param player The player
     */
    @Override
    public void join(Player player) {
        if (getPlayers().size() >= getMaxPlayers()) {
            player.sendMessage(CC.translate("&cThis FFA match is full. " + getMaxPlayers() + " players are already in the match."));
            return;
        }

        getPlayers().add(player);
        getPlayers().forEach(online -> online.sendMessage(CC.translate("&a" + player.getName() + " has joined the FFA match.")));
        setupPlayer(player);
    }

    /**
     * Leave a player from the FFA match
     *
     * @param player The player
     */
    @Override
    public void leave(Player player) {
        getPlayers().remove(player);
        getPlayers().forEach(online -> online.sendMessage(CC.translate("&c" + player.getName() + " has left the FFA match.")));

        player.sendMessage(CC.translate("&aYou have left the FFA match."));

        Profile profile = Alley.getInstance().getProfileService().getProfile(player.getUniqueId());
        profile.setState(EnumProfileState.LOBBY);
        profile.setFfaMatch(null);

        PlayerUtil.reset(player, false);
        Alley.getInstance().getSpawnService().teleportToSpawn(player);
        Alley.getInstance().getHotbarService().applyHotbarItems(player, EnumHotbarType.LOBBY);
    }

    /**
     * Setup a player for the FFA match
     *
     * @param player The player
     */
    @Override
    public void setupPlayer(Player player) {
        Profile profile = Alley.getInstance().getProfileService().getProfile(player.getUniqueId());
        profile.setState(EnumProfileState.FFA);
        profile.setFfaMatch(this);

        AbstractArena arena = getArena();
        player.teleport(arena.getPos1());

        Kit kit = getKit();
        player.getInventory().setArmorContents(kit.getArmor());
        player.getInventory().setContents(kit.getItems());
    }

    /**
     * Handle the respawn of a player
     *
     * @param player The player
     */
    public void handleRespawn(Player player) {
        Profile profile = Alley.getInstance().getProfileService().getProfile(player.getUniqueId());
        profile.setState(EnumProfileState.FFA);
        profile.setFfaMatch(this);

        AbstractArena arena = getArena();

        Bukkit.getScheduler().runTaskLater(Alley.getInstance(), () -> {
            player.teleport(arena.getPos1());

            Kit kit = getKit();
            player.getInventory().clear();
            player.getInventory().setArmorContents(kit.getArmor());
            player.getInventory().setContents(kit.getItems());
            player.updateInventory();
        }, 1L);
    }

    /**
     * Handle the death of a player
     *
     * @param player The player
     * @param killer The killer
     */
    @Override
    public void handleDeath(Player player, Player killer) {
        if (killer == null) {
            Profile profile = Alley.getInstance().getProfileService().getProfile(player.getUniqueId());
            profile.getProfileData().getFfaData().get(getKit().getName()).incrementDeaths();
            getPlayers().forEach(online -> online.sendMessage(CC.translate("&c" + player.getName() + " has died.")));
            handleRespawn(player);
            return;
        }

        Profile killerProfile = Alley.getInstance().getProfileService().getProfile(killer.getUniqueId());
        if (killerProfile.getProfileData().getFfaData().get(getKit().getName()) != null) {
            killerProfile.getProfileData().getFfaData().get(getKit().getName()).incrementKills();
        }

        Profile profile = Alley.getInstance().getProfileService().getProfile(player.getUniqueId());
        profile.getProfileData().getFfaData().get(getKit().getName()).incrementDeaths();

        Alley.getInstance().getReflectionRepository().getReflectionService(ActionBarReflectionService.class).sendDeathMessage(killer, player);
        Alley.getInstance().getCombatService().resetCombatLog(player);

        this.getPlayers().forEach(online -> online.sendMessage(CC.translate("&b" + player.getName() + " &ahas been killed by &b" + killer.getName() + "&a.")));
        //PlayerUtil.resetLastAttacker(player);
        this.handleRespawn(player);
    }
}