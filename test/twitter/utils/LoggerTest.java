package twitter.utils;

import org.junit.Test;

import java.nio.file.Files;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class LoggerTest {

    @Test
    public void writesStructuredLogEntry() throws Exception {
        Logger logger = new Logger();
        logger.setSessionUser("tester");
        logger.info("Test log entry");

        List<String> lines = Files.readAllLines(AppPaths.logFile());
        String lastLine = lines.get(lines.size() - 1);

        assertTrue(lastLine.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2} INFO  \\[user=tester\\] Test log entry"));
    }
}
