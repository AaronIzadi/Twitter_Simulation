package twitter.state;

import twitter.utils.ConsoleColors;
import twitter.Context;
import twitter.logic.AccountManager;
import twitter.logic.TweetManager;
import twitter.model.Tweet;
import twitter.utils.Logger;

import java.io.IOException;

public class MakeTweetState extends State{

    private final long fatherId;

    public MakeTweetState(long fatherId) {
        this.fatherId = fatherId;
    }

    @Override
    public void printCliMenu(Context context) {
        System.out.println(ConsoleColors.YELLOW + "Write..");
    }

    @Override
    public State doAction(Context context) throws IOException {

        printCliMenu(context);

        AccountManager accountManager = context.getAccountManager();
        TweetManager tweetManager = context.getTweetManager();
        Logger log = context.getLogger();

        String text = context.getScanner().nextLine();

        Tweet tweet = new Tweet(accountManager.getUser().getId(), fatherId, text);
        tweetManager.writeTweet(tweet);

        log.info("User wrote a tweet with id:" + tweet.getId());

        return null;
    }

    @Override
    public void printFinalCliError() {

    }

    @Override
    public void close(Context context) {

    }
}
