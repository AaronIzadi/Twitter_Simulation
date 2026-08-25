package twitter.support;

import twitter.repository.Repository;
import twitter.utils.AppPaths;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class TestResources {

    private TestResources() {
    }

    public static Path createIsolatedRoot() throws IOException {
        Path root = Files.createTempDirectory("twitter-sim-test-");
        Files.createDirectories(root.resolve("data").resolve("account"));
        Files.createDirectories(root.resolve("data").resolve("tweet"));
        Files.createDirectories(root.resolve("app info"));
        Files.write(root.resolve("app info").resolve("account.txt"), "0".getBytes(StandardCharsets.UTF_8));
        Files.write(root.resolve("app info").resolve("tweet.txt"), "0".getBytes(StandardCharsets.UTF_8));
        return root;
    }

    public static void useIsolatedRoot(Path root) throws IOException {
        AppPaths.setResourcesRootForTests(root);
        Repository.getInstance().getIdCounter();
    }

    public static void deleteRecursively(Path root) throws IOException {
        if (root == null || !Files.exists(root)) {
            return;
        }
        Files.walk(root)
                .sorted((a, b) -> b.compareTo(a))
                .forEach(path -> {
                    try {
                        Files.deleteIfExists(path);
                    } catch (IOException ignored) {
                        // best effort cleanup for temp test data
                    }
                });
    }
}
