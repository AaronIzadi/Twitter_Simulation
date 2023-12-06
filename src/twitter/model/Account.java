package twitter.model;

import java.util.LinkedList;

public class Account {
    private String userName;
    private long id;
    private static long idCounter = 0;
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
    private LinkedList<Long> followers = new LinkedList<>();
    private LinkedList<Long> followings = new LinkedList<>();
    private LinkedList<Long> blacklist = new LinkedList<>();
    private LinkedList<Long> mutedAccounts = new LinkedList<>();
    private LinkedList<Long> tweets = new LinkedList<>();
    private LinkedList<Long> replied = new LinkedList<>();
    private LinkedList<Long> savedTweet = new LinkedList<>();
    private LinkedList<Long> likedTweet = new LinkedList<>();
    private LinkedList<Long> followRequest = new LinkedList<>();
    private LinkedList<Long> accountsRequestedToFollow = new LinkedList<>();

    public Account(String userName, String password, int type) {
        this.userName = userName;
        this.password = password;
        this.type = type;
        this.status = "Last seen recently";
        this.setId(idCounter);
        idCounter++;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public static long getIdCounter() {
        return idCounter;
    }

    public static void setIdCounter(long idCounter) {
        Account.idCounter = idCounter;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getNumberOfFollowers() {
        return numberOfFollowers;
    }

    public void setNumberOfFollowers(int numberOfFollowers) {
        this.numberOfFollowers = numberOfFollowers;
    }

    public void setNumberOfFollowers() {
        this.numberOfFollowers = followers.size();
    }

    public int getNumberOfFollowings() {
        return numberOfFollowings;
    }

    public void setNumberOfFollowings(int numberOfFollowings) {
        this.numberOfFollowings = numberOfFollowings;
    }

    public void setNumberOfFollowings() {
        this.numberOfFollowings = followings.size();
    }

    public int getNumberOfBlackList() {
        return numberOfBlackList;
    }

    public void setNumberOfBlackList(int numberOfBlackList) {
        this.numberOfBlackList = numberOfBlackList;
    }

    public void setNumberOfBlackList() {
        this.numberOfBlackList = blacklist.size();
    }

    public int getNumberOfFollowRequest() {
        return numberOfFollowRequest;
    }

    public void setNumberOfFollowRequest(int numberOfFollowRequest) {
        this.numberOfFollowRequest = numberOfFollowRequest;
    }

    public void setNumberOfFollowRequest() {
        this.numberOfFollowRequest = followRequest.size();
    }

    public int getNumberOfAccountsSentRequest() {
        return numberOfAccountsSentRequest;
    }

    public void setNumberOfAccountsSentRequest(int numberOfAccountsSentRequest) {
        this.numberOfAccountsSentRequest = numberOfAccountsSentRequest;
    }

    public void setNumberOfAccountsSentRequest() {
        this.numberOfAccountsSentRequest = accountsRequestedToFollow.size();
    }

    public long getNumberOfTweets() {
        return numberOfTweets;
    }

    public void setNumberOfTweets(long numberOfTweets) {
        this.numberOfTweets = numberOfTweets;
    }

    public void setNumberOfTweets() {
        this.numberOfTweets = tweets.size();
    }

    public LinkedList<Long> getFollowers() {
        return followers;
    }

    public void setFollowers(LinkedList<Long> followers) {
        this.followers = followers;
    }

    public LinkedList<Long> getFollowings() {
        return followings;
    }

    public void setFollowings(LinkedList<Long> followings) {
        this.followings = followings;
    }

    public LinkedList<Long> getBlacklist() {
        return blacklist;
    }

    public void setBlacklist(LinkedList<Long> blacklist) {
        this.blacklist = blacklist;
    }

    public LinkedList<Long> getMutedAccounts() {
        return mutedAccounts;
    }

    public void setMutedAccounts(LinkedList<Long> mutedAccounts) {
        this.mutedAccounts = mutedAccounts;
    }

    public LinkedList<Long> getTweets() {
        return tweets;
    }

    public void setTweets(LinkedList<Long> tweets) {
        this.tweets = tweets;
    }

    public LinkedList<Long> getReplied() {
        return replied;
    }

    public void setReplied(LinkedList<Long> replied) {
        this.replied = replied;
    }

    public LinkedList<Long> getSavedTweet() {
        return savedTweet;
    }

    public void setSavedTweet(LinkedList<Long> savedTweet) {
        this.savedTweet = savedTweet;
    }

    public LinkedList<Long> getLikedTweet() {
        return likedTweet;
    }

    public void setLikedTweet(LinkedList<Long> likedTweet) {
        this.likedTweet = likedTweet;
    }

    public LinkedList<Long> getFollowRequest() {
        return followRequest;
    }

    public void setFollowRequest(LinkedList<Long> followRequest) {
        this.followRequest = followRequest;
    }

    public LinkedList<Long> getAccountsRequestedToFollow() {
        return accountsRequestedToFollow;
    }

    public void setAccountsRequestedToFollow(LinkedList<Long> accountsRequestedToFollow) {
        this.accountsRequestedToFollow = accountsRequestedToFollow;
    }

    public void addTweet(long id) {
        tweets.add(id);
    }

    public void addFollower(long id){
        followers.add(id);
    }

    public void addFollowing(long id){
        followings.add(id);
    }

    public void addBlackList(long id){
        blacklist.add(id);
    }

    public void addMutedAccount(long id){
        mutedAccounts.add(id);
    }

    public void addReplied(long id){
        replied.add(id);
    }

    public void addSavedTweet(long id){
        savedTweet.add(id);
    }

    public void addLikedTweet(long id){
        likedTweet.add(id);
    }

    public void addFollowRequest(long id){
        followRequest.add(id);
    }

    public void addAccountRequestedToFollow(long id){
        accountsRequestedToFollow.add(id);
    }
}
