package twitter.state;

import twitter.utils.ConsoleColors;
import twitter.main.Context;
import twitter.logic.AccountManager;
import twitter.logic.TweetManager;
import twitter.model.Tweet;
import twitter.utils.InputValidator;
import twitter.utils.Logger;

import java.io.IOException;

public class MakeTweetState extends State {

    private final long fatherId;

    public MakeTweetState(long fatherId) {
        this.fatherId = fatherId;
    }

    @Override
    public void printCliMenu(Context context) {
        System.out.println(ConsoleColors.YELLOW + "What's happening?");
    }

    @Override
    public State doAction(Context context) throws IOException {

        printCliMenu(context);

        AccountManager accountManager = context.getAccountManager();
        TweetManager tweetManager = context.getTweetManager();
        Logger log = context.getLogger();

        String text = context.getScanner().nextLine();

        if (!InputValidator.isValidTweetText(text)) {
            System.out.println(ConsoleColors.RED + "Tweet cannot be empty and must be at most 280 characters.");
            log.warn("Tweet creation failed: invalid text");
            return this;
        }

        Tweet tweet = new Tweet(accountManager.getUser().getId(), fatherId, text.trim());
        tweetManager.writeTweet(tweet);

        log.info("Tweet created | tweetId=" + tweet.getId());

        return null;
    }

    @Override
    public void printFinalCliError() {

    }

    @Override
    public void close(Context context) {

    }
}
