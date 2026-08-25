package twitter.utils;

import org.junit.Test;

import java.nio.file.Files;

import static org.junit.Assert.assertTrue;

public class AppPathsTest {

    @Test
    public void dataDirectoriesAreUnderResourcesRoot() {
        assertTrue(AppPaths.accountDataDir().startsWith(AppPaths.resourcesRoot()));
        assertTrue(AppPaths.tweetDataDir().startsWith(AppPaths.resourcesRoot()));
        assertTrue(AppPaths.logFile().startsWith(AppPaths.resourcesRoot()));
    }

    @Test
    public void resolvesExistingProjectDataDirectory() {
        assertTrue(Files.isDirectory(AppPaths.accountDataDir()));
        assertTrue(Files.isDirectory(AppPaths.tweetDataDir()));
    }
}
