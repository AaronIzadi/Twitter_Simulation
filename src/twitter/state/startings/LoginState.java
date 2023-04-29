package twitter.state.startings;

import twitter.Context;
import twitter.logic.HandleAccount;
import twitter.model.Account;
import twitter.utils.ConsoleColors;
import twitter.state.MenuState;
import twitter.state.State;
import twitter.utils.Logger;

public class LoginState extends State {


    @Override
    public void printCliMenu(Context context) {
        System.out.println(ConsoleColors.YELLOW + "Enter your username:");
    }

    @Override
    public State doAction(Context context) {

        printCliMenu(context);

        HandleAccount handleAccount = context.getHandleAccount();
        Logger log = context.getLogger();

        String username = context.getScanner().nextLine();

        if (handleAccount.checkIfExist(username)) {

            System.out.println(ConsoleColors.YELLOW + "Enter your password:");
            String password = context.getScanner().nextLine();
            if (handleAccount.checkPassword(username, password)) {
                System.out.println(ConsoleColors.BLUE + "Login successful.");
                handleAccount.login(username);
                handleAccount.updateStatus(Account.ONLINE);
                log.info("Logged in as @" + username);
                return new MenuState();
            }
            log.info("Entered incorrect password for @" + username);
            printFinalCliError();
            return this;
        }
        System.out.println(ConsoleColors.RED + "User not found. Try again.");
        log.info("Username they entered seems not to exist.");
        return null;
    }

    @Override
    public void printFinalCliError() {
        System.out.println(ConsoleColors.RED + "Incorrect password.");
    }

    @Override
    public void close(Context context) {

    }

}
