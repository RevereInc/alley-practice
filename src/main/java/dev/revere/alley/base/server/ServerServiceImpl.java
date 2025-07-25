package dev.revere.alley.base.server;

import dev.revere.alley.base.hotbar.HotbarService;
import dev.revere.alley.base.spawn.SpawnService;
import dev.revere.alley.config.ConfigService;
import dev.revere.alley.game.match.Match;
import dev.revere.alley.game.match.MatchService;
import dev.revere.alley.game.party.PartyService;
import dev.revere.alley.game.party.Party;
import dev.revere.alley.plugin.AlleyContext;
import dev.revere.alley.plugin.annotation.Service;
import dev.revere.alley.profile.ProfileService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.enums.ProfileState;
import dev.revere.alley.tool.logger.Logger;
import dev.revere.alley.util.chat.CC;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Emmy
 * @project Alley
 * @since 09/03/2025
 */
@Service(provides = ServerService.class, priority = 300)
public class ServerServiceImpl implements ServerService {
    private final MatchService matchService;
    private final PartyService partyService;
    private final ProfileService profileService;
    private final HotbarService hotbarService;
    private final SpawnService spawnService;
    private final ConfigService configService;

    private boolean queueingAllowed = true;

    private final Set<Material> blockedCraftingItems = new HashSet<>();
    private final String BLOCKED_ITEMS_PATH = "blocked-crafting-items";

    /**
     * Constructor for dependency injection.
     *
     * @param matchService   The match service.
     * @param partyService   The party service.
     * @param profileService The profile service.
     * @param hotbarService  The hotbar service.
     * @param spawnService   The spawn service.
     * @param configService  The config service.
     */
    public ServerServiceImpl(MatchService matchService, PartyService partyService, ProfileService profileService, HotbarService hotbarService, SpawnService spawnService, ConfigService configService) {
        this.matchService = matchService;
        this.partyService = partyService;
        this.profileService = profileService;
        this.hotbarService = hotbarService;
        this.spawnService = spawnService;
        this.configService = configService;
    }

    @Override
    public void initialize(AlleyContext context) {
        this.loadBlockedCraftingItems();
    }

    @Override
    public boolean isQueueingAllowed() {
        return this.queueingAllowed;
    }

    @Override
    public void setQueueingAllowed(boolean allowed) {
        this.queueingAllowed = allowed;
    }

    @Override
    public void endAllMatches(Player issuer) {
        List<Match> matches = new ArrayList<>(this.matchService.getMatches());
        if (matches.isEmpty()) {
            if (issuer != null) issuer.sendMessage(CC.translate("&cCould not find any matches to end."));
            return;
        }

        int rankedMatches = 0;
        int unrankedMatches = 0;

        for (Match match : matches) {
            if (match.isRanked()) rankedMatches++;
            else unrankedMatches++;
            match.endMatch();
        }

        if (issuer != null) {
            issuer.sendMessage(CC.translate("&cEnding a total of &f" + unrankedMatches + " &cunranked matches and &f" + rankedMatches + " &cranked matches."));
        }
    }

    @Override
    public void disbandAllParties(Player issuer) {
        List<Party> parties = new ArrayList<>(this.partyService.getParties());
        if (parties.isEmpty()) {
            if (issuer != null) issuer.sendMessage(CC.translate("&cCould not find any parties to disband."));
            return;
        }

        if (issuer != null)
            issuer.sendMessage(CC.translate("&cDisbanding a total of &f" + parties.size() + " &cparties."));

        for (Party party : parties) {
            this.partyService.disbandParty(party.getLeader());
        }
    }

    @Override
    public void clearAllQueues(Player issuer) {
        int playersRemoved = 0;
        for (Profile profile : this.profileService.getProfiles().values()) {
            if (profile.getState() == ProfileState.WAITING && profile.getQueueProfile() != null) {
                Player queuePlayer = Bukkit.getPlayer(profile.getUuid());
                if (queuePlayer != null) {
                    profile.getQueueProfile().getQueue().removePlayer(profile.getQueueProfile());

                    profile.setState(ProfileState.LOBBY);
                    profile.setQueueProfile(null);
                    this.hotbarService.applyHotbarItems(queuePlayer);
                    this.spawnService.teleportToSpawn(queuePlayer);
                    queuePlayer.sendMessage(CC.translate("&cYou have been removed from the queue by an administrator."));
                    playersRemoved++;
                }
            }
        }

        if (issuer != null) {
            if (playersRemoved > 0) {
                issuer.sendMessage(CC.translate("&cRemoved &f" + playersRemoved + " &cplayer(s) from the queue."));
            } else {
                issuer.sendMessage(CC.translate("&cCould not find any players in a queue."));
            }
        }
    }

    @Override
    public Set<Material> getBlockedCraftingItems() {
        return Collections.unmodifiableSet(this.blockedCraftingItems);
    }

    @Override
    public void loadBlockedCraftingItems() {
        FileConfiguration config = this.configService.getSettingsConfig();
        List<String> blocked = config.getStringList(this.BLOCKED_ITEMS_PATH);

        this.blockedCraftingItems.clear();
        for (String mat : blocked) {
            try {
                Material material = Material.matchMaterial(mat);
                this.blockedCraftingItems.add(material);
            } catch (IllegalArgumentException exception) {
                Logger.logException("Invalid material in blocked crafting items: " + mat, exception);
            }
        }
    }

    @Override
    public void saveBlockedItems(Material material) {
        FileConfiguration config = this.configService.getSettingsConfig();
        File configFile = this.configService.getConfigFile("settings.yml");

        List<String> materialNames = this.blockedCraftingItems.stream().map(Material::name).collect(Collectors.toList());
        config.set(this.BLOCKED_ITEMS_PATH, materialNames);

        this.configService.saveConfig(configFile, config);
    }

    @Override
    public void addToBlockedCraftingList(Material material) {
        this.blockedCraftingItems.add(material);
    }

    @Override
    public void removeFromBlockedCraftingList(Material material) {
        this.blockedCraftingItems.remove(material);
    }

    @Override
    public boolean isCraftable(Material material) {
        ItemStack itemStack = new ItemStack(material);
        return !Bukkit.getServer().getRecipesFor(itemStack).isEmpty();
    }
}