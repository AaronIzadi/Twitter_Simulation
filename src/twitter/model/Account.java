package twitter.model;

import java.util.LinkedList;
import java.util.List;

public class Account {
    private String userName;
    private long id;
    private String name;
    private String emailAddress;
    private String password;
    private String dateOfBirth;
    private String biography;
    private String status;
    public static final int OFFLINE = 0;
    public static final int ONLINE = 1;
    public static final int DEFAULT_STATUS = 2;
    private int type;
    public static final int DEFAULT = 1;
    public static final int PUBLIC = 1;
    public static final int PRIVATE = 2;
    private long phoneNumber;
    private int numberOfFollowers;
    private int numberOfFollowings;
    private int numberOfBlackList;
    private int numberOfFollowRequest;
    private int numberOfAccountsSentRequest;
    private long numberOfTweets;
    private List<Long> followers = new LinkedList<>();
    private List<Long> followings = new LinkedList<>();
    private List<Long> blacklist = new LinkedList<>();
    private List<Long> mutedAccounts = new LinkedList<>();
    private List<Long> tweets = new LinkedList<>();
    private List<Long> replied = new LinkedList<>();
    private List<Long> savedTweet = new LinkedList<>();
    private List<Long> likedTweet = new LinkedList<>();
    private List<Long> followRequest = new LinkedList<>();
    private List<Long> accountsRequestedToFollow = new LinkedList<>();

    public Account(String userName, String password, int type) {
        this.userName = userName;
        this.password = password;
        this.type = type;
        this.status = "Last seen recently";
    }

    public Account(String userName, String password, int type, int status) {
        this.userName = userName;
        this.password = password;
        this.type = type;
        switch (status) {
            case 0:
                this.status = "Offline";
                break;
            case 1:
                this.status = "Online";
                break;
            default:
                this.status = "Last seen recently";
                break;
        }
    }

    public long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getName() {
        return name;
    }

    public List<Long> getTweets() {
        return tweets;
    }

    public String getPassword() {
        return password;
    }

    public List<Long> getFollowers() {
        return followers;
    }

    public List<Long> getFollowings() {
        return followings;
    }

    public List<Long> getLikedTweet() {
        return likedTweet;
    }

    public List<Long> getBlacklist() {
        return blacklist;
    }

    public String getBiography() {
        return biography;
    }

    public int getNumberOfFollowers() {
        return numberOfFollowers;
    }

    public int getNumberOfFollowings() {
        return numberOfFollowings;
    }

    public int getNumberOfBlackList() {
        return numberOfBlackList;
    }

    public long getNumberOfTweets() {
        return numberOfTweets;
    }

    public List<Long> getMutedAccounts() {
        return mutedAccounts;
    }

    public long getPhoneNumber() {
        return phoneNumber;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public List<Long> getSavedTweet() {
        return savedTweet;
    }

    public String getStatus() {
        return status;
    }

    public int getNumberOfFollowRequest() {
        return numberOfFollowRequest;
    }

    public int getNumberOfAccountsSentRequest() {
        return numberOfAccountsSentRequest;
    }

    public List<Long> getFollowRequest() {
        return followRequest;
    }

    public List<Long> getAccountsRequestedToFollow() {
        return accountsRequestedToFollow;
    }

    public int getType() {
        return type;
    }

    public List<Long> getReplied() {
        return replied;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setNumberOfAccountsSentRequest(int num) {
        numberOfAccountsSentRequest += num;
    }

    public void setNumberOfFollowRequest(int followRequest) {
        numberOfFollowRequest += followRequest;
    }

    public void setFollowRequest(long request) {
        followRequest.add(request);
    }
    public void setFollowRequest(LinkedList<Long> followRequest) { this.followRequest = followRequest;}

    public void setAccountsRequestedToFollow(long sendRequestTo) {
        accountsRequestedToFollow.add(sendRequestTo);
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setMutedAccounts(long idNum) {
        mutedAccounts.add(idNum);
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setTweets(long tweetId) {
        tweets.add(tweetId);
    }

    public void setSavedTweet(long idNum) {
        savedTweet.add(idNum);
    }

    public void setLikedTweet(long idNum) {
        likedTweet.add(idNum);
    }

    public void setReplied(long repliedId) {
        replied.add(repliedId);
    }

    public void setNumberOfTweets(int n) {
        numberOfTweets += n;
    }

    public void setNumberOfFollowers() {
        numberOfFollowers = followers.size();
    }

    public void setFollowers(long idNum) {
        followers.add(idNum);
    }

    public void setNumberOfFollowings() {
        numberOfFollowings = followings.size();
    }

    public void setFollowings(long idNum) {
        followings.add(idNum);
    }

    public void setNumberOfBlackList(int n) {
        numberOfBlackList += n;
    }

    public void setBlacklist(long idNum) {
        blacklist.add(idNum);
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setPhoneNumber(long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public void setFollowers(List<Long> followers) {
        this.followers = followers;
    }

    public void setFollowings(List<Long> followings) {
        this.followings = followings;
    }

    public void setBlacklist(List<Long> blacklist) {
        this.blacklist = blacklist;
    }

    public void setMutedAccounts(List<Long> mutedAccounts) {
        this.mutedAccounts = mutedAccounts;
    }

    public void setTweets(List<Long> tweets) {
        this.tweets = tweets;
    }

    public void setReplied(List<Long> replied) {
        this.replied = replied;
    }

    public void setSavedTweet(List<Long> savedTweet) {
        this.savedTweet = savedTweet;
    }

    public void setLikedTweet(List<Long> likedTweet) {
        this.likedTweet = likedTweet;
    }

    public void setFollowRequest(List<Long> followRequest) {
        this.followRequest = followRequest;
    }

    public void setAccountsRequestedToFollow(List<Long> accountsRequestedToFollow) {
        this.accountsRequestedToFollow = accountsRequestedToFollow;
    }
}
