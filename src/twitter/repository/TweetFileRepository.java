package twitter.repository;

import twitter.model.Tweet;

import java.io.*;

public class TweetFileRepository extends Repository implements TweetRepository {

    private final String path = "src/resources/data/tweet/";
    private static final TweetFileRepository instance = new TweetFileRepository();

    public static TweetFileRepository getInstance() {
        return instance;
    }

    @Override
    public Tweet update(Tweet tweet) throws IOException {
        return (Tweet) update(tweet, tweet.getId(), path);
    }

    @Override
    public Tweet add(Tweet tweet) throws IOException {
        addAppInfo(tweet, tweet.getIdCounter());
        return (Tweet) add(tweet, tweet.getId(), path);
    }

    @Override
    public Tweet getTweet(long id) throws IOException {
        return (Tweet) getObject(id, path, Repository.tweetType);
    }


    @Override
    public boolean removeTweet(long id) {
        return removeObject(id, path);
    }

    @Override
    public boolean exists(long id) {
        return exists(id, path);
    }

}
