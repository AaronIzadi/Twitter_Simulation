package twitter.state;

import twitter.utils.ConsoleColors;
import twitter.main.Context;
import twitter.logic.AccountManager;
import twitter.logic.TweetManager;
import twitter.model.Tweet;
import twitter.utils.Logger;

import java.io.IOException;

public class TweetListState extends State {

    private static final String INVALID_INPUT = ConsoleColors.RED + "Invalid input.";

    private String username;
    private int index;

    public void setIndex(int index) {
        this.index = index;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public TweetListState(String username, int index) {
        this.username = username;
        this.index = index;
    }

    public TweetListState(String username) {
        this.username = username;
        this.index = 0;
    }

    @Override
    public void printCliMenu(Context context) {
        System.out.println(ConsoleColors.BLUE + "@" + username + "'s tweets:");
    }

    @Override
    public State doAction(Context context) throws IOException {

        printCliMenu(context);

        AccountManager accountManager = context.getAccountManager();
        TweetManager tweetManager = context.getTweetManager();
        Logger log = context.getLogger();

        if (!accountManager.canViewProfileContent(username)) {
            System.out.println(ConsoleColors.RED + "This account is private. You can't view their tweets.");
            return null;
        }

        if (index >= accountManager.getTweetList(username).size()) {
            printFinalCliError();
            return null;
        }

        Tweet tweet = tweetManager.getTweet(accountManager.getTweetList(username).get(index));
        TweetActionHandler.displayTweet(tweet, accountManager);
        log.info("Tweet viewed | tweetId=" + tweet.getId());
        TweetActionHandler.printActionMenu(tweet, accountManager);

        String choice = context.getScanner().nextLine();
        return TweetActionHandler.handleAction(context, tweet, choice, navigation(), true, INVALID_INPUT);
    }

    private TweetActionHandler.Navigation navigation() {
        return new TweetActionHandler.Navigation() {
            @Override
            public State refresh(Tweet tweet) {
                return TweetListState.this;
            }

            @Override
            public State next(Tweet tweet) {
                return new TweetListState(username, index + 1);
            }

            @Override
            public State back() {
                return null;
            }

            @Override
            public State afterDeleteOrMute(Tweet tweet) {
                return TweetListState.this;
            }
        };
    }

    @Override
    public void printFinalCliError() {
        System.out.println(ConsoleColors.RED + "There are no more tweets to show!");
    }

    @Override
    public void close(Context context) {

    }
}
