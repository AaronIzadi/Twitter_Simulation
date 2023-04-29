package twitter.repository;

import twitter.model.Tweet;

public interface TweetRepository {

    Tweet update(Tweet tweet);

    Tweet add(Tweet tweet);

    Tweet getTweet(long id);

    boolean removeTweet(long id);

    boolean exists(long id);
}
