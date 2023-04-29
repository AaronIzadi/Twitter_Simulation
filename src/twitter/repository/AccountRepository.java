package twitter.repository;

import twitter.model.Account;

public interface AccountRepository {

    Account update(Account account);

    Account add(Account account);

    Account getAccount(long id);

    Account getAccountByUserName(String username);

    Account getUser();

    void setUser(Account user);

    boolean removeAccount(long id);

    boolean exists(long id);


}
