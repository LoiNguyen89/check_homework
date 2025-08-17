package service;

import model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repo.impl.MessageRepo;
import service.impl.MessageService;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageRepo messageRepo;

    @Override
    public void sendMessage(Message message) {
        messageRepo.save(message);
    }

    @Override
    public List<Message> getConversation(Long senderId, Long receiverId) {
        return messageRepo.findConversation(senderId, receiverId);
    }
}
