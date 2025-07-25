package dev.revere.alley.provider.placeholder;

import dev.revere.alley.Alley;
import dev.revere.alley.plugin.AlleyContext;
import dev.revere.alley.plugin.annotation.Service;
import dev.revere.alley.provider.placeholder.impl.AlleyPlaceholderExpansion;
import dev.revere.alley.tool.logger.Logger;

/**
 * @author Emmy
 * @project alley-practice
 * @since 17/07/2025
 */
@Service(provides = PlaceholderService.class, priority = 430)
public class PlaceholderServiceImpl implements PlaceholderService {

    @Override
    public void initialize(AlleyContext context) {
        this.registerExpansion(context.getPlugin());
    }

    @Override
    public void registerExpansion(Alley plugin) {
        if (plugin.getServer().getPluginManager().getPlugin("PlaceholderAPI") == null) {
            Logger.info("PlaceholderAPI is not installed! Alley Placeholder Expansion will not be registered.");
            return;
        }

        Logger.logTime(AlleyPlaceholderExpansion.class.getSimpleName(), () -> {
            AlleyPlaceholderExpansion expansion = new AlleyPlaceholderExpansion(plugin);
            expansion.register();
        });
    }
}