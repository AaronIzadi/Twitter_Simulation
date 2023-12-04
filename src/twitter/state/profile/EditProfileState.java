package twitter.state.profile;

import twitter.Context;
import twitter.logic.AccountManager;
import twitter.model.Account;
import twitter.utils.ConsoleColors;
import twitter.state.State;
import twitter.utils.Logger;

import java.io.IOException;

public class EditProfileState extends State {
    @Override
    public void printCliMenu(Context context) throws IOException {

        AccountManager accountManager = context.getAccountManager();

        System.out.println(ConsoleColors.YELLOW + "Edit profile:");
        System.out.println(ConsoleColors.YELLOW + "What do you want to do?");
        System.out.println(ConsoleColors.YELLOW + "1.Change name");
        System.out.println(ConsoleColors.YELLOW + "2.Edit biography");
        System.out.println(ConsoleColors.YELLOW + "3.Edit date of birth");
        System.out.println(ConsoleColors.YELLOW + "4.Change Email address");
        System.out.println(ConsoleColors.YELLOW + "5.Change username");
        System.out.println(ConsoleColors.YELLOW + "6.Change password");
        System.out.println(ConsoleColors.YELLOW + "7.Change phone number");
        System.out.println(ConsoleColors.YELLOW + "8.Switch status to recently/last seen");
        System.out.println(accountManager.isPublic(accountManager.getUser().getUserName()) ? ConsoleColors.YELLOW + "9.Switch account to private" : ConsoleColors.YELLOW + "9.Switch account to public");
        System.out.println(ConsoleColors.YELLOW + "10.Back");
    }

    @Override
    public State doAction(Context context) throws IOException {

        printCliMenu(context);

        AccountManager accountManager = context.getAccountManager();
        Logger log = context.getLogger();

        String choice = context.getScanner().nextLine();

        switch (choice) {
            case "1":

                System.out.println(ConsoleColors.YELLOW + "Enter your new name:");
                String name = context.getScanner().nextLine();
                accountManager.changeName(name);
                log.info("User changed their name to:" + name);
                return this;

            case "2":

                System.out.println(ConsoleColors.YELLOW + "Enter your new bio:");
                String bio = context.getScanner().nextLine();
                accountManager.changeBiography(bio);
                log.info("User changed their bio to: " + bio);
                return this;

            case "3":

                System.out.println(ConsoleColors.YELLOW + "Enter your date of birth:");
                String birthday = context.getScanner().nextLine();
                accountManager.changeDateOfBirth(birthday);
                log.info("User changed their birthday to: " + birthday);
                return this;

            case "4":

                System.out.println(ConsoleColors.YELLOW + "Enter your new email address:");
                String email = context.getScanner().nextLine();
                accountManager.changeEmailAddress(email);
                log.info("User changed their email to: " + email);
                return this;

            case "5":

                System.out.println(ConsoleColors.YELLOW + "Enter your new username:");
                String user = context.getScanner().nextLine();
                if (!accountManager.checkIfExist(user)) {
                    accountManager.changeUserName(user);
                    log.info("User changed their username to: " + user);
                } else {
                    System.out.println(ConsoleColors.RED + "This user already exists! Try another one.");
                }
                return this;

            case "6":

                log.info("User wants to change their password.");
                System.out.println(ConsoleColors.YELLOW + "Enter your old password:");
                String oldPass = context.getScanner().nextLine();
                System.out.println(ConsoleColors.YELLOW + "Enter your new password:");
                String newPass = context.getScanner().nextLine();
                accountManager.changePassword(oldPass, newPass);
                return this;

            case "7":

                System.out.println(ConsoleColors.YELLOW + "Enter your new phone number:");
                long phoneNum = context.getScanner().nextLong();
                accountManager.changePhoneNumber(phoneNum);
                log.info("User changed their phone number to: " + phoneNum);
                return this;

            case "8":

                log.info("User changed their status.");
                accountManager.updateStatus(accountManager.ifRecently() ? Account.ONLINE : Account.DEFAULT_STATUS);
                return this;

            case "9":

                log.info("User switched their account to public/private.");
                accountManager.makePublicOrPrivate();
                return this;

            case "10":

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
        System.out.println(ConsoleColors.RED + "Enter valid number.");
    }

    @Override
    public void close(Context context) {

    }
}
