package dev.revere.alley.common;

import dev.revere.alley.common.logger.Logger;
import lombok.experimental.UtilityClass;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Comparator;

/**
 * @author Remi
 * @project alley-practice
 * @date 23/06/2025
 */
@UtilityClass
public class FileUtil {
    /**
     * Deletes the specified world folder, including all its contents. If an
     * initial deletion attempt fails, an alternative method is used to ensure
     * all files and subdirectories are removed. Logs any exceptions encountered
     * during the process.
     *
     * @param worldFolder the folder representing the world to delete
     */
    public void deleteWorldFolder(File worldFolder) {
        try {
            deleteDirectory(worldFolder);
        } catch (Exception e) {
            Logger.logException("Failed to delete world folder", e);

            try {
                Files.walk(worldFolder.toPath())
                        .sorted(Comparator.reverseOrder())
                        .forEach(path -> {
                            try {
                                Files.delete(path);
                            } catch (Exception ex) {
                                Logger.logException("Failed to delete: " + path, ex);
                            }
                        });
            } catch (Exception ex) {
                Logger.logException("Alternative deletion method also failed", ex);
            }
        }
    }

    /**
     * Deletes a directory and all its contents, including subdirectories and files.
     * Logs an error message for any file or directory that fails to be deleted.
     *
     * @param directory the directory to delete
     */
    public void deleteDirectory(File directory) {
        if (!directory.exists()) {
            return;
        }

        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    if (!file.delete()) {
                        Logger.error("Failed to delete file: " + file.getAbsolutePath());
                    }
                }
            }
        }

        if (!directory.delete()) {
            Logger.error("Failed to delete directory: " + directory.getAbsolutePath());
        }
    }

    /**
     * Converts single-quoted YAML values to double-quoted ones.
     * <p>
     * Regex pattern explanation:
     * <ul>
     *   <li><code>:\s+</code> – matches a colon followed by spaces (YAML key-value separator)</li>
     *   <li><code>'</code> – matches opening single quote</li>
     *   <li><code>[^']*</code> – captures everything that's not a single quote (the content)</li>
     *   <li><code>'</code> – matches closing single quote</li>
     *   <li><code>(?=\s*$)</code> – positive lookahead for end of line (with optional whitespace)</li>
     * </ul>
     * <p>
     * This ensures that only single quotes wrapping entire values are replaced,
     * while quotes within text (e.g., <code>can't</code>) remain untouched.
     *
     * @param content the YAML content to process
     * @return processed content with double quotes
     */
    public String convertQuotesToDouble(String content) {
        return content.replaceAll("(:\\s+)'([^']*)'(?=\\s*$)", "$1\"$2\"");
    }

    /**
     * Post-processes a saved config file to convert single quotes to double quotes.
     *
     * @param file the config file to process.
     */
    public void postProcessConfigFile(File file) {
        try {
            String content = new String(Files.readAllBytes(file.toPath()));
            String processedContent = convertQuotesToDouble(content);
            Files.write(file.toPath(), processedContent.getBytes());
        } catch (IOException exception) {
            Logger.error("Failed to post-process config file: " + file.getName());
        }
    }
}