package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;
import Model.Message;
import Model.Account;
import DAO.AccountDAO;
import DAO.MessageDAO;
import Service.MessageService;
import Service.AccountService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.SQLException;
import java.util.*;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */

    private AccountService accountService;
    private MessageService messageService;
    private ObjectMapper map;

  

  public SocialMediaController () {
    setup();
  }

 public SocialMediaController(AccountService accountService, MessageService messageService) {
      this.accountService = accountService;
      this.messageService = messageService;
      this.map = new ObjectMapper();
  }

    public Javalin startAPI() {
      Javalin app = Javalin.create();
      app.post("/register", this::registerAccountHandler);
      app.post("/login", this::loginAccountHandler);
      app.post("/messages", this::createMessageHandler);
      app.get("/messages", this::getAllMessagesHandler);
      app.get("/messages/{message_id}", this::getOneMessageHandler);
      app.delete("/messages/{message_id}", this::deleteMessageByIdHandler);
      app.patch("/messages/{message_id}", this::updateMessageHandler);
      app.get("/accounts/{account_id}", this::getAllMessagesByAccountHandler);
      return app;
    }

      private void setup() {
        AccountDAO accountDao = new AccountDAO();
        MessageDAO messageDao = new MessageDAO();
    
        AccountService accountService = new AccountService(accountDao);
        MessageService messageService = new MessageService(messageDao);

        this.accountService = accountService;
        this.messageService = messageService;
        this.map = new ObjectMapper();
    }


    private void registerAccountHandler(Context ctx) throws JsonProcessingException {
        Account account = map.readValue(ctx.body(), Account.class);
        Account accountRegistered = accountService.registerAccount(account);
        if (accountRegistered == null) {
          ctx.status(400);
        } else {
          ctx.json(accountRegistered);
        }
    }

    private void loginAccountHandler(Context ctx) throws JsonProcessingException {
        Account account = map.readValue(ctx.body(), Account.class);
        Account accountLogged = accountService.loginAccount(account);
        if (accountLogged != null) {
          ctx.json(accountLogged);
        } else {
          ctx.status(401);
        }
    }

    private void  createMessageHandler(Context ctx) throws JsonProcessingException {
        Message message = map.readValue(ctx.body(), Model.Message.class);
        if (message != null) {
        Message createdMessage = messageService.createMessage(message);
          if (createdMessage.message_id != -999) {
            ctx.json(createdMessage);
          } else {
            ctx.status(400);
          }
        } else {
          ctx.status(400);
        }
    }

    private void getAllMessagesHandler(Context ctx) {
        List<Message> messages = messageService.getAllMessages();
        ctx.json(messages); 
    }

    private void getOneMessageHandler(Context ctx) {
        String messageIdString = ctx.pathParam("message_id");
        try {
          int messageId = Integer.parseInt(messageIdString);
          Message message = messageService.getOneMessage(messageId);
          if (message != null) {
            ctx.json(message);
          } else {
            ctx.status(200);
          }
        } catch (NumberFormatException ex) {
          ctx.status(400);
        }
      }

    private void deleteMessageByIdHandler(Context ctx) {
        String messageString = ctx.pathParam("message_id");
        try {
          int messageId = Integer.parseInt(messageString);
          Message message = messageService.deleteMessage(messageId);
          if (message != null) {
            ctx.json(message);
          }
        } catch (NumberFormatException ex) {
          ctx.status(400);
        }
    }

    private void updateMessageHandler(Context ctx) {
        String messageString = ctx.pathParam("message_id");
        try {
          int messageId = Integer.parseInt(messageString);
          Message updatedMessage = ctx.bodyAsClass(Message.class);
          updatedMessage.setMessage_id(messageId);
          Message update = messageService.updateOneMessage(updatedMessage);
          if (update != null && update.getMessage_id() != -999) {
            ctx.json(update);
          } else if (update.getMessage_id() == -999) {
            System.out.println(update.toString());
            ctx.status(400);
          } else {
            ctx.status(501);
          }
        } catch (NumberFormatException ex) {
          ctx.status(400);
        }
        
    }

    


    private void getAllMessagesByAccountHandler(Context ctx) {
        String accountIdString = ctx.pathParam("account_id");

        try {
          int accountId = Integer.parseInt(accountIdString);
          List<Message> messages = messageService.getAllMessagesByAccount(accountId);
          if (messages != null) {
            ctx.json(messages);
          } else {
            ctx.json(200);
          }
        } catch (NumberFormatException ex) {
          ctx.status(400);
        }
    }

}