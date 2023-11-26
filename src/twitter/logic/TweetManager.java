package twitter.logic;

import twitter.model.Account;
import twitter.model.Record;
import twitter.model.Time;
import twitter.model.Tweet;
import twitter.repository.AccountRepository;
import twitter.repository.SecureAccountRepository;
import twitter.repository.TweetHashMapRepository;
import twitter.repository.TweetRepository;

import java.io.IOException;

public class TweetManager {

    TweetRepository tweetRepository = TweetHashMapRepository.getInstance();
    AccountRepository accountRepository = SecureAccountRepository.getInstance();

    public void writeTweet(Tweet tweet) throws IOException {
        tweet.setTweetTime(Time.now());
        tweet.setRecord(new Record(tweet.getAccountId(), tweet.getTweetTime(), Record.DEFAULT));
        tweetRepository.add(tweet);
        if (tweet.getIdRepliedTweet() == Tweet.DEFAULT_ID) {
            accountRepository.getAccount(tweet.getAccountId()).setTweets(tweet.getId());
            accountRepository.getAccount(tweet.getAccountId()).setNumberOfTweets(1);
        } else {
            Tweet repliedTweet = tweetRepository.getTweet(tweet.getIdRepliedTweet());
            repliedTweet.setNumberOfReplies(1);
            tweet.setReplies(tweet.getId());
            accountRepository.getAccount(tweet.getAccountId()).setReplied(tweet.getId());
            tweetRepository.update(repliedTweet);
        }
        Account acc = accountRepository.getAccount(tweet.getAccountId());
        accountRepository.update(acc);
        tweetRepository.update(tweet);
    }

    public void deleteTweet(Tweet tweet) throws IOException {
        tweetRepository.removeTweet(tweet.getId());
        accountRepository.getAccount(tweet.getAccountId()).getTweets().remove(tweet.getId());
        accountRepository.getAccount(tweet.getAccountId()).setNumberOfTweets(-1);
        for (Record idAccount : tweet.getAccountRetweeted()) {
            long idRetweeted = idAccount.getAccountId();
            Account acc = accountRepository.getAccount(idRetweeted);
            acc.getTweets().remove(idRetweeted);
            acc.setNumberOfTweets(-1);
            accountRepository.update(acc);
        }
        for (long idLiked: tweet.getIdAccountLiked()) {
            accountRepository.getAccount(idLiked).getLikedTweet().remove(tweet.getId());
            accountRepository.update(accountRepository.getAccount(idLiked));
        }
        for (long idSaved: tweet.getIdAccountSaved()) {
            accountRepository.getAccount(idSaved).getSavedTweet().remove(tweet.getId());
            accountRepository.update(accountRepository.getAccount(idSaved));
        }
        tweet.getReplies().clear();
        tweet.clearLikes();
        tweet.clearReplies();
        tweet.clearRetweet();
    }

    public Tweet getTweet(long id) {
        return tweetRepository.getTweet(id);
    }
}
