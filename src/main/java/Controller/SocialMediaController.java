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
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("/messages", this::getAllMessagesHandler);


        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    private void getAllMessagesHandler(Context ctx) {
        List<Message> messages = MessageService.getAllMessages();
        ctx.json(messages);
        ctx.status(200);
    }

    //private void getOneMessageHandler(Context ctx) {
    //    List<Message> message = MessageService.getOneMessage();
    //    ctx.json(message);
    //}

    //private void deleteOneMessageHandler(Context ctx) {
    //    List<Message> message = MessageService.deleteOneMessage();
    //    ctx.json(message);
    //}

    //private void updateOneMessageHandler(Context ctx) {
    //    List<Message> message = MessageService.updateOneMessage();
    //    ctx.json(message);
    //}

    //private void getAllMessagesFromUserHandler(Context ctx) {
    //    List<Message> messages = MessageService.getAllUserMessages();
    //    ctx.json(messages);
    //}

}