package twitter.state.startings;

import twitter.main.Context;
import twitter.logic.AccountManager;
import twitter.model.Account;
import twitter.utils.ConsoleColors;
import twitter.state.MenuState;
import twitter.state.State;
import twitter.utils.Logger;

import java.io.IOException;

public class LoginState extends State {


    @Override
    public void printCliMenu(Context context) {
        System.out.println(ConsoleColors.YELLOW + "Enter your username:");
    }

    @Override
    public State doAction(Context context) throws IOException {

        printCliMenu(context);

        AccountManager accountManager = context.getAccountManager();
        Logger log = context.getLogger();

        String username = context.getScanner().nextLine();

        if (accountManager.checkIfExist(username)) {

            System.out.println(ConsoleColors.YELLOW + "Enter your password:");
            String password = context.getScanner().nextLine();
            if (accountManager.login(username, password)) {
                System.out.println(ConsoleColors.BLUE + "Login successful.");
                context.getLogger().setSessionUser(username);
                accountManager.updateStatus(Account.ONLINE);
                log.info("Login successful for user @" + username);
                return new MenuState();
            }
            log.warn("Login failed: incorrect password for @" + username);
            printFinalCliError();
            return this;
        }
        System.out.println(ConsoleColors.RED + "User not found. Try again.");
        log.warn("Login failed: user not found");
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
