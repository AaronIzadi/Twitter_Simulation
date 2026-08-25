package twitter.utils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class AppPaths {

    private static final String ENV_HOME = "TWITTER_SIM_HOME";
    private static final Path RESOURCES_ROOT = resolveResourcesRoot();

    private AppPaths() {
    }

    public static Path resourcesRoot() {
        return RESOURCES_ROOT;
    }

    public static Path accountDataDir() {
        return RESOURCES_ROOT.resolve("data").resolve("account");
    }

    public static Path tweetDataDir() {
        return RESOURCES_ROOT.resolve("data").resolve("tweet");
    }

    public static Path appInfoDir() {
        return RESOURCES_ROOT.resolve("app info");
    }

    public static Path logFile() {
        return RESOURCES_ROOT.resolve("log").resolve("logging.txt");
    }

    private static Path resolveResourcesRoot() {
        String envHome = System.getenv(ENV_HOME);
        if (!isBlank(envHome)) {
            Path configured = Paths.get(envHome.trim()).toAbsolutePath().normalize();
            if (Files.isDirectory(configured.resolve("data").resolve("account"))) {
                return configured;
            }
            Path fromProjectRoot = configured.resolve("src").resolve("resources");
            if (Files.isDirectory(fromProjectRoot.resolve("data").resolve("account"))) {
                return fromProjectRoot;
            }
        }

        Path cwd = Paths.get(System.getProperty("user.dir")).toAbsolutePath().normalize();
        Path discovered = findResourcesRoot(cwd);
        if (discovered != null) {
            return discovered;
        }

        return cwd.resolve("src").resolve("resources");
    }

    private static Path findResourcesRoot(Path start) {
        Path current = start;
        while (current != null) {
            Path candidate = current.resolve("src").resolve("resources");
            if (Files.isDirectory(candidate.resolve("data").resolve("account"))) {
                return candidate;
            }
            current = current.getParent();
        }
        return null;
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
