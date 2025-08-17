package service.impl;

import model.Message;
import java.util.List;

public interface MessageService {
    void sendMessage(Message message);
    List<Message> getConversation(Long senderId, Long receiverId);
}
