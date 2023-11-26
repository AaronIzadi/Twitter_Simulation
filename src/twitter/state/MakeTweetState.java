package twitter.state;

import twitter.utils.ConsoleColors;
import twitter.Context;
import twitter.logic.AccountManager;
import twitter.logic.TweetManager;
import twitter.model.Tweet;
import twitter.utils.Logger;

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
    public State doAction(Context context) {

        printCliMenu(context);

        AccountManager accountManager = context.getHandleAccount();
        TweetManager tweetManager = context.getHandleTweet();
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
