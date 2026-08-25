package twitter.logic;

import twitter.model.Account;
import twitter.model.Record;
import twitter.model.Time;
import twitter.model.Tweet;
import twitter.repository.*;

import java.io.IOException;

public class TweetManager {

    private final TweetRepository tweetRepository = TweetFileRepository.getInstance();
    private final AccountRepository accountRepository = AccountFileRepository.getInstance();

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
            repliedTweet.addReply(tweet.getId());
            repliedTweet.setNumberOfReplies();
            account.addReplied(tweet.getId());
            tweetRepository.update(repliedTweet);
        }
        accountRepository.update(account);
        tweetRepository.update(tweet);
    }

    public void deleteTweet(Tweet tweet) throws IOException {
        if (tweet.getIdRepliedTweet() != Tweet.DEFAULT_ID) {
            Tweet parentTweet = tweetRepository.getTweet(tweet.getIdRepliedTweet());
            if (parentTweet != null) {
                parentTweet.getReplies().remove(tweet.getId());
                parentTweet.setNumberOfReplies();
                tweetRepository.update(parentTweet);
            }
        }

        tweetRepository.removeTweet(tweet.getId());
        Account account = accountRepository.getUser();
        account.getTweets().remove(tweet.getId());
        account.getReplied().remove(tweet.getId());
        account.setNumberOfTweets();
        accountRepository.update(account);
        for (Record retweetRecord : tweet.getAccountRetweeted()) {
            long retweeterId = retweetRecord.getAccountId();
            Account acc = accountRepository.getAccount(retweeterId);
            acc.getTweets().remove(tweet.getId());
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
