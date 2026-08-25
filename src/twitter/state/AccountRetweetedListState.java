package twitter.state;

import twitter.utils.ConsoleColors;
import twitter.main.Context;
import twitter.logic.AccountManager;
import twitter.model.Tweet;
import twitter.state.profile.ViewProfileState;
import twitter.utils.Logger;

import java.io.IOException;

public class AccountRetweetedListState extends State {

    private Tweet tweet;

    public AccountRetweetedListState(Tweet tweet) {
        this.tweet = tweet;
    }

    @Override
    public void printCliMenu(Context context) {
        System.out.println(ConsoleColors.BLUE + "Retweets:");
    }

    @Override
    public State doAction(Context context) throws IOException {

        printCliMenu(context);

        AccountManager accountManager = context.getAccountManager();
        Logger log = context.getLogger();

        if (tweet.getNumberOfRetweets() == 0) {
            log.info("Empty list");
            System.out.println(ConsoleColors.RED + "The list is empty!");
            return null;
        }

        for (long idAcc : tweet.getIdAccountRetweeted()) {
            String user = accountManager.getUsername(idAcc);
            System.out.println(ConsoleColors.BLUE + "@" + user);
        }

        System.out.println(ConsoleColors.YELLOW + "What do you want to do next?");
        System.out.println(ConsoleColors.YELLOW + "1. Check a profile");
        System.out.println(ConsoleColors.YELLOW + "2. Back");

        String choice = context.getScanner().nextLine().trim();

        switch (choice) {
            case "1":
                log.info("Opened profile search");
                return new ViewProfileState();
            case "2":
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
