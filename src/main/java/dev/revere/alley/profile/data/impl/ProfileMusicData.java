package dev.revere.alley.profile.data.impl;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Emmy
 * @project alley-practice
 * @since 19/07/2025
 */
@Getter
@Setter
public class ProfileMusicData {
    private Set<String> selectedDiscs;

    public ProfileMusicData() {
        this.selectedDiscs = new HashSet<>();
    }

    /**
     * Adds a music disc to the selected discs set.
     *
     * @param disc The name of the music disc to add.
     */
    public void addDisc(String disc) {
        this.selectedDiscs.add(disc);
    }

    /**
     * Removes a music disc from the selected discs set.
     *
     * @param disc The name of the music disc to remove.
     */
    public void removeDisc(String disc) {
        this.selectedDiscs.remove(disc);
    }

    /**
     * Checks if a music disc is selected.
     *
     * @param disc The name of the music disc to check.
     * @return True if the disc is selected, false otherwise.
     */
    public boolean isDiscSelected(String disc) {
        return this.selectedDiscs.contains(disc);
    }
}