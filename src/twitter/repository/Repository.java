package twitter.repository;

import twitter.model.Account;
import twitter.model.Tweet;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class Repository {

    private static final String APP_INFO_DIR = "src/resources/app info";
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
        Account.setIdCounter(readCounter(ACCOUNT_COUNTER_FILE));
        Tweet.setIdCounter(readCounter(TWEET_COUNTER_FILE));
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
}
