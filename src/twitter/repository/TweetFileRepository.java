package twitter.repository;

import twitter.model.Tweet;

public class TweetFileRepository implements TweetRepository{
    @Override
    public Tweet update(Tweet tweet) {
        return null;
    }

    @Override
    public Tweet add(Tweet tweet) {
        return null;
    }

    @Override
    public Tweet getTweet(long id) {
        return null;
    }

    @Override
    public boolean removeTweet(long id) {
        return false;
    }

    @Override
    public boolean exists(long id) {
        return false;
    }
}
