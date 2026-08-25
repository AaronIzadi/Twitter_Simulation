package twitter.state.profile;

import twitter.main.Context;
import twitter.logic.AccountManager;
import twitter.utils.ConsoleColors;
import twitter.state.State;
import twitter.utils.Logger;

import java.io.IOException;

public class FollowingListState extends State {

    private final String username;

    public FollowingListState(String username) {
        this.username = username;
    }

    @Override
    public void printCliMenu(Context context) {
        System.out.println(ConsoleColors.YELLOW + "Following:");
    }

    @Override
    public State doAction(Context context) throws IOException {

        printCliMenu(context);

        AccountManager accountManager = context.getAccountManager();
        Logger log = context.getLogger();

        if (accountManager.getNumberOfFollowings(username) == 0) {
            log.info("Empty list");
            System.out.println(ConsoleColors.RED + "The list is empty!");
            return null;
        }

        for (String following: accountManager.viewAccountList(accountManager.getFollowingsList(username))) {
            System.out.println(ConsoleColors.BLUE + following);
        }

        System.out.println(ConsoleColors.YELLOW + "Do you want to view a profile?");

        String choice = context.getScanner().nextLine();

        switch (choice) {
            case "y":
            case "Y":
                log.info("Opened profile search");
                return new ViewProfileState();
            case "n":
            case "N":
                log.info("Returned to previous screen");
                return null;
            default:
                printFinalCliError();
                return this;
        }
    }

    @Override
    public void printFinalCliError() {
        System.out.println(ConsoleColors.RED + "Please enter only y or n to continue.");
    }

    @Override
    public void close(Context context) {

    }
}
