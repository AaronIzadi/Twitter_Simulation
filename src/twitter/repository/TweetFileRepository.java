package twitter.repository;

import org.json.simple.JSONObject;
import twitter.model.Account;
import twitter.model.Record;
import twitter.model.Time;
import twitter.model.Tweet;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class TweetFileRepository implements TweetRepository {

    private static final String TWEET_DIR = "src/resources/data/tweet/";
    private static final TweetFileRepository instance = new TweetFileRepository();

    public static TweetFileRepository getInstance() {
        return instance;
    }

    @Override
    public Tweet update(Tweet tweet) throws IOException {
        if (!exists(tweet.getId())) {
            return add(tweet);
        }
        save(tweet);
        return tweet;
    }

    @Override
    public Tweet add(Tweet tweet) throws IOException {
        Repository.addAppInfo(tweet, Account.getIdCounter());
        if (exists(tweet.getId())) {
            return update(tweet);
        }
        save(tweet);
        return tweet;
    }

    @Override
    public Tweet getTweet(long id) throws IOException {
        if (!exists(id)) {
            return null;
        }
        return readFromFile(id);
    }

    @Override
    public boolean removeTweet(long id) {
        if (!exists(id)) {
            return false;
        }
        return JsonFileHelper.entityFile(TWEET_DIR, id).toFile().delete();
    }

    @Override
    public boolean exists(long id) {
        File tweet = JsonFileHelper.entityFile(TWEET_DIR, id).toFile();
        return tweet.exists() && !tweet.isDirectory();
    }

    private void save(Tweet tweet) throws IOException {
        JsonFileHelper.writeJson(filePath(tweet.getId()), toJson(tweet));
    }

    private Tweet readFromFile(long id) throws IOException {
        return fromJson(JsonFileHelper.readJson(filePath(id)));
    }

    private Path filePath(long id) {
        return JsonFileHelper.entityFile(TWEET_DIR, id);
    }

    private JSONObject toJson(Tweet tweet) {
        JSONObject json = new JSONObject();

        json.put("id", tweet.getId());
        json.put("id counter", tweet.getIdCounter());
        json.put("account id", tweet.getAccountId());
        json.put("text", tweet.getTextOfTweet());
        json.put("id replied tweet", tweet.getIdRepliedTweet());
        json.put("time", tweet.getTweetTime().toString());
        json.put("record", tweet.getRecord().toString());

        json.put("reply num", tweet.getNumberOfReplies());
        JsonFileHelper.putLongItems(json, "reply id ", tweet.getReplies());

        json.put("retweet num", tweet.getNumberOfRetweets());
        JsonFileHelper.putMappedItems(json, "account retweeted record ", tweet.getAccountRetweeted(), Record::toString);
        JsonFileHelper.putLongItems(json, "account retweeted ", tweet.getIdAccountRetweeted());

        json.put("like num", tweet.getNumberOfLikes());
        JsonFileHelper.putMappedItems(json, "account liked record ", tweet.getAccountLiked(), Record::toString);
        JsonFileHelper.putLongItems(json, "account liked ", tweet.getIdAccountLiked());

        json.put("saved num", tweet.getIdAccountSaved().size());
        JsonFileHelper.putLongItems(json, "account saved ", tweet.getIdAccountSaved());

        return json;
    }

    private Tweet fromJson(JSONObject json) {
        long tweetId = (Long) json.get("id");
        long accountId = (Long) json.get("account id");
        long idRepliedTweet = (Long) json.get("id replied tweet");
        String text = (String) json.get("text");

        Tweet tweet = new Tweet(accountId, idRepliedTweet, text);
        tweet.setId(tweetId);
        Tweet.setIdCounter((Long) json.get("id counter"));
        tweet.setTweetTime(Time.valueOf((String) json.get("time")));
        tweet.setRecord(Record.valueOf((String) json.get("record")));
        tweet.setReplies(JsonFileHelper.readLongList(json, "reply num", "reply id "));
        tweet.setAccountRetweeted(JsonFileHelper.readMappedList(json, "retweet num", "account retweeted record ", Record::valueOf));
        tweet.setAccountLiked(JsonFileHelper.readMappedList(json, "like num", "account liked record ", Record::valueOf));
        tweet.setIdAccountLiked(JsonFileHelper.readLongList(json, "like num", "account liked "));
        tweet.setIdAccountRetweeted(JsonFileHelper.readLongList(json, "retweet num", "account retweeted "));
        tweet.setIdAccountSaved(JsonFileHelper.readLongList(json, "saved num", "account saved "));
        tweet.setNumberOfLikes();
        tweet.setNumberOfRetweets();
        tweet.setNumberOfReplies();

        return tweet;
    }

}
