package twitter.repository;

import twitter.model.Account;

import java.io.*;

public class AccountFileRepository extends Repository implements AccountRepository {

    private Account userAccount;
    private final String path = "src/resources/data/account/";
    private static final AccountFileRepository instance = new AccountFileRepository();

    public static AccountFileRepository getInstance() {
        return instance;
    }

    @Override
    public Account update(Account account) throws IOException {
        return (Account) update(account, account.getId(), path);
    }

    @Override
    public Account add(Account account) throws IOException {
        return (Account) add(account, account.getId(), path);
    }

    @Override
    public Account getAccount(long id) throws IOException {
        return (Account) getObject(id, path, Repository.accountType);
    }

    @Override
    public Account getAccountByUserName(String username) throws IOException {
        File folder = new File("src/resources/data/account");
        File[] listOfFiles = folder.listFiles();

        assert listOfFiles != null;
        for (File file : listOfFiles) {
            if (file.isFile()) {
                long id = Long.parseLong(file.getName().substring(0, file.getName().length() - 4));
                jsonToReadFile(id, path);
                if (username.equals(getJsonObject().get("username"))) {
                    Account account = (Account) readByJson(id, path, Repository.accountType);
                    clearJsonData();
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
        return removeObject(id,path);
    }

    @Override
    public boolean exists(long id) {
        return exists(id, path);
    }


}
