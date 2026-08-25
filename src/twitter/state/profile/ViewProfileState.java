package twitter.state.profile;

import twitter.main.Context;
import twitter.logic.AccountManager;
import twitter.utils.ConsoleColors;
import twitter.state.State;
import twitter.state.TweetListState;
import twitter.utils.Logger;

import java.io.IOException;

public class ViewProfileState extends State {

    private String username;

    public ViewProfileState() {

    }

    public ViewProfileState(String username) {
        this.username = username;
    }

    @Override
    public void printCliMenu(Context context) {
        if (username == null) {
            System.out.println(ConsoleColors.YELLOW + "Enter username:");
        }
    }

    @Override
    public State doAction(Context context) throws IOException {

        printCliMenu(context);

        AccountManager accountManager = context.getAccountManager();
        Logger log = context.getLogger();


        if (username == null) {
            username = context.getScanner().nextLine().trim();
        }

        if (!accountManager.checkIfExist(username)) {
            System.out.println(ConsoleColors.RED + "Sorry! This account does not exist!");
            return null;
        }

        boolean canViewContent = accountManager.canViewProfileContent(username);

        System.out.println(ConsoleColors.BLUE + "Profile information:");
        System.out.println(ConsoleColors.BLUE + "Username: @" + username);

        if (accountManager.ifYouAreBlocked(username)) {
            System.out.println(ConsoleColors.BLUE + "This user has blocked you.");
        } else if (!canViewContent) {
            System.out.println(ConsoleColors.BLUE + "This account is private.");
        } else {
            System.out.println(ConsoleColors.BLUE + "Name: " + accountManager.getName(username));
            System.out.println(ConsoleColors.BLUE + "Biography: " + accountManager.getBiography(username));
            System.out.println(ConsoleColors.BLUE + "Followers: " + accountManager.getNumberOfFollowers(username));
            System.out.println(ConsoleColors.BLUE + "Following: " + accountManager.getNumberOfFollowings(username));
            System.out.println(ConsoleColors.BLUE + "Tweets: " + accountManager.getNumberOfTweets(username));
            System.out.println(ConsoleColors.BLUE + "Status: " + accountManager.getStatus(username));
        }
        if (accountManager.isFollowed(username)) {
            System.out.println(ConsoleColors.BLUE + "You currently follow this user.");
        }

        if (accountManager.isRequested(username)) {
            System.out.println(ConsoleColors.BLUE + "You have sent a follow request to this user.");
        }

        if (accountManager.isFollowingYOu(username)) {
            System.out.println(ConsoleColors.BLUE + "This user follows you.");
        }

        if (accountManager.isMute(username)) {
            System.out.println(ConsoleColors.BLUE + "You have muted this user.");
        }

        log.info("Profile viewed | username=@" + username);

        System.out.println(ConsoleColors.YELLOW + "What do you want to do next?");
        System.out.println(ConsoleColors.YELLOW + "1. Back");
        System.out.println(ConsoleColors.YELLOW + "2. Check another profile");
        if (accountManager.isPublic(username)) {
            if (accountManager.isFollowed(username)) {
                System.out.println(ConsoleColors.YELLOW + "3. Unfollow this user");
            } else {
                System.out.println(ConsoleColors.YELLOW + "3. Follow this user");
            }
        } else {
            if (accountManager.isFollowed(username)) {
                System.out.println(ConsoleColors.YELLOW + "3. Unfollow this user");
            } else if (accountManager.isRequested(username)) {
                System.out.println(ConsoleColors.YELLOW + "3. Delete my follow request");
            } else {
                System.out.println(ConsoleColors.YELLOW + "3. Send a follow request");
            }
        }

        System.out.println(accountManager.isMute(username) ? ConsoleColors.YELLOW + "4. Unmute this user" : ConsoleColors.YELLOW + "4. Mute this user");
        System.out.println(accountManager.isBlocked(username) ? ConsoleColors.YELLOW + "5. Unblock this user" : ConsoleColors.YELLOW + "5. Block this user");

        if (canViewContent) {
            System.out.println(ConsoleColors.YELLOW + "6. Check their follower list");
            System.out.println(ConsoleColors.YELLOW + "7. Check their following list");
            System.out.println(ConsoleColors.YELLOW + "8. Check their tweets");
        }


        String ch = context.getScanner().nextLine();

        switch (ch) {
            case "1":

                log.info("Returned to previous screen");
                return null;

            case "2":

                log.info("Opened account search");
                username = null;
                return this;

            case "3":

                if (accountManager.isPublic(username)) {
                    log.info("Follow action | username=@" + username + " | action=" + (accountManager.isFollowed(username) ? "unfollowed" : "followed"));
                    accountManager.followOrUnfollow(username);
                    return this;
                } else {
                    if (accountManager.isFollowed(username)) {
                        log.info("Follow action | username=@" + username + " | action=unfollowed");
                        accountManager.followOrUnfollow(username);
                    } else if (accountManager.isRequested(username)) {
                        log.info("Follow request cancelled | username=@" + username);
                        accountManager.unsendFollowRequest(username);
                    } else {
                        log.info("Follow request sent | username=@" + username);
                        accountManager.sendFollowRequest(username);
                    }
                    return this;
                }

            case "4":

                log.info("Mute action | username=@" + username + " | action=" + (accountManager.isMute(username) ? "unmuted" : "muted"));
                accountManager.muteOrUnmute(username);
                return this;

            case "5":

                if (accountManager.isBlocked(username)) {
                    log.info("Block action | username=@" + username + " | action=unblocked");
                    accountManager.unblock(username);
                } else {
                    log.info("Block action | username=@" + username + " | action=blocked");
                    accountManager.block(username);
                }
                return this;

            case "6":
                if (canViewContent) {
                    if (accountManager.getNumberOfFollowers(username) != 0) {
                        log.info("Opened follower list | username=@" + username);
                        return new FollowerListState(username);
                    }
                    log.info("Empty list | context=followers | username=@" + username);
                    System.out.println(ConsoleColors.RED + "The list is empty!");
                    return this;
                }
                break;

            case "7":
                if (canViewContent) {
                    log.info("Opened following list | username=@" + username);
                    return new FollowingListState(username);
                }
                break;

            case "8":
                if (canViewContent) {
                    if (accountManager.getNumberOfTweets(username) != 0) {
                        log.info("Opened tweet list | username=@" + username);
                        return new TweetListState(username);
                    }
                    System.out.println(ConsoleColors.RED + "There are no tweets to show!");
                    return this;
                }
                break;

            default:
                log.warn("Invalid menu selection");
                printFinalCliError();
                return this;
        }

        log.warn("Invalid menu selection");
        printFinalCliError();
        return this;
    }

    @Override
    public void printFinalCliError() {
        System.out.println(ConsoleColors.RED + "Enter a valid number.");
    }

    @Override
    public void close(Context context) {

    }
}
