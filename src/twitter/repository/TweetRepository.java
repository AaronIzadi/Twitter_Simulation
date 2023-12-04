package twitter.repository;

import twitter.model.Tweet;

import java.io.IOException;

public interface TweetRepository {

    Tweet update(Tweet tweet) throws IOException;

    Tweet add(Tweet tweet) throws IOException;

    Tweet getTweet(long id) throws IOException;

    boolean removeTweet(long id);

    boolean exists(long id);
}
