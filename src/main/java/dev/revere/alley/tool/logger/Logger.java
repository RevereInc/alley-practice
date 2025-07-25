package dev.revere.alley.tool.logger;

import dev.revere.alley.Alley;
import dev.revere.alley.api.constant.PluginConstant;
import dev.revere.alley.util.chat.CC;
import lombok.experimental.UtilityClass;
import org.bukkit.command.ConsoleCommandSender;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Remi
 * @project Alley
 * @date 5/27/2024
 */
@UtilityClass
public class Logger {
    private final static ConsoleCommandSender consoleSender;
    private static final Map<UUID, Exception> storedExceptions;

    private static final String PHASE_HEADER_PREFIX = "&6&l--- ";
    private static final String PHASE_HEADER_SUFFIX = " ---";
    private static final String TASK_PREFIX_SUCCESS = "&a✔  &f";
    private static final String TASK_PREFIX_FAIL = "&c✖ &f";

    static {
        consoleSender = Alley.getInstance().getServer().getConsoleSender();
        storedExceptions = new HashMap<>();
    }

    /**
     * Log a message to the console.
     *
     * @param message the message to log
     */
    public void info(String message) {
        consoleSender.sendMessage(CC.translate(CC.PREFIX + message));
    }

    /**
     * Log an error to the console.
     *
     * @param message the error message to log
     */
    public void error(String message) {
        consoleSender.sendMessage(CC.translate(CC.ERROR_PREFIX + "&c(ERROR) &8" + message));
    }

    /**
     * Log a warning to the console.
     *
     * @param message the warning message to log
     */
    public void warn(String message) {
        consoleSender.sendMessage(CC.translate(CC.WARNING_PREFIX + "&e(WARNING) &8" + message));
    }

    /**
     * Log an exception to the console.
     *
     * @param message   the info message or the class name
     * @param exception the exception to log
     */
    public static void logException(String message, Exception exception) {
        UUID errorId = UUID.randomUUID();
        storedExceptions.put(errorId, exception);

        Arrays.asList(
                "",
                CC.ERROR_PREFIX + "&c&lEXCEPTION",
                " &f" + message + ": &r" + exception.getMessage(),
                "",
                " &c(Type &4viewerror " + errorId + " &cin console to see details)",
                ""
        ).forEach(line -> consoleSender.sendMessage(CC.translate(line)));
    }

    /**
     * Retrieve and print the full stack trace of a stored exception.
     *
     * @param errorId The UUID of the error.
     */
    @SuppressWarnings("all")
    public static void viewException(UUID errorId) {
        Exception exception = storedExceptions.get(errorId);
        if (exception == null) {
            consoleSender.sendMessage(CC.translate(CC.ERROR_PREFIX + "&cNo exception found with ID: " + errorId));
            return;
        }

        Arrays.asList(
                "",
                CC.MENU_BAR + CC.MENU_BAR + CC.MENU_BAR + CC.MENU_BAR,
                "",
                "&c&lVIEWING ERROR: " + errorId,
                ""
        ).forEach(line -> consoleSender.sendMessage(CC.translate(line)));

        exception.printStackTrace();

        StackTraceElement[] stackTrace = exception.getStackTrace();
        String locationMessage = "&cError occurred at: Unknown location";

        for (StackTraceElement element : stackTrace) {
            if (element.getClassName().startsWith(Alley.getInstance().getService(PluginConstant.class).getPackageDirectory())) {
                locationMessage = "&cError occurred at: " + element.getClassName() + " (Line " + element.getLineNumber() + ")";
                break;
            }
        }

        consoleSender.sendMessage("");
        consoleSender.sendMessage(CC.translate(locationMessage));
        consoleSender.sendMessage("");
        consoleSender.sendMessage(CC.MENU_BAR + CC.MENU_BAR + CC.MENU_BAR + CC.MENU_BAR);
        consoleSender.sendMessage("");

        storedExceptions.remove(errorId);
    }

    /**
     * Log the time it takes to run a task with clear success/failure indication.
     *
     * @param taskName the name of the task to run
     * @param runnable the task to run
     */
    public void logTime(String taskName, Runnable runnable) {
        long start = System.currentTimeMillis();
        boolean success = false;
        try {
            runnable.run();
            success = true;
        } catch (Exception exception) {
            logException("Failed to run the " + taskName + " task", exception);
        } finally {
            long end = System.currentTimeMillis();
            String prefix = success ? TASK_PREFIX_SUCCESS : TASK_PREFIX_FAIL;
            String message = success ? "&fSuccessfully initialized &6" : "&cFailed to initialize &6";
            consoleSender.sendMessage(CC.translate(prefix + message + taskName + " &fin &6" + (end - start) + "ms&f."));
        }
    }
    /**
     * Log the time it takes to run a task.
     *
     * @param runnableTaskName the name of the task to run
     * @param runnable         the task to run
     */
    public void logTimeTask(String runnableTaskName, Runnable runnable) {
        long start = System.currentTimeMillis();
        boolean success = false;
        try {
            runnable.run();
            success = true;
        } catch (Exception exception) {
            logException("Failed to run the " + runnableTaskName + " task", exception);
        } finally {
            long end = System.currentTimeMillis();
            String prefix = success ? TASK_PREFIX_SUCCESS : TASK_PREFIX_FAIL;
            String message = success ? "&fSuccessfully ran &6" : "&cFailed to run &6";
            consoleSender.sendMessage(CC.translate( prefix + message + runnableTaskName + " &fin &6" + (end - start) + "ms&f."));
        }
    }

    /**
     * Measure the runtime of a task and log it to the console with the provided action in its parameter.
     *
     * @param action   the action
     * @param task     the task to measure
     * @param runnable the task to run
     */
    public void logTimeWithAction(String action, String task, Runnable runnable) {
        long start = System.currentTimeMillis();
        boolean success = false;
        try {
            runnable.run();
            success = true;
        } catch (Exception exception) {
            logException("Failed to " + action + " the " + task + " task", exception);
        } finally {
            long runtime = System.currentTimeMillis() - start;
            String prefix = success ? TASK_PREFIX_SUCCESS : TASK_PREFIX_FAIL;
            String message = success ? "&fSuccessfully " + action + "&f the &6" : "&cFailed to " + action + "&f the &6";
            consoleSender.sendMessage(CC.translate(prefix + message + task + " &fin &6" + runtime + "ms&f."));
        }
    }

    /**
     * Logs the start of a major initialization phase.
     * @param phaseName The name of the phase (e.g., "Service Setup Phase").
     */
    public void logPhaseStart(String phaseName) {
        consoleSender.sendMessage(CC.translate(""));
        consoleSender.sendMessage(CC.translate(PHASE_HEADER_PREFIX + phaseName.toUpperCase() + PHASE_HEADER_SUFFIX));
    }

    /**
     * Logs the completion of a major initialization phase.
     * @param phaseName The name of the phase (e.g., "Service Initialization").
     */
    public void logPhaseComplete(String phaseName) {
        consoleSender.sendMessage(CC.translate(PHASE_HEADER_PREFIX + phaseName.toUpperCase() + " COMPLETE" + PHASE_HEADER_SUFFIX));
        consoleSender.sendMessage(CC.translate(""));
    }
}