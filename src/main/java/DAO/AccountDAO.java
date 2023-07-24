package DAO;
import java.sql.*;
import Util.ConnectionUtil;
import Model.Account;

public class AccountDAO {

    public Account registerAccount(Account account) {
        //Verify username
        if (account.getUsername().isBlank() || account.getPassword().length() < 4) {
            return null;
        }

        try {Connection connection = ConnectionUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
            "insert into account (username, password) values (?, ?)",
            PreparedStatement.RETURN_GENERATED_KEYS); 

                if (usernameExisting(account.getUsername())) {
                    return null;
                }

                preparedStatement.setString(1, account.getUsername());
                preparedStatement.setString(2, account.getPassword());
                preparedStatement.executeUpdate();

                ResultSet keys = preparedStatement.getGeneratedKeys(); {
                    if (keys.next()) {
                        int id = keys.getInt(1);
                        account.setAccount_id(id);
                        return account;
                    }
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return null;
    }

    public boolean usernameExisting(String username) {
        try {Connection connection = ConnectionUtil.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(
        "select count(*) from account where username =  ?");

                preparedStatement.setString(1, username);
                ResultSet rs = preparedStatement.executeQuery(); {
                    if (rs.next()) {
                        int count = rs.getInt(1);
                        return count > 0;
                    }
                }   
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
    }

    public Account loginAccount(Account account) {
        try {Connection connection = ConnectionUtil.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(
            "select * from account where username = ? and password = ?");
            
                preparedStatement.setString(1, account.getUsername());
                preparedStatement.setString(2, account.getPassword());
                try (ResultSet rs = preparedStatement.executeQuery()) {
                    if (rs.next()) {
                        int id = rs.getInt("account_id");
                        return new Account(id, account.getUsername(), account.getPassword());
                    } 
                }
                return null;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }
    
}
