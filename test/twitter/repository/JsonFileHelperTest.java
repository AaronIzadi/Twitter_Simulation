package twitter.repository;

import org.json.simple.JSONObject;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class JsonFileHelperTest {

    @Test
    public void roundTripsLongListThroughJsonFile() throws Exception {
        Path file = Files.createTempFile("twitter-test", ".txt");
        JSONObject json = new JSONObject();
        LinkedList<Long> values = new LinkedList<>(Arrays.asList(1L, 2L, 3L));

        JsonFileHelper.putLongList(json, "count", "item ", values);
        JsonFileHelper.writeJson(file, json);

        JSONObject loaded = JsonFileHelper.readJson(file);
        LinkedList<Long> restored = JsonFileHelper.readLongList(loaded, "count", "item ");

        assertEquals(values, restored);
        assertTrue(Files.deleteIfExists(file));
    }
}
