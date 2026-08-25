package twitter.state;

import twitter.utils.ConsoleColors;
import twitter.main.Context;
import twitter.logic.AccountManager;
import twitter.logic.TweetManager;
import twitter.model.Tweet;
import twitter.state.profile.ViewProfileState;
import twitter.utils.Logger;

import java.io.IOException;

public class TweetListState extends State {

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
        System.out.println(ConsoleColors.BLUE + tweet.getNumberOfLikes() + " Likes");
        System.out.println(ConsoleColors.BLUE + tweet.getNumberOfRetweets() + " Retweets");
        System.out.println(ConsoleColors.BLUE + tweet.getNumberOfReplies() + " Comments");
        if (accountManager.isLiked(tweet)) {
            System.out.println(ConsoleColors.BLUE + "You have liked this tweet.");
        }
        if (accountManager.isRetweeted(tweet)) {
            System.out.println(ConsoleColors.BLUE + "You have retweeted this tweet.");
        }
        System.out.println();

        log.info("Tweet viewed | tweetId=" + tweet.getId());

        System.out.println(ConsoleColors.YELLOW + "What do you want to do next?");
        System.out.println(ConsoleColors.YELLOW + "1. Back");
        System.out.println(ConsoleColors.YELLOW + "2. View list of accounts that liked this tweet");
        System.out.println(ConsoleColors.YELLOW + "3. View list of accounts that retweeted this tweet");
        System.out.println(ConsoleColors.YELLOW + "4. View comments");
        System.out.println(ConsoleColors.YELLOW + "5. Like or remove like");
        System.out.println(ConsoleColors.YELLOW + "6. Retweet or undo retweet");

        if (tweet.getAccountId() == accountManager.getUser().getId()) {
            System.out.println(ConsoleColors.YELLOW + "7. Delete this tweet");
        } else {
            System.out.println(ConsoleColors.YELLOW + "7. Mute this user");
        }

        System.out.println(ConsoleColors.YELLOW + "8. View this user's profile");
        System.out.println(ConsoleColors.YELLOW + "9. Save this tweet");
        System.out.println(ConsoleColors.YELLOW + "10. Add a comment");
        System.out.println(ConsoleColors.YELLOW + "11. Next tweet");


        String choice = context.getScanner().nextLine();

        switch (choice) {
            case "1":

                log.info("Returned to previous screen");
                return null;

            case "2":

                log.info("Opened like list | tweetId=" + tweet.getId());
                return new AccountLikedListState(tweet);


            case "3":

                log.info("Opened retweet list | tweetId=" + tweet.getId());
                if (tweet.getNumberOfRetweets() != 0) {
                    return new AccountRetweetedListState(tweet);
                } else {
                    log.info("Empty list | context=retweets | tweetId=" + tweet.getId());
                    System.out.println(ConsoleColors.RED + "The list is empty!");
                    return this;
                }

            case "4":

                log.info("Opened reply list | tweetId=" + tweet.getId());
                return new ShowReplyState(tweet);

            case "5":

                log.info((accountManager.isLiked(tweet) ? "Like removed" : "Like added") + " | tweetId=" + tweet.getId());
                accountManager.likeOrRemoveLike(tweet);
                return this;

            case "6":

                if (accountManager.isPublic(accountManager.getUsername(tweet.getAccountId()))) {
                    log.info((accountManager.isRetweeted(tweet) ? "Retweet removed" : "Retweet added") + " | tweetId=" + tweet.getId());
                    accountManager.retweetOrRemoveRetweet(tweet);
                } else {
                    System.out.println(ConsoleColors.RED + "This account is private. You can't retweet this tweet!");
                }
                return this;

            case "7":

                if (tweet.getAccountId() == accountManager.getUser().getId()) {
                    log.info("Tweet deleted | tweetId=" + tweet.getId());
                    tweetManager.deleteTweet(tweet);
                } else {
                    log.info("Tweet owner muted | username=" + accountManager.getUsername(tweet.getAccountId()));
                    accountManager.muteOrUnmute(accountManager.getUsername(tweet.getAccountId()));
                }
                return this;

            case "8":

                log.info("Opened author profile | username=" + accountManager.getUsername(tweet.getAccountId()));
                return new ViewProfileState(accountManager.getUsername(tweet.getAccountId()));

            case "9":

                log.info("Tweet saved | tweetId=" + tweet.getId());
                accountManager.saveTweet(tweet);
                return this;

            case "10":

                log.info("Opened reply composer | tweetId=" + tweet.getId());
                return new MakeTweetState(tweet.getId());

            case "11":

                log.info("Navigated to next tweet");
                return new TweetListState(username, index + 1);

            default:
                log.warn("Invalid menu selection");
                System.out.println(ConsoleColors.RED + "Invalid input.");
                return this;
        }
    }

    @Override
    public void printFinalCliError() {
        System.out.println(ConsoleColors.RED + "There are no more tweets to show!");
    }

    @Override
    public void close(Context context) {

    }
}
