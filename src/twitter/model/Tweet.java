package twitter.model;

import java.util.LinkedList;
import java.util.List;

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
    private List<Long> replies = new LinkedList<>();
    private List<Record> accountRetweeted = new LinkedList<>();
    private List<Record> accountLiked = new LinkedList<>();
    private List<Long> idAccountLiked = new LinkedList<>();
    private List<Long> idAccountRetweeted = new LinkedList<>();
    private List<Long> idAccountSaved = new LinkedList<>();
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

    public long getAccountId() {
        return accountId;
    }

    public long getIdRepliedTweet() {
        return idRepliedTweet;
    }

    public List<Record> getAccountRetweeted() {
        return accountRetweeted;
    }

    public List<Long> getReplies() {
        return replies;
    }

    public List<Record> getAccountLiked() {
        return accountLiked;
    }

    public Time getTweetTime() {
        return tweetTime;
    }

    public long getIdCounter() {
        return idCounter;
    }

    public Record getRecord() {
        return record;
    }

    public List<Long> getIdAccountRetweeted() {
        return idAccountRetweeted;
    }

    public String getTextOfTweet() {
        return textOfTweet;
    }

    public List<Long> getIdAccountLiked() {
        return idAccountLiked;
    }

    public int getNumberOfLikes() {
        return numberOfLikes;
    }

    public int getNumberOfReplies() {
        return numberOfReplies;
    }

    public int getNumberOfRetweets() {
        return numberOfRetweets;
    }

    public List<Long> getIdAccountSaved() {
        return idAccountSaved;
    }

    public void addIdAccountSaved(long idAccount) {
        idAccountSaved.add(idAccount);
    }

    public void setReplies(List<Long> replies) {
        this.replies = replies;
    }

    public void setAccountRetweeted(List<Record> accountRetweeted) {
        this.accountRetweeted = accountRetweeted;
    }

    public void setAccountLiked(List<Record> accountLiked) {
        this.accountLiked = accountLiked;
    }

    public void setIdAccountLiked(List<Long> idAccountLiked) {
        this.idAccountLiked = idAccountLiked;
    }

    public void setIdAccountRetweeted(List<Long> idAccountRetweeted) {
        this.idAccountRetweeted = idAccountRetweeted;
    }

    public void setIdAccountSaved(List<Long> idAccountSaved) {
        this.idAccountSaved = idAccountSaved;
    }

    public void addIdAccountLiked(long idAccount) {
        idAccountLiked.add(idAccount);
    }

    public void addIdAccountRetweeted(long idAccount) {
        idAccountRetweeted.add(idAccount);
    }

    public void setRecord(Record record) {
        this.record = record;
    }

    public void setTweetTime(Time tweetTime) {
        this.tweetTime = tweetTime;
    }

    public void setId(long id) {
        this.id = id;
    }

    public static void setIdCounter(long idCounter) {
        Tweet.idCounter = idCounter;
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
