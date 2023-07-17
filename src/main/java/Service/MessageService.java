package Service;

import Model.Message;
import DAO.MessageDAO;
import java.util.*;

public class MessageService {
    private static MessageDAO messageDAO;

    public MessageService(){
        messageDAO = new MessageDAO();
    }

    public static List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    //public static List<Message> getOneMessage() {
    //    return messageDAO.getOneMessage();
   //}








}
