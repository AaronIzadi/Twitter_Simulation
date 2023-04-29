package twitter.repository;

import twitter.model.Tweet;

import java.util.HashMap;

public class TweetHashMapRepository implements TweetRepository{

    private static TweetRepository instance = new TweetHashMapRepository();
    private HashMap<Long, Tweet> map = new HashMap<>();
    private long id;

    private TweetHashMapRepository() {

    }

    public static TweetRepository getInstance() {
        return instance;
    }


    @Override
    public Tweet update(Tweet tweet) {
        return map.replace(tweet.getId(),tweet);
    }

    @Override
    public Tweet add(Tweet tweet) {
        tweet.setId(id);
        id++;
        return map.put(tweet.getId(), tweet);
    }

    @Override
    public Tweet getTweet(long id) { return map.get(id); }

    @Override
    public boolean removeTweet(long id) {
        Tweet tweet = map.remove(id);
        return tweet != null;
    }

    @Override
    public boolean exists(long id) {
        return map.containsKey(id);
    }
}
