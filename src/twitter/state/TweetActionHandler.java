package twitter.state;

import twitter.main.Context;
import twitter.logic.AccountManager;
import twitter.logic.TweetManager;
import twitter.model.Tweet;
import twitter.state.profile.ViewProfileState;
import twitter.utils.ConsoleColors;
import twitter.utils.Logger;

import java.io.IOException;

public final class TweetActionHandler {

    private TweetActionHandler() {
    }

    public interface Navigation {
        State refresh(Tweet tweet) throws IOException;

        State next(Tweet tweet) throws IOException;

        State back();

        State afterDeleteOrMute(Tweet tweet) throws IOException;
    }

    public static void displayTweet(Tweet tweet, AccountManager accountManager) throws IOException {
        System.out.println(ConsoleColors.BLUE + tweet.getTextOfTweet());
        System.out.println(ConsoleColors.BLUE + "User: @" + accountManager.getUsername(tweet.getAccountId()));
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
    }

    public static void printActionMenu(Tweet tweet, AccountManager accountManager) {
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
    }

    public static State handleAction(Context context,
                                     Tweet tweet,
                                     String choice,
                                     Navigation navigation,
                                     boolean requireRetweetsForList,
                                     String invalidInputMessage) throws IOException {
        AccountManager accountManager = context.getAccountManager();
        TweetManager tweetManager = context.getTweetManager();
        Logger log = context.getLogger();

        switch (choice) {
            case "1":
                log.info("Returned to previous screen");
                return navigation.back();

            case "2":
                log.info("Opened like list | tweetId=" + tweet.getId());
                return new AccountLikedListState(tweet);

            case "3":
                log.info("Opened retweet list | tweetId=" + tweet.getId());
                if (requireRetweetsForList && tweet.getNumberOfRetweets() == 0) {
                    log.info("Empty list | context=retweets | tweetId=" + tweet.getId());
                    System.out.println(ConsoleColors.RED + "The list is empty!");
                    return navigation.refresh(tweet);
                }
                return new AccountRetweetedListState(tweet);

            case "4":
                log.info("Opened reply list | tweetId=" + tweet.getId());
                return new ShowReplyState(tweet);

            case "5":
                log.info((accountManager.isLiked(tweet) ? "Like removed" : "Like added") + " | tweetId=" + tweet.getId());
                accountManager.likeOrRemoveLike(tweet);
                return navigation.refresh(tweet);

            case "6":
                if (accountManager.isPublic(accountManager.getUsername(tweet.getAccountId()))) {
                    log.info((accountManager.isRetweeted(tweet) ? "Retweet removed" : "Retweet added") + " | tweetId=" + tweet.getId());
                    accountManager.retweetOrRemoveRetweet(tweet);
                } else {
                    System.out.println(ConsoleColors.RED + "This account is private. You can't retweet this tweet!");
                }
                return navigation.refresh(tweet);

            case "7":
                if (tweet.getAccountId() == accountManager.getUser().getId()) {
                    log.info("Tweet deleted | tweetId=" + tweet.getId());
                    tweetManager.deleteTweet(tweet);
                } else {
                    log.info("Tweet owner muted | username=" + accountManager.getUsername(tweet.getAccountId()));
                    accountManager.muteOrUnmute(accountManager.getUsername(tweet.getAccountId()));
                }
                return navigation.afterDeleteOrMute(tweet);

            case "8":
                log.info("Opened author profile | username=" + accountManager.getUsername(tweet.getAccountId()));
                return new ViewProfileState(accountManager.getUsername(tweet.getAccountId()));

            case "9":
                log.info("Tweet saved | tweetId=" + tweet.getId());
                accountManager.saveTweet(tweet);
                return navigation.refresh(tweet);

            case "10":
                log.info("Opened reply composer | tweetId=" + tweet.getId());
                return new MakeTweetState(tweet.getId());

            case "11":
                log.info("Navigated to next tweet");
                return navigation.next(tweet);

            default:
                log.warn("Invalid menu selection");
                System.out.println(invalidInputMessage);
                return navigation.refresh(tweet);
        }
    }
}
