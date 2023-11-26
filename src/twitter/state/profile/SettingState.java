package twitter.state.profile;

import twitter.Context;
import twitter.logic.AccountManager;
import twitter.model.Account;
import twitter.state.BlackListState;
import twitter.utils.ConsoleColors;
import twitter.state.State;
import twitter.state.startings.StartState;
import twitter.utils.Logger;

public class SettingState extends State {
    @Override
    public void printCliMenu(Context context) {
        System.out.println(ConsoleColors.YELLOW + "What do you want to do?");
        System.out.println(ConsoleColors.YELLOW + "1.Edit profile");
        System.out.println(ConsoleColors.YELLOW + "2.Check blacklist");
        System.out.println(ConsoleColors.YELLOW + "3.Log out");
        System.out.println(ConsoleColors.YELLOW + "4.Delete account permanently");
        System.out.println(ConsoleColors.YELLOW + "5.Back");
    }

    @Override
    public State doAction(Context context) {

        printCliMenu(context);

        AccountManager accountManager = context.getHandleAccount();
        Logger log = context.getLogger();

        String choice = context.getScanner().nextLine();

        switch (choice) {
            case "1":

                log.info("User wants to edit their profile.");
                return new EditProfileState();

            case "2":

                log.info("User wants to view their blacklist.");
                return new BlackListState();

            case "3": {

                log.info("User wants to log out.");
                System.out.println(ConsoleColors.YELLOW + "Sure you want to log out?(y/n)");
                String ch = context.getScanner().nextLine();
                switch (ch) {
                    case "y":
                    case "Y":
                        log.info("Logged out of @" + accountManager.getUser().getUserName());
                        accountManager.updateStatus(Account.OFFLINE);
                        context.getHandleAccount().logout();
                        context.clearStack();
                        return new StartState();
                    case "n":
                    case "N":
                        log.info("User chose to stay login.");
                        return this;
                    default:
                        System.out.println(ConsoleColors.RED + "Please only enter y or n to continue.");
                        return this;
                }

            }
            case "4": {

                log.info("User wants to delete their account.");
                System.out.println(ConsoleColors.YELLOW + "Sure you want to delete your account?(y/n)");
                String ch = context.getScanner().nextLine();
                switch (ch) {
                    case "y":
                    case "Y":
                        log.info("User @" + accountManager.getUser().getUserName() + "has just deleted their account.");
                        accountManager.deleteAccount();
                        context.clearStack(); //TODO: no todo just a tip: always clearStack after logout and delete account
                        return new StartState();
                    case "n":
                    case "N":
                        log.info("User changed their mind.");
                        return this;
                    default:
                        System.out.println(ConsoleColors.RED + "Please only enter y or n to continue.");
                        return this;
                }

            }
            case "5":

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
        System.out.println(ConsoleColors.RED + "Invalid choice!");
    }

    @Override
    public void close(Context context) {

    }
}
