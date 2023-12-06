package twitter.state.startings;

import twitter.main.Context;
import twitter.logic.AccountManager;
import twitter.utils.ConsoleColors;
import twitter.state.MenuState;
import twitter.state.State;
import twitter.utils.Logger;

import java.io.IOException;

public class CreateAccountState extends State {


    @Override
    public void printCliMenu(Context context) {
        System.out.println(ConsoleColors.YELLOW + "Set username:");
    }

    @Override
    public State doAction(Context context) throws IOException {

        printCliMenu(context);

        AccountManager accountManager = context.getAccountManager();

        String username = context.getScanner().nextLine();

        Logger log = context.getLogger();

        if (!accountManager.checkIfExist(username)) {
            System.out.println(ConsoleColors.YELLOW + "Set password:");

            String password = context.getScanner().nextLine();

            accountManager.createAccount(username, password);
            System.out.println(ConsoleColors.BLUE + "Account created.");
            System.out.println(ConsoleColors.BLUE + "You can later complete your profile info in settings!");
            log.info("Account successfully created.");
            log.info("Logged in as @" + username);
            return new MenuState();
        } else {
            printFinalCliError();
            log.info("Username they entered already exists.");
            return null;
        }
    }

    @Override
    public void printFinalCliError() {
        System.out.println(ConsoleColors.RED + "User already exists. Try another one.");
    }

    @Override
    public void close(Context context) {

    }
}
