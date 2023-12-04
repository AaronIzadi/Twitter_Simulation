package twitter.logic;

import twitter.model.Record;
import twitter.model.Tweet;
import twitter.repository.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TimeLineManager {
    TweetRepository tweetRepository = TweetFileRepository.getInstance();
    AccountRepository accountRepository = AccountFileRepository.getInstance();
    AccountManager accountManager = new AccountManager();


    public Map<Record, Tweet> makeTimeLine() throws IOException {

        Map<Record, Tweet> map = new HashMap<>();

        //User's tweets
        for (long idTweet : accountManager.getUser().getTweets()) {
            if (tweetRepository.getTweet(idTweet).getAccountId() != accountManager.getUser().getId()) {
                for (Record rec : tweetRepository.getTweet(idTweet).getAccountRetweeted()) {
                    if (rec.getAccountId() == accountManager.getUser().getId()) {
                        Tweet tweet = tweetRepository.getTweet(idTweet);
                        map.put(rec, tweet);
                    }
                }
            } else {
                Record rec = tweetRepository.getTweet(idTweet).getRecord();
                Tweet tweet = tweetRepository.getTweet(idTweet);
                map.put(rec, tweet);
            }
        }
        //Followings' tweets and retweets
        for (long idAcc : accountManager.getUser().getFollowings()) {
            String username = accountRepository.getAccount(idAcc).getUserName();
            if (!accountManager.isMute(username)) {
                for (long idTweet : accountRepository.getAccount(idAcc).getTweets()) {
                    if (tweetRepository.getTweet(idTweet).getAccountId() != idAcc) {
                        for (Record rec : tweetRepository.getTweet(idTweet).getAccountRetweeted()) {
                            if (rec.getAccountId() == idAcc) {
                                Tweet tweet = tweetRepository.getTweet(idTweet);
                                map.put(rec, tweet);
                            }
                        }
                    } else {
                        Record rec = tweetRepository.getTweet(idTweet).getRecord();
                        Tweet tweet = tweetRepository.getTweet(idTweet);
                        map.put(rec, tweet);
                    }
                }
            }

        }
        //Followings' liked tweets
        for (long idAcc : accountManager.getUser().getFollowings()) {
            String username = accountRepository.getAccount(idAcc).getUserName();
            if (!accountManager.isMute(username)) {
                for (long idTweet : accountRepository.getAccount(idAcc).getLikedTweet()) {
                    if (!tweetRepository.getTweet(idTweet).getIdAccountRetweeted().contains(idAcc)) {
                        for (Record rec : tweetRepository.getTweet(idTweet).getAccountLiked()) {
                            if (rec.getAccountId() == idAcc) {
                                Tweet tweet = tweetRepository.getTweet(idTweet);
                                map.put(rec, tweet);
                            }
                        }
                    }
                }
            }

        }
        return map;
    }

}
