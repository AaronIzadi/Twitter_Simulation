package twitter.logic;

import twitter.model.Record;
import twitter.model.Tweet;
import twitter.repository.AccountRepository;
import twitter.repository.SecureAccountRepository;
import twitter.repository.TweetHashMapRepository;
import twitter.repository.TweetRepository;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class HandleTimeLine {
    TweetRepository tweetRep = TweetHashMapRepository.getInstance();
    AccountRepository accountRep = SecureAccountRepository.getInstance();
    HandleAccount handleAccount = new HandleAccount();


    public Map<Record, Tweet> makeTimeLine() {

        Map<Record, Tweet> map = new HashMap<>();

        //User's tweets
        for (long idTweet : handleAccount.getUser().getTweets()) {
            if (tweetRep.getTweet(idTweet).getAccountId() != handleAccount.getUser().getId()) {
                for (Record rec : tweetRep.getTweet(idTweet).getAccountRetweeted()) {
                    if (rec.getAccountId() == handleAccount.getUser().getId()) {
                        Tweet tweet = tweetRep.getTweet(idTweet);
                        map.put(rec, tweet);
                    }
                }
            } else {
                Record rec = tweetRep.getTweet(idTweet).getRecord();
                Tweet tweet = tweetRep.getTweet(idTweet);
                map.put(rec, tweet);
            }
        }
        //Followings' tweets and retweets
        for (long idAcc : handleAccount.getUser().getFollowings()) {
            String username = accountRep.getAccount(idAcc).getUserName();
            if (!handleAccount.isMute(username)) {
                for (long idTweet : accountRep.getAccount(idAcc).getTweets()) {
                    if (tweetRep.getTweet(idTweet).getAccountId() != idAcc) {
                        for (Record rec : tweetRep.getTweet(idTweet).getAccountRetweeted()) {
                            if (rec.getAccountId() == idAcc) {
                                Tweet tweet = tweetRep.getTweet(idTweet);
                                map.put(rec, tweet);
                            }
                        }
                    } else {
                        Record rec = tweetRep.getTweet(idTweet).getRecord();
                        Tweet tweet = tweetRep.getTweet(idTweet);
                        map.put(rec, tweet);
                    }
                }
            }

        }
        //Followings' liked tweets
        for (long idAcc : handleAccount.getUser().getFollowings()) {
            String username = accountRep.getAccount(idAcc).getUserName();
            if (!handleAccount.isMute(username)) {
                for (long idTweet : accountRep.getAccount(idAcc).getLikedTweet()) {
                    if (!tweetRep.getTweet(idTweet).getIdAccountRetweeted().contains(idAcc)) {
                        for (Record rec : tweetRep.getTweet(idTweet).getAccountLiked()) {
                            if (rec.getAccountId() == idAcc) {
                                Tweet tweet = tweetRep.getTweet(idTweet);
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
