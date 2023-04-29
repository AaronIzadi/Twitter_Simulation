package twitter.repository;

import twitter.model.Account;

public class SecureAccountRepository implements AccountRepository{

    private static AccountRepository instance = new SecureAccountRepository();
    private AccountRepository accRep = AccountHashMapRepository.getInstance();


    private SecureAccountRepository() {}

    public static AccountRepository getInstance() {
        return instance;
    }

    @Override
    public Account update(Account newAccount) {
        Account target = accRep.getAccount(getUser().getId());
        if (hasPermission(getUser(),target) && hasPermission(newAccount,getUser())) {
            return accRep.update(newAccount);
        }
        return null;
    }

    @Override
    public Account add(Account account) {
        return accRep.add(account);
    }

    @Override
    public Account getAccount(long id) {
        Account target = accRep.getAccount(id);
        return secureAccount(getUser(), target);
    }

    @Override
    public Account getAccountByUserName(String username) {
        Account result = accRep.getAccountByUserName(username);
        if (result != null){
            result = secureAccount(result, getUser());
        }
        return result;
    }

    @Override
    public Account getUser() {
        return accRep.getUser();
    }

    @Override
    public void setUser(Account user) {
        accRep.setUser(user);
    }

    private boolean hasPermission(Account account1, Account account2) {
        if (account1 == null || account2 == null) {
            return false;
        }
        return (account1.getUserName().equals(account2.getUserName())) && account1.getPassword().equals(account2.getPassword());
    }

    private Account secureAccount(Account userAccount, Account resultAccount) {
        if (resultAccount != null && !hasPermission(userAccount, resultAccount) && userAccount != null) {
            resultAccount.setPassword("");
        }
        return resultAccount;
    }

    @Override
    public boolean removeAccount(long id) {
        Account target = accRep.getAccount(id);
        if (hasPermission(getUser(),target)) {
            return accRep.removeAccount(id);
        }
        return false;
    }

    @Override
    public boolean exists(long id) {
        return accRep.exists(id);
    }
}
