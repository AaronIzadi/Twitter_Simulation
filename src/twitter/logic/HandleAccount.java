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

public class HandleAccount {

    TweetRepository tweetRep = TweetHashMapRepository.getInstance();
    AccountRepository accountRep = SecureAccountRepository.getInstance();
    HandleTweet handleTweet = new HandleTweet();

    public Account getUser() {
        return accountRep.getUser();
    }

    public void login(String userName) {
        accountRep.setUser(accountRep.getAccountByUserName(userName));
    }

    public void logout() {
        accountRep.setUser(null);
    }

    public void createAccount(String username, String password) {
        Account acc = new Account(username, password, Account.DEFAULT, Account.DEFAULT_STATUS);
        accountRep.add(acc);
        accountRep.setUser(acc);
    }

    public boolean isPublic(String username){
        return accountRep.getAccountByUserName(username).getType() == Account.DEFAULT;
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
            getUser().setNumberOfFollowings(1);
            accountRep.getAccountByUserName(userName).setNumberOfFollowers(1);
            getUser().setFollowings(accountRep.getAccountByUserName(userName).getId());
            accountRep.getAccountByUserName(userName).setFollowers(getUser().getId());
        } else {
            getUser().setNumberOfFollowings(-1);
            accountRep.getAccountByUserName(userName).setNumberOfFollowers(-1);
            getUser().getFollowings().remove(accountRep.getAccountByUserName(userName).getId());
            accountRep.getAccountByUserName(userName).getFollowers().remove(getUser().getId());
        }
        accountRep.update(getUser());
        accountRep.update(accountRep.getAccountByUserName(userName));
    }

    public boolean isFollowingYOu(String username) {
        return getUser().getFollowers().contains(accountRep.getAccountByUserName(username).getId());
    }

    public boolean isFollowed(String username) {
        return getUser().getFollowings().contains(accountRep.getAccountByUserName(username).getId());
    }

    public void sendFollowRequest(String username){
        getUser().setAccountsRequestedToFollow(accountRep.getAccountByUserName(username).getId());
        getUser().setNumberOfAccountsSentRequest(1);
        accountRep.getAccountByUserName(username).setFollowRequest(getUser().getId());
        accountRep.getAccountByUserName(username).setNumberOfFollowRequest(1);
        accountRep.update(accountRep.getAccountByUserName(username));
        accountRep.update(getUser());
    }

    public void unsendFollowRequest(String username){
        getUser().getAccountsRequestedToFollow().remove(accountRep.getAccountByUserName(username).getId());
        getUser().setNumberOfAccountsSentRequest(-1);
        accountRep.getAccountByUserName(username).setNumberOfFollowRequest(-1);
        accountRep.getAccountByUserName(username).getFollowRequest().remove(getUser().getId());
        accountRep.update(accountRep.getAccountByUserName(username));
        accountRep.update(getUser());
    }

    public void acceptFollowRequest(String username){
        accountRep.getAccountByUserName(username).setNumberOfFollowings(1);
        accountRep.getAccountByUserName(username).setFollowings(getUser().getId());
        accountRep.getAccountByUserName(username).getAccountsRequestedToFollow().remove(getUser().getId());
        accountRep.getAccountByUserName(username).setNumberOfAccountsSentRequest(-1);
        getUser().setNumberOfFollowers(1);
        getUser().setFollowers(accountRep.getAccountByUserName(username).getId());
        getUser().setNumberOfFollowRequest(-1);
        getUser().getFollowRequest().remove(accountRep.getAccountByUserName(username).getId());
        accountRep.update(accountRep.getAccountByUserName(username));
        accountRep.update(getUser());
    }

    public void deleteFollowRequest(String username){
        accountRep.getAccountByUserName(username).getAccountsRequestedToFollow().remove(getUser().getId());
        accountRep.getAccountByUserName(username).setNumberOfAccountsSentRequest(-1);
        getUser().setNumberOfFollowRequest(-1);
        getUser().getFollowRequest().remove(accountRep.getAccountByUserName(username).getId());
        accountRep.update(accountRep.getAccountByUserName(username));
        accountRep.update(getUser());
    }

    public boolean isRequested(String username){
        return getUser().getAccountsRequestedToFollow().contains(accountRep.getAccountByUserName(username).getId());
    }

    public void block(String userName) {
        getUser().setBlacklist(accountRep.getAccountByUserName(userName).getId());
        getUser().setNumberOfBlackList(1);
        if (getUser().getFollowers().contains(accountRep.getAccountByUserName(userName).getId())) {
            getUser().getFollowers().remove(accountRep.getAccountByUserName(userName).getId());
            getUser().setNumberOfFollowers(-1);
            accountRep.getAccountByUserName(userName).getFollowings().remove(getUser().getId());
            accountRep.getAccountByUserName(userName).setNumberOfFollowings(-1);
        }
        if (getUser().getFollowings().contains(accountRep.getAccountByUserName(userName).getId())) {
            getUser().getFollowings().remove(accountRep.getAccountByUserName(userName).getId());
            getUser().setNumberOfFollowings(-1);
            accountRep.getAccountByUserName(userName).getFollowers().remove(getUser().getId());
            accountRep.getAccountByUserName(userName).setNumberOfFollowers(-1);
        }
        accountRep.update(getUser());
        accountRep.update(accountRep.getAccountByUserName(userName));
    }

    public boolean isBlocked(String username) {
        return getUser().getBlacklist().contains(accountRep.getAccountByUserName(username).getId());
    }

    public void unblock(String userName) {
        getUser().getBlacklist().remove(accountRep.getAccountByUserName(userName).getId());
        getUser().setNumberOfBlackList(-1);
        accountRep.update(getUser());
    }

    public boolean ifYouAreBlocked(String username){
        return accountRep.getAccountByUserName(username).getBlacklist().contains(getUser().getId());
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
        accountRep.update(getUser());
        tweetRep.update(tweet);
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
        accountRep.update(getUser());
        tweetRep.update(tweet);
    }

    public boolean isRetweeted(Tweet tweet) {
        return tweet.getIdAccountRetweeted().contains(getUser().getId());
    }

    public void changePassword(String oldPassword, String newPassword) {
        if (getUser().getPassword().equals(oldPassword)) {
            getUser().setPassword(newPassword);
        }
        accountRep.update(getUser());
    }

    public void saveTweet(Tweet tweet) {
        getUser().setSavedTweet(tweet.getId());
        tweet.setIdAccountSaved(getUser().getId());
        accountRep.update(getUser());
        tweetRep.update(tweet);
    }

    public void changeUserName(String newUserName) {
        Account acc = accountRep.getAccountByUserName(newUserName);
        if (acc == null) {
            getUser().setUserName(newUserName);
            accountRep.update(getUser());
        }
    }

    public boolean checkIfExist(String userName) {
        return accountRep.getAccountByUserName(userName) != null;
    }

    public boolean checkPassword(String userName, String password) {
        return accountRep.getAccountByUserName(userName).getPassword().equals(password);
    }

    public void changeBiography(String biography) {
        getUser().setBiography(biography);
        accountRep.update(getUser());
    }

    public void changeDateOfBirth(String dateOfBirth) {
        getUser().setDateOfBirth(dateOfBirth);
        accountRep.update(getUser());
    }

    public void changePhoneNum(long phoneNum) {
        getUser().setPhoneNumber(phoneNum);
        accountRep.update(getUser());
    }

    public void changeName(String name) {
        getUser().setName(name);
        accountRep.update(getUser());
    }

    public void changeEmailAddress(String emailAddress) {
        getUser().setEmailAddress(emailAddress);
        accountRep.update(getUser());
    }

    public void muteOrUnmute(String username) {
        if (!isMute(username)) {
            getUser().setMutedAccounts(accountRep.getAccountByUserName(username).getId());
        } else {
            getUser().getMutedAccounts().remove(accountRep.getAccountByUserName(username).getId());
        }
        accountRep.update(getUser());
    }

    public boolean isMute(String username) {
        return getUser().getMutedAccounts().contains(accountRep.getAccountByUserName(username).getId());
    }

    public List<String> viewAccountList(List<Long> list) {
        List<String> username = new LinkedList<>();
        for (long id : list) {
            username.add(accountRep.getAccount(id).getUserName());
        }
        return username;
    }

    public String getUsername(long id) {
        return accountRep.getAccount(id).getUserName();
    }

    public String getName(String username) {
        return accountRep.getAccountByUserName(username)
                .getName();
    }

    public String getBiography(String username) {
        return accountRep.getAccountByUserName(username).getBiography();
    }

    public int getNumberOfFollowers(String username) {
        return accountRep.getAccountByUserName(username).getNumberOfFollowers();
    }

    public int getNumberOfFollowings(String username) {
        return accountRep.getAccountByUserName(username).getNumberOfFollowings();
    }

    public long getNumberOfTweets(String username) {
        return accountRep.getAccountByUserName(username).getNumberOfTweets();
    }

    public List<Long> getFollowersList(String username) {
        return accountRep.getAccountByUserName(username).getFollowers();
    }

    public List<Long> getFollowingsList(String username) {
        return accountRep.getAccountByUserName(username).getFollowings();
    }

    public List<Long> getTweetList(String username) {
        return accountRep.getAccountByUserName(username).getTweets();
    }

    public void updateStatus(int status) {
        if (!ifRecently()) {
            if (status == Account.OFFLINE) {
                getUser().setStatus("Last seen at " + Time.now().toString());
            } else {
                getUser().setStatus("Online");
            }
        } else {
            getUser().setStatus("Last seen recently");
        }
        accountRep.update(getUser());
    }

    public String getStatus(String username) {
        return accountRep.getAccountByUserName(username).getStatus();
    }

    public boolean ifRecently(){
        return getUser().getStatus().equals("Last seen recently");
    }

    public void deleteAccount(){
        for (long id: getUser().getTweets()) {
            Tweet tweet = tweetRep.getTweet(id);
            handleTweet.deleteTweet(tweet);
        }
        for (long id: getUser().getFollowers() ) {
            accountRep.getAccount(id).getFollowings().remove(getUser().getId());
            accountRep.getAccount(id).setNumberOfFollowings(-1);
            accountRep.update(accountRep.getAccount(id));
        }
        for (long id:getUser().getFollowings()) {
            accountRep.getAccount(id).getFollowers().remove(getUser().getId());
            accountRep.getAccount(id).setNumberOfFollowers(-1);
            accountRep.update(accountRep.getAccount(id));
        }
        for (long id: getUser().getAccountsRequestedToFollow()) {
            accountRep.getAccount(id).getFollowRequest().remove(getUser().getId());
            accountRep.getAccount(id).setNumberOfFollowRequest(-1);
            accountRep.update(accountRep.getAccount(id));
        }
        for (long id: getUser().getFollowRequest()) {
            accountRep.getAccount(id).getAccountsRequestedToFollow().remove(getUser().getId());
            accountRep.getAccount(id).setNumberOfAccountsSentRequest(-1);
            accountRep.update(accountRep.getAccount(id));
        }
        accountRep.removeAccount(getUser().getId());
    }

}
