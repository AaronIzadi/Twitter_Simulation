package twitter.repository;

import twitter.model.Account;
import twitter.model.Tweet;

import twitter.utils.AppPaths;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;

public class Repository {

    private static final Repository instance = new Repository();

    public static Repository getInstance() {
        return instance;
    }

    public static void addAppInfo(Object object, long idCounter) throws FileNotFoundException, UnsupportedEncodingException {
        String path = object instanceof Account ? accountCounterFile() : tweetCounterFile();
        try (PrintWriter writer = new PrintWriter(path, "UTF-8")) {
            writer.print(idCounter);
        }
    }

    public void getIdCounter() throws IOException {
        long accountCounter = Math.max(readCounter(accountCounterFile()), maxEntityId(AppPaths.accountDataDir()) + 1);
        long tweetCounter = Math.max(readCounter(tweetCounterFile()), maxEntityId(AppPaths.tweetDataDir()) + 1);
        Account.setIdCounter(accountCounter);
        Tweet.setIdCounter(tweetCounter);
    }

    private long readCounter(String path) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            long counter = 0;
            while ((line = reader.readLine()) != null) {
                counter = Long.parseLong(line);
            }
            return counter;
        }
    }

    private long maxEntityId(Path directory) {
        File folder = directory.toFile();
        File[] files = folder.listFiles();
        if (files == null) {
            return 0;
        }

        long maxId = 0;
        for (File file : files) {
            if (!file.isFile() || !file.getName().endsWith(".txt")) {
                continue;
            }
            String idPart = file.getName().substring(0, file.getName().length() - 4);
            try {
                maxId = Math.max(maxId, Long.parseLong(idPart));
            } catch (NumberFormatException ignored) {
                // skip malformed filenames
            }
        }
        return maxId;
    }

    private static String accountCounterFile() {
        return AppPaths.appInfoDir().resolve("account.txt").toString();
    }

    private static String tweetCounterFile() {
        return AppPaths.appInfoDir().resolve("tweet.txt").toString();
    }
}
