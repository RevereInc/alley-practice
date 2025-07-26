package dev.revere.alley.updater;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.revere.alley.Alley;
import dev.revere.alley.config.ConfigService;
import dev.revere.alley.plugin.AlleyContext;
import dev.revere.alley.plugin.annotation.Service;
import dev.revere.alley.tool.logger.Logger;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author Remi
 * @project alley-practice
 * @date 24/07/2025
 */
@Service(provides = UpdaterService.class, priority = 1500)
public class UpdaterServiceImpl implements UpdaterService {
    private final ConfigService configService;
    private final String currentVersion;

    private final String githubRepo = "RevereInc/alley-practice";
    private String latestVersion;

    /**
     * Constructor for DI. Receives the main plugin instance and config service.
     *
     * @param configService The configuration service to access settings.
     */
    public UpdaterServiceImpl(ConfigService configService) {
        this.configService = configService;
        this.currentVersion = Alley.getInstance().getDescription().getVersion();
    }

    @Override
    public void setup(AlleyContext context) {
        try {
            this.latestVersion = getLatestVersion();
        } catch (IOException e) {
            Logger.logException("Failed to fetch the latest version from GitHub", e);
        }
    }

    @Override
    public void initialize(AlleyContext context) {
        checkForUpdates();
    }

    @Override
    public void checkForUpdates() {
        Alley.getInstance().getServer().getScheduler().runTaskAsynchronously(Alley.getInstance(), () -> {
            try {
                if (latestVersion != null && !currentVersion.equals(latestVersion)) {
                    Logger.warn("New version available: " + latestVersion + " (Current: " + currentVersion + ")");

                    if (shouldAutoUpdate()) {
                        downloadAndUpdate(latestVersion);
                    }
                }
            } catch (Exception e) {
                Logger.logException("Failed to check for updates", e);
            }
        });
    }

    @Override
    public void downloadAndUpdate(String version) {
        try {
            String downloadUrl = "https://github.com/" + githubRepo + "/releases/download/v" + version + "/Alley-" + version + ".jar";

            URL url = new URL(downloadUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            File pluginFile = new File(Alley.getInstance().getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
            File pluginDirectory = pluginFile.getParentFile();

            File updateFile = new File(pluginDirectory, "Alley-" + version + ".jar");
            File tempFile = new File(pluginDirectory, "Alley-temp.jar");

            try (InputStream inputStream = connection.getInputStream();
                 FileOutputStream outputStream = new FileOutputStream(tempFile)) {

                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }

            if (pluginFile.delete()) {
                Logger.info("Successfully deleted old plugin file: " + pluginFile.getName());
            } else {
                Logger.warn("Failed to delete old plugin file: " + pluginFile.getName());
            }

            if (tempFile.renameTo(updateFile)) {
                Logger.info("Successfully updated plugin to version " + version);
            } else {
                Logger.warn("Failed to rename temporary file to plugin file: " + updateFile.getName());
            }

            Logger.info("Shutting down the server to apply the update. Please restart the server manually.");
            Alley.getInstance().getServer().shutdown();
        } catch (Exception e) {
            Logger.logException("Failed to download and update to version " + version, e);
        }
    }

    @Override
    public String getLatestVersion() throws IOException {
        URL url = new URL("https://api.github.com/repos/" + githubRepo + "/releases/latest");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/vnd.github.v3+json");

        try (InputStream inputStream = connection.getInputStream();
             InputStreamReader inputStreamReader = new InputStreamReader(inputStream)) {

            JsonParser parser = new JsonParser();
            JsonObject json = parser.parse(inputStreamReader).getAsJsonObject();
            return json.get("tag_name").getAsString().replace("v", "");
        }
    }

    private boolean shouldAutoUpdate() {
        return configService.getSettingsConfig().getBoolean("auto-update", true);
    }
}
