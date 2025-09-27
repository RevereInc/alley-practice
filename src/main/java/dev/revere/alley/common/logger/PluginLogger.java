package dev.revere.alley.common.logger;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.adapter.core.CoreAdapter;
import dev.revere.alley.adapter.knockback.KnockbackAdapter;
import dev.revere.alley.common.constants.PluginConstant;
import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.locale.LocaleService;
import dev.revere.alley.core.profile.ProfileService;
import dev.revere.alley.feature.arena.ArenaService;
import dev.revere.alley.feature.division.DivisionService;
import dev.revere.alley.feature.emoji.EmojiService;
import dev.revere.alley.feature.ffa.FFAService;
import dev.revere.alley.feature.kit.Kit;
import dev.revere.alley.feature.kit.KitService;
import dev.revere.alley.feature.kit.setting.types.mode.KitSettingRanked;
import dev.revere.alley.feature.level.LevelService;
import dev.revere.alley.feature.title.TitleService;
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
     * Send a message to the console when the bootstrap is enabled.
     *
     * @param timeTaken The time taken to enable the bootstrap.
     */
    public void onEnable(long timeTaken) {
        AlleyPlugin plugin = AlleyPlugin.getInstance();
        Server server = plugin.getServer();

        PluginConstant constants = plugin.getService(PluginConstant.class);
        CoreAdapter coreAdapter = plugin.getService(CoreAdapter.class);
        KnockbackAdapter knockbackAdapter = plugin.getService(KnockbackAdapter.class);
        KitService kitService = plugin.getService(KitService.class);
        FFAService ffaService = plugin.getService(FFAService.class);
        ArenaService arenaService = plugin.getService(ArenaService.class);
        DivisionService divisionService = plugin.getService(DivisionService.class);
        TitleService titleService = plugin.getService(TitleService.class);
        LevelService levelService = plugin.getService(LevelService.class);
        EmojiService emojiService = plugin.getService(EmojiService.class);
        LocaleService localeService = plugin.getService(LocaleService.class);
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
                "    " + secondary + "Arenas: " + color + arenaService.getArenas().size(),
                "    " + secondary + "Divisions: " + color + divisionService.getDivisions().size(),
                "    " + secondary + "Unranked Kits: " + color + kitService.getKits().size(),
                "    " + secondary + "Ranked Kits: " + color + kitService.getKits().stream().filter(Kit::isEnabled).filter(kit -> kit.isSettingEnabled(KitSettingRanked.class)).count(),
                "    " + secondary + "FFA Kits: " + color + ffaService.getFfaKits().size(),
                "    " + secondary + "Levels: " + color + levelService.getLevels().size(),
                "    " + secondary + "Titles: " + color + titleService.getTitles().size(),
                "",
                "    " + color + "Essentials",
                "     " + secondary + "Emojis: " + (emojiService.isEnabled() ? "&aEnabled" : "&cDisabled"),
                "",
                "    " + color + "Database",
                "     " + secondary + "Type: " + color + profileService.getDatabaseProfile().getType(),
                //"     " + secondary + "Database: " + color + localeService.getMessage(SettingsLocaleImpl.MONGO_CREDENTIALS_DATABASE),
                "",
                "    " + secondary + "Spigot: " + color + server.getName(),
                "    " + secondary + "Version: " + color + constants.getSpigotVersion(),
                "",
                "    " + secondary + "Loaded in " + color + timeTaken + "ms",
                ""
        ).forEach(line -> server.getConsoleSender().sendMessage(CC.translate(line)));
    }

    public void onDisable() {
        AlleyPlugin plugin = AlleyPlugin.getInstance();
        Arrays.asList(
                "",
                CC.PREFIX + "&cDisabled.",
                ""
        ).forEach(line -> plugin.getServer().getConsoleSender().sendMessage(CC.translate(line)));
    }
}