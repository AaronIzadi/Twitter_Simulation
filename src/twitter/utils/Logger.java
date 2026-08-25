package twitter.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {

    private static final Path LOG_FILE = AppPaths.logFile();
    private static final DateTimeFormatter TIMESTAMP = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final Object LOCK = new Object();

    private String sessionUser = "-";

    public void setSessionUser(String username) {
        sessionUser = isBlank(username) ? "-" : username;
    }

    public void clearSessionUser() {
        sessionUser = "-";
    }

    public void info(String message) {
        write(LogLevel.INFO, message);
    }

    public void warn(String message) {
        write(LogLevel.WARN, message);
    }

    public void error(String message) {
        write(LogLevel.ERROR, message);
    }

    public void error(String message, Throwable throwable) {
        write(LogLevel.ERROR, message + " | error=" + throwable.getMessage());
    }

    private void write(LogLevel level, String message) {
        if (isBlank(message)) {
            return;
        }

        String entry = String.format("%s %-5s [user=%s] %s",
                LocalDateTime.now().format(TIMESTAMP),
                level.name(),
                sessionUser,
                message.trim());

        synchronized (LOCK) {
            try {
                Path logPath = LOG_FILE;
                Files.createDirectories(logPath.getParent());
                try (PrintWriter writer = new PrintWriter(
                        new OutputStreamWriter(
                                new FileOutputStream(logPath.toFile(), true),
                                StandardCharsets.UTF_8),
                        false)) {
                    writer.println(entry);
                }
            } catch (IOException e) {
                System.err.println("Failed to write log entry: " + e.getMessage());
            }
        }
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private enum LogLevel {
        INFO,
        WARN,
        ERROR
    }
}
