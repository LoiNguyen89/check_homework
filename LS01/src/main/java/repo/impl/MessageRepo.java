package repo.impl;

import model.Message;
import java.util.List;

public interface MessageRepo {
    void save(Message message);
    List<Message> findConversation(Long senderId, Long receiverId);
}
