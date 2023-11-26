package twitter.state;

import twitter.utils.ConsoleColors;
import twitter.Context;
import twitter.logic.AccountManager;
import twitter.logic.TweetManager;
import twitter.model.Tweet;
import twitter.state.profile.ViewProfileState;
import twitter.utils.Logger;

public class ShowReplyState extends State {

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
        this.index=index;
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
    public State doAction(Context context) {


        printCliMenu(context);

        AccountManager accountManager = context.getHandleAccount();
        TweetManager tweetManager = context.getHandleTweet();
        Logger log = context.getLogger();

        if (index >= sourceTweet.getReplies().size()) {
            printFinalCliError();
            return null;
        }

        Tweet replyTweet = tweetManager.getTweet(sourceTweet.getReplies().get(index));
        System.out.println(ConsoleColors.BLUE + replyTweet.getNumberOfLikes() + " Likes");
        System.out.println(ConsoleColors.BLUE + replyTweet.getNumberOfRetweets() + " Retweets");
        System.out.println(ConsoleColors.BLUE + replyTweet.getNumberOfReplies() + " Comments");
        if (accountManager.isLiked(replyTweet)) {
            System.out.println(ConsoleColors.BLUE + "You have liked this tweet.");
        }
        if (accountManager.isRetweeted(replyTweet)) {
            System.out.println(ConsoleColors.BLUE + "You have retweeted this tweet.");
        }
        System.out.println();

        log.info("Tweet with id:" + replyTweet.getId() + " is shown.");

        System.out.println(ConsoleColors.YELLOW + "What do you want to do next?");
        System.out.println(ConsoleColors.YELLOW + "1.Back");
        System.out.println(ConsoleColors.YELLOW + "2.View list of accounts that liked this tweet");
        System.out.println(ConsoleColors.YELLOW + "3.View list of accounts that retweeted this tweet");
        System.out.println(ConsoleColors.YELLOW + "4.View comments");
        System.out.println(ConsoleColors.YELLOW + "5.Like/remove like");
        System.out.println(ConsoleColors.YELLOW + "6.Retweet/undo");

        if (replyTweet.getAccountId() == accountManager.getUser().getId()) {
            System.out.println(ConsoleColors.YELLOW + "7.Delete this tweet");
        } else {
            System.out.println(ConsoleColors.YELLOW + "7.Mute this user");
        }

        System.out.println(ConsoleColors.YELLOW + "8.View this user's profile");
        System.out.println(ConsoleColors.YELLOW + "9.Save this tweet");
        System.out.println(ConsoleColors.YELLOW + "10.Add comment");
        System.out.println(ConsoleColors.YELLOW + "11.Next tweet");


        String choice = context.getScanner().nextLine();

        switch (choice) {
            case "1":

                log.info("User chose to go back.");
                return null;

            case "2":

                log.info("User wants to see the list of people that liked this tweet.");
                return new AccountLikedListState(replyTweet);

            case "3":

                log.info("User wants to see the list of people that retweeted this tweet.");
                return new AccountRetweetedListState(replyTweet);

            case "4":

                log.info("User wants to see the list of comments for this tweet.");
                return new ShowReplyState(replyTweet);

            case "5":

                log.info(accountManager.isLiked(replyTweet) ? "User removed their like." : "User liked this tweet.");
                accountManager.likeOrRemoveLike(replyTweet);
                return this;

            case "6":

                if (accountManager.isPublic(accountManager.getUsername(replyTweet.getAccountId()))) {
                    log.info(accountManager.isRetweeted(replyTweet) ? "User removed their retweet." : "User retweeted this tweet.");
                    accountManager.retweetOrUndo(replyTweet);
                } else {
                    System.out.println(ConsoleColors.RED + "This account is private. You can't retweet this tweet!");
                }
                return this;

            case "7":

                if (replyTweet.getAccountId() == accountManager.getUser().getId()) {
                    log.info("User deleted this tweet.");
                    tweetManager.deleteTweet(replyTweet);
                } else {
                    log.info("User muted this tweet's owner.");
                    accountManager.muteOrUnmute(accountManager.getUsername(replyTweet.getAccountId()));
                }
                return this;

            case "8":

                log.info("User wants to check this tweet's owner's profile.");
                return new ViewProfileState(accountManager.getUsername(replyTweet.getAccountId()));

            case "9":

                log.info("User saved this tweet.");
                accountManager.saveTweet(replyTweet);
                return this;

            case "10":

                log.info("User wants to write a new comment.");
                return new MakeTweetState(replyTweet.getId());

            case "11":

                log.info("User wants to see next tweet.");
                return new ShowReplyState(sourceTweet, index + 1);

            default:

                printFinalCliError();
                return this;
        }
    }

    @Override
    public void printFinalCliError() {
        System.out.println(ConsoleColors.RED + "There is no tweet to show!");
    }

    @Override
    public void close(Context context) {

    }
}
