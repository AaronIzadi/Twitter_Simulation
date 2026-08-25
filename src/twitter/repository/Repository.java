package twitter.repository;

import twitter.model.Account;
import twitter.model.Tweet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class Repository {

    private static final String APP_INFO_DIR = "src/resources/app info";
    private static final String ACCOUNT_DATA_DIR = "src/resources/data/account/";
    private static final String TWEET_DATA_DIR = "src/resources/data/tweet/";
    private static final String ACCOUNT_COUNTER_FILE = APP_INFO_DIR + "/account.txt";
    private static final String TWEET_COUNTER_FILE = APP_INFO_DIR + "/tweet.txt";

    private static final Repository instance = new Repository();

    public static Repository getInstance() {
        return instance;
    }

    public static void addAppInfo(Object object, long idCounter) throws FileNotFoundException, UnsupportedEncodingException {
        String path = object instanceof Account ? ACCOUNT_COUNTER_FILE : TWEET_COUNTER_FILE;
        try (PrintWriter writer = new PrintWriter(path, "UTF-8")) {
            writer.print(idCounter);
        }
    }

    public void getIdCounter() throws IOException {
        long accountCounter = Math.max(readCounter(ACCOUNT_COUNTER_FILE), maxEntityId(ACCOUNT_DATA_DIR) + 1);
        long tweetCounter = Math.max(readCounter(TWEET_COUNTER_FILE), maxEntityId(TWEET_DATA_DIR) + 1);
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

    private long maxEntityId(String directory) {
        File folder = new File(directory);
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
}
