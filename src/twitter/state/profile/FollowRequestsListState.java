package twitter.state.profile;

import twitter.Context;
import twitter.logic.HandleAccount;
import twitter.utils.ConsoleColors;
import twitter.state.State;
import twitter.utils.Logger;

public class FollowRequestsListState extends State {
    @Override
    public void printCliMenu(Context context) {
        System.out.println(ConsoleColors.BLUE + "Follow requests:");
    }

    @Override
    public State doAction(Context context) {

        printCliMenu(context);

        HandleAccount handleAccount = context.getHandleAccount();
        Logger log = context.getLogger();

        for (long idAcc : handleAccount.getUser().getFollowRequest()) {
            System.out.println(ConsoleColors.BLUE + handleAccount.getUsername(idAcc));
        }

        System.out.println(ConsoleColors.YELLOW + "What do you want to do next?");
        System.out.println(ConsoleColors.YELLOW + "1.Accept a follow request");
        System.out.println(ConsoleColors.YELLOW + "2.Delete a follow request");
        System.out.println(ConsoleColors.YELLOW + "3.Check a profile");
        System.out.println(ConsoleColors.YELLOW + "4.Back");

        char choice = context.getScanner().next().charAt(0);

        if (choice == '1') {
            System.out.println(ConsoleColors.YELLOW + "Enter username:");
            String username = context.getScanner().next();
            if (handleAccount.checkIfExist(username)) {
                handleAccount.acceptFollowRequest(username);
                log.info("User accepted @" + username + "'s follow request.");
            } else {
                System.out.println(ConsoleColors.RED + "This user seems not to exist!");
            }
            return this;
        } else if (choice == '2') {
            System.out.println(ConsoleColors.YELLOW + "Enter username:");
            String username = context.getScanner().next();
            if (handleAccount.checkIfExist(username)) {
                handleAccount.deleteFollowRequest(username);
                log.info("User deleted @" + username + "'s follow request.");
            } else {
                System.out.println(ConsoleColors.RED + "This user seems not to exist!");
            }
            return this;
        } else if (choice == '3') {
            log.info("User wants to check a profile.");
            return new ViewProfileState();
        } else if (choice == '4') {
            log.info("User wants to go back.");
            return null;
        } else {
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
