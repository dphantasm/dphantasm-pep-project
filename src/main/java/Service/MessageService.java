package Service;

import Model.Message;
import DAO.MessageDAO;
import java.util.*;

public class MessageService {
    private MessageDAO messageDAO;

    public MessageService(MessageDAO messageDao) {
        this.messageDAO = messageDao;
      }

    public Message createMessage(Message message) {
        return messageDAO.createMessage(message);
    }

    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    public Message getOneMessage(int id) {
        return messageDAO.getOneMessage(id);
    }

    public Message deleteMessage(int id) {
        return messageDAO.deleteMessage(id);
    }

    public Message updateOneMessage(Message message) {
        return messageDAO.updateMessage(message);
    }

    public List<Message> getAllMessagesByAccount(int accountId) {
        return messageDAO.getAllMessagesByAccount(accountId);
    }




}
