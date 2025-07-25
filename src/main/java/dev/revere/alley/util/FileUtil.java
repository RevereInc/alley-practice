package dev.revere.alley.util;

import dev.revere.alley.tool.logger.Logger;
import lombok.experimental.UtilityClass;

import java.io.File;
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

}
