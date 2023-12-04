package twitter.state.profile;

import twitter.Context;
import twitter.logic.AccountManager;
import twitter.logic.TweetManager;
import twitter.utils.ConsoleColors;
import twitter.state.RequestSentListState;
import twitter.state.State;
import twitter.state.TweetListState;
import twitter.utils.Logger;

import java.io.IOException;

public class ViewPersonalInfoState extends State {


    @Override
    public void printCliMenu(Context context) {

        AccountManager accountManager = context.getAccountManager();
        TweetManager tweetManager = context.getTweetManager();

        System.out.println(ConsoleColors.BLUE + "Profile information:");
        System.out.println(ConsoleColors.BLUE + "Username: @" + accountManager.getUser().getUserName());
        System.out.println(ConsoleColors.BLUE + "Name: " + accountManager.getUser().getName());
        System.out.println(ConsoleColors.BLUE + "Biography: " + accountManager.getUser().getBiography());
        System.out.println(ConsoleColors.BLUE + "Date of birth: " + accountManager.getUser().getDateOfBirth());
        System.out.println(ConsoleColors.BLUE + "Email address: " + accountManager.getUser().getEmailAddress());
        System.out.println(ConsoleColors.BLUE + "Phone number: " + accountManager.getUser().getPhoneNumber());
        System.out.println(ConsoleColors.BLUE + "Followers: " + accountManager.getUser().getNumberOfFollowers());
        System.out.println(ConsoleColors.BLUE + "Followings: " + accountManager.getUser().getNumberOfFollowings());
        System.out.println(ConsoleColors.BLUE + "Tweets: " + accountManager.getUser().getNumberOfTweets());

        System.out.println(ConsoleColors.YELLOW + "What do you want to do next?");
        System.out.println(ConsoleColors.YELLOW + "1.View saved tweets");
        System.out.println(ConsoleColors.YELLOW + "2.View followers' list");
        System.out.println(ConsoleColors.YELLOW + "3.View followings' list");
        System.out.println(ConsoleColors.YELLOW + "4.View tweets list");
        System.out.println(ConsoleColors.YELLOW + "5.View follow requests");
        System.out.println(ConsoleColors.YELLOW + "6.View list of requests you sent");
        System.out.println(ConsoleColors.YELLOW + "7.Go to setting");
        System.out.println(ConsoleColors.YELLOW + "8.Back");
    }

    @Override
    public State doAction(Context context) throws IOException {

        printCliMenu(context);

        AccountManager accountManager = context.getAccountManager();
        TweetManager tweetManager = context.getTweetManager();
        Logger log = context.getLogger();

        log.info("User checked their profile info.");

        String choice = context.getScanner().nextLine();

        switch (choice) {
            case "1":

                log.info("User wants to view their saved tweets.");
                for (long idTweet : accountManager.getUser().getSavedTweet()) {
                    String tweet = tweetManager.getTweet(idTweet).getTextOfTweet();
                    System.out.println(ConsoleColors.BLUE + tweet);
                }
                return this;

            case "2":

                log.info("User wants to view their followers' list.");
                return new FollowerListState(accountManager.getUser().getUserName());

            case "3":
                log.info("User wants to view their followings' list.");
                return new FollowingListState(accountManager.getUser().getUserName());

            case "4":
                log.info("User wants to view their tweets.");
                if (accountManager.getUser().getNumberOfTweets() != 0) {
                    return new TweetListState(accountManager.getUser().getUserName());
                } else {
                    log.info("There is no tweet to show.");
                    System.out.println(ConsoleColors.RED + "There is no tweet to show!");
                    return this;
                }
            case "5":
                log.info("User wants to view follow request's list.");
                if (accountManager.getUser().getNumberOfFollowRequest() != 0) {
                    return new FollowRequestsListState();
                } else {
                    log.info("There is no list to show.");
                    System.out.println(ConsoleColors.RED + "There is no list to show!");
                    return this;
                }
            case "6":
                log.info("User wants to view the list of follow request that they sent.");
                if (accountManager.getUser().getNumberOfAccountsSentRequest() != 0) {
                    return new RequestSentListState();
                } else {
                    log.info("There is no list to show.");
                    System.out.println(ConsoleColors.RED + "There is no list to show!");
                    return this;
                }
            case "7":

                log.info("User wants to go to the settings.");
                return new SettingState();

            case "8":

                log.info("User chose to go back.");
                return null;

            default:

                log.info("User entered invalid number.");
                printFinalCliError();
                return this;
        }
    }

    @Override
    public void printFinalCliError() {
        System.out.println(ConsoleColors.RED + "Enter valid number.");
    }

    @Override
    public void close(Context context) {

    }
}
