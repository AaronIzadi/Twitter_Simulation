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
                    log.info("Follow request accepted | username=@" + acceptUsername);
                } else {
                    log.warn("Follow request accept failed: user not found | username=@" + acceptUsername);
                    System.out.println(ConsoleColors.RED + "This user does not exist!");
                }
                return this;
            case "2":
                System.out.println(ConsoleColors.YELLOW + "Enter username:");
                String declineUsername = context.getScanner().nextLine().trim();
                if (accountManager.checkIfExist(declineUsername)) {
                    accountManager.deleteFollowRequest(declineUsername);
                    log.info("Follow request declined | username=@" + declineUsername);
                } else {
                    log.warn("Follow request decline failed: user not found | username=@" + declineUsername);
                    System.out.println(ConsoleColors.RED + "This user does not exist!");
                }
                return this;
            case "3":
                log.info("Opened profile search");
                return new ViewProfileState();
            case "4":
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
