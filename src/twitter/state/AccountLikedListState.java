package twitter.state;

import twitter.utils.ConsoleColors;
import twitter.main.Context;
import twitter.logic.AccountManager;
import twitter.model.Tweet;
import twitter.state.profile.ViewProfileState;
import twitter.utils.Logger;

import java.io.IOException;

public class AccountLikedListState extends State {

    private Tweet tweet;

    public AccountLikedListState(Tweet tweet) {
        this.tweet = tweet;
    }

    @Override
    public void printCliMenu(Context context) {
        System.out.println(ConsoleColors.BLUE + "Likes:");
    }

    @Override
    public State doAction(Context context) throws IOException {

        printCliMenu(context);

        AccountManager accountManager = context.getAccountManager();
        Logger log = context.getLogger();

        if(tweet.getNumberOfLikes() == 0) {
            log.info("There is no list to show.");
            System.out.println(ConsoleColors.RED + "There is no list to show!");
            return null;
        }

        for (long idAcc : tweet.getIdAccountLiked()) {
            String user = accountManager.getUsername(idAcc);
            System.out.println(ConsoleColors.BLUE + "@" + user);
        }

        System.out.println(ConsoleColors.YELLOW + "What do you want to do next?");
        System.out.println(ConsoleColors.YELLOW + "1.Check a profile");
        System.out.println(ConsoleColors.YELLOW + "2.Back");

        String choice = context.getScanner().nextLine();

        switch (choice) {
            case "1":

                log.info("User wants to check a profile.");
                return new ViewProfileState();

            case "2":

                log.info("User wants to go back.");
                return null;

            default:

                log.info("User entered invalid input.");
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
