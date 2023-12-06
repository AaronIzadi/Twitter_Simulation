package twitter.logic;

import twitter.model.Account;
import twitter.model.Record;
import twitter.model.Time;
import twitter.model.Tweet;
import twitter.repository.*;

import java.io.IOException;

public class TweetManager {

    TweetRepository tweetRepository = TweetFileRepository.getInstance();
    AccountRepository accountRepository = AccountFileRepository.getInstance();

    public void writeTweet(Tweet tweet) throws IOException {
        Time time = Time.now();
        tweet.setTweetTime(time);
        tweet.setRecord(new Record(tweet.getAccountId(), time, Record.DEFAULT));
        tweetRepository.add(tweet);
        Account account = accountRepository.getUser();
        if (tweet.getIdRepliedTweet() == Tweet.DEFAULT_ID) {
            account.addTweet(tweet.getId());
            account.setNumberOfTweets();
        } else {
            Tweet repliedTweet = tweetRepository.getTweet(tweet.getIdRepliedTweet());
            repliedTweet.setNumberOfReplies();
            tweet.addReply(tweet.getId());
            account.addReplied(tweet.getId());
            tweetRepository.update(repliedTweet);
        }
        accountRepository.update(account);
        tweetRepository.update(tweet);
    }

    public void deleteTweet(Tweet tweet) throws IOException {
        tweetRepository.removeTweet(tweet.getId());
        Account account = accountRepository.getUser();
        account.getTweets().remove(tweet.getId());
        account.setNumberOfTweets();
        for (Record idAccount : tweet.getAccountRetweeted()) {
            long idRetweeted = idAccount.getAccountId();
            Account acc = accountRepository.getAccount(idRetweeted);
            acc.getTweets().remove(idRetweeted);
            acc.setNumberOfTweets();
            accountRepository.update(acc);
        }
        for (long idLiked : tweet.getIdAccountLiked()) {
            Account acc = accountRepository.getAccount(idLiked);
            acc.getLikedTweet().remove(tweet.getId());
            accountRepository.update(acc);
        }
        for (long idSaved : tweet.getIdAccountSaved()) {
            Account acc = accountRepository.getAccount(idSaved);
            acc.getSavedTweet().remove(tweet.getId());
            accountRepository.update(acc);
        }
        tweet.getReplies().clear();
        tweet.clearLikes();
        tweet.clearReplies();
        tweet.clearRetweet();
    }

    public Tweet getTweet(long id) throws IOException {
        return tweetRepository.getTweet(id);
    }
}
