package dev.revere.alley.base.queue;

import dev.revere.alley.Alley;
import dev.revere.alley.api.menu.Menu;
import dev.revere.alley.base.kit.KitService;
import dev.revere.alley.base.kit.setting.impl.mode.KitSettingRanked;
import dev.revere.alley.base.queue.menu.QueuesMenuDefault;
import dev.revere.alley.base.queue.menu.QueuesMenuModern;
import dev.revere.alley.base.queue.runnable.QueueRunnable;
import dev.revere.alley.config.ConfigService;
import dev.revere.alley.plugin.AlleyContext;
import dev.revere.alley.plugin.annotation.Service;
import dev.revere.alley.profile.ProfileService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.enums.ProfileState;
import dev.revere.alley.tool.logger.Logger;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Remi
 * @project Alley
 * @date 5/21/2024
 */
@Getter
@Service(provides = QueueService.class, priority = 90)
public class QueueServiceImpl implements QueueService {
    private final ConfigService configService;
    private final KitService kitService;
    private final ProfileService profileService;

    private final List<Queue> queues = new ArrayList<>();
    private Menu queueMenu;
    private QueueRunnable queueRunnable;

    public QueueServiceImpl(ConfigService configService, KitService kitService, ProfileService profileService) {
        this.configService = configService;
        this.kitService = kitService;
        this.profileService = profileService;
    }

    @Override
    public void initialize(AlleyContext context) {
        this.queueMenu = determineMenu();
        this.reloadQueues();

        this.queueRunnable = new QueueRunnable();
        context.getPlugin().getServer().getScheduler().runTaskTimer(Alley.getInstance(), this.queueRunnable, 20L, 20L);
    }

    @Override
    public void reloadQueues() {
        this.queues.clear();
        this.kitService.getKits().forEach(kit -> {
            if (!kit.isEnabled()) return;
            this.queues.add(new Queue(kit, false, false));
            this.queues.add(new Queue(kit, false, true));
            if (kit.isSettingEnabled(KitSettingRanked.class)) {
                this.queues.add(new Queue(kit, true, false));
            }
        });
    }

    /**
     * Determines the menu type based on the configuration.
     *
     * @return the appropriate menu instance
     */
    private Menu determineMenu() {
        FileConfiguration config = this.configService.getMenusConfig();
        String menuType = config.getString("queues-menu.type", "DEFAULT");

        switch (menuType.toUpperCase()) {
            case "MODERN":
                return new QueuesMenuModern();
            case "DEFAULT":
            default:
                if (!menuType.equalsIgnoreCase("DEFAULT")) {
                    Logger.warn("Invalid queues menu type '" + menuType + "'. Defaulting to DEFAULT.");
                }
                return new QueuesMenuDefault();
        }
    }

    /**
     * Get the player count of a specific game type
     *
     * @param queue the queue to get the player count of
     * @return the player count of the game type
     */
    public int getPlayerCountOfGameType(String queue) {
        ProfileState stateForQueue = this.getStateForQueue(queue);

        if (stateForQueue == null) {
            return 0;
        }

        return (int) this.profileService.getProfiles().values().stream()
                .filter(profile -> profile.getState().equals(stateForQueue))
                .filter(profile -> this.isMatchForQueue(profile, queue))
                .count();
    }

    /**
     * Get the state of a profile for a specific queue
     *
     * @param queue the queue to get the state for
     * @return the state of the profile for the queue
     */
    private ProfileState getStateForQueue(String queue) {
        switch (queue) {
            case "Unranked":
            case "Ranked":
            case "Duos":
                return ProfileState.PLAYING;
            case "FFA":
                return ProfileState.FFA;
            case "Bots":
                return ProfileState.FIGHTING_BOT;
            default:
                return null;
        }
    }

    /**
     * Check if a profile's match type matches the queue
     *
     * @param profile the profile to check
     * @param queue   the queue to check the profile's match for
     * @return true if the profile's match type matches the queue
     */
    private boolean isMatchForQueue(Profile profile, String queue) {
        if (queue.equals("FFA")) {
            return true;
        } else if (queue.equals("Ranked")) {
            return profile.getMatch().isRanked();
        } else if (queue.equals("Duos")) {
            return true;
        }

        return false;
    }
}