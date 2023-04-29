package twitter.logic;

import twitter.model.Account;
import twitter.model.Record;
import twitter.model.Time;
import twitter.model.Tweet;
import twitter.repository.AccountRepository;
import twitter.repository.SecureAccountRepository;
import twitter.repository.TweetHashMapRepository;
import twitter.repository.TweetRepository;

public class HandleTweet {

    TweetRepository tweetRep = TweetHashMapRepository.getInstance();
    AccountRepository accountRep = SecureAccountRepository.getInstance();

    public void makeTweet(Tweet tweet) {
        tweet.setTweetTime(Time.now());
        tweet.setRecord(new Record(tweet.getAccountId(), tweet.getTweetTime(), Record.DEFAULT));
        tweetRep.add(tweet);
        if (tweet.getIdRepliedTweet() == Tweet.DEFAULT_ID) {
            accountRep.getAccount(tweet.getAccountId()).setTweets(tweet.getId());
            accountRep.getAccount(tweet.getAccountId()).setNumberOfTweets(1);
        } else {
            Tweet repliedTweet = tweetRep.getTweet(tweet.getIdRepliedTweet());
            repliedTweet.setNumberOfReplies(1);
            tweet.setReplies(tweet.getId());
            accountRep.getAccount(tweet.getAccountId()).setReplied(tweet.getId());
            tweetRep.update(repliedTweet);
        }
        Account acc = accountRep.getAccount(tweet.getAccountId());
        accountRep.update(acc);
        tweetRep.update(tweet);
    }

    public void deleteTweet(Tweet tweet) {
        tweetRep.removeTweet(tweet.getId());
        accountRep.getAccount(tweet.getAccountId()).getTweets().remove(tweet.getId());
        accountRep.getAccount(tweet.getAccountId()).setNumberOfTweets(-1);
        for (Record idAccount : tweet.getAccountRetweeted()) {
            long idRetweeted = idAccount.getAccountId();
            Account acc = accountRep.getAccount(idRetweeted);
            acc.getTweets().remove(idRetweeted);
            acc.setNumberOfTweets(-1);
            accountRep.update(acc);
        }
        for (long idLiked: tweet.getIdAccountLiked()) {
            accountRep.getAccount(idLiked).getLikedTweet().remove(tweet.getId());
            accountRep.update(accountRep.getAccount(idLiked));
        }
        for (long idSaved: tweet.getIdAccountSaved()) {
            accountRep.getAccount(idSaved).getSavedTweet().remove(tweet.getId());
            accountRep.update(accountRep.getAccount(idSaved));
        }
        tweet.getReplies().clear();
        tweet.clearLikes();
        tweet.clearReplies();
        tweet.clearRetweet();
    }

    public Tweet getTweet(long id) {
        return tweetRep.getTweet(id);
    }
}
