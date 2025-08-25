package dev.revere.alley.common.bstats.internal;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.bootstrap.AlleyContext;
import dev.revere.alley.bootstrap.annotation.Service;
import dev.revere.alley.common.bstats.BStatsService;
import org.bstats.bukkit.Metrics;

/**
 * @author Remi
 * @project alley-practice
 * @date 27/07/2025
 */
@Service(provides = BStatsService.class, priority = 1)
public class BStatsServiceImpl implements BStatsService {
    @Override
    public void setup(AlleyContext context) {
        Metrics metrics = new Metrics(AlleyPlugin.getInstance(), 26678);
    }
}