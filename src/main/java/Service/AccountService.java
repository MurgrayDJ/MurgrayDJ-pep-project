package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    private AccountDAO accountDAO;

    public AccountService(){
        this.accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }

    public Account createAccount(Account account){
        if (account.getUsername().length() > 0 &&
            account.getPassword().length() >= 4 &&
            verifyUser(account) == null){
                return accountDAO.insertAccount(account);
        }
        return null;
    }

    public Account verifyUser(Account account){
        return accountDAO.verifyAccount(account);
    }

    public Account findUser(int account_id){
        return accountDAO.findUserById(account_id);
    }
}
