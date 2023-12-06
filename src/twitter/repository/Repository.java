package twitter.repository;

import twitter.model.Account;

import twitter.model.Tweet;

import java.io.*;


public class Repository {

    private static final Repository instance = new Repository();

    public static Repository getInstance() {
        return instance;
    }

    public static void addAppInfo(Object object, long idCounter) throws FileNotFoundException, UnsupportedEncodingException {

        String path;

        if (object instanceof Account) {
            path = "src/resources/app info/account.txt";
        } else {
            path = "src/resources/app info/tweet.txt";
        }

        PrintWriter writer = new PrintWriter(path, "UTF-8");
        writer.print(idCounter);
        writer.close();
    }

    public void getIdCounter() throws IOException {

        File account = new File("src/resources/app info/account.txt");
        File tweet = new File("src/resources/app info/tweet.txt");

        BufferedReader bufferedReader = new BufferedReader(new FileReader(account));
        String string;
        long idCounterAccount = 0;
        while ((string = bufferedReader.readLine()) != null) {
            idCounterAccount = Long.parseLong(string);
        }
        Account.setIdCounter(idCounterAccount);

        bufferedReader = new BufferedReader(new FileReader(tweet));
        long idCounterTweet = 0;
        while ((string = bufferedReader.readLine()) != null) {
            idCounterTweet = Long.parseLong(string);
        }
        Tweet.setIdCounter(idCounterTweet);

    }
}
