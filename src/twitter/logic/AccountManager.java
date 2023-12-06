package twitter.logic;

import twitter.model.Account;
import twitter.model.Record;
import twitter.model.Time;
import twitter.model.Tweet;
import twitter.repository.*;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class AccountManager {

    TweetRepository tweetRepository = TweetFileRepository.getInstance();
    AccountRepository accountRepository = AccountFileRepository.getInstance();
    TweetManager tweetManager = new TweetManager();

    public Account getUser() {
        return accountRepository.getUser();
    }

    public void login(String userName) throws IOException {
        accountRepository.setUser(accountRepository.getAccountByUserName(userName));
    }

    public void logout() {
        accountRepository.setUser(null);
    }

    public void createAccount(String username, String password) throws IOException {
        Account account = new Account(username, password, Account.DEFAULT);
        accountRepository.add(account);
        accountRepository.setUser(account);
    }

    public boolean isPublic(String username) throws IOException {
        return accountRepository.getAccountByUserName(username).getType() == Account.DEFAULT;
    }

    public void makePublicOrPrivate() throws IOException {
        if (isPublic(getUser().getUserName())) {
            getUser().setType(Account.PRIVATE);
        } else {
            getUser().setType(Account.PUBLIC);
        }
    }

    public void followOrUnfollow(String userName) throws IOException {
        Account account = accountRepository.getAccountByUserName(userName);
        if (!isFollowed(userName)) {
            getUser().addFollowing(account.getId());
            getUser().setNumberOfFollowings();
            account.addFollower(getUser().getId());
            account.setNumberOfFollowers();
        } else {
            getUser().getFollowings().remove(account.getId());
            getUser().setNumberOfFollowings();
            account.getFollowers().remove(getUser().getId());
            account.setNumberOfFollowers();
        }
        accountRepository.update(getUser());
        accountRepository.update(account);
    }

    public boolean isFollowingYOu(String username) throws IOException {
        return getUser().getFollowers().contains(accountRepository.getAccountByUserName(username).getId());
    }

    public boolean isFollowed(String username) throws IOException {
        return getUser().getFollowings().contains(accountRepository.getAccountByUserName(username).getId());
    }

    public void sendFollowRequest(String username) throws IOException {
        Account account = accountRepository.getAccountByUserName(username);
        getUser().addAccountRequestedToFollow(account.getId());
        getUser().setNumberOfAccountsSentRequest();
        account.addFollowRequest(getUser().getId());
        account.setNumberOfFollowRequest();
        accountRepository.update(getUser());
        accountRepository.update(account);
    }

    public void unsendFollowRequest(String username) throws IOException {
        Account account = accountRepository.getAccountByUserName(username);
        getUser().getAccountsRequestedToFollow().remove(account.getId());
        getUser().setNumberOfAccountsSentRequest();
        account.getFollowRequest().remove(getUser().getId());
        account.setNumberOfFollowRequest();
        accountRepository.update(getUser());
        accountRepository.update(account);
    }

    public void acceptFollowRequest(String username) throws IOException {
        Account account = accountRepository.getAccountByUserName(username);
        account.addFollowing(getUser().getId());
        account.setNumberOfFollowings();
        account.getAccountsRequestedToFollow().remove(getUser().getId());
        account.setNumberOfAccountsSentRequest();
        getUser().addFollower(account.getId());
        getUser().setNumberOfFollowers();
        getUser().getFollowRequest().remove(account.getId());
        getUser().setNumberOfFollowRequest();
        accountRepository.update(getUser());
        accountRepository.update(account);
    }

    public void deleteFollowRequest(String username) throws IOException {
        Account account = accountRepository.getAccountByUserName(username);
        account.getAccountsRequestedToFollow().remove(getUser().getId());
        account.setNumberOfAccountsSentRequest();
        getUser().getFollowRequest().remove(account.getId());
        getUser().setNumberOfFollowRequest();
        accountRepository.update(getUser());
        accountRepository.update(account);
    }

    public boolean isRequested(String username) throws IOException {
        return getUser().getAccountsRequestedToFollow().contains(accountRepository.getAccountByUserName(username).getId());
    }

    public void block(String userName) throws IOException {
        Account account = accountRepository.getAccountByUserName(userName);
        getUser().addBlackList(account.getId());
        getUser().setNumberOfBlackList();
        if (getUser().getFollowers().contains(account.getId())) {
            getUser().getFollowers().remove(account.getId());
            getUser().setNumberOfFollowers();
            account.getFollowings().remove(getUser().getId());
            account.setNumberOfFollowings();
        }
        if (getUser().getFollowings().contains(account.getId())) {
            getUser().getFollowings().remove(account.getId());
            getUser().setNumberOfFollowings();
            account.getFollowers().remove(getUser().getId());
            account.setNumberOfFollowers();
        }
        accountRepository.update(getUser());
        accountRepository.update(account);
    }

    public boolean isBlocked(String username) throws IOException {
        return getUser().getBlacklist().contains(accountRepository.getAccountByUserName(username).getId());
    }

    public void unblock(String userName) throws IOException {
        getUser().getBlacklist().remove(accountRepository.getAccountByUserName(userName).getId());
        getUser().setNumberOfBlackList();
        accountRepository.update(getUser());
    }

    public boolean ifYouAreBlocked(String username) throws IOException {
        return accountRepository.getAccountByUserName(username).getBlacklist().contains(getUser().getId());
    }

    public void likeOrRemoveLike(Tweet tweet) throws IOException {
        if (!isLiked(tweet)) {
            getUser().addLikedTweet(tweet.getId());
            tweet.addAccountLiked(new Record(getUser().getId(), Time.now(), Record.LIKE_RECORD));
            tweet.addIdAccountLiked(getUser().getId());
            tweet.setNumberOfLikes();
        } else {
            getUser().getLikedTweet().remove(tweet.getId());
            tweet.getIdAccountLiked().remove(getUser().getId());
            tweet.setNumberOfLikes();
        }
        accountRepository.update(getUser());
        tweetRepository.update(tweet);
    }

    public boolean isLiked(Tweet tweet) {
        return getUser().getLikedTweet().contains(tweet.getId());
    }

    public void retweetOrRemoveRetweet(Tweet tweet) throws IOException {
        if (!isRetweeted(tweet)) {
            getUser().addTweet(tweet.getId());
            tweet.addAccountRetweeted(new Record(getUser().getId(), Time.now(), Record.RETWEET_RECORD));
            tweet.addIdAccountRetweeted(getUser().getId());
            tweet.setNumberOfRetweets();
        } else {
            getUser().getTweets().remove(tweet.getId());
            tweet.getIdAccountRetweeted().remove(getUser().getId());
            tweet.setNumberOfRetweets();
        }
        accountRepository.update(getUser());
        tweetRepository.update(tweet);
    }

    public boolean isRetweeted(Tweet tweet) {
        return tweet.getIdAccountRetweeted().contains(getUser().getId());
    }

    public void saveTweet(Tweet tweet) throws IOException {
        getUser().addSavedTweet(tweet.getId());
        tweet.addIdAccountSaved(getUser().getId());
        accountRepository.update(getUser());
        tweetRepository.update(tweet);
    }

    public boolean checkIfExist(String userName) throws IOException {
        return accountRepository.getAccountByUserName(userName) != null;
    }

    public boolean checkPassword(String userName, String password) throws IOException {
        return accountRepository.getAccountByUserName(userName).getPassword().equals(password);
    }

    public void muteOrUnmute(String username) throws IOException {
        if (!isMute(username)) {
            getUser().addMutedAccount(accountRepository.getAccountByUserName(username).getId());
        } else {
            getUser().getMutedAccounts().remove(accountRepository.getAccountByUserName(username).getId());
        }
        accountRepository.update(getUser());
    }

    public boolean isMute(String username) throws IOException {
        return getUser().getMutedAccounts().contains(accountRepository.getAccountByUserName(username).getId());
    }

    public LinkedList<String> viewAccountList(LinkedList<Long> list) throws IOException {
        LinkedList<String> username = new LinkedList<>();
        for (long id : list) {
            username.add(accountRepository.getAccount(id).getUserName());
        }
        return username;
    }

    public void updateStatus(int status) throws IOException {
        if (!ifRecently()) {
            if (status == Account.OFFLINE) {
                getUser().setStatus("Last seen at " + Time.now());
            } else {
                getUser().setStatus("Online");
            }
        } else {
            getUser().setStatus("Last seen recently");
        }
        accountRepository.update(getUser());
    }

    public boolean ifRecently() {
        return getUser().getStatus().equals("Last seen recently");
    }

    public void deleteAccount() throws IOException {
        for (long id : getUser().getTweets()) {
            Tweet tweet = tweetRepository.getTweet(id);
            tweetManager.deleteTweet(tweet);
        }
        Account account;
        for (long id : getUser().getFollowers()) {
            account = accountRepository.getAccount(id);
            account.getFollowings().remove(getUser().getId());
            account.setNumberOfFollowings();
            accountRepository.update(account);
        }
        for (long id : getUser().getFollowings()) {
            account = accountRepository.getAccount(id);
            account.getFollowers().remove(getUser().getId());
            account.setNumberOfFollowers();
            accountRepository.update(account);
        }
        for (long id : getUser().getAccountsRequestedToFollow()) {
            account = accountRepository.getAccount(id);
            account.getFollowRequest().remove(getUser().getId());
            account.setNumberOfFollowRequest();
            accountRepository.update(account);
        }
        for (long id : getUser().getFollowRequest()) {
            account = accountRepository.getAccount(id);
            account.getAccountsRequestedToFollow().remove(getUser().getId());
            account.setNumberOfAccountsSentRequest();
            accountRepository.update(account);
        }
        accountRepository.removeAccount(getUser().getId());
    }

    public void changePassword(String oldPassword, String newPassword) throws IOException {
        if (getUser().getPassword().equals(oldPassword)) {
            getUser().setPassword(newPassword);
        }
        accountRepository.update(getUser());
    }

    public void changeUserName(String newUserName) throws IOException {
        Account account = accountRepository.getAccountByUserName(newUserName);
        if (account == null) {
            getUser().setUserName(newUserName);
            accountRepository.update(getUser());
        }
    }

    public void changeBiography(String biography) throws IOException {
        getUser().setBiography(biography);
        accountRepository.update(getUser());
    }

    public void changeDateOfBirth(String dateOfBirth) throws IOException {
        getUser().setDateOfBirth(dateOfBirth);
        accountRepository.update(getUser());
    }

    public void changePhoneNumber(long phoneNum) throws IOException {
        getUser().setPhoneNumber(phoneNum);
        accountRepository.update(getUser());
    }

    public void changeName(String name) throws IOException {
        getUser().setName(name);
        accountRepository.update(getUser());
    }

    public void changeEmailAddress(String emailAddress) throws IOException {
        getUser().setEmailAddress(emailAddress);
        accountRepository.update(getUser());
    }

    public String getUsername(long id) throws IOException {
        return accountRepository.getAccount(id).getUserName();
    }

    public String getName(String username) throws IOException {
        return accountRepository.getAccountByUserName(username)
                .getName();
    }

    public String getBiography(String username) throws IOException {
        return accountRepository.getAccountByUserName(username).getBiography();
    }

    public int getNumberOfFollowers(String username) throws IOException {
        return accountRepository.getAccountByUserName(username).getNumberOfFollowers();
    }

    public int getNumberOfFollowings(String username) throws IOException {
        return accountRepository.getAccountByUserName(username).getNumberOfFollowings();
    }

    public long getNumberOfTweets(String username) throws IOException {
        return accountRepository.getAccountByUserName(username).getNumberOfTweets();
    }

    public LinkedList<Long> getFollowersList(String username) throws IOException {
        return accountRepository.getAccountByUserName(username).getFollowers();
    }

    public LinkedList<Long> getFollowingsList(String username) throws IOException {
        return accountRepository.getAccountByUserName(username).getFollowings();
    }

    public LinkedList<Long> getTweetList(String username) throws IOException {
        return accountRepository.getAccountByUserName(username).getTweets();
    }

    public String getStatus(String username) throws IOException {
        return accountRepository.getAccountByUserName(username).getStatus();
    }

}
