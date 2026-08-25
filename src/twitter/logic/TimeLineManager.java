package twitter.logic;

import twitter.model.Record;
import twitter.model.Tweet;
import twitter.repository.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TimeLineManager {
    private final TweetRepository tweetRepository = TweetFileRepository.getInstance();
    private final AccountRepository accountRepository = AccountFileRepository.getInstance();

    public Map<Record, Tweet> makeTimeLine(AccountManager accountManager) throws IOException {

        Map<Record, Tweet> timeLine = new HashMap<>();

        //User's tweets
        for (long idTweet : accountManager.getUser().getTweets()) {
            Tweet tweet = tweetRepository.getTweet(idTweet);
            if (!accountManager.canViewTweetsFrom(tweet.getAccountId())) {
                continue;
            }
            if (tweet.getAccountId() != accountManager.getUser().getId()) {
                for (Record rec : tweet.getAccountRetweeted()) {
                    if (rec.getAccountId() == accountManager.getUser().getId()) {
                        timeLine.put(rec, tweet);
                    }
                }
            } else {
                Record rec = tweet.getRecord();
                timeLine.put(rec, tweet);
            }
        }
        //Followings' tweets and retweets
        for (long idAcc : accountManager.getUser().getFollowings()) {
            String username = accountRepository.getAccount(idAcc).getUserName();
            if (!accountManager.isMute(username)) {
                for (long idTweet : accountRepository.getAccount(idAcc).getTweets()) {
                    Tweet tweet = tweetRepository.getTweet(idTweet);
                    if (!accountManager.canViewTweetsFrom(tweet.getAccountId())) {
                        continue;
                    }
                    if (tweet.getAccountId() != idAcc) {
                        for (Record rec : tweet.getAccountRetweeted()) {
                            if (rec.getAccountId() == idAcc) {
                                timeLine.put(rec, tweet);
                            }
                        }
                    } else {
                        Record rec = tweet.getRecord();
                        timeLine.put(rec, tweet);
                    }
                }
            }

        }
        //Followings' liked tweets
        for (long idAcc : accountManager.getUser().getFollowings()) {
            String username = accountRepository.getAccount(idAcc).getUserName();
            if (!accountManager.isMute(username)) {
                for (long idTweet : accountRepository.getAccount(idAcc).getLikedTweet()) {
                    Tweet tweet = tweetRepository.getTweet(idTweet);
                    if (!accountManager.canViewTweetsFrom(tweet.getAccountId())) {
                        continue;
                    }
                    if (!tweet.getIdAccountRetweeted().contains(idAcc)) {
                        for (Record rec : tweet.getAccountLiked()) {
                            if (rec.getAccountId() == idAcc) {
                                timeLine.put(rec, tweet);
                            }
                        }
                    }
                }
            }

        }
        List<Map.Entry<Record, Tweet>> entries = new ArrayList<>(timeLine.entrySet());
        entries.sort((a, b) -> -a.getKey().compareTo(b.getKey()));
        Map<Record, Tweet> sorted = new LinkedHashMap<>();
        for (Map.Entry<Record, Tweet> entry : entries) {
            sorted.put(entry.getKey(), entry.getValue());
        }
        return sorted;
    }

}
