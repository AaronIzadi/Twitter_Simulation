package twitter.state.profile;

import twitter.main.Context;
import twitter.logic.AccountManager;
import twitter.utils.ConsoleColors;
import twitter.state.State;
import twitter.utils.Logger;

import java.io.IOException;

public class FollowRequestsListState extends State {
    @Override
    public void printCliMenu(Context context) {
        System.out.println(ConsoleColors.BLUE + "Follow requests:");
    }

    @Override
    public State doAction(Context context) throws IOException {

        printCliMenu(context);

        AccountManager accountManager = context.getAccountManager();
        Logger log = context.getLogger();

        for (long idAcc : accountManager.getUser().getFollowRequest()) {
            System.out.println(ConsoleColors.BLUE + accountManager.getUsername(idAcc));
        }

        System.out.println(ConsoleColors.YELLOW + "What do you want to do next?");
        System.out.println(ConsoleColors.YELLOW + "1. Accept a follow request");
        System.out.println(ConsoleColors.YELLOW + "2. Decline a follow request");
        System.out.println(ConsoleColors.YELLOW + "3. Check a profile");
        System.out.println(ConsoleColors.YELLOW + "4. Back");

        String choice = context.getScanner().nextLine().trim();

        switch (choice) {
            case "1":
                System.out.println(ConsoleColors.YELLOW + "Enter username:");
                String acceptUsername = context.getScanner().nextLine().trim();
                if (accountManager.checkIfExist(acceptUsername)) {
                    accountManager.acceptFollowRequest(acceptUsername);
                    log.info("User accepted @" + acceptUsername + "'s follow request.");
                } else {
                    System.out.println(ConsoleColors.RED + "This user does not exist!");
                }
                return this;
            case "2":
                System.out.println(ConsoleColors.YELLOW + "Enter username:");
                String declineUsername = context.getScanner().nextLine().trim();
                if (accountManager.checkIfExist(declineUsername)) {
                    accountManager.deleteFollowRequest(declineUsername);
                    log.info("User deleted @" + declineUsername + "'s follow request.");
                } else {
                    System.out.println(ConsoleColors.RED + "This user does not exist!");
                }
                return this;
            case "3":
                log.info("User wants to check a profile.");
                return new ViewProfileState();
            case "4":
                log.info("User wants to go back.");
                return null;
            default:
                log.info("User entered invalid number.");
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
