package twitter.repository;

import twitter.model.Account;
import java.io.IOException;

public interface AccountRepository {

    Account update(Account account) throws IOException;

    Account add(Account account) throws IOException;

    Account getAccount(long id) throws IOException;

    Account getAccountByUserName(String username) throws IOException;

    Account getUser();

    void setUser(Account user);

    boolean removeAccount(long id) throws IOException;

    boolean exists(long id);


}
