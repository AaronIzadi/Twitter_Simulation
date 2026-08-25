package twitter.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TweetEngagementTest {

    @Test
    public void removeAccountLikeRemovesIdAndRecord() {
        Tweet tweet = new Tweet(1, Tweet.DEFAULT_ID, "hello");
        tweet.addIdAccountLiked(2);
        tweet.addAccountLiked(new Record(2, Time.now(), Record.LIKE_RECORD));
        tweet.setNumberOfLikes();

        tweet.removeAccountLike(2);

        assertTrue(tweet.getIdAccountLiked().isEmpty());
        assertTrue(tweet.getAccountLiked().isEmpty());
        assertEquals(0, tweet.getNumberOfLikes());
    }

    @Test
    public void removeAccountRetweetRemovesIdAndRecord() {
        Tweet tweet = new Tweet(1, Tweet.DEFAULT_ID, "hello");
        tweet.addIdAccountRetweeted(3);
        tweet.addAccountRetweeted(new Record(3, Time.now(), Record.RETWEET_RECORD));
        tweet.setNumberOfRetweets();

        tweet.removeAccountRetweet(3);

        assertTrue(tweet.getIdAccountRetweeted().isEmpty());
        assertTrue(tweet.getAccountRetweeted().isEmpty());
        assertEquals(0, tweet.getNumberOfRetweets());
    }

    @Test
    public void removeAccountLikeKeepsOtherUsers() {
        Tweet tweet = new Tweet(1, Tweet.DEFAULT_ID, "hello");
        tweet.addIdAccountLiked(2);
        tweet.addAccountLiked(new Record(2, Time.now(), Record.LIKE_RECORD));
        tweet.addIdAccountLiked(4);
        tweet.addAccountLiked(new Record(4, Time.now(), Record.LIKE_RECORD));
        tweet.setNumberOfLikes();

        tweet.removeAccountLike(2);

        assertFalse(tweet.getIdAccountLiked().contains(2L));
        assertTrue(tweet.getIdAccountLiked().contains(4L));
        assertEquals(1, tweet.getAccountLiked().size());
        assertEquals(1, tweet.getNumberOfLikes());
    }
}
