package twitter.state;

import twitter.utils.ConsoleColors;
import twitter.main.Context;
import twitter.logic.AccountManager;
import twitter.state.profile.ViewProfileState;
import twitter.utils.Logger;

import java.io.IOException;

public class RequestSentListState extends State {

    @Override
    public void printCliMenu(Context context) {
        System.out.println(ConsoleColors.BLUE + "Request you sent:");
    }

    @Override
    public State doAction(Context context) throws IOException {
        printCliMenu(context);

        AccountManager accountManager = context.getAccountManager();
        Logger log = context.getLogger();

        for (long idAcc : accountManager.getUser().getAccountsRequestedToFollow()) {
            System.out.println(ConsoleColors.BLUE + accountManager.getUsername(idAcc));
        }

        System.out.println(ConsoleColors.YELLOW + "What do you want to do next?");
        System.out.println(ConsoleColors.YELLOW + "1.Unsent a follow request");
        System.out.println(ConsoleColors.YELLOW + "2.Check a profile");
        System.out.println(ConsoleColors.YELLOW + "3.Back");

        String choice = context.getScanner().nextLine();

        switch (choice) {
            case "1":

                System.out.println(ConsoleColors.YELLOW + "Enter username:");
                String username = context.getScanner().next();
                if (accountManager.checkIfExist(username)) {
                    accountManager.unsendFollowRequest(username);
                    log.info("User unsent their follow request to @" + username);
                } else {
                    System.out.println(ConsoleColors.RED + "This user seems not to exist!");
                }
                return this;

            case "2":

                log.info("User wants to check a profile.");
                return new ViewProfileState();

            case "3":

                log.info("User wants to go back.");
                return null;

            default:

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
