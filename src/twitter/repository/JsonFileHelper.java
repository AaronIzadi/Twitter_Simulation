package twitter.repository;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

final class JsonFileHelper {

    private JsonFileHelper() {
    }

    static Path entityFile(String directory, long id) {
        return Paths.get(directory, id + ".txt");
    }

    static JSONObject readJson(Path filePath) throws IOException {
        String content = Files.readAllLines(filePath, StandardCharsets.UTF_8)
                .stream()
                .collect(Collectors.joining("\n"));
        try {
            return (JSONObject) new JSONParser().parse(content);
        } catch (ParseException e) {
            throw new IOException("Failed to parse JSON file: " + filePath, e);
        }
    }

    static void writeJson(Path filePath, JSONObject jsonObject) throws IOException {
        try (PrintWriter writer = new PrintWriter(filePath.toString(), StandardCharsets.UTF_8.name())) {
            writer.println(jsonObject);
        }
    }

    static void putLongList(JSONObject json, String countKey, String itemPrefix, List<Long> values) {
        json.put(countKey, values.size());
        putLongItems(json, itemPrefix, values);
    }

    static void putLongItems(JSONObject json, String itemPrefix, List<Long> values) {
        for (int i = 0; i < values.size(); i++) {
            json.put(itemPrefix + i, values.get(i));
        }
    }

    static LinkedList<Long> readLongList(JSONObject json, String countKey, String itemPrefix) {
        LinkedList<Long> values = new LinkedList<>();
        long count = (Long) json.get(countKey);
        for (int i = 0; i < count; i++) {
            values.add((Long) json.get(itemPrefix + i));
        }
        return values;
    }

    static <T> void putMappedList(JSONObject json, String countKey, String itemPrefix, List<T> values, Function<T, String> mapper) {
        json.put(countKey, values.size());
        putMappedItems(json, itemPrefix, values, mapper);
    }

    static <T> void putMappedItems(JSONObject json, String itemPrefix, List<T> values, Function<T, String> mapper) {
        for (int i = 0; i < values.size(); i++) {
            json.put(itemPrefix + i, mapper.apply(values.get(i)));
        }
    }

    static <T> LinkedList<T> readMappedList(JSONObject json, String countKey, String itemPrefix, Function<String, T> mapper) {
        LinkedList<T> values = new LinkedList<>();
        long count = (Long) json.get(countKey);
        for (int i = 0; i < count; i++) {
            values.add(mapper.apply((String) json.get(itemPrefix + i)));
        }
        return values;
    }

}
