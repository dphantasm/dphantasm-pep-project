package Service;
import java.util.*;
import Model.Account;
import DAO.AccountDAO;

public class AccountService {
    private AccountDAO accountDAO;

    public AccountService(AccountDAO accountDao) {
        this.accountDAO = accountDao;
      }

    public Account registerAccount(Account account){
        return accountDAO.registerAccount(account);
    }

    public Account loginAccount(Account account){
        return accountDAO.loginAccount(account);
    }

}
