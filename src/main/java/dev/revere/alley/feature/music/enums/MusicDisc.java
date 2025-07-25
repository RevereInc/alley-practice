package dev.revere.alley.feature.music.enums;

import lombok.Getter;
import org.bukkit.Material;

/**
 * Represents a music disc in the game.
 * Each music disc has a material type, a title, and a unique ID.
 * This enum is used to manage and identify different music discs.
 *
 * @author Emmy
 * @project Alley
 * @date 27/10/2024 - 08:43
 */
@Getter
public enum MusicDisc {
    GOLD_RECORD(Material.GOLD_RECORD, "13", "records.13", 178),
    GREEN_RECORD(Material.GREEN_RECORD, "Cat", "records.cat", 185),
    RECORD_3(Material.RECORD_3, "Blocks", "records.blocks", 345),
    RECORD_4(Material.RECORD_4, "Chirp", "records.chirp", 185),
    RECORD_5(Material.RECORD_5, "Far", "records.far", 174),
    RECORD_6(Material.RECORD_6, "Mall", "records.mall", 197),
    RECORD_7(Material.RECORD_7, "Mellohi", "records.mellohi", 96),
    RECORD_8(Material.RECORD_8, "Stal", "records.stal", 150),
    RECORD_9(Material.RECORD_9, "Strad", "records.strad", 188),
    RECORD_10(Material.RECORD_10, "Ward", "records.ward", 251),
    RECORD_11(Material.RECORD_11, "11", "records.11", 71),
    RECORD_12(Material.RECORD_12, "Wait", "records.wait", 238);

    private final Material material;
    private final String title;
    private final String soundName;
    private final int duration;

    MusicDisc(Material material, String title, String soundName, int duration) {
        this.material = material;
        this.title = title;
        this.soundName = soundName;
        this.duration = duration;
    }
}