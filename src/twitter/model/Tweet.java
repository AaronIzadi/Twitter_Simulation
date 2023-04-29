package twitter.model;

import java.util.LinkedList;
import java.util.List;


public class Tweet {
    private long id;
    private long accountId;
    private String textOfTweet;
    private int numberOfLikes;
    private int numberOfRetweets;
    private int numberOfReplies;
    public static final int DEFAULT_ID = 0;
    private long idRepliedTweet;
    private List<Long> replies = new LinkedList<>();
    private List<Record> accountRetweeted = new LinkedList<>();
    private List<Record> accountLiked = new LinkedList<>();
    private List<Long> idAccountLiked = new LinkedList<>();
    private List<Long> idAccountRetweeted = new LinkedList<>();
    private List<Long> idAccountSaved = new LinkedList<>();
    private Time tweetTime;
    private Record record;


    public Tweet(long accountId, long idRepliedTweet, String textOfTweet){
        this.accountId = accountId;
        this.idRepliedTweet = idRepliedTweet;
        this.textOfTweet = textOfTweet;
    }

    public long getId() { return id; }

    public long getAccountId() { return accountId; }

    public long getIdRepliedTweet() { return idRepliedTweet; }

    public List<Record> getAccountRetweeted() { return accountRetweeted; }

    public List<Long> getReplies() { return replies; }

    public List<Record> getAccountLiked() { return accountLiked; }

    public Time getTweetTime() { return tweetTime; }

    public Record getRecord() { return record; }

    public List<Long> getIdAccountRetweeted() { return idAccountRetweeted; }

    public String getTextOfTweet() { return textOfTweet; }

    public List<Long> getIdAccountLiked() { return idAccountLiked; }

    public int getNumberOfLikes() { return numberOfLikes; }

    public int getNumberOfReplies() { return numberOfReplies; }

    public int getNumberOfRetweets() { return numberOfRetweets; }

    public List<Long> getIdAccountSaved() { return idAccountSaved; }

    public void setIdAccountSaved(long idAccount) { idAccountSaved.add(idAccount); }

    public void setIdAccountLiked(long idAccount) { idAccountLiked.add(idAccount); }

    public void setIdAccountRetweeted(long idAccount) { idAccountRetweeted.add(idAccount); }

    public void setRecord(Record record) { this.record = record; }

    public void setTweetTime(Time tweetTime) { this.tweetTime = tweetTime; }

    public void setId(long id) { this.id = id; }

    public void setReplies(Long reply) { replies.add(reply); }

    public void setAccountRetweeted(Record idNum) { accountRetweeted.add(idNum); }

    public void setAccountLiked(Record idNum) { accountLiked.add(idNum); }

    public void setNumberOfLikes(int n) { numberOfLikes += n; }

    public void setNumberOfRetweets(int n) { numberOfRetweets += n; }

    public void setNumberOfReplies(int n) { numberOfReplies += n; }

    public void clearLikes() { numberOfLikes = 0; }

    public void clearRetweet() { numberOfRetweets = 0; }

    public void clearReplies() { numberOfReplies = 0; }

}
