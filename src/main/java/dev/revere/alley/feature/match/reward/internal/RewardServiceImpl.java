package dev.revere.alley.feature.match.reward.internal;

import dev.revere.alley.bootstrap.AlleyContext;
import dev.revere.alley.bootstrap.annotation.Service;
import dev.revere.alley.common.constants.PluginConstant;
import dev.revere.alley.common.logger.Logger;
import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.profile.Profile;
import dev.revere.alley.core.profile.ProfileService;
import dev.revere.alley.feature.match.reward.RewardCalculator;
import dev.revere.alley.feature.match.reward.RewardService;
import dev.revere.alley.feature.match.reward.RewardType;
import dev.revere.alley.feature.match.reward.annotation.RewardTypeProvider;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.EnumMap;
import java.util.Map;

/**
 * @author Emmy
 * @project alley-practice
 * @since 01/09/2025
 */
@Getter
@Service(provides = RewardService.class, priority = 450)
public class RewardServiceImpl implements RewardService {
    private final ProfileService profileService;
    private final Map<RewardType, RewardCalculator> calculators = new EnumMap<>(RewardType.class);

    /**
     * DI Constructor for the RewardServiceImpl class.
     *
     * @param profileService The profile service instance.
     */
    public RewardServiceImpl(ProfileService profileService) {
        this.profileService = profileService;
    }

    @Override
    public void initialize(AlleyContext context) {
        PluginConstant pluginConstant = context.getPlugin().getService(PluginConstant.class);
        String packageDirectory = pluginConstant.getPackageDirectory() + ".feature.match.reward.internal.calculators";

        try {
            ScanResult scanResult = new ClassGraph()
                    .enableClassInfo()
                    .enableAnnotationInfo()
                    .acceptPackages(packageDirectory)
                    .scan();

            for (ClassInfo classInfo : scanResult.getClassesImplementing(RewardCalculator.class)) {
                Class<?> clazz = classInfo.loadClass();
                RewardTypeProvider annotation = clazz.getAnnotation(RewardTypeProvider.class);
                if (annotation == null) {
                    Logger.warn("RewardCalculator " + clazz.getName() + " is missing @RewardTypeProvider annotation. Skipping registration.");
                    continue;
                }

                RewardCalculator calculator = (RewardCalculator) clazz.getDeclaredConstructor().newInstance();
                RewardType type = annotation.type();

                this.calculators.put(type, calculator);
                Logger.info("Registered RewardCalculator for type: " + type.name());
            }
        } catch (Exception exception) {
            Logger.logException("An error occurred while scanning for RewardCalculators.", exception);
        }
    }

    @Override
    public void issueReward(Player player, RewardType type) {
        Profile profile = profileService.getProfile(player.getUniqueId());
        RewardCalculator calculator = this.calculators.get(type);
        if (calculator == null) {
            Logger.error("No RewardCalculator found for type: " + type.name());
            return;
        }

        int coins = calculator.calculateReward(player, profile);
        profile.getProfileData().incrementCoins(coins);

        player.sendMessage(CC.translate(" &7(&a+&6" + coins + "&f&7)"));
    }
}