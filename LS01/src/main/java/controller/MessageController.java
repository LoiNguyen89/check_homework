package controller;

import model.Message;
import model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import service.impl.MessageService;

import java.util.List;

@Controller
@RequestMapping("/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;


    @GetMapping("/sender/{senderId}/receiver/{receiverId}")
    public String getConversation(@PathVariable Long senderId,
                                  @PathVariable Long receiverId,
                                  Model model) {
        List<Message> messages = messageService.getConversation(senderId, receiverId);

        model.addAttribute("messages", messages);
        model.addAttribute("senderId", senderId);
        model.addAttribute("receiverId", receiverId);
        model.addAttribute("newMessage", new Message());

        return "messages";
    }

    // ✅ Gửi tin nhắn mới
    @PostMapping("/sender/{senderId}/receiver/{receiverId}")
    public String sendMessage(@PathVariable Long senderId,
                              @PathVariable Long receiverId,
                              @ModelAttribute("newMessage") Message message) {
        User sender = new User();
        sender.setId(senderId.intValue());

        User receiver = new User();
        receiver.setId(receiverId.intValue());

        message.setSender(sender);
        message.setReceiver(receiver);

        messageService.sendMessage(message);

        return "redirect:/messages/sender/" + senderId + "/receiver/" + receiverId;
    }
}
