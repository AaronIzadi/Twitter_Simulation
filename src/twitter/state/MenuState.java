package twitter.state;

import twitter.utils.ConsoleColors;
import twitter.main.Context;
import twitter.logic.AccountManager;
import twitter.model.Tweet;
import twitter.state.profile.ViewPersonalInfoState;
import twitter.state.profile.ViewProfileState;
import twitter.utils.Logger;

import java.io.IOException;

public class MenuState extends State {


    @Override
    public void printCliMenu(Context context) {
        System.out.println(ConsoleColors.BLUE + "WELCOME!");
    }

    @Override
    public State doAction(Context context) throws IOException {

        printCliMenu(context);

        AccountManager accountManager = context.getAccountManager();
        Logger log = context.getLogger();

        System.out.println(ConsoleColors.YELLOW + "Choose what you want to do:");
        System.out.println(ConsoleColors.YELLOW + "1.Go to timeline");
        System.out.println(ConsoleColors.YELLOW + "2.Check my profile info");
        System.out.println(ConsoleColors.YELLOW + "3.Search for a user");
        System.out.println(ConsoleColors.YELLOW + "4.Make tweet");
        System.out.println(ConsoleColors.YELLOW + "5.Exit");

        String choice = context.getScanner().nextLine();

        switch (choice) {
            case "1":

                log.info("User chose to check their timeline.");
                return new TimeLineState(context);

            case "2":

                log.info("User chose to check their profile info.");
                return new ViewPersonalInfoState();

            case "3":

                log.info("User chose to search for an account.");
                return new ViewProfileState();

            case "4":

                log.info("User chose to write a new tweet.");
                return new MakeTweetState(Tweet.DEFAULT_ID);

            case "5":

                log.info("User chose to close the app.");
                return new ExitState();

            default:

                printFinalCliError();
                log.info("User entered invalid number.");
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
