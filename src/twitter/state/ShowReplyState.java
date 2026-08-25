package twitter.state;

import twitter.utils.ConsoleColors;
import twitter.main.Context;
import twitter.logic.AccountManager;
import twitter.logic.TweetManager;
import twitter.model.Tweet;
import twitter.utils.Logger;

import java.io.IOException;

public class ShowReplyState extends State {

    private static final String INVALID_INPUT = ConsoleColors.RED + "Invalid input.";

    private Tweet sourceTweet;
    private int index;

    public void setIndex(int index) {
        this.index = index;
    }

    public void setTweet(Tweet tweet1) {
        this.sourceTweet = tweet1;
    }

    public ShowReplyState(Tweet tweet, int index) {
        this.sourceTweet = tweet;
        this.index = index;
    }

    public ShowReplyState(Tweet tweet) {
        this.sourceTweet = tweet;
        this.index = 0;
    }

    @Override
    public void printCliMenu(Context context) {
        System.out.println(ConsoleColors.YELLOW + "Comments:");
    }

    @Override
    public State doAction(Context context) throws IOException {

        printCliMenu(context);

        AccountManager accountManager = context.getAccountManager();
        TweetManager tweetManager = context.getTweetManager();
        Logger log = context.getLogger();

        if (index >= sourceTweet.getReplies().size()) {
            printFinalCliError();
            return null;
        }

        Tweet replyTweet = tweetManager.getTweet(sourceTweet.getReplies().get(index));
        TweetActionHandler.displayTweet(replyTweet, accountManager);
        log.info("Tweet viewed | tweetId=" + replyTweet.getId());
        TweetActionHandler.printActionMenu(replyTweet, accountManager);

        String choice = context.getScanner().nextLine();
        return TweetActionHandler.handleAction(context, replyTweet, choice, navigation(), false, INVALID_INPUT);
    }

    private TweetActionHandler.Navigation navigation() {
        return new TweetActionHandler.Navigation() {
            @Override
            public State refresh(Tweet tweet) {
                return ShowReplyState.this;
            }

            @Override
            public State next(Tweet tweet) {
                return new ShowReplyState(sourceTweet, index + 1);
            }

            @Override
            public State back() {
                return null;
            }

            @Override
            public State afterDeleteOrMute(Tweet tweet) {
                return ShowReplyState.this;
            }
        };
    }

    @Override
    public void printFinalCliError() {
        System.out.println(ConsoleColors.RED + "There are no more comments to show!");
    }

    @Override
    public void close(Context context) {

    }
}
