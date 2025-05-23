package dev.revere.alley.feature.arena.impl;

import dev.revere.alley.Alley;
import dev.revere.alley.feature.arena.AbstractArena;
import dev.revere.alley.feature.arena.enums.EnumArenaType;
import dev.revere.alley.game.match.impl.MatchRoundsImpl;
import dev.revere.alley.game.match.player.impl.MatchGamePlayerImpl;
import dev.revere.alley.game.match.player.participant.GameParticipant;
import dev.revere.alley.tool.serializer.Serializer;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * @author Emmy
 * @project Alley
 * @date 20/05/2024 - 19:14
 */
@Setter
@Getter
public class StandAloneArena extends AbstractArena {
    private boolean active = false;

    private Location team1Portal;
    private Location team2Portal;

    private int portalRadius;
    private int heightLimit;

    /**
     * Constructor for the StandAloneArena class.
     *
     * @param name    The name of the arena.
     * @param minimum The minimum location of the arena.
     * @param maximum The maximum location of the arena.
     */
    public StandAloneArena(String name, Location minimum, Location maximum, Location team1Portal, Location team2Portal, int heightLimit) {
        super(name, minimum, maximum);

        if (team1Portal != null) this.team1Portal = team1Portal;
        if (team2Portal != null) this.team2Portal = team2Portal;
        this.portalRadius = Alley.getInstance().getConfigService().getSettingsConfig().getInt("game.portal-radius");
        this.heightLimit = heightLimit;
    }

    @Override
    public EnumArenaType getType() {
        return EnumArenaType.STANDALONE;
    }

    @Override
    public void createArena() {
        Alley.getInstance().getArenaService().getArenas().add(this);
        this.saveArena();
    }

    @Override
    public void saveArena() {
        String name = "arenas." + this.getName();
        FileConfiguration config = Alley.getInstance().getConfigService().getConfig("storage/arenas.yml");

        config.set(name, null);
        config.set(name + ".type", this.getType().name());
        config.set(name + ".minimum", Serializer.serializeLocation(this.getMinimum()));
        config.set(name + ".maximum", Serializer.serializeLocation(this.getMaximum()));
        config.set(name + ".center", Serializer.serializeLocation(this.getCenter()));
        config.set(name + ".pos1", Serializer.serializeLocation(this.getPos1()));
        config.set(name + ".pos2", Serializer.serializeLocation(this.getPos2()));
        config.set(name + ".kits", this.getKits());
        config.set(name + ".enabled", this.isEnabled());
        config.set(name + ".display-name", this.getDisplayName());

        if (this.team1Portal != null) config.set(name + ".team-one-portal", Serializer.serializeLocation(this.team1Portal));
        if (this.team2Portal != null) config.set(name + ".team-two-portal", Serializer.serializeLocation(this.team2Portal));

        config.set(name + ".height-limit", this.heightLimit);

        Alley.getInstance().getConfigService().saveConfig(Alley.getInstance().getConfigService().getConfigFile("storage/arenas.yml"), config);
    }

    @Override
    public void deleteArena() {
        FileConfiguration config = Alley.getInstance().getConfigService().getConfig("storage/arenas.yml");
        config.set("arenas." + this.getName(), null);

        Alley.getInstance().getArenaService().getArenas().remove(this);
        Alley.getInstance().getConfigService().saveConfig(Alley.getInstance().getConfigService().getConfigFile("storage/arenas.yml"), config);
    }

    /**
     * Check if the player is in the enemy portal.
     *
     * @param match          The match.
     * @param playerLocation The location of the player.
     * @param playerTeam     The team of the player.
     * @return Whether the player is in the enemy portal or not.
     */
    public boolean isEnemyPortal(MatchRoundsImpl match, Location playerLocation, GameParticipant<MatchGamePlayerImpl> playerTeam) {
        Location enemyPortal = playerTeam == match.getParticipantA() ? this.team2Portal : this.team1Portal;
        return playerLocation.distance(enemyPortal) < this.portalRadius;
    }
}