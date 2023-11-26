package twitter.repository;

import twitter.model.Tweet;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public interface TweetRepository {

    Tweet update(Tweet tweet) throws IOException;

    Tweet add(Tweet tweet) throws IOException;

    Tweet getTweet(long id);

    boolean removeTweet(long id);

    boolean exists(long id);
}
