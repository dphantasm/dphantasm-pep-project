package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;
import Model.Message;
import Model.Account;
import Service.MessageService;
import Service.AccountService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
        Message message = map.readValue(ctx.body(), Message.class);
        if (message != null) {
        Message createdMessage = messageService.createMessage(message);
          if (createdMessage != null) {
            ctx.json(createdMessage);
          } else {
            ctx.status(500);
          }
        } else {
          ctx.status(400);
        }
    }

    private void getAllMessagesHandler(Context ctx) {
        ctx.json(messageService.getAllMessages()); 
    }

    private void getOneMessageHandler(Context ctx) {
        String messageIdString = ctx.pathParam("message_id");
        try {
          int messageId = Integer.parseInt(messageIdString);
          Message message = messageService.getOneMessage(messageId);
          if (message != null) {
            ctx.json(message);
          } else {
            ctx.status(404);
          }
        } catch (NumberFormatException ex) {
          ctx.status(400);
        }
      }

    private void deleteMessageByIdHandler(Context ctx) {
        String messageString = ctx.pathParam("message_id");
        try {
          int messageId = Integer.parseInt(messageString);
          messageService.deleteMessage(messageId);
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
          if (update != null) {
            ctx.json(update);
          } else {
            ctx.status(500);
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
          ctx.json(messages);
        } catch (NumberFormatException ex) {
          ctx.status(400);
        }
    }

}