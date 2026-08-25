package twitter.state.profile;

import twitter.main.Context;
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

        System.out.println(ConsoleColors.BLUE + "Profile information:");
        System.out.println(ConsoleColors.BLUE + "Username: @" + accountManager.getUser().getUserName());
        System.out.println(ConsoleColors.BLUE + "Name: " + accountManager.getUser().getName());
        System.out.println(ConsoleColors.BLUE + "Biography: " + accountManager.getUser().getBiography());
        System.out.println(ConsoleColors.BLUE + "Date of birth: " + accountManager.getUser().getDateOfBirth());
        System.out.println(ConsoleColors.BLUE + "Email address: " + accountManager.getUser().getEmailAddress());
        System.out.println(ConsoleColors.BLUE + "Phone number: " + accountManager.getUser().getPhoneNumber());
        System.out.println(ConsoleColors.BLUE + "Followers: " + accountManager.getUser().getNumberOfFollowers());
        System.out.println(ConsoleColors.BLUE + "Following: " + accountManager.getUser().getNumberOfFollowings());
        System.out.println(ConsoleColors.BLUE + "Tweets: " + accountManager.getUser().getNumberOfTweets());

        System.out.println(ConsoleColors.YELLOW + "What do you want to do next?");
        System.out.println(ConsoleColors.YELLOW + "1. View saved tweets");
        System.out.println(ConsoleColors.YELLOW + "2. View follower list");
        System.out.println(ConsoleColors.YELLOW + "3. View following list");
        System.out.println(ConsoleColors.YELLOW + "4. View your tweets");
        System.out.println(ConsoleColors.YELLOW + "5. View follow requests");
        System.out.println(ConsoleColors.YELLOW + "6. View sent follow requests");
        System.out.println(ConsoleColors.YELLOW + "7. Go to settings");
        System.out.println(ConsoleColors.YELLOW + "8. Back");
    }

    @Override
    public State doAction(Context context) throws IOException {

        context.getAccountManager().refreshLoggedInUser();
        printCliMenu(context);

        AccountManager accountManager = context.getAccountManager();
        TweetManager tweetManager = context.getTweetManager();
        Logger log = context.getLogger();

        log.info("Personal profile viewed");

        String choice = context.getScanner().nextLine();

        switch (choice) {
            case "1":

                log.info("Viewed saved tweets");
                for (long idTweet : accountManager.getUser().getSavedTweet()) {
                    String tweet = tweetManager.getTweet(idTweet).getTextOfTweet();
                    System.out.println(ConsoleColors.BLUE + tweet);
                }
                return this;

            case "2":

                log.info("Opened follower list | username=@" + accountManager.getUser().getUserName());
                return new FollowerListState(accountManager.getUser().getUserName());

            case "3":
                log.info("Opened following list | username=@" + accountManager.getUser().getUserName());
                return new FollowingListState(accountManager.getUser().getUserName());

            case "4":
                log.info("Opened tweet list | username=@" + accountManager.getUser().getUserName());
                if (accountManager.getUser().getNumberOfTweets() != 0) {
                    return new TweetListState(accountManager.getUser().getUserName());
                } else {
                    log.info("Empty list | context=tweets");
                    System.out.println(ConsoleColors.RED + "There are no tweets to show!");
                    return this;
                }
            case "5":
                log.info("Opened follow requests");
                if (accountManager.getUser().getNumberOfFollowRequest() != 0) {
                    return new FollowRequestsListState();
                } else {
                    log.info("Empty list | context=followRequests");
                    System.out.println(ConsoleColors.RED + "The list is empty!");
                    return this;
                }
            case "6":
                log.info("Opened sent follow requests");
                if (accountManager.getUser().getNumberOfAccountsSentRequest() != 0) {
                    return new RequestSentListState();
                } else {
                    log.info("Empty list | context=sentFollowRequests");
                    System.out.println(ConsoleColors.RED + "The list is empty!");
                    return this;
                }
            case "7":

                log.info("Opened settings");
                return new SettingState();

            case "8":

                log.info("Returned to previous screen");
                return null;

            default:

                log.warn("Invalid menu selection");
                printFinalCliError();
                return this;
        }
    }

    @Override
    public void printFinalCliError() {
        System.out.println(ConsoleColors.RED + "Enter a valid number.");
    }

    @Override
    public void close(Context context) {

    }
}
