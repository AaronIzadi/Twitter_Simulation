package twitter.logic;

import twitter.model.Account;
import twitter.model.Record;
import twitter.model.Time;
import twitter.model.Tweet;
import twitter.repository.AccountRepository;
import twitter.repository.SecureAccountRepository;
import twitter.repository.TweetHashMapRepository;
import twitter.repository.TweetRepository;

import java.util.LinkedList;
import java.util.List;

public class AccountManager {

    TweetRepository tweetRepository = TweetHashMapRepository.getInstance();
    AccountRepository accountRepository = SecureAccountRepository.getInstance();
    TweetManager tweetManager = new TweetManager();

    public Account getUser() {
        return accountRepository.getUser();
    }

    public void login(String userName) {
        accountRepository.setUser(accountRepository.getAccountByUserName(userName));
    }

    public void logout() {
        accountRepository.setUser(null);
    }

    public void createAccount(String username, String password) {
        Account acc = new Account(username, password, Account.DEFAULT);
        accountRepository.add(acc);
        accountRepository.setUser(acc);
    }

    public boolean isPublic(String username){
        return accountRepository.getAccountByUserName(username).getType() == Account.DEFAULT;
    }

    public void makePublicOrPrivate(){
        if (isPublic(getUser().getUserName())){
            getUser().setType(Account.PRIVATE);
        }else{
            getUser().setType(Account.PUBLIC);
        }
    }

    public void followOrUnfollow(String userName) {
        if (!isFollowed(userName)) {
            getUser().setNumberOfFollowings();
            accountRepository.getAccountByUserName(userName).setNumberOfFollowers();
            getUser().setFollowings(accountRepository.getAccountByUserName(userName).getId());
            accountRepository.getAccountByUserName(userName).setFollowers(getUser().getId());
        } else {
            getUser().setNumberOfFollowings();
            accountRepository.getAccountByUserName(userName).setNumberOfFollowers();
            getUser().getFollowings().remove(accountRepository.getAccountByUserName(userName).getId());
            accountRepository.getAccountByUserName(userName).getFollowers().remove(getUser().getId());
        }
        accountRepository.update(getUser());
        accountRepository.update(accountRepository.getAccountByUserName(userName));
    }

    public boolean isFollowingYOu(String username) {
        return getUser().getFollowers().contains(accountRepository.getAccountByUserName(username).getId());
    }

    public boolean isFollowed(String username) {
        return getUser().getFollowings().contains(accountRepository.getAccountByUserName(username).getId());
    }

    public void sendFollowRequest(String username){
        getUser().setAccountsRequestedToFollow(accountRepository.getAccountByUserName(username).getId());
        getUser().setNumberOfAccountsSentRequest(1);
        accountRepository.getAccountByUserName(username).setFollowRequest(getUser().getId());
        accountRepository.getAccountByUserName(username).setNumberOfFollowRequest(1);
        accountRepository.update(accountRepository.getAccountByUserName(username));
        accountRepository.update(getUser());
    }

    public void unsendFollowRequest(String username){
        getUser().getAccountsRequestedToFollow().remove(accountRepository.getAccountByUserName(username).getId());
        getUser().setNumberOfAccountsSentRequest(-1);
        accountRepository.getAccountByUserName(username).setNumberOfFollowRequest(-1);
        accountRepository.getAccountByUserName(username).getFollowRequest().remove(getUser().getId());
        accountRepository.update(accountRepository.getAccountByUserName(username));
        accountRepository.update(getUser());
    }

    public void acceptFollowRequest(String username){
        accountRepository.getAccountByUserName(username).setNumberOfFollowings();
        accountRepository.getAccountByUserName(username).setFollowings(getUser().getId());
        accountRepository.getAccountByUserName(username).getAccountsRequestedToFollow().remove(getUser().getId());
        accountRepository.getAccountByUserName(username).setNumberOfAccountsSentRequest(-1);
        getUser().setNumberOfFollowers();
        getUser().setFollowers(accountRepository.getAccountByUserName(username).getId());
        getUser().setNumberOfFollowRequest(-1);
        getUser().getFollowRequest().remove(accountRepository.getAccountByUserName(username).getId());
        accountRepository.update(accountRepository.getAccountByUserName(username));
        accountRepository.update(getUser());
    }

    public void deleteFollowRequest(String username){
        accountRepository.getAccountByUserName(username).getAccountsRequestedToFollow().remove(getUser().getId());
        accountRepository.getAccountByUserName(username).setNumberOfAccountsSentRequest(-1);
        getUser().setNumberOfFollowRequest(-1);
        getUser().getFollowRequest().remove(accountRepository.getAccountByUserName(username).getId());
        accountRepository.update(accountRepository.getAccountByUserName(username));
        accountRepository.update(getUser());
    }

    public boolean isRequested(String username){
        return getUser().getAccountsRequestedToFollow().contains(accountRepository.getAccountByUserName(username).getId());
    }

    public void block(String userName) {
        getUser().setBlacklist(accountRepository.getAccountByUserName(userName).getId());
        getUser().setNumberOfBlackList(1);
        if (getUser().getFollowers().contains(accountRepository.getAccountByUserName(userName).getId())) {
            getUser().getFollowers().remove(accountRepository.getAccountByUserName(userName).getId());
            getUser().setNumberOfFollowers();
            accountRepository.getAccountByUserName(userName).getFollowings().remove(getUser().getId());
            accountRepository.getAccountByUserName(userName).setNumberOfFollowings();
        }
        if (getUser().getFollowings().contains(accountRepository.getAccountByUserName(userName).getId())) {
            getUser().getFollowings().remove(accountRepository.getAccountByUserName(userName).getId());
            getUser().setNumberOfFollowings();
            accountRepository.getAccountByUserName(userName).getFollowers().remove(getUser().getId());
            accountRepository.getAccountByUserName(userName).setNumberOfFollowers();
        }
        accountRepository.update(getUser());
        accountRepository.update(accountRepository.getAccountByUserName(userName));
    }

    public boolean isBlocked(String username) {
        return getUser().getBlacklist().contains(accountRepository.getAccountByUserName(username).getId());
    }

    public void unblock(String userName) {
        getUser().getBlacklist().remove(accountRepository.getAccountByUserName(userName).getId());
        getUser().setNumberOfBlackList(-1);
        accountRepository.update(getUser());
    }

    public boolean ifYouAreBlocked(String username){
        return accountRepository.getAccountByUserName(username).getBlacklist().contains(getUser().getId());
    }

    public void likeOrRemoveLike(Tweet tweet) {
        if (!isLiked(tweet)) {
            tweet.setNumberOfLikes(1);
            tweet.setAccountLiked(new Record(getUser().getId(), Time.now(), Record.LIKE_RECORD));
            tweet.setIdAccountLiked(getUser().getId());
            getUser().setLikedTweet(tweet.getId());
        } else {
            tweet.setNumberOfLikes(-1);
            getUser().getLikedTweet().remove(tweet.getId());
            tweet.getIdAccountLiked().remove(getUser().getId());
        }
        accountRepository.update(getUser());
        tweetRepository.update(tweet);
    }

    public boolean isLiked(Tweet tweet) {
        return getUser().getLikedTweet().contains(tweet.getId());
    }

    public void retweetOrUndo(Tweet tweet) {
        if (!isRetweeted(tweet)) {
            getUser().setTweets(tweet.getId());
            tweet.setNumberOfRetweets(1);
            tweet.setIdAccountRetweeted(getUser().getId());
            tweet.setAccountRetweeted(new Record(getUser().getId(), Time.now(), Record.RETWEET_RECORD));
        } else {
            getUser().getTweets().remove(tweet.getId());
            tweet.setNumberOfRetweets(-1);
            tweet.getIdAccountRetweeted().remove(getUser().getId());
        }
        accountRepository.update(getUser());
        tweetRepository.update(tweet);
    }

    public boolean isRetweeted(Tweet tweet) {
        return tweet.getIdAccountRetweeted().contains(getUser().getId());
    }

    public void changePassword(String oldPassword, String newPassword) {
        if (getUser().getPassword().equals(oldPassword)) {
            getUser().setPassword(newPassword);
        }
        accountRepository.update(getUser());
    }

    public void saveTweet(Tweet tweet) {
        getUser().setSavedTweet(tweet.getId());
        tweet.setIdAccountSaved(getUser().getId());
        accountRepository.update(getUser());
        tweetRepository.update(tweet);
    }

    public void changeUserName(String newUserName) {
        Account acc = accountRepository.getAccountByUserName(newUserName);
        if (acc == null) {
            getUser().setUserName(newUserName);
            accountRepository.update(getUser());
        }
    }

    public boolean checkIfExist(String userName) {
        return accountRepository.getAccountByUserName(userName) != null;
    }

    public boolean checkPassword(String userName, String password) {
        return accountRepository.getAccountByUserName(userName).getPassword().equals(password);
    }

    public void changeBiography(String biography) {
        getUser().setBiography(biography);
        accountRepository.update(getUser());
    }

    public void changeDateOfBirth(String dateOfBirth) {
        getUser().setDateOfBirth(dateOfBirth);
        accountRepository.update(getUser());
    }

    public void changePhoneNum(long phoneNum) {
        getUser().setPhoneNumber(phoneNum);
        accountRepository.update(getUser());
    }

    public void changeName(String name) {
        getUser().setName(name);
        accountRepository.update(getUser());
    }

    public void changeEmailAddress(String emailAddress) {
        getUser().setEmailAddress(emailAddress);
        accountRepository.update(getUser());
    }

    public void muteOrUnmute(String username) {
        if (!isMute(username)) {
            getUser().setMutedAccounts(accountRepository.getAccountByUserName(username).getId());
        } else {
            getUser().getMutedAccounts().remove(accountRepository.getAccountByUserName(username).getId());
        }
        accountRepository.update(getUser());
    }

    public boolean isMute(String username) {
        return getUser().getMutedAccounts().contains(accountRepository.getAccountByUserName(username).getId());
    }

    public List<String> viewAccountList(List<Long> list) {
        List<String> username = new LinkedList<>();
        for (long id : list) {
            username.add(accountRepository.getAccount(id).getUserName());
        }
        return username;
    }

    public String getUsername(long id) {
        return accountRepository.getAccount(id).getUserName();
    }

    public String getName(String username) {
        return accountRepository.getAccountByUserName(username)
                .getName();
    }

    public String getBiography(String username) {
        return accountRepository.getAccountByUserName(username).getBiography();
    }

    public int getNumberOfFollowers(String username) {
        return accountRepository.getAccountByUserName(username).getNumberOfFollowers();
    }

    public int getNumberOfFollowings(String username) {
        return accountRepository.getAccountByUserName(username).getNumberOfFollowings();
    }

    public long getNumberOfTweets(String username) {
        return accountRepository.getAccountByUserName(username).getNumberOfTweets();
    }

    public List<Long> getFollowersList(String username) {
        return accountRepository.getAccountByUserName(username).getFollowers();
    }

    public List<Long> getFollowingsList(String username) {
        return accountRepository.getAccountByUserName(username).getFollowings();
    }

    public List<Long> getTweetList(String username) {
        return accountRepository.getAccountByUserName(username).getTweets();
    }

    public void updateStatus(int status) {
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

    public String getStatus(String username) {
        return accountRepository.getAccountByUserName(username).getStatus();
    }

    public boolean ifRecently(){
        return getUser().getStatus().equals("Last seen recently");
    }

    public void deleteAccount(){
        for (long id: getUser().getTweets()) {
            Tweet tweet = tweetRepository.getTweet(id);
            tweetManager.deleteTweet(tweet);
        }
        for (long id: getUser().getFollowers() ) {
            accountRepository.getAccount(id).getFollowings().remove(getUser().getId());
            accountRepository.getAccount(id).setNumberOfFollowings();
            accountRepository.update(accountRepository.getAccount(id));
        }
        for (long id:getUser().getFollowings()) {
            accountRepository.getAccount(id).getFollowers().remove(getUser().getId());
            accountRepository.getAccount(id).setNumberOfFollowers();
            accountRepository.update(accountRepository.getAccount(id));
        }
        for (long id: getUser().getAccountsRequestedToFollow()) {
            accountRepository.getAccount(id).getFollowRequest().remove(getUser().getId());
            accountRepository.getAccount(id).setNumberOfFollowRequest(-1);
            accountRepository.update(accountRepository.getAccount(id));
        }
        for (long id: getUser().getFollowRequest()) {
            accountRepository.getAccount(id).getAccountsRequestedToFollow().remove(getUser().getId());
            accountRepository.getAccount(id).setNumberOfAccountsSentRequest(-1);
            accountRepository.update(accountRepository.getAccount(id));
        }
        accountRepository.removeAccount(getUser().getId());
    }

}
