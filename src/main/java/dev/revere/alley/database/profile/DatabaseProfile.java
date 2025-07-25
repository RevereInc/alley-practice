package dev.revere.alley.database.profile;

import dev.revere.alley.profile.Profile;

/**
 * @author Remi
 * @project Alley
 * @date 5/22/2024
 */
public interface DatabaseProfile {
    /**
     * Saves a profile to the database.
     *
     * @param profile The profile to save.
     */
    void saveProfile(Profile profile);

    /**
     * Loads a profile from the database.
     *
     * @param profile The profile to load.
     */
    void loadProfile(Profile profile);

    /**
     * Archives a profile in the database.
     *
     * @param profile The profile to archive.
     */
    void archiveProfile(Profile profile);
}
