package twitter.state.profile;

import twitter.main.Context;
import twitter.logic.AccountManager;
import twitter.model.Account;
import twitter.state.BlackListState;
import twitter.utils.ConsoleColors;
import twitter.state.State;
import twitter.state.startings.StartState;
import twitter.utils.Logger;

import java.io.IOException;

public class SettingState extends State {
    @Override
    public void printCliMenu(Context context) {
        System.out.println(ConsoleColors.YELLOW + "What do you want to do?");
        System.out.println(ConsoleColors.YELLOW + "1. Edit profile");
        System.out.println(ConsoleColors.YELLOW + "2. View blocked users");
        System.out.println(ConsoleColors.YELLOW + "3. Log out");
        System.out.println(ConsoleColors.YELLOW + "4. Delete account permanently");
        System.out.println(ConsoleColors.YELLOW + "5. Back");
    }

    @Override
    public State doAction(Context context) throws IOException {

        printCliMenu(context);

        AccountManager accountManager = context.getAccountManager();
        Logger log = context.getLogger();

        String choice = context.getScanner().nextLine();

        switch (choice) {
            case "1":

                log.info("Opened edit profile");
                return new EditProfileState();

            case "2":

                log.info("Opened blocked users list");
                return new BlackListState();

            case "3": {

                log.info("Logout requested");
                System.out.println(ConsoleColors.YELLOW + "Are you sure you want to log out? (y/n)");
                String ch = context.getScanner().nextLine();
                switch (ch) {
                    case "y":
                    case "Y":
                        String username = accountManager.getUser().getUserName();
                        log.info("Logout completed for user @" + username);
                        accountManager.updateStatus(Account.OFFLINE);
                        context.getAccountManager().logout();
                        context.getLogger().clearSessionUser();
                        context.clearStack();
                        return new StartState();
                    case "n":
                    case "N":
                        log.info("Logout cancelled");
                        return this;
                    default:
                        log.warn("Invalid confirmation input during logout");
                        System.out.println(ConsoleColors.RED + "Please enter only y or n to continue.");
                        return this;
                }

            }
            case "4": {

                log.info("Account deletion requested");
                System.out.println(ConsoleColors.YELLOW + "Are you sure you want to delete your account? (y/n)");
                String ch = context.getScanner().nextLine();
                switch (ch) {
                    case "y":
                    case "Y":
                        String deletedUser = accountManager.getUser().getUserName();
                        log.info("Account deleted for user @" + deletedUser);
                        accountManager.deleteAccount();
                        context.getLogger().clearSessionUser();
                        context.clearStack();
                        return new StartState();
                    case "n":
                    case "N":
                        log.info("Account deletion cancelled");
                        return this;
                    default:
                        log.warn("Invalid confirmation input during account deletion");
                        System.out.println(ConsoleColors.RED + "Please enter only y or n to continue.");
                        return this;
                }

            }
            case "5":

                log.info("Returned to previous screen");
                return null;

            default:

                log.warn("Invalid menu selection in settings");
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
