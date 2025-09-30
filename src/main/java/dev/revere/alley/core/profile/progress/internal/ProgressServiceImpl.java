package dev.revere.alley.core.profile.progress.internal;

import dev.revere.alley.bootstrap.annotation.Service;
import dev.revere.alley.core.profile.Profile;
import dev.revere.alley.core.profile.progress.PlayerProgress;
import dev.revere.alley.core.profile.progress.ProgressService;
import dev.revere.alley.feature.division.Division;
import dev.revere.alley.feature.division.DivisionService;
import dev.revere.alley.feature.division.model.DivisionTier;
import lombok.Getter;

import java.util.List;

/**
 * @author Remi
 * @project alley-practice
 * @date 2/07/2025
 */
@Service(provides = ProgressService.class, priority = 410)
public class ProgressServiceImpl implements ProgressService {
    protected final DivisionService divisionService;

    /**
     * DI Constructor for the ProgressServiceImpl class.
     *
     * @param divisionService The Division Service.
     */
    public ProgressServiceImpl(DivisionService divisionService) {
        this.divisionService = divisionService;
    }

    @Override
    public PlayerProgress calculateProgress(Profile profile, String kitName) {
        Division currentDivision = profile.getProfileData().getUnrankedKitData().get(kitName).getDivision();
        DivisionTier currentTier = profile.getProfileData().getUnrankedKitData().get(kitName).getTier();
        int currentWins = profile.getProfileData().getUnrankedKitData().get(kitName).getWins();

        Division nextDivision = profile.getNextDivision(kitName);
        DivisionTier nextTier = profile.getNextTier(kitName);

        int winsRequired = nextTier != null ? nextTier.getRequiredWins() : (nextDivision != null ? nextDivision.getTiers().get(0).getRequiredWins() : -1);
        int winsRemaining = winsRequired != -1 ? winsRequired - currentWins : -1;

        return new PlayerProgress(currentDivision, nextDivision, currentTier, nextTier, currentWins, winsRemaining);
    }
}