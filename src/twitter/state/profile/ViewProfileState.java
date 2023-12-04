package twitter.state.profile;

import twitter.Context;
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
            username = context.getScanner().next();
        }

        if (!accountManager.checkIfExist(username)) {
            System.out.println(ConsoleColors.RED + "Sorry! This account seems not to exist!");
            return null;
        }

        System.out.println(ConsoleColors.BLUE + "Profile information:");
        System.out.println(ConsoleColors.BLUE + "Username: @" + username);
        System.out.println(ConsoleColors.BLUE + "Name: " + accountManager.getName(username));
        System.out.println(ConsoleColors.BLUE + "Biography: " + accountManager.getBiography(username));
        System.out.println(ConsoleColors.BLUE + "Followers: " + accountManager.getNumberOfFollowers(username));
        System.out.println(ConsoleColors.BLUE + "Followings: " + accountManager.getNumberOfFollowings(username));
        System.out.println(ConsoleColors.BLUE + "Tweets: " + accountManager.getNumberOfTweets(username));

        if (!accountManager.ifYouAreBlocked(username)) {
            System.out.println(ConsoleColors.BLUE + "Status: " + accountManager.getStatus(username));
        }
        if (accountManager.ifYouAreBlocked(username)) {
            System.out.println(ConsoleColors.BLUE + "This user has blocked you.");
        }
        if (accountManager.isFollowed(username)) {
            System.out.println(ConsoleColors.BLUE + "You currently follow this user.");
        }

        if (accountManager.isRequested(username)) {
            System.out.println(ConsoleColors.BLUE + "You have sent follow request to this user.");
        }

        if (accountManager.isFollowingYOu(username)) {
            System.out.println(ConsoleColors.BLUE + "This user follows you.");
        }

        if (accountManager.isMute(username)) {
            System.out.println(ConsoleColors.BLUE + "You have muted this user.");
        }

        log.info("User checked @" + username + "'s profile.");

        System.out.println(ConsoleColors.YELLOW + "What do you want to do next?");
        System.out.println(ConsoleColors.YELLOW + "1.Back");
        System.out.println(ConsoleColors.YELLOW + "2.Check another profile");
        if (accountManager.isPublic(username)) {
            if (accountManager.isFollowed(username)) {
                System.out.println(ConsoleColors.YELLOW + "3.Unfollow this user");
            } else {
                System.out.println(ConsoleColors.YELLOW + "3.Follow this user");
            }
        } else {
            if (accountManager.isRequested(username)) {
                System.out.println(ConsoleColors.YELLOW + "3.Delete my follow request");
            } else {
                System.out.println(ConsoleColors.YELLOW + "3.Send follow request");
            }
            //TODO a private page can also be unfollowed, not just deleting request.
        }

        System.out.println(accountManager.isMute(username) ? ConsoleColors.YELLOW + "4.Unmute this user" : ConsoleColors.YELLOW + "4.Mute this user");
        System.out.println(accountManager.isBlocked(username) ? ConsoleColors.YELLOW + "5.Unlock this user" : ConsoleColors.YELLOW + "5.Block this user");

        if (accountManager.isPublic(username)) {
            System.out.println(ConsoleColors.YELLOW + "6.Check their follower list");
            System.out.println(ConsoleColors.YELLOW + "7.Check their following list");
            System.out.println(ConsoleColors.YELLOW + "8.Check their tweets");
        }


        String ch = context.getScanner().nextLine();

        switch (ch) {
            case "1":

                log.info("User wants to go back.");
                return null;

            case "2":

                log.info("User wants to check another account.");
                return this;

            case "3":

                if (accountManager.isPublic(username)) {
                    log.info(accountManager.isFollowed(username) ? "User unfollowed @" + username : "User followed @" + username);
                    accountManager.followOrUnfollow(username);
                    return this;
                } else {
                    if (accountManager.isRequested(username)) {
                        log.info("User unsent their follow request to @" + username);
                        accountManager.unsendFollowRequest(username);
                        return this;
                    } else {
                        log.info("User sent follow request to @" + username);
                        accountManager.sendFollowRequest(username);
                        return this;
                    }
                }

            case "4":

                log.info(accountManager.isMute(username) ? "User unmuted @" + username : "User muted @" + username);
                accountManager.muteOrUnmute(username);
                return this;

            case "5":

                if (accountManager.isBlocked(username)) {
                    log.info("User unblocked @" + username);
                    accountManager.unblock(username);
                } else {
                    log.info("User blocked @" + username);
                    accountManager.block(username);
                }
                return this;

        }
        //TODO this if is mess because of splitting the switchcase. you need to make it one chain again.
        if (accountManager.isPublic(username) || accountManager.isFollowed(username)) {
            switch (ch) {
                case "6":
                    if (accountManager.getNumberOfFollowers(username) != 0) {
                        log.info("User wants to check @" + username + "'s follower list.");
                        return new FollowerListState(username);
                    } else {
                        System.out.println(ConsoleColors.RED + "There is no list to show!");
                        return this;
                    }
                case "7":

                    log.info("User wants to check @" + username + "'s following list.");
                    return new FollowingListState(username);

                case "8":
                    if (accountManager.getNumberOfTweets(username) != 0) {
                        log.info("User wants to check @" + username + "'s tweets.");
                        return new TweetListState(username);
                    } else {
                        System.out.println(ConsoleColors.RED + "There is no tweet to show!");
                        return this;
                    }
                default:
                    log.info("User entered invalid number.");
                    printFinalCliError();
                    return this;
            }
        } else {
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
