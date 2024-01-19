package Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.javalin.Javalin;
import io.javalin.http.Context;
import java.util.List;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController(){
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        //app.get("example-endpoint", this::exampleHandler);
        app.post("/register", this::registerUserHandler);
        app.post("/login", this::verifyUserHandler);
        app.post("/messages", this::createMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageHandler);
        app.delete("/messages/{message_id}", this::deleteMessageHandler);
        app.patch("/messages/{message_id}", this::updateMessageHandler);
        app.get("/accounts/{account_id}/messages", this::getUserMessagesHandler);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    // private void exampleHandler(Context context) {
    //     context.json("sample text");
    // }
    
    private void registerUserHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account createdAccount = accountService.createAccount(account);
        if(createdAccount != null){
            ctx.json(mapper.writeValueAsString(createdAccount));
        }else{
            ctx.status(400);
        }
    }

    private void verifyUserHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account userAccount = accountService.verifyUser(account);
        if(userAccount != null){
            ctx.json(mapper.writeValueAsString(userAccount));
        }else{
            ctx.status(401);
        }
    }

    private void createMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        Account userAccount = accountService.findUser(message.posted_by);
        Message postedMessage = messageService.createMessage(message);
        if(userAccount != null && postedMessage != null){
            ctx.json(mapper.writeValueAsString(postedMessage));
        }else{
            ctx.status(400);
        }
    }

    private void getAllMessagesHandler(Context ctx){
        List<Message> messages = messageService.getAllMessages();
        ctx.json(messages);
    }

    private void getMessageHandler(Context ctx){
        try{
            int message_id = Integer.parseInt(ctx.pathParam("message_id"));
            Message message = messageService.getMessage(message_id);
            ctx.json(message);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void deleteMessageHandler(Context ctx){
        try{
            int message_id = Integer.parseInt(ctx.pathParam("message_id"));
            Message message = messageService.deleteMessage(message_id);
            ctx.json(message);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void updateMessageHandler(Context ctx){
        try{
            ObjectMapper mapper = new ObjectMapper();
            Message message = mapper.readValue(ctx.body(), Message.class);
            String message_text = message.getMessage_text();
            
            int message_id = Integer.parseInt(ctx.pathParam("message_id"));
            
            Message updatedMessage = messageService.updateMessage(message_id, message_text);
            ctx.json(updatedMessage);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            ctx.status(400);
        }
    }

    private void getUserMessagesHandler(Context ctx){
        try{
            int account_id = Integer.parseInt(ctx.pathParam("account_id"));
            List<Message> messages = messageService.getUserMessages(account_id);
            ctx.json(messages);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}