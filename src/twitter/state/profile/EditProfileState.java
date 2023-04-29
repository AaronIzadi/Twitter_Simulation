package twitter.state.profile;

import twitter.Context;
import twitter.logic.HandleAccount;
import twitter.model.Account;
import twitter.utils.ConsoleColors;
import twitter.state.State;
import twitter.utils.Logger;

public class EditProfileState extends State {
    @Override
    public void printCliMenu(Context context) {

        HandleAccount handleAccount = context.getHandleAccount();

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
        System.out.println(handleAccount.isPublic(handleAccount.getUser().getUserName()) ? ConsoleColors.YELLOW + "9.Switch account to private" : ConsoleColors.YELLOW + "9.Switch account to public");
        System.out.println(ConsoleColors.YELLOW + "10.Back");
    }

    @Override
    public State doAction(Context context) {

        printCliMenu(context);

        HandleAccount handleAccount = context.getHandleAccount();
        Logger log = context.getLogger();

        String choice = context.getScanner().nextLine();

        switch (choice) {
            case "1":

                System.out.println(ConsoleColors.YELLOW + "Enter your new name:");
                String name = context.getScanner().nextLine();
                handleAccount.changeName(name);
                log.info("User changed their name to:" + name);
                return this;

            case "2":

                System.out.println(ConsoleColors.YELLOW + "Enter your new bio:");
                String bio = context.getScanner().nextLine();
                handleAccount.changeBiography(bio);
                log.info("User changed their bio to: " + bio);
                return this;

            case "3":

                System.out.println(ConsoleColors.YELLOW + "Enter your date of birth:");
                String birthday = context.getScanner().nextLine();
                handleAccount.changeDateOfBirth(birthday);
                log.info("User changed their birthday to: " + birthday);
                return this;

            case "4":

                System.out.println(ConsoleColors.YELLOW + "Enter your new email address:");
                String email = context.getScanner().nextLine();
                handleAccount.changeEmailAddress(email);
                log.info("User changed their email to: " + email);
                return this;

            case "5":

                System.out.println(ConsoleColors.YELLOW + "Enter your new username:");
                String user = context.getScanner().nextLine();
                if (!handleAccount.checkIfExist(user)) {
                    handleAccount.changeUserName(user);
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
                handleAccount.changePassword(oldPass, newPass);
                return this;

            case "7":

                System.out.println(ConsoleColors.YELLOW + "Enter your new phone number:");
                long phoneNum = context.getScanner().nextLong();
                handleAccount.changePhoneNum(phoneNum);
                log.info("User changed their phone number to: " + phoneNum);
                return this;

            case "8":

                log.info("User changed their status.");
                handleAccount.updateStatus(handleAccount.ifRecently() ? Account.ONLINE : Account.DEFAULT_STATUS);
                return this;

            case "9":

                log.info("User switched their account to public/private.");
                handleAccount.makePublicOrPrivate();
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
