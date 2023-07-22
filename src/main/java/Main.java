import Controller.SocialMediaController;
import io.javalin.Javalin;
import DAO.AccountDAO;
import DAO.MessageDAO;
import Model.Message;
import Model.Account;
import Service.AccountService;
import Service.MessageService;

/**
 * This class is provided with a main method to allow you to manually run and test your application. This class will not
 * affect your program in any way and you may write whatever code you like here.
 */
public class Main {
    public static void main(String[] args) {
        AccountDAO accountDao = new AccountDAO();
        MessageDAO messageDao = new MessageDAO();
    
        AccountService accountService = new AccountService(accountDao);
        MessageService messageService = new MessageService(messageDao);
    
        SocialMediaController controller = new SocialMediaController(accountService, messageService);
    
        Javalin app = controller.startAPI();
        app.start(8080);
    }
}
