package twitter.state.profile;

import twitter.main.Context;
import twitter.logic.AccountManager;
import twitter.model.Account;
import twitter.utils.ConsoleColors;
import twitter.state.State;
import twitter.utils.InputValidator;
import twitter.utils.Logger;

import java.io.IOException;

public class EditProfileState extends State {
    @Override
    public void printCliMenu(Context context) throws IOException {

        AccountManager accountManager = context.getAccountManager();

        System.out.println(ConsoleColors.YELLOW + "Edit profile:");
        System.out.println(ConsoleColors.YELLOW + "What do you want to do?");
        System.out.println(ConsoleColors.YELLOW + "1. Change name");
        System.out.println(ConsoleColors.YELLOW + "2. Edit biography");
        System.out.println(ConsoleColors.YELLOW + "3. Edit date of birth");
        System.out.println(ConsoleColors.YELLOW + "4. Change email address");
        System.out.println(ConsoleColors.YELLOW + "5. Change username");
        System.out.println(ConsoleColors.YELLOW + "6. Change password");
        System.out.println(ConsoleColors.YELLOW + "7. Change phone number");
        System.out.println(ConsoleColors.YELLOW + "8. Switch between 'Last seen recently' and 'Last seen'");
        System.out.println(accountManager.isPublic(accountManager.getUser().getUserName()) ? ConsoleColors.YELLOW + "9. Switch account to private" : ConsoleColors.YELLOW + "9. Switch account to public");
        System.out.println(ConsoleColors.YELLOW + "10. Back");
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
                if (!InputValidator.isValidDisplayName(name)) {
                    System.out.println(ConsoleColors.RED + "Name cannot be empty.");
                    log.warn("Profile update failed: invalid name");
                    return this;
                }
                accountManager.changeName(name.trim());
                log.info("Profile updated | field=name");
                return this;

            case "2":

                System.out.println(ConsoleColors.YELLOW + "Enter your new bio:");
                String bio = context.getScanner().nextLine();
                if (!InputValidator.isValidBio(bio)) {
                    System.out.println(ConsoleColors.RED + "Biography must be at most 160 characters.");
                    log.warn("Profile update failed: biography too long");
                    return this;
                }
                accountManager.changeBiography(bio);
                log.info("Profile updated | field=biography");
                return this;

            case "3":

                System.out.println(ConsoleColors.YELLOW + "Enter your date of birth:");
                String birthday = context.getScanner().nextLine();
                if (InputValidator.isBlank(birthday)) {
                    System.out.println(ConsoleColors.RED + "Date of birth cannot be empty.");
                    log.warn("Profile update failed: invalid date of birth");
                    return this;
                }
                accountManager.changeDateOfBirth(birthday.trim());
                log.info("Profile updated | field=dateOfBirth");
                return this;

            case "4":

                System.out.println(ConsoleColors.YELLOW + "Enter your new email address:");
                String email = context.getScanner().nextLine();
                if (!InputValidator.isValidEmail(email)) {
                    System.out.println(ConsoleColors.RED + "Enter a valid email address.");
                    log.warn("Profile update failed: invalid email");
                    return this;
                }
                accountManager.changeEmailAddress(email.trim());
                log.info("Profile updated | field=email");
                return this;

            case "5":

                System.out.println(ConsoleColors.YELLOW + "Enter your new username:");
                String user = context.getScanner().nextLine().trim();
                if (!InputValidator.isValidUsername(user)) {
                    System.out.println(ConsoleColors.RED + "Invalid username. Use 3-20 letters, numbers, or underscores.");
                    log.warn("Profile update failed: invalid username");
                    return this;
                }
                if (!accountManager.checkIfExist(user)) {
                    accountManager.changeUserName(user);
                    log.info("Profile updated | field=username | value=@" + user);
                } else {
                    log.warn("Username change failed: already exists | value=@" + user);
                    System.out.println(ConsoleColors.RED + "This username already exists. Try another one.");
                }
                return this;

            case "6":

                log.info("Password change requested");
                System.out.println(ConsoleColors.YELLOW + "Enter your old password:");
                String oldPass = context.getScanner().nextLine();
                System.out.println(ConsoleColors.YELLOW + "Enter your new password:");
                String newPass = context.getScanner().nextLine();
                if (!InputValidator.isValidPassword(newPass)) {
                    System.out.println(ConsoleColors.RED + "New password must be at least 4 characters.");
                    log.warn("Password change failed: invalid new password");
                    return this;
                }
                if (accountManager.changePassword(oldPass, newPass)) {
                    log.info("Password updated");
                    System.out.println(ConsoleColors.BLUE + "Password updated.");
                } else {
                    log.warn("Password change failed: incorrect old password");
                    System.out.println(ConsoleColors.RED + "Incorrect old password.");
                }
                return this;

            case "7":

                System.out.println(ConsoleColors.YELLOW + "Enter your new phone number:");
                Long phoneNum = InputValidator.parsePhoneNumber(context.getScanner().nextLine());
                if (phoneNum == null) {
                    System.out.println(ConsoleColors.RED + "Enter a valid phone number using digits only.");
                    log.warn("Profile update failed: invalid phone number");
                    return this;
                }
                accountManager.changePhoneNumber(phoneNum);
                log.info("Profile updated | field=phoneNumber");
                return this;

            case "8":

                log.info("Profile updated | field=lastSeenStatus");
                accountManager.updateStatus(accountManager.ifRecently() ? Account.ONLINE : Account.DEFAULT_STATUS);
                return this;

            case "9":

                log.info("Profile updated | field=visibility");
                accountManager.makePublicOrPrivate();
                return this;

            case "10":

                log.info("Returned to previous screen");
                return null;

            default:

                log.warn("Invalid menu selection");
                printFinalCliError();
                return this;
        }
    }

    @Override
    public void printFinalCliError() {
        System.out.println(ConsoleColors.RED + "Enter a valid number.");
    }

    @Override
    public void close(Context context) {

    }
}
