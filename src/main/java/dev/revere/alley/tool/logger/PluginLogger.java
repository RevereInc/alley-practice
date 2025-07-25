package dev.revere.alley.tool.logger;

import dev.revere.alley.Alley;
import dev.revere.alley.api.constant.PluginConstant;
import dev.revere.alley.base.arena.ArenaService;
import dev.revere.alley.base.kit.KitService;
import dev.revere.alley.config.ConfigService;
import dev.revere.alley.feature.division.DivisionService;
import dev.revere.alley.adapter.knockback.KnockbackAdapter;
import dev.revere.alley.feature.level.LevelService;
import dev.revere.alley.feature.title.TitleService;
import dev.revere.alley.game.ffa.FFAService;
import dev.revere.alley.profile.ProfileService;
import dev.revere.alley.adapter.core.CoreAdapter;
import dev.revere.alley.util.chat.CC;
import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;
import org.bukkit.Server;

import java.util.Arrays;

/**
 * @author Emmy
 * @project Alley
 * @since 03/04/2025
 */
@UtilityClass
public class PluginLogger {
    /**
     * Send a message to the console when the plugin is enabled.
     *
     * @param timeTaken The time taken to enable the plugin.
     */
    public void onEnable(long timeTaken) {
        Alley plugin = Alley.getInstance();
        Server server = plugin.getServer();

        PluginConstant constants = plugin.getService(PluginConstant.class);
        ConfigService configService = plugin.getService(ConfigService.class);
        CoreAdapter coreAdapter = plugin.getService(CoreAdapter.class);
        KnockbackAdapter knockbackAdapter = plugin.getService(KnockbackAdapter.class);
        KitService kitService = plugin.getService(KitService.class);
        FFAService ffaService = plugin.getService(FFAService.class);
        ArenaService arenaService = plugin.getService(ArenaService.class);
        DivisionService divisionService = plugin.getService(DivisionService.class);
        TitleService titleService = plugin.getService(TitleService.class);
        LevelService levelService = plugin.getService(LevelService.class);
        ProfileService profileService = plugin.getService(ProfileService.class);

        ChatColor color = constants.getMainColor();
        ChatColor secondary = ChatColor.WHITE;

        Arrays.asList(
                "",
                "        " + color + "&l" + constants.getName() + color + " Practice",
                "",
                "    " + secondary + "Authors: " + color + constants.getAuthors().toString().replace("[", "").replace("]", ""),
                "",
                "    " + secondary + "Version: " + color + constants.getVersion(),
                "    " + secondary + "Description: " + color + constants.getDescription(),
                "",
                "    " + secondary + "Hooked Core: " + color + coreAdapter.getCore().getType().getPluginName(),
                "    " + secondary + "Hooked Knockback: " + color + knockbackAdapter.getKnockbackImplementation().getType().getSpigotName(),
                "",
                "    " + secondary + "Kits: " + color + kitService.getKits().size(),
                "    " + secondary + "FFA Kits: " + color + ffaService.getFfaKits().size(),
                "    " + secondary + "Arenas: " + color + arenaService.getArenas().size(),
                "    " + secondary + "Divisions: " + color + divisionService.getDivisions().size(),
                "    " + secondary + "Titles: " + color + titleService.getTitles().size(),
                "    " + secondary + "Levels: " + color + levelService.getLevels().size(),
                "",
                "    " + color + "MongoDB " + secondary + "| " + color + "Status: &aConnected",
                "     " + secondary + "Database: " + color + configService.getDatabaseConfig().getString("mongo.database"),
                "     " + secondary + "Loaded Profiles: " + color + profileService.getProfiles().size(),
                "",
                "    " + secondary + "Spigot: " + color + server.getName(),
                "    " + secondary + "Version: " + color + constants.getSpigotVersion(),
                "",
                "    " + secondary + "Loaded in " + color + timeTaken + "ms",
                ""
        ).forEach(line -> server.getConsoleSender().sendMessage(CC.translate(line)));
    }

    public void onDisable() {
        Alley plugin = Alley.getInstance();
        Arrays.asList(
                "",
                CC.PREFIX + "&cDisabled.",
                ""
        ).forEach(line -> plugin.getServer().getConsoleSender().sendMessage(CC.translate(line)));
    }
}