package twitter.repository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import twitter.logic.AccountManager;
import twitter.logic.TweetManager;
import twitter.model.Account;
import twitter.model.Tweet;
import twitter.support.TestResources;
import twitter.utils.PasswordHasher;

import java.nio.file.Path;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class AccountFileRepositoryTest {

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
    public void writeTweetAddsTweetToAccountFile() throws Exception {
        TweetManager tweetManager = new TweetManager();
        AccountManager accountManager = new AccountManager(tweetManager);
        accountManager.createAccount("writer", "password");

        Tweet tweet = new Tweet(accountManager.getUser().getId(), Tweet.DEFAULT_ID, "stored tweet");
        tweetManager.writeTweet(tweet);

        Account loaded = AccountFileRepository.getInstance().getAccount(accountManager.getUser().getId());
        assertEquals(1, loaded.getTweets().size());
        assertEquals(tweet.getId(), (long) loaded.getTweets().get(0));
    }

    @Test
    public void persistsAndLoadsAccountByUsername() throws Exception {
        Account account = new Account("bob", "secret", Account.DEFAULT);
        account.setPassword(PasswordHasher.hash("secret"));

        AccountFileRepository repository = AccountFileRepository.getInstance();
        repository.add(account);

        Account loaded = repository.getAccountByUserName("bob");
        assertNotNull(loaded);
        assertEquals("bob", loaded.getUserName());
        assertTrue(PasswordHasher.matches("secret", loaded.getPassword()));
    }
}
