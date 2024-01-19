package Service;

import DAO.MessageDAO;
import Model.Message;

import java.util.List;

public class MessageService {
    private MessageDAO messageDAO;

    public MessageService(){
        this.messageDAO = new MessageDAO();
    }

    public MessageService(MessageDAO messageDAO){
        this.messageDAO = messageDAO;
    }

    private Boolean validMessage(String message_text){
        if(message_text.length() > 0 &&
        message_text.length() < 255){
            return true;
        }
        return false;
    }

    public Message createMessage(Message message){
        if(validMessage(message.getMessage_text())){
            return messageDAO.insertMessage(message);
        }
        return null;
    }

    public List<Message> getAllMessages(){
        return messageDAO.selectAllMessages();
    }

    public Message getMessage(int message_id){
        return messageDAO.getMessageById(message_id);
    }

    public Message deleteMessage(int message_id){
        return messageDAO.deleteMessageById(message_id);
    }

    public Message updateMessage(int message_id, String message_text){
        if(validMessage(message_text)){
            return messageDAO.updateMessageById(message_id, message_text);
        }
        return null;
    }

    public List<Message> getUserMessages(int account_id){
        return messageDAO.getAllUserMessages(account_id);
    }
}
