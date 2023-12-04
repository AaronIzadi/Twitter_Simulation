package twitter.state;

import twitter.utils.ConsoleColors;
import twitter.Context;
import twitter.logic.AccountManager;
import twitter.state.profile.ViewProfileState;
import twitter.utils.Logger;

import java.io.IOException;

public class BlackListState extends State{

    @Override
    public void printCliMenu(Context context) {
        System.out.println(ConsoleColors.YELLOW + "Your blacklist is:");
    }

    @Override
    public State doAction(Context context) throws IOException {

        printCliMenu(context);

        AccountManager accountManager = context.getAccountManager();
        Logger log = context.getLogger();

        System.out.println(ConsoleColors.BLUE + accountManager.viewAccountList(accountManager.getUser().getBlacklist()));
        System.out.println(ConsoleColors.YELLOW + "Do you want to check a profile?(y/n)");

        if(accountManager.getUser().getNumberOfBlackList() == 0) {
            log.info("There is no list to show.");
            System.out.println(ConsoleColors.RED + "There is no list to show!");
            return null;
        }

        String choice = context.getScanner().nextLine();

        switch (choice) {
            case "y":
            case "Y":
                log.info("User wants to check a profile.");
                return new ViewProfileState();
            case "n":
            case "N":
                log.info("User wants to go back.");
                return null;
            default:
                printFinalCliError();
                return this;
        }
    }

    @Override
    public void printFinalCliError() {
        System.out.println(ConsoleColors.RED + "Please only enter y or n to continue.");
    }

    @Override
    public void close(Context context) {

    }
}
