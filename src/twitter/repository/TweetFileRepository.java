package twitter.repository;

import org.json.simple.JSONObject;
import twitter.model.Tweet;

import java.io.*;

public class TweetFileRepository implements TweetRepository{

    private final String path = "src/resources/data/tweet/";
    private JSONObject jsonObject = new JSONObject();
    @Override
    public Tweet update(Tweet tweet) throws IOException {
        if (!exists(tweet.getId())) {
            add(tweet);
        } else {
            FileWriter fileWriter = new FileWriter(path + tweet.getId() + ".txt", false);
            PrintWriter printWriter = new PrintWriter(fileWriter, false);
            writeInJson(tweet);
            printWriter.println(jsonObject);
            printWriter.flush();
            printWriter.close();
            fileWriter.close();
            jsonObject.clear();

            return tweet;
        }
        return null;
    }

    @Override
    public Tweet add(Tweet tweet) throws IOException {
        if (exists(tweet.getId())) {
            update(tweet);
        } else {
            PrintWriter writer = new PrintWriter(path + tweet.getId() + ".txt", "UTF-8");
            writeInJson(tweet);
            writer.print(jsonObject);
            jsonObject.clear();
            writer.close();
        }
        return tweet;
    }

    @Override
    public Tweet getTweet(long id) {
        return null;
    }

    @Override
    public boolean removeTweet(long id) {
        if (exists(id)) {
            File tweet = new File(path + id + ".txt");
            return tweet.delete();
        }
        return false;
    }

    @Override
    public boolean exists(long id) {
        File tweet = new File(path + id + ".txt");
        return tweet.exists() && !tweet.isDirectory();
    }

    private void writeInJson(Tweet tweet){

        jsonObject.put("id",tweet.getId());
        jsonObject.put("account id", tweet.getAccountId());
        jsonObject.put("text",tweet.getTextOfTweet());
        jsonObject.put("like num", tweet.getNumberOfLikes());
        jsonObject.put("retweet num", tweet.getNumberOfRetweets());
        jsonObject.put("reply num", tweet.getNumberOfReplies());
        jsonObject.put("id replied tweet",tweet.getIdRepliedTweet());



    }
}
