package twitter.repository;

import org.json.simple.JSONObject;
import twitter.model.Account;
import twitter.utils.AppPaths;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class AccountFileRepository implements AccountRepository {

    private static final Path ACCOUNT_DIR = AppPaths.accountDataDir();
    private static final AccountFileRepository instance = new AccountFileRepository();

    private Account userAccount;

    public static AccountFileRepository getInstance() {
        return instance;
    }

    @Override
    public Account update(Account account) throws IOException {
        if (!exists(account.getId())) {
            return add(account);
        }
        save(account);
        syncLoggedInUser(account.getId());
        return account;
    }

    @Override
    public Account add(Account account) throws IOException {
        Repository.addAppInfo(account, Account.getIdCounter());
        if (exists(account.getId())) {
            return update(account);
        }
        save(account);
        syncLoggedInUser(account.getId());
        return account;
    }

    @Override
    public Account getAccount(long id) throws IOException {
        if (!exists(id)) {
            return null;
        }
        return readFromFile(id);
    }

    @Override
    public Account getAccountByUserName(String username) throws IOException {
        File folder = ACCOUNT_DIR.toFile();
        File[] files = folder.listFiles();
        if (files == null) {
            return null;
        }

        for (File file : files) {
            if (!file.isFile()) {
                continue;
            }
            long id = Long.parseLong(file.getName().substring(0, file.getName().length() - 4));
            Account account = readFromFile(id);
            if (username.equals(account.getUserName())) {
                return account;
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
        if (!exists(id)) {
            return false;
        }
        return JsonFileHelper.entityFile(ACCOUNT_DIR.toString(), id).toFile().delete();
    }

    @Override
    public boolean exists(long id) {
        File account = JsonFileHelper.entityFile(ACCOUNT_DIR.toString(), id).toFile();
        return account.exists() && !account.isDirectory();
    }

    private void syncLoggedInUser(long accountId) throws IOException {
        if (userAccount != null && userAccount.getId() == accountId) {
            userAccount = readFromFile(accountId);
        }
    }

    private void save(Account account) throws IOException {
        JsonFileHelper.writeJson(filePath(account.getId()), toJson(account));
    }

    private Account readFromFile(long id) throws IOException {
        return fromJson(JsonFileHelper.readJson(filePath(id)), id);
    }

    private Path filePath(long id) {
        return JsonFileHelper.entityFile(ACCOUNT_DIR.toString(), id);
    }

    private JSONObject toJson(Account account) {
        JSONObject json = new JSONObject();

        json.put("id", account.getId());
        json.put("id counter", account.getIdCounter());
        json.put("username", account.getUserName());
        json.put("password", account.getPassword());
        json.put("name", account.getName());
        json.put("email", account.getEmailAddress());
        json.put("birthday", account.getDateOfBirth());
        json.put("biography", account.getBiography());
        json.put("status", account.getStatus());
        json.put("phone number", account.getPhoneNumber());
        json.put("type", account.getType());

        JsonFileHelper.putLongList(json, "follower num", "follower ", account.getFollowers());
        JsonFileHelper.putLongList(json, "following num", "following ", account.getFollowings());
        JsonFileHelper.putLongList(json, "blacklist num", "blacklist ", account.getBlacklist());
        JsonFileHelper.putLongList(json, "muted num", "muted ", account.getMutedAccounts());
        JsonFileHelper.putLongList(json, "tweet num", "tweet ", account.getTweets());
        JsonFileHelper.putLongList(json, "replied num", "replied ", account.getReplied());
        JsonFileHelper.putLongList(json, "saved num", "saved tweet ", account.getSavedTweet());
        JsonFileHelper.putLongList(json, "liked num", "liked tweet ", account.getLikedTweet());
        JsonFileHelper.putLongList(json, "request num", "follow request ", account.getFollowRequest());
        JsonFileHelper.putLongList(json, "request sent num", "follow request sent ", account.getAccountsRequestedToFollow());

        return json;
    }

    private Account fromJson(JSONObject json, long id) {
        String username = (String) json.get("username");
        String password = (String) json.get("password");
        String status = (String) json.get("status");
        int type = ((Long) json.get("type")).intValue();

        Account account = createAccountWithStatus(username, password, type, status);
        account.setId(id);
        account.setName((String) json.get("name"));
        account.setEmailAddress((String) json.get("email"));
        account.setBiography((String) json.get("biography"));
        account.setDateOfBirth((String) json.get("birthday"));
        account.setPhoneNumber((Long) json.get("phone number"));

        account.setFollowers(JsonFileHelper.readLongList(json, "follower num", "follower "));
        account.setFollowings(JsonFileHelper.readLongList(json, "following num", "following "));
        account.setBlacklist(JsonFileHelper.readLongList(json, "blacklist num", "blacklist "));
        account.setMutedAccounts(JsonFileHelper.readLongList(json, "muted num", "muted "));
        account.setTweets(JsonFileHelper.readLongList(json, "tweet num", "tweet "));
        account.setReplied(JsonFileHelper.readLongList(json, "replied num", "replied "));
        account.setSavedTweet(JsonFileHelper.readLongList(json, "saved num", "saved tweet "));
        account.setLikedTweet(JsonFileHelper.readLongList(json, "liked num", "liked tweet "));
        account.setFollowRequest(JsonFileHelper.readLongList(json, "request num", "follow request "));
        account.setAccountsRequestedToFollow(JsonFileHelper.readLongList(json, "request sent num", "follow request sent "));

        syncCounts(account);
        return account;
    }

    private Account createAccountWithStatus(String username, String password, int type, String status) {
        switch (status) {
            case "Offline":
                return new Account(username, password, type, Account.OFFLINE);
            case "Online":
                return new Account(username, password, type, Account.ONLINE);
            default:
                return new Account(username, password, type, Account.DEFAULT_STATUS);
        }
    }

    private void syncCounts(Account account) {
        account.setNumberOfFollowers();
        account.setNumberOfFollowings();
        account.setNumberOfBlackList();
        account.setNumberOfFollowRequest();
        account.setNumberOfAccountsSentRequest();
        account.setNumberOfTweets();
    }

}
