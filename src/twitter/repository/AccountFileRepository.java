package twitter.repository;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import twitter.model.Account;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

public class AccountFileRepository implements AccountRepository {

    private final String path = "src/resources/data/account/";
    private JSONObject jsonObject = new JSONObject();
    private Account userAccount;
    private static final AccountFileRepository instance = new AccountFileRepository();

    public static AccountFileRepository getInstance() {
        return instance;
    }

    @Override
    public Account update(Account account) throws IOException {
        if (!exists(account.getId())) {
            add(account);
        } else {
            FileWriter fileWriter = new FileWriter(path + account.getId() + ".txt", false);
            PrintWriter printWriter = new PrintWriter(fileWriter, false);
            writeInJson(account);
            printWriter.println(jsonObject);
            printWriter.flush();
            printWriter.close();
            fileWriter.close();
            jsonObject.clear();

            return account;
        }
        return null;
    }

    @Override
    public Account add(Account account) throws IOException {
        if (exists(account.getId())) {
            update(account);
        } else {
            PrintWriter writer = new PrintWriter(path + account.getId() + ".txt", "UTF-8");
            writeInJson(account);
            writer.print(jsonObject);
            jsonObject.clear();
            writer.close();
        }
        return account;
    }

    @Override
    public Account getAccount(long id) throws IOException {
        File folder = new File("src/resources/data");
        File[] listOfFiles = folder.listFiles();

        assert listOfFiles != null;
        for (File file : listOfFiles) {
            if (file.isFile()) {
                if (file.getName().equals(id + ".txt")) {
                    Account account = readByJson(id);
                    jsonObject.clear();
                    return account;
                }
            }
        }
        return null;
    }

    @Override
    public Account getAccountByUserName(String username) throws IOException {
        File folder = new File("src/resources/data");
        File[] listOfFiles = folder.listFiles();

        assert listOfFiles != null;
        for (File file : listOfFiles) {
            if (file.isFile()) {
                long id = Long.parseLong(file.getName().substring(0, file.getName().length() - 4));
                jsonToReadFile(id);
                if (username.equals(jsonObject.get("username"))) {
                    Account account = readByJson(id);
                    jsonObject.clear();
                    return account;
                }
            }
        }
        return null;
    }

    @Override
    public Account getUser() {
        return userAccount;
    }

    @Override
    public void setUser(Account user) {
        this.userAccount = user;
    }

    @Override
    public boolean removeAccount(long id) {
        if (exists(id)) {
            File account = new File(path + id + ".txt");
            return account.delete();
        }
        return false;
    }

    @Override
    public boolean exists(long id) {
        File account = new File(path + id + ".txt");
        return account.exists() && !account.isDirectory();
    }

    private void writeInJson(Account account) {

        jsonObject.put("id", account.getId());
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
    }

    private Account readByJson(long id) throws IOException {

        jsonToReadFile(id);

        String username = (String) jsonObject.get("username");
        String password = (String) jsonObject.get("password");
        String name = (String) jsonObject.get("name");
        String email = (String) jsonObject.get("email");
        String birthday = (String) jsonObject.get("birthday");
        String bio = (String) jsonObject.get("biography");
        String status = (String) jsonObject.get("status");
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
        account.setName(name);
        account.setEmailAddress(email);
        account.setBiography(bio);
        account.setDateOfBirth(birthday);
        account.setPhoneNumber(phoneNumber);
        account.setFollowers(follower);
        account.setFollowings(following);
        account.setBlacklist(blacklist);
        account.setMutedAccounts(muted);
        account.setTweets(tweet);
        account.setLikedTweet(liked);
        account.setReplied(replied);
        account.setSavedTweet(saved);
        account.setFollowRequest(followRequest);
        account.setAccountsRequestedToFollow(followRequestSent);

        return account;
    }

    private void jsonToReadFile(long id) throws IOException {
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
}
