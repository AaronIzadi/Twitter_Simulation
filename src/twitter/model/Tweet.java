package twitter.model;

import java.util.LinkedList;


public class Tweet {
    private long id;
    private static long idCounter = 0;
    private final long accountId;
    private final String textOfTweet;
    private int numberOfLikes;
    private int numberOfRetweets;
    private int numberOfReplies;
    public static final int DEFAULT_ID = 0;
    private final long idRepliedTweet;
    private LinkedList<Long> replies = new LinkedList<>();
    private LinkedList<Record> accountRetweeted = new LinkedList<>();
    private LinkedList<Record> accountLiked = new LinkedList<>();
    private LinkedList<Long> idAccountLiked = new LinkedList<>();
    private LinkedList<Long> idAccountRetweeted = new LinkedList<>();
    private LinkedList<Long> idAccountSaved = new LinkedList<>();
    private Time tweetTime;
    private Record record;


    public Tweet(long accountId, long idRepliedTweet, String textOfTweet) {
        this.accountId = accountId;
        this.idRepliedTweet = idRepliedTweet;
        this.textOfTweet = textOfTweet;
        this.setId(idCounter);
        idCounter++;
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
        Tweet.idCounter = idCounter;
    }

    public long getAccountId() {
        return accountId;
    }

    public String getTextOfTweet() {
        return textOfTweet;
    }

    public int getNumberOfLikes() {
        return numberOfLikes;
    }

    public void setNumberOfLikes(int numberOfLikes) {
        this.numberOfLikes = numberOfLikes;
    }

    public int getNumberOfRetweets() {
        return numberOfRetweets;
    }

    public void setNumberOfRetweets(int numberOfRetweets) {
        this.numberOfRetweets = numberOfRetweets;
    }

    public int getNumberOfReplies() {
        return numberOfReplies;
    }

    public void setNumberOfReplies(int numberOfReplies) {
        this.numberOfReplies = numberOfReplies;
    }

    public long getIdRepliedTweet() {
        return idRepliedTweet;
    }

    public LinkedList<Long> getReplies() {
        return replies;
    }

    public void setReplies(LinkedList<Long> replies) {
        this.replies = replies;
    }

    public LinkedList<Record> getAccountRetweeted() {
        return accountRetweeted;
    }

    public void setAccountRetweeted(LinkedList<Record> accountRetweeted) {
        this.accountRetweeted = accountRetweeted;
    }

    public LinkedList<Record> getAccountLiked() {
        return accountLiked;
    }

    public void setAccountLiked(LinkedList<Record> accountLiked) {
        this.accountLiked = accountLiked;
    }

    public LinkedList<Long> getIdAccountLiked() {
        return idAccountLiked;
    }

    public void setIdAccountLiked(LinkedList<Long> idAccountLiked) {
        this.idAccountLiked = idAccountLiked;
    }

    public LinkedList<Long> getIdAccountRetweeted() {
        return idAccountRetweeted;
    }

    public void setIdAccountRetweeted(LinkedList<Long> idAccountRetweeted) {
        this.idAccountRetweeted = idAccountRetweeted;
    }

    public LinkedList<Long> getIdAccountSaved() {
        return idAccountSaved;
    }

    public void setIdAccountSaved(LinkedList<Long> idAccountSaved) {
        this.idAccountSaved = idAccountSaved;
    }

    public Time getTweetTime() {
        return tweetTime;
    }

    public void setTweetTime(Time tweetTime) {
        this.tweetTime = tweetTime;
    }

    public Record getRecord() {
        return record;
    }

    public void setRecord(Record record) {
        this.record = record;
    }

    public void addIdAccountSaved(long idAccount) {
        idAccountSaved.add(idAccount);
    }

    public void addIdAccountLiked(long idAccount) {
        idAccountLiked.add(idAccount);
    }

    public void addIdAccountRetweeted(long idAccount) {
        idAccountRetweeted.add(idAccount);
    }

    public void addReply(Long reply) {
        replies.add(reply);
    }

    public void addAccountRetweeted(Record idNum) {
        accountRetweeted.add(idNum);
    }

    public void addAccountLiked(Record idNum) {
        accountLiked.add(idNum);
    }

    public void setNumberOfLikes() {
        numberOfLikes = idAccountLiked.size();
    }

    public void setNumberOfRetweets() {
        numberOfRetweets = idAccountRetweeted.size();
    }

    public void setNumberOfReplies() {
        numberOfReplies = replies.size();
    }

    public void clearLikes() {
        numberOfLikes = 0;
    }

    public void clearRetweet() {
        numberOfRetweets = 0;
    }

    public void clearReplies() {
        numberOfReplies = 0;
    }

}
