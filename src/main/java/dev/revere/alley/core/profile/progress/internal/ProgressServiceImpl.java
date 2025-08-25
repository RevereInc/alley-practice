package dev.revere.alley.core.profile.progress.internal;

import dev.revere.alley.bootstrap.annotation.Service;
import dev.revere.alley.core.profile.Profile;
import dev.revere.alley.core.profile.data.ProfileData;
import dev.revere.alley.core.profile.data.types.ProfileUnrankedKitData;
import dev.revere.alley.core.profile.progress.PlayerProgress;
import dev.revere.alley.core.profile.progress.ProgressService;
import dev.revere.alley.feature.division.Division;
import dev.revere.alley.feature.division.model.DivisionTier;

import java.util.List;

/**
 * @author Remi
 * @project alley-practice
 * @date 2/07/2025
 */
@Service(provides = ProgressService.class, priority = 410)
public class ProgressServiceImpl implements ProgressService {
    @Override
    public PlayerProgress calculateProgress(Profile profile, String kitName) {
        ProfileData profileData = profile.getProfileData();
        ProfileUnrankedKitData kitData = profileData.getUnrankedKitData().get(kitName);

        if (kitData == null) {
            return new PlayerProgress(0, 0, "N/A", true);
        }

        int wins = kitData.getWins();
        Division currentDivision = kitData.getDivision();
        DivisionTier currentTier = kitData.getTier();

        List<DivisionTier> tiers = currentDivision.getTiers();
        int tierIndex = tiers.indexOf(currentTier);

        int nextTierWins;
        boolean isMaxRank = false;

        if (tierIndex < tiers.size() - 1) {
            nextTierWins = tiers.get(tierIndex + 1).getRequiredWins();
        } else {
            Division nextDivision = profile.getNextDivision(kitName);
            if (nextDivision != null) {
                nextTierWins = nextDivision.getTiers().get(0).getRequiredWins();
            } else {
                nextTierWins = currentTier.getRequiredWins();
                isMaxRank = true;
            }
        }

        String nextRankName = isMaxRank ? "Max Rank" : profile.getNextDivisionAndTier(kitName);

        return new PlayerProgress(wins, nextTierWins, nextRankName, isMaxRank);
    }
}