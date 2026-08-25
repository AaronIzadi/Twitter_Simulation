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
            context.getLogger().setSessionUser(username);
            System.out.println(ConsoleColors.BLUE + "Account created.");
            System.out.println(ConsoleColors.BLUE + "You can complete your profile information in Settings later!");
            log.info("Account created for user @" + username);
            return new MenuState();
        } else {
            printFinalCliError();
            log.warn("Account creation failed: username already exists");
            return null;
        }
    }

    @Override
    public void printFinalCliError() {
        System.out.println(ConsoleColors.RED + "This username already exists. Try another one.");
    }

    @Override
    public void close(Context context) {

    }
}
