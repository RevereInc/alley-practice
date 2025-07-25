package dev.revere.alley.profile;

import com.mongodb.client.MongoCollection;
import dev.revere.alley.base.kit.Kit;
import dev.revere.alley.plugin.AlleyContext;
import dev.revere.alley.plugin.annotation.Service;
import dev.revere.alley.database.MongoService;
import dev.revere.alley.database.profile.DatabaseProfile;
import dev.revere.alley.database.profile.impl.MongoProfileImpl;
import dev.revere.alley.feature.layout.data.LayoutData;
import dev.revere.alley.profile.data.ProfileData;
import dev.revere.alley.tool.logger.Logger;
import dev.revere.alley.util.chat.CC;
import lombok.Getter;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * @author Remi
 * @project Alley
 * @date 5/20/2024
 */
@Getter
@Service(provides = ProfileService.class, priority = 180)
public class ProfileServiceImpl implements ProfileService {
    private final Map<UUID, Profile> profiles = new HashMap<>();
    private final MongoService mongoService;

    private MongoCollection<Document> collection;
    private DatabaseProfile databaseProfile;

    /**
     * Constructor for DI.
     */
    public ProfileServiceImpl(MongoService mongoService) {
        this.mongoService = mongoService;
    }

    @Override
    public void initialize(AlleyContext context) {
        this.collection = mongoService.getMongoDatabase().getCollection("profiles");
        this.databaseProfile = new MongoProfileImpl();
    }

    @Override
    public void shutdown(AlleyContext context) {
        Logger.info("Saving all loaded player profiles...");
        this.profiles.values().forEach(Profile::save);
        Logger.info("Profile saving complete.");
    }

    @Override
    public Profile getProfile(UUID uuid) {
        return this.profiles.computeIfAbsent(uuid, k -> {
            Profile profile = new Profile(k);
            profile.load();
            return profile;
        });
    }

    @Override
    public MongoCollection<Document> getCollection() {
        return collection;
    }

    @Override
    public Map<UUID, Profile> getProfiles() {
        return this.profiles;
    }

    @Override
    public void loadProfiles() {
        for (Document document : this.collection.find()) {
            UUID uuid = UUID.fromString(document.getString("uuid"));
            this.profiles.computeIfAbsent(uuid, k -> {
                Profile profile = new Profile(k);
                profile.load();
                return profile;
            });
        }
    }

    @Override
    public void addProfile(Profile profile) {
        this.profiles.put(profile.getUuid(), profile);
    }

    @Override
    public void resetStats(Player player, UUID target) {
        Profile profile = this.getProfile(target);
        OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(target);

        this.databaseProfile.archiveProfile(profile);

        profile.setProfileData(new ProfileData());
        profile.save();

        Arrays.asList(
                "", "&c&lSTAT RESET ISSUED", "&cSuccessfully reset stats of " + targetPlayer.getName() + ".",
                "&7Be aware that if this is being abused, you will be punished.", ""
        ).forEach(line -> player.sendMessage(CC.translate(line)));

        if (targetPlayer.isOnline() && targetPlayer.getPlayer() != null) {
            Arrays.asList(
                    "", "&c&lSTAT RESET ACTION", "&cYour stats have been wiped due to suspicious activity.",
                    "&7If you believe this was unjust, create a support ticket.", ""
            ).forEach(line -> targetPlayer.getPlayer().sendMessage(CC.translate(line)));
        }
    }

    @Override
    public void resetLayoutForKit(Kit kit) {
        this.profiles.values().forEach(profile -> {
            List<LayoutData> layouts = profile.getProfileData().getLayoutData().getLayouts().get(kit.getName());
            if (layouts != null) {
                layouts.forEach(layout -> layout.setItems(kit.getItems()));
                profile.getProfileData().getLayoutData().getLayouts().put(kit.getName(), layouts);
            }
        });
        Bukkit.broadcastMessage(CC.translate("&c&lLAYOUT RESET: &cThe layout for kit " + kit.getName() + " has been reset for all players."));
    }
}