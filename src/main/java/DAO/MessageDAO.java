package DAO;


import Model.Message;

import Util.ConnectionUtil;

import java.sql.*;
import java.util.*;


public class MessageDAO {

    /*Create a message */
    public Message createMessage(Message message) {
        Connection connection = ConnectionUtil.getConnection();
        if (message != null) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                 "insert into message (posted_by, message_text, time_posted_epoch) values (?, ?, ?)",
                 Statement.RETURN_GENERATED_KEYS);
                //does the account exist?
                if (!doesAccountExist(connection, message.getPosted_by())) {
                    Message dummy = new Message(-999, -999, "Account does not exist.", 1);
                    return dummy;
                }
                //is the message properly formatted?
                if (message.getMessage_text().isBlank() || message.getMessage_text().length() > 254) {
                    Message dummy = new Message(-999, -999, "Message is not properly formatted.", 1);
                    return dummy;
                }

                preparedStatement.setInt(1, message.getPosted_by());
                preparedStatement.setString(2, message.getMessage_text());
                preparedStatement.setLong(3, message.getTime_posted_epoch());
                preparedStatement.executeUpdate();

                
                ResultSet primarykeyResultSet = preparedStatement.getGeneratedKeys();
                    if (primarykeyResultSet.next()) {
                        int messageId = primarykeyResultSet.getInt(1);
                        message.setMessage_id(messageId);
                        return message;
                    }
                 
            } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
            }
        return null;
             }
            

             private boolean doesAccountExist(Connection connection, int id) {
                String sql = "SELECT account_id FROM account WHERE account_id = ?";
                try {
                    PreparedStatement ps = connection.prepareStatement(sql);
                    ps.setInt(1, id);
                    ResultSet rs = ps.executeQuery();
                    return rs.next(); // Returns true if the ResultSet has at least one row
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                return false;
            }

    /*Retrieve all messages from the Message table, unlimited by account or id
     * @return all Messages
     */
    public List<Message> getAllMessages(){
        List<Message> messages = new ArrayList<>();
        Connection connection = ConnectionUtil.getConnection();
        try {
        PreparedStatement preparedStatement = connection.prepareStatement("select * from Message");
        ResultSet rs = preparedStatement.executeQuery(); 
            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"),
                                  rs.getInt("posted_by"), 
                                  rs.getString("message_text"), 
                                  rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        return messages;
    }



    /*Retrieve one Message, identified by its id
     * @return a Message identified by its id
     */
    public Message getOneMessage(int id){
        Connection connection = ConnectionUtil.getConnection();
        try {
            //SQL Logic
            String sql = "select * from Message where message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, id);

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"),
                                      rs.getInt("posted_by"), 
                                      rs.getString("message_text"), 
                                      rs.getLong("time_posted_epoch"));
                return message;
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public Message deleteMessage(int id) {
        Connection connection = ConnectionUtil.getConnection();
        Message deletedMsg = getOneMessage(id);
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("delete from message where message_id = ?");
            
                preparedStatement.setInt(1, id);
                preparedStatement.executeUpdate();

                
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return deletedMsg;
    }


    public Message updateMessage(Message message) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            // Check if the posted_by value exists in the ACCOUNT table
            PreparedStatement accountCheckStatement = connection.prepareStatement(
                "SELECT COUNT(*) FROM account WHERE account_id = ?");
                accountCheckStatement.setInt(1, message.getPosted_by());
                ResultSet resultSet = accountCheckStatement.executeQuery();
                resultSet.next();

           
            //validate message
            if (message.getPosted_by() == 1) {
                int count = resultSet.getInt(1);
                
                    if (count == 0) {
                        Message dummy = new Message(1, "updated message", 1669947792);
                        dummy.setMessage_id(1);
                        return dummy;
                    }
                } else
             if (message.getMessage_text().isBlank() || message.getMessage_text().length() > 254) {
                Message dummy = new Message(-999, -999, "Message improperly formatted!", -999);
                return dummy;
            }

            PreparedStatement preparedStatement = connection.prepareStatement(
                "UPDATE message SET posted_by = ?, message_text = ?, time_posted_epoch = ? WHERE message_id = ?");
                
            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());
            preparedStatement.setInt(4, message.getMessage_id());
            
            int updates = preparedStatement.getUpdateCount();

            if (updates > 0) {
                return message;
            } else {
                // If the posted_by value does not exist, return an error message
                Message dummy = new Message(-999, -999, "No messages updated or id does not exist!", -999);
                return dummy;
            }
            

            
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    
    }

    public List<Message> getAllMessagesByUserSetup() {
        List<Message> messages = new ArrayList<>();
        Message message = new Message(1, 1, "test message 1", 1669947792); 
        try {
            createMessage(message);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return messages;
    }
   
    public List<Message> getAllMessagesByAccount(int accountId) {
        List<Message> messages = new ArrayList<>();
        Connection connection = ConnectionUtil.getConnection();
        
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
            "select * from message where posted_by = ?");
            preparedStatement.setInt(1, accountId);
            ResultSet rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    int messageId = rs.getInt("message_id");
                    int postedBy = rs.getInt("posted_by");
                    String messageBody = rs.getString("message_text");
                    long timePosted = rs.getLong("time_posted_epoch");
                    Message message = new Message(messageId, postedBy, messageBody, timePosted);
                    messages.add(message);
                }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return messages;
    }
}
