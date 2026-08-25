package twitter.state.startings;

import twitter.main.Context;
import twitter.logic.AccountManager;
import twitter.utils.ConsoleColors;
import twitter.state.MenuState;
import twitter.state.State;
import twitter.utils.InputValidator;
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

        String username = context.getScanner().nextLine().trim();

        Logger log = context.getLogger();

        if (!InputValidator.isValidUsername(username)) {
            System.out.println(ConsoleColors.RED + "Invalid username. Use 3-20 letters, numbers, or underscores.");
            log.warn("Account creation failed: invalid username");
            return this;
        }

        if (!accountManager.checkIfExist(username)) {
            System.out.println(ConsoleColors.YELLOW + "Set password:");

            String password = context.getScanner().nextLine();

            if (!InputValidator.isValidPassword(password)) {
                System.out.println(ConsoleColors.RED + "Invalid password. It must be at least 4 characters.");
                log.warn("Account creation failed: invalid password");
                return this;
            }

            accountManager.createAccount(username, password);
            context.getLogger().setSessionUser(username);
            System.out.println(ConsoleColors.BLUE + "Account created.");
            System.out.println(ConsoleColors.BLUE + "You can complete your profile information in Settings later!");
            log.info("Account created for user @" + username);
            return new MenuState();
        } else {
            printFinalCliError();
            log.warn("Account creation failed: username already exists");
            return this;
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
