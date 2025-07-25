package dev.revere.alley.database.profile.impl;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.model.UpdateOptions;
import dev.revere.alley.Alley;
import dev.revere.alley.database.MongoService;
import dev.revere.alley.database.profile.DatabaseProfile;
import dev.revere.alley.database.util.MongoUtility;
import dev.revere.alley.profile.ProfileService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.tool.date.DateFormatter;
import dev.revere.alley.tool.date.enums.DateFormat;
import org.bson.Document;

import java.util.UUID;

/**
 * @author Remi
 * @project Alley
 * @date 5/22/2024
 */
public class MongoProfileImpl implements DatabaseProfile {
    /**
     * Saves a profile to the database.
     *
     * @param profile The profile to save.
     */
    @Override
    public void saveProfile(Profile profile) {
        Document document = MongoUtility.toDocument(profile);
        ProfileService profileService = Alley.getInstance().getService(ProfileService.class);
        profileService.getCollection()
                .replaceOne(Filters.eq("uuid", profile.getUuid().toString()), document, new ReplaceOptions().upsert(true));
    }

    /**
     * Loads a profile from the database.
     *
     * @param profile The profile to load.
     */
    @Override
    public void loadProfile(Profile profile) {
        if (profile.getUuid() == null) return;
        ProfileService profileService = Alley.getInstance().getService(ProfileService.class);

        if (profileService.getCollection() == null) return;

        Document document = profileService.getCollection().find(Filters.eq("uuid", profile.getUuid().toString())).first();
        if (document == null) {
            this.saveProfile(profile);
            return;
        }

        MongoUtility.updateProfileFromDocument(profile, document);
    }

    /**
     * Archives a profile in the database.
     *
     * @param profile The profile to archive.
     */
    @Override
    public void archiveProfile(Profile profile) {
        Document archiveDocument = new Document();

        DateFormatter dateFormatter = new DateFormatter(DateFormat.DATE_PLUS_TIME, System.currentTimeMillis());
        String archiveId = UUID.randomUUID().toString();

        archiveDocument.put("archive_id", archiveId);
        archiveDocument.put("archived_at", dateFormatter.getDateFormat().format(dateFormatter.getDate()));
        archiveDocument.put("data", MongoUtility.toDocument(profile));

        MongoService mongoService = Alley.getInstance().getService(MongoService.class);
        mongoService.getMongoDatabase().getCollection("profile_archives").updateOne(
                new Document("uuid", profile.getUuid().toString()),
                new Document("$push", new Document("archives", archiveDocument)),
                new UpdateOptions().upsert(true)
        );
    }
}