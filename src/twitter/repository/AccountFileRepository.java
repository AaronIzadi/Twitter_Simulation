package twitter.repository;

import twitter.model.Account;

public class AccountFileRepository implements AccountRepository{
    @Override
    public Account update(Account account) {
        return null;
    }

    @Override
    public Account add(Account account) {
        return null;
    }

    @Override
    public Account getAccount(long id) {
        return null;
    }

    @Override
    public Account getAccountByUserName(String username) {
        return null;
    }

    @Override
    public Account getUser() {
        return null;
    }

    @Override
    public void setUser(Account user) {

    }

    @Override
    public boolean removeAccount(long id) {
        return false;
    }

    @Override
    public boolean exists(long id) {
        return false;
    }
}
