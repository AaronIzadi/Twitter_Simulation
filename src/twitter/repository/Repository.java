package twitter.repository;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import twitter.model.Account;
import twitter.model.Record;
import twitter.model.Time;
import twitter.model.Tweet;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;


public class Repository {

    private static final Repository instance = new Repository();
    private JSONObject jsonObject = new JSONObject();
    public static final int accountType = 0;
    public static final int tweetType = 1;


    public static Repository getInstance() {
        return instance;
    }
    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public Object update(Object object, long id, String path) throws IOException {
        if (!exists(id, path)) {
            add(object, id, path);
        } else {
            FileWriter fileWriter = new FileWriter(path + id + ".txt", false);
            PrintWriter printWriter = new PrintWriter(fileWriter, false);
            writeInJson(object);
            printWriter.println(jsonObject);
            printWriter.flush();
            printWriter.close();
            fileWriter.close();
            jsonObject.clear();

            return object;
        }
        return null;
    }

    public Object add(Object object, long id, String path) throws IOException {
        if (exists(id, path)) {
            update(object, id, path);
        } else {
            PrintWriter writer = new PrintWriter(path + id + ".txt", "UTF-8");
            writeInJson(object);
            writer.print(jsonObject);
            jsonObject.clear();
            writer.close();
        }
        return object;
    }

    public void addAppInfo(Object object, long idCounter) throws FileNotFoundException, UnsupportedEncodingException {

        String path;

        if (object instanceof Account) {
            path = "src/resources/app info/account.txt";
        } else {
            path = "src/resources/app info/tweet.txt";
        }

        PrintWriter writer = new PrintWriter(path, "UTF-8");
        writer.print(idCounter);
        writer.close();
    }

    public void getIdCounter() throws IOException {

        File account = new File("src/resources/app info/account.txt");
        File tweet = new File("src/resources/app info/tweet.txt");

        BufferedReader bufferedReader = new BufferedReader(new FileReader(account));
        String string;
        long idCounterAccount = 0;
        while ((string = bufferedReader.readLine()) != null) {
            idCounterAccount = Long.parseLong(string);
        }
        Account.setIdCounter(idCounterAccount);

        bufferedReader = new BufferedReader(new FileReader(tweet));
        long idCounterTweet = 0;
        while ((string = bufferedReader.readLine()) != null) {
            idCounterTweet = Long.parseLong(string);
        }
        Tweet.setIdCounter(idCounterTweet);

    }

    public Object getObject(long id, String path, int idObject) throws IOException {
        File folder = new File(path.substring(0, path.length() - 1));
        File[] listOfFiles = folder.listFiles();

        assert listOfFiles != null;
        for (File file : listOfFiles) {
            if (file.isFile()) {
                if (file.getName().equals(id + ".txt")) {
                    Object object = readByJson(id, path, idObject);
                    clearJsonData();
                    return object;
                }
            }
        }
        return null;
    }

    public boolean exists(long id, String path) {
        File account = new File(path + id + ".txt");
        return account.exists() && !account.isDirectory();
    }

    public void writeInJson(Object object) {
        if (object instanceof Account) {

            Account account = (Account) object;

            jsonObject.put("id", account.getId());
            jsonObject.put("id counter", account.getIdCounter());
            jsonObject.put("username", account.getUserName());
            jsonObject.put("password", account.getPassword());
            jsonObject.put("name", account.getName());
            jsonObject.put("email", account.getEmailAddress());
            jsonObject.put("birthday", account.getDateOfBirth());
            jsonObject.put("biography", account.getBiography());
            jsonObject.put("status", account.getStatus());
            jsonObject.put("phone number", account.getPhoneNumber());
            jsonObject.put("type", account.getType());
            jsonObject.put("follower num", account.getNumberOfFollowers());
            jsonObject.put("following num", account.getNumberOfFollowings());
            jsonObject.put("blacklist num", account.getNumberOfBlackList());
            jsonObject.put("muted num", account.getMutedAccounts().size());
            jsonObject.put("tweet num", account.getNumberOfTweets());
            jsonObject.put("replied num", account.getReplied().size());
            jsonObject.put("saved num", account.getNumberOfFollowers());
            jsonObject.put("liked num", account.getLikedTweet().size());
            jsonObject.put("request num", account.getNumberOfFollowRequest());
            jsonObject.put("request sent num", account.getNumberOfAccountsSentRequest());

            for (int i = 0; i < account.getNumberOfFollowers(); i++) {
                jsonObject.put("follower " + i, account.getFollowers().get(i));
            }
            for (int i = 0; i < account.getNumberOfFollowings(); i++) {
                jsonObject.put("following " + i, account.getFollowings().get(i));
            }
            for (int i = 0; i < account.getNumberOfBlackList(); i++) {
                jsonObject.put("blacklist " + i, account.getBlacklist().get(i));
            }
            for (int i = 0; i < account.getMutedAccounts().size(); i++) {
                jsonObject.put("muted " + i, account.getMutedAccounts().get(i));
            }
            for (int i = 0; i < account.getNumberOfTweets(); i++) {
                jsonObject.put("tweet " + i, account.getTweets().get(i));
            }
            for (int i = 0; i < account.getReplied().size(); i++) {
                jsonObject.put("replied " + i, account.getReplied().get(i));
            }
            for (int i = 0; i < account.getSavedTweet().size(); i++) {
                jsonObject.put("saved tweet " + i, account.getSavedTweet().get(i));
            }
            for (int i = 0; i < account.getLikedTweet().size(); i++) {
                jsonObject.put("liked tweet " + i, account.getLikedTweet().get(i));
            }
            for (int i = 0; i < account.getNumberOfFollowRequest(); i++) {
                jsonObject.put("follow request " + i, account.getFollowRequest().get(i));
            }
            for (int i = 0; i < account.getNumberOfAccountsSentRequest(); i++) {
                jsonObject.put("follow request sent " + i, account.getAccountsRequestedToFollow().get(i));
            }
        } else if (object instanceof Tweet) {

            Tweet tweet = (Tweet) object;
            jsonObject.put("id", tweet.getId());
            jsonObject.put("id counter", tweet.getIdCounter());
            jsonObject.put("account id", tweet.getAccountId());
            jsonObject.put("text", tweet.getTextOfTweet());
            jsonObject.put("like num", tweet.getNumberOfLikes());
            jsonObject.put("retweet num", tweet.getNumberOfRetweets());
            jsonObject.put("reply num", tweet.getNumberOfReplies());
            jsonObject.put("id replied tweet", tweet.getIdRepliedTweet());
            jsonObject.put("time", tweet.getTweetTime().toString());
            jsonObject.put("record", tweet.getRecord().toString());
            jsonObject.put("saved num", tweet.getIdAccountSaved().size());
            for (int i = 0; i < tweet.getNumberOfReplies(); i++) {
                jsonObject.put("reply id " + i, tweet.getReplies().get(i));
            }
            for (int i = 0; i < tweet.getNumberOfRetweets(); i++) {
                jsonObject.put("account retweeted record " + i, tweet.getAccountRetweeted().get(i).toString());
            }
            for (int i = 0; i < tweet.getAccountLiked().size(); i++) {
                jsonObject.put("account liked record " + i, tweet.getAccountLiked().get(i).toString());
            }
            for (int i = 0; i < tweet.getIdAccountLiked().size(); i++) {
                jsonObject.put("account liked " + i, tweet.getIdAccountLiked().get(i));
            }
            for (int i = 0; i < tweet.getIdAccountRetweeted().size(); i++) {
                jsonObject.put("account retweeted " + i, tweet.getIdAccountRetweeted().get(i));
            }
            for (int i = 0; i < tweet.getIdAccountSaved().size(); i++) {
                jsonObject.put("account saved " + i, tweet.getIdAccountSaved().get(i));
            }
        } else {
            System.out.println("Oop! Problem at reading file!");
        }
    }

    public void jsonToReadFile(long id, String path) throws IOException {
        String address = path + id + ".txt";

        Charset encoding = Charset.defaultCharset();
        List<String> lines = Files.readAllLines(Paths.get(address), encoding);

        JSONParser parser = new JSONParser();
        String string = String.join("\n", lines);
        try {
            jsonObject = (JSONObject) parser.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public Object readByJson(long id, String path, int idObject) throws IOException {

        jsonToReadFile(id, path);

        if (idObject == accountType) {

            String username = (String) jsonObject.get("username");
            String password = (String) jsonObject.get("password");
            String name = (String) jsonObject.get("name");
            String email = (String) jsonObject.get("email");
            String birthday = (String) jsonObject.get("birthday");
            String bio = (String) jsonObject.get("biography");
            String status = (String) jsonObject.get("status");
            Long idCounter = (Long) jsonObject.get("id counter");
            Long phoneNumber = (Long) jsonObject.get("phone number");
            Long typeAsLong = (Long) jsonObject.get("type");
            int type = typeAsLong.intValue();

            LinkedList<Long> follower = new LinkedList<>();
            for (int i = 0; i < (Long) jsonObject.get("follower num"); i++) {
                follower.add((Long) jsonObject.get("follower " + i));
            }
            LinkedList<Long> following = new LinkedList<>();
            for (int i = 0; i < (Long) jsonObject.get("following num"); i++) {
                following.add((Long) jsonObject.get("following " + i));
            }
            LinkedList<Long> blacklist = new LinkedList<>();
            for (int i = 0; i < (Long) jsonObject.get("blacklist num"); i++) {
                blacklist.add((Long) jsonObject.get("blacklist " + i));
            }
            LinkedList<Long> muted = new LinkedList<>();
            for (int i = 0; i < (Long) jsonObject.get("muted num"); i++) {
                muted.add((Long) jsonObject.get("muted " + i));
            }
            LinkedList<Long> tweet = new LinkedList<>();
            for (int i = 0; i < (Long) jsonObject.get("tweet num"); i++) {
                tweet.add((Long) jsonObject.get("tweet " + i));
            }
            LinkedList<Long> replied = new LinkedList<>();
            for (int i = 0; i < (Long) jsonObject.get("replied num"); i++) {
                replied.add((Long) jsonObject.get("replied " + i));
            }
            LinkedList<Long> saved = new LinkedList<>();
            for (int i = 0; i < (Long) jsonObject.get("saved num"); i++) {
                saved.add((Long) jsonObject.get("saved tweet " + i));
            }
            LinkedList<Long> liked = new LinkedList<>();
            for (int i = 0; i < (Long) jsonObject.get("liked num"); i++) {
                liked.add((Long) jsonObject.get("liked tweet " + i));
            }
            LinkedList<Long> followRequest = new LinkedList<>();
            for (int i = 0; i < (Long) jsonObject.get("request num"); i++) {
                followRequest.add((Long) jsonObject.get("follow request " + i));
            }
            LinkedList<Long> followRequestSent = new LinkedList<>();
            for (int i = 0; i < (Long) jsonObject.get("request sent num"); i++) {
                followRequestSent.add((Long) jsonObject.get("follow request sent " + i));
            }

            Account account;
            switch (status) {
                case "Offline":
                    account = new Account(username, password, type, 0);
                    break;
                case "Online":
                    account = new Account(username, password, type, 1);
                    break;
                default:
                    account = new Account(username, password, type, 2);
                    break;
            }
            account.setId(id);
            account.setIdCounter(idCounter);
            account.setName(name);
            account.setEmailAddress(email);
            account.setBiography(bio);
            account.setDateOfBirth(birthday);
            account.setPhoneNumber(phoneNumber);
            account.setFollowers(follower);
            account.setFollowings(following);
            account.setBlacklist(blacklist);
            account.setMutedAccounts(muted);
            account.setFollowRequest(followRequest);
            account.setTweets(tweet);
            account.setLikedTweet(liked);
            account.setReplied(replied);
            account.setSavedTweet(saved);
            account.setFollowRequest(followRequest);
            account.setAccountsRequestedToFollow(followRequestSent);

            return account;
        } else if (idObject == tweetType) {

            jsonToReadFile(id, path);

            Long tweetId = (Long) jsonObject.get("id");
            Long idCounter = (Long) jsonObject.get("id counter");
            Long accountId = (Long) jsonObject.get("account id");
            String text = (String) jsonObject.get("text");
            Long likeAsLong = (Long) jsonObject.get("like num");
            int like = likeAsLong.intValue();
            Long retweetAsLong = (Long) jsonObject.get("retweet num");
            int retweet = retweetAsLong.intValue();
            Long replyAsLong = (Long) jsonObject.get("reply num");
            int replyId = replyAsLong.intValue();
            Time time = Time.valueOf((String) jsonObject.get("time"));
            Record record = Record.valueOf((String) jsonObject.get("record"));
            Long savedAsLong = (Long) jsonObject.get("saved num");
            int saved = savedAsLong.intValue();
            LinkedList<Record> accountsRetweeted = new LinkedList<>();
            for (int i = 0; i < retweet; i++) {
                accountsRetweeted.add(Record.valueOf((String) jsonObject.get("account retweeted record " + i)));
            }
            LinkedList<Record> accountsLiked = new LinkedList<>();
            for (int i = 0; i < like; i++) {
                accountsLiked.add(Record.valueOf((String) jsonObject.get("account liked record " + i)));
            }
            LinkedList<Long> idAccountsLiked = new LinkedList<>();
            for (int i = 0; i < like; i++) {
                idAccountsLiked.add((Long) jsonObject.get("account liked " + i));
            }
            LinkedList<Long> idAccountsRetweeted = new LinkedList<>();
            for (int i = 0; i < like; i++) {
                idAccountsRetweeted.add((Long) jsonObject.get("account retweeted " + i));
            }
            LinkedList<Long> idAccountSaved = new LinkedList<>();
            for (int i = 0; i < saved; i++) {
                idAccountSaved.add((Long) jsonObject.get("account saved " + i));
            }
            LinkedList<Long> idReplies = new LinkedList<>();
            for (int i = 0; i < saved; i++) {
                idReplies.add((Long) jsonObject.get("reply id " + i));
            }

            Tweet tweet = new Tweet(accountId, replyId, text);
            tweet.setId(tweetId);
            tweet.setIdCounter(idCounter);
            tweet.setTweetTime(time);
            tweet.setRecord(record);
            tweet.setReplies(idReplies);
            tweet.setAccountLiked(accountsLiked);
            tweet.setAccountRetweeted(accountsRetweeted);
            tweet.setIdAccountLiked(idAccountsLiked);
            tweet.setIdAccountRetweeted(idAccountsRetweeted);
            tweet.setIdAccountSaved(idAccountSaved);
            tweet.setNumberOfLikes();
            tweet.setNumberOfRetweets();
            tweet.setNumberOfReplies();

            return tweet;
        } else {
            System.out.println("Oop! Object not found!");
            return null;
        }
    }

    public boolean removeObject(long id, String path) {
        if (exists(id, path)) {
            File file = new File(path + id + ".txt");
            return file.delete();
        }
        return false;
    }

    public void clearJsonData() {
        jsonObject.clear();
    }
}
