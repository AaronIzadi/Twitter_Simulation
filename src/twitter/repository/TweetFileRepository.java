package twitter.repository;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import twitter.model.Account;
import twitter.model.Record;
import twitter.model.Time;
import twitter.model.Tweet;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

public class TweetFileRepository implements TweetRepository {

    private final String path = "src/resources/data/tweet/";
    private JSONObject jsonObject = new JSONObject();
    private static final TweetFileRepository instance = new TweetFileRepository();

    public static TweetFileRepository getInstance() {
        return instance;
    }

    @Override
    public Tweet update(Tweet tweet) throws IOException {
        if (!exists(tweet.getId())) {
            add(tweet);
        } else {
            FileWriter fileWriter = new FileWriter(path + tweet.getId() + ".txt", false);
            PrintWriter printWriter = new PrintWriter(fileWriter, false);
            writeInJson(tweet);
            printWriter.println(jsonObject);
            printWriter.flush();
            printWriter.close();
            fileWriter.close();
            jsonObject.clear();

            return tweet;
        }
        return null;
    }

    @Override
    public Tweet add(Tweet tweet) throws IOException {
        Repository.addAppInfo(tweet, Account.getIdCounter());
        if (exists(tweet.getId())) {
            update(tweet);
        } else {
            PrintWriter writer = new PrintWriter(path + tweet.getId() + ".txt", "UTF-8");
            writeInJson(tweet);
            writer.print(jsonObject);
            jsonObject.clear();
            writer.close();
        }
        return tweet;
    }

    @Override
    public Tweet getTweet(long id) throws IOException {
        File folder = new File(path.substring(0, path.length() - 1));
        File[] listOfFiles = folder.listFiles();

        assert listOfFiles != null;
        for (File file : listOfFiles) {
            if (file.isFile()) {
                if (file.getName().equals(id + ".txt")) {
                    Tweet tweet = readByJson(id);
                    clearJsonData();
                    return tweet;
                }
            }
        }
        return null;
    }


    @Override
    public boolean removeTweet(long id) {
        if (exists(id)) {
            File file = new File(path + id + ".txt");
            return file.delete();
        }
        return false;
    }

    @Override
    public boolean exists(long id) {
        File tweet = new File(path + id + ".txt");
        return tweet.exists() && !tweet.isDirectory();
    }

    public void writeInJson(Tweet tweet) {

        jsonObject.put("id", tweet.getId());
        jsonObject.put("id counter", tweet.getIdCounter());
        jsonObject.put("account id", tweet.getAccountId());
        jsonObject.put("text", tweet.getTextOfTweet());
        jsonObject.put("like num", tweet.getNumberOfLikes());
        jsonObject.put("retweet num", tweet.getNumberOfRetweets());
        jsonObject.put("reply num", tweet.getNumberOfReplies());
        jsonObject.put("id replied tweet", tweet.getIdRepliedTweet());
        jsonObject.put("time", tweet.getTweetTime().toString());
        jsonObject.put("record", tweet.getRecord().toString());
        jsonObject.put("saved num", tweet.getIdAccountSaved().size());
        for (int i = 0; i < tweet.getNumberOfReplies(); i++) {
            jsonObject.put("reply id " + i, tweet.getReplies().get(i));
        }
        for (int i = 0; i < tweet.getNumberOfRetweets(); i++) {
            jsonObject.put("account retweeted record " + i, tweet.getAccountRetweeted().get(i).toString());
        }
        for (int i = 0; i < tweet.getAccountLiked().size(); i++) {
            jsonObject.put("account liked record " + i, tweet.getAccountLiked().get(i).toString());
        }
        for (int i = 0; i < tweet.getIdAccountLiked().size(); i++) {
            jsonObject.put("account liked " + i, tweet.getIdAccountLiked().get(i));
        }
        for (int i = 0; i < tweet.getIdAccountRetweeted().size(); i++) {
            jsonObject.put("account retweeted " + i, tweet.getIdAccountRetweeted().get(i));
        }
        for (int i = 0; i < tweet.getIdAccountSaved().size(); i++) {
            jsonObject.put("account saved " + i, tweet.getIdAccountSaved().get(i));
        }
    }

    public void jsonToReadFile(long id) throws IOException {
        String address = path + id + ".txt";

        Charset encoding = Charset.defaultCharset();
        List<String> lines = Files.readAllLines(Paths.get(address), encoding);

        JSONParser parser = new JSONParser();
        String string = String.join("\n", lines);
        try {
            jsonObject = (JSONObject) parser.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public Tweet readByJson(long id) throws IOException {

        jsonToReadFile(id);

        Long tweetId = (Long) jsonObject.get("id");
        Long idCounter = (Long) jsonObject.get("id counter");
        Long accountId = (Long) jsonObject.get("account id");
        String text = (String) jsonObject.get("text");
        Long likeAsLong = (Long) jsonObject.get("like num");
        int like = likeAsLong.intValue();
        Long retweetAsLong = (Long) jsonObject.get("retweet num");
        int retweet = retweetAsLong.intValue();
        Long replyAsLong = (Long) jsonObject.get("reply num");
        int replyId = replyAsLong.intValue();
        Time time = Time.valueOf((String) jsonObject.get("time"));
        Record record = Record.valueOf((String) jsonObject.get("record"));
        Long savedAsLong = (Long) jsonObject.get("saved num");
        int saved = savedAsLong.intValue();
        LinkedList<Record> accountsRetweeted = new LinkedList<>();
        for (int i = 0; i < retweet; i++) {
            accountsRetweeted.add(Record.valueOf((String) jsonObject.get("account retweeted record " + i)));
        }
        LinkedList<Record> accountsLiked = new LinkedList<>();
        for (int i = 0; i < like; i++) {
            accountsLiked.add(Record.valueOf((String) jsonObject.get("account liked record " + i)));
        }
        LinkedList<Long> idAccountsLiked = new LinkedList<>();
        for (int i = 0; i < like; i++) {
            idAccountsLiked.add((Long) jsonObject.get("account liked " + i));
        }
        LinkedList<Long> idAccountsRetweeted = new LinkedList<>();
        for (int i = 0; i < like; i++) {
            idAccountsRetweeted.add((Long) jsonObject.get("account retweeted " + i));
        }
        LinkedList<Long> idAccountSaved = new LinkedList<>();
        for (int i = 0; i < saved; i++) {
            idAccountSaved.add((Long) jsonObject.get("account saved " + i));
        }
        LinkedList<Long> idReplies = new LinkedList<>();
        for (int i = 0; i < saved; i++) {
            idReplies.add((Long) jsonObject.get("reply id " + i));
        }

        Tweet tweet = new Tweet(accountId, replyId, text);
        tweet.setId(tweetId);
        Tweet.setIdCounter(idCounter);
        tweet.setTweetTime(time);
        tweet.setRecord(record);
        tweet.setReplies(idReplies);
        tweet.setAccountLiked(accountsLiked);
        tweet.setAccountRetweeted(accountsRetweeted);
        tweet.setIdAccountLiked(idAccountsLiked);
        tweet.setIdAccountRetweeted(idAccountsRetweeted);
        tweet.setIdAccountSaved(idAccountSaved);
        tweet.setNumberOfLikes();
        tweet.setNumberOfRetweets();
        tweet.setNumberOfReplies();

        return tweet;

    }

    public void clearJsonData() {
        jsonObject.clear();
    }

}
