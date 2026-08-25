package twitter.state;

import twitter.utils.ConsoleColors;
import twitter.main.Context;
import twitter.logic.AccountManager;
import twitter.logic.TweetManager;
import twitter.model.Record;
import twitter.model.Tweet;
import twitter.utils.Logger;

import java.io.IOException;
import java.util.Map;

public class TimeLineState extends State {

    private static final String INVALID_INPUT = ConsoleColors.RED + "Invalid input.";

    private final int index;
    private Map<Record, Tweet> map;

    public TimeLineState(Map<Record, Tweet> map, int index) {
        this.map = map;
        this.index = index;
    }

    public TimeLineState(Context context) throws IOException {
        this(makeTimeline(context), 0);
    }

    public TimeLineState(Context context, int index) throws IOException {
        this(makeTimeline(context), index);
    }

    private static Map<Record, Tweet> makeTimeline(Context context) throws IOException {
        context.getAccountManager().refreshLoggedInUser();
        return context.getTimeLineManager().makeTimeLine(context.getAccountManager());
    }

    @Override
    public void printCliMenu(Context context) {
        System.out.println(ConsoleColors.YELLOW + "Welcome to the timeline!");
    }

    @Override
    public State doAction(Context context) throws IOException {

        printCliMenu(context);

        AccountManager accountManager = context.getAccountManager();
        Logger log = context.getLogger();

        if (map.isEmpty() || index >= map.size()) {
            printFinalCliError();
            return null;
        }

        Record key = (Record) map.keySet().toArray()[index];
        Tweet tweet = map.get(key);

        TweetActionHandler.displayTweet(tweet, accountManager);
        log.info("Tweet viewed | tweetId=" + tweet.getId());
        TweetActionHandler.printActionMenu(tweet, accountManager);

        String choice = context.getScanner().nextLine();
        return TweetActionHandler.handleAction(context, tweet, choice, navigation(context), false, INVALID_INPUT);
    }

    private TweetActionHandler.Navigation navigation(Context context) {
        return new TweetActionHandler.Navigation() {
            @Override
            public State refresh(Tweet tweet) throws IOException {
                return new TimeLineState(context, index);
            }

            @Override
            public State next(Tweet tweet) {
                return new TimeLineState(map, index + 1);
            }

            @Override
            public State back() {
                return null;
            }

            @Override
            public State afterDeleteOrMute(Tweet tweet) {
                return null;
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
