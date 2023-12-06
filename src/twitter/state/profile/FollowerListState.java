package twitter.state.profile;

import twitter.main.Context;
import twitter.logic.AccountManager;
import twitter.utils.ConsoleColors;
import twitter.state.State;
import twitter.utils.Logger;

import java.io.IOException;

public class FollowerListState extends State {

    private String username;


    public void setUsername(String username) { this.username = username; }

    public FollowerListState(String username) {
        this.username = username;
    }

    @Override
    public void printCliMenu(Context context) {
        System.out.println(ConsoleColors.YELLOW + "Followers:");
    }

    @Override
    public State doAction(Context context) throws IOException {

        printCliMenu(context);

        AccountManager accountManager = context.getAccountManager();
        Logger log = context.getLogger();

        if (accountManager.getUser().getNumberOfFollowers() != 0) {
            log.info("There is no list to show.");
            System.out.println(ConsoleColors.RED + "There is no list to show!");
            return null;
        }

        for (String follower: accountManager.viewAccountList(accountManager.getFollowersList(username))) {
            System.out.println(ConsoleColors.BLUE + follower);
        }

        System.out.println(ConsoleColors.YELLOW + "Do you want to check a profile?");

        String choice = context.getScanner().nextLine();

        switch (choice) {
            case "y":
            case "Y":
                log.info("User wants to check a profile.");
                return new ViewProfileState();
            case "n":
            case "N":
                log.info("User wants to go back.");
                return null;
            default:
                printFinalCliError();
                return this;
        }

    }

    @Override
    public void printFinalCliError() {
        System.out.println(ConsoleColors.RED + "Please only enter y or n to continue.");
    }

    @Override
    public void close(Context context) {

    }
}
