package twitter.repository;

import twitter.model.Account;

import java.util.HashMap;

class AccountHashMapRepository implements AccountRepository{

    private static AccountHashMapRepository instance = new AccountHashMapRepository();
    private HashMap<Long , Account> map = new HashMap<>();
    private static int id = 1;

    private Account userAccount;

    private AccountHashMapRepository() { }

    public static AccountRepository getInstance() {
        return instance;
    }

    @Override
    public Account update(Account account) {
        return map.replace(account.getId(),account);
    }

    @Override
    public Account add(Account account) {
        account.setId(id);
        id++;
        return map.put(account.getId(), account);
    }

    @Override
    public Account getAccount(long id) { return map.get(id); }

    @Override
    public Account getAccountByUserName(String username) {
        for (Account acc: map.values()) {
            String accUserName = acc.getUserName();
            if (accUserName.equalsIgnoreCase(username)) {
                return acc;
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
        userAccount = user;
    }

    @Override
    public boolean removeAccount(long id) {
        Account account = map.remove(id);
        return account != null;
    }

    @Override
    public boolean exists(long id) {
        return map.containsKey(id);
    }
}
