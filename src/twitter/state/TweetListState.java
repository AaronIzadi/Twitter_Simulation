package twitter.state;

import twitter.model.Record;
import twitter.utils.ConsoleColors;
import twitter.Context;
import twitter.logic.HandleAccount;
import twitter.logic.HandleTweet;
import twitter.model.Tweet;
import twitter.state.profile.ViewProfileState;
import twitter.utils.Logger;

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
    public State doAction(Context context) {

        printCliMenu(context);

        HandleAccount handleAccount = context.getHandleAccount();
        HandleTweet handleTweet = context.getHandleTweet();
        Logger log = context.getLogger();

        if (index >= handleAccount.getTweetList(username).size()) {
            printFinalCliError();
            return null;
        }

        Tweet tweet = handleTweet.getTweet(handleAccount.getTweetList(username).get(index));
        System.out.println(ConsoleColors.BLUE + tweet.getNumberOfLikes() + " Likes");
        System.out.println(ConsoleColors.BLUE + tweet.getNumberOfRetweets() + " Retweets");
        System.out.println(ConsoleColors.BLUE + tweet.getNumberOfReplies() + " Comments");
        if (handleAccount.isLiked(tweet)) {
            System.out.println(ConsoleColors.BLUE + "You have liked this tweet.");
        }
        if (handleAccount.isRetweeted(tweet)) {
            System.out.println(ConsoleColors.BLUE + "You have retweeted this tweet.");
        }
        System.out.println();

        log.info("Tweet with id:" + tweet.getId() + " is shown.");

        System.out.println(ConsoleColors.YELLOW + "What do you want to do next?");
        System.out.println(ConsoleColors.YELLOW + "1.Back");
        System.out.println(ConsoleColors.YELLOW + "2.View list of accounts that liked this tweet");
        System.out.println(ConsoleColors.YELLOW + "3.View list of accounts that retweeted this tweet");
        System.out.println(ConsoleColors.YELLOW + "4.View comments");
        System.out.println(ConsoleColors.YELLOW + "5.Like/remove like");
        System.out.println(ConsoleColors.YELLOW + "6.Retweet/undo");

        if (tweet.getAccountId() == handleAccount.getUser().getId()) {
            System.out.println(ConsoleColors.YELLOW + "7.Delete this tweet");
        } else {
            System.out.println(ConsoleColors.YELLOW + "7.Mute this user");
        }

        System.out.println(ConsoleColors.YELLOW + "8.View this user's profile");
        System.out.println(ConsoleColors.YELLOW + "9.Save this tweet");
        System.out.println(ConsoleColors.YELLOW + "10.Add comment");
        System.out.println(ConsoleColors.YELLOW + "11.Next tweet");


        String choice = context.getScanner().next();

        switch (choice) {
            case "1":

                log.info("User chose to go back.");
                return null;

            case "2":

                log.info("User wants to see the list of people that liked this tweet.");
                return new AccountLikedListState(tweet);


            case "3":

                log.info("User wants to see the list of people that retweeted this tweet.");
                if (tweet.getNumberOfRetweets() != 0) {
                    return new AccountRetweetedListState(tweet);
                } else {
                    log.info("There is no list to show.");
                    System.out.println(ConsoleColors.RED + "There is no list to show!");
                    return this;
                }

            case "4":

                log.info("User wants to see the list of comments for this tweet.");
                return new ShowReplyState(tweet);

            case "5":

                log.info(handleAccount.isLiked(tweet) ? "User removed their like." : "User liked this tweet.");
                handleAccount.likeOrRemoveLike(tweet);
                return this;

            case "6":

                if (handleAccount.isPublic(handleAccount.getUsername(tweet.getAccountId()))) {
                    log.info(handleAccount.isRetweeted(tweet) ? "User removed their retweet." : "User retweeted this tweet.");
                    handleAccount.retweetOrUndo(tweet);
                } else {
                    System.out.println(ConsoleColors.RED + "This account is private. You can't retweet this tweet!");
                }
                return this;

            case "7":

                if (tweet.getAccountId() == handleAccount.getUser().getId()) {
                    log.info("User deleted this tweet.");
                    handleTweet.deleteTweet(tweet);
                } else {
                    log.info("User muted this tweet's owner.");
                    handleAccount.muteOrUnmute(handleAccount.getUsername(tweet.getAccountId()));
                }
                return this;

            case "8":

                log.info("User wants to check this tweet's owner's profile.");
                return new ViewProfileState(handleAccount.getUsername(tweet.getAccountId()));

            case "9":

                log.info("User saved this tweet.");
                handleAccount.saveTweet(tweet);
                return this;

            case "10":

                log.info("User wants to write a new comment.");
                return new MakeTweetState(tweet.getId());

            case "11":

                log.info("User wants to see next tweet.");
                return new TweetListState(username, index + 1);

            default:
                System.out.println(ConsoleColors.RED + "Invalid Input.");
                return this;
        }
    }

    @Override
    public void printFinalCliError() {
        System.out.println(ConsoleColors.RED + "There is no tweet to Show!");
    }

    @Override
    public void close(Context context) {

    }
}
