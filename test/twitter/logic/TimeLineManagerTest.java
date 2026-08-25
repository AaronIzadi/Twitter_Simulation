package twitter.logic;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import twitter.model.Record;
import twitter.model.Tweet;
import twitter.repository.AccountFileRepository;
import twitter.support.TestResources;

import java.nio.file.Path;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class TimeLineManagerTest {

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
    public void timelineIncludesUsersOwnTweet() throws Exception {
        AccountManager accountManager = new AccountManager();
        accountManager.createAccount("alice", "password");

        TweetManager tweetManager = new TweetManager();
        Tweet tweet = new Tweet(accountManager.getUser().getId(), Tweet.DEFAULT_ID, "timeline tweet");
        tweetManager.writeTweet(tweet);
        accountManager.refreshLoggedInUser();

        Map<Record, Tweet> timeline = new TimeLineManager().makeTimeLine(accountManager);

        assertEquals(1, accountManager.getUser().getTweets().size());
        assertEquals(1, timeline.size());
        assertEquals(tweet.getId(), timeline.values().iterator().next().getId());
    }
}
