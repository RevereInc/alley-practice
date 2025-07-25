package dev.revere.alley.base.arena.schematic;

import dev.revere.alley.base.arena.Arena;
import dev.revere.alley.base.arena.impl.StandAloneArena;
import dev.revere.alley.plugin.lifecycle.Service;
import org.bukkit.Location;

import java.io.File;
import java.util.List;

/**
 * @author Remi
 * @project alley-practice
 * @date 2/07/2025
 */
public interface ArenaSchematicService extends Service {
    /**
     * Checks for missing schematic files for a given list of arenas and creates them.
     * This is intended to be called once on startup.
     * @param arenas The list of all loaded arenas to check.
     */
    void generateMissingSchematics(List<Arena> arenas);

    /**
     * Saves the schematic of a given arena to a file.
     *
     * @param arena         The arena to save.
     * @param schematicFile The file to save the schematic to.
     */
    void save(Arena arena, File schematicFile);

    /**
     * Updates the schematic file for a given arena by re-saving it.
     *
     * @param arena The arena whose schematic needs updating.
     */
    void updateSchematic(Arena arena);

    /**
     * Pastes a schematic into the world at a specific location.
     *
     * @param location      The location to paste the schematic.
     * @param schematicFile The file containing the schematic data.
     */
    void paste(Location location, File schematicFile);

    /**
     * Deletes the physical blocks of a temporary arena from the world.
     *
     * @param arena The temporary StandAloneArena to delete.
     */
    void delete(StandAloneArena arena);

    /**
     * Gets the schematic file for an arena by its name.
     *
     * @param name The name of the schematic (typically the arena name).
     * @return The File object pointing to the schematic.
     */
    File getSchematicFile(String name);

    /**
     * Gets the schematic file for a given arena instance.
     *
     * @param arena The arena.
     * @return The File object pointing to the schematic.
     */
    File getSchematicFile(Arena arena);
}