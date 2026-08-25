package twitter.logic;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import twitter.model.Tweet;
import twitter.repository.AccountFileRepository;
import twitter.repository.TweetFileRepository;
import twitter.support.TestResources;

import java.nio.file.Path;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AccountManagerTest {

    private Path tempRoot;

    @Before
    public void setUp() throws Exception {
        tempRoot = TestResources.createIsolatedRoot();
        TestResources.useIsolatedRoot(tempRoot);
        AccountFileRepository.getInstance().setUser(null);
    }

    @After
    public void tearDown() throws Exception {
        AccountFileRepository.getInstance().setUser(null);
        TestResources.deleteRecursively(tempRoot);
        twitter.utils.AppPaths.resetResourcesRootForTests();
    }

    @Test
    public void unlikeRemovesLikeRecordsFromTweet() throws Exception {
        TweetManager tweetManager = new TweetManager();
        AccountManager accountManager = new AccountManager(tweetManager);
        accountManager.createAccount("alice", "password");
        accountManager.login("alice", "password");

        Tweet tweet = new Tweet(accountManager.getUser().getId(), Tweet.DEFAULT_ID, "hello");
        tweetManager.writeTweet(tweet);

        accountManager.refreshLoggedInUser();
        assertEquals(1, accountManager.getUser().getTweets().size());

        accountManager.likeOrRemoveLike(tweet);
        assertTrue(accountManager.isLiked(tweet));

        Tweet reloaded = TweetFileRepository.getInstance().getTweet(tweet.getId());
        accountManager.likeOrRemoveLike(reloaded);

        Tweet updated = TweetFileRepository.getInstance().getTweet(tweet.getId());
        assertFalse(accountManager.isLiked(updated));
        assertTrue(updated.getIdAccountLiked().isEmpty());
        assertTrue(updated.getAccountLiked().isEmpty());
    }

    @Test
    public void deleteAccountPreservesRetweetedOriginalTweet() throws Exception {
        TweetManager tweetManager = new TweetManager();
        AccountManager accountManager = new AccountManager(tweetManager);
        accountManager.createAccount("author", "password");

        Tweet original = new Tweet(accountManager.getUser().getId(), Tweet.DEFAULT_ID, "original");
        tweetManager.writeTweet(original);
        long originalId = original.getId();

        accountManager.createAccount("retweeter", "password");
        accountManager.login("retweeter", "password");

        Tweet loadedOriginal = TweetFileRepository.getInstance().getTweet(originalId);
        accountManager.retweetOrRemoveRetweet(loadedOriginal);
        accountManager.deleteAccount();

        assertTrue(TweetFileRepository.getInstance().exists(originalId));
    }
}
