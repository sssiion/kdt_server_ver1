package com.example.KDT_bank_server_project2.manager.RabbitMQ;

import com.example.KDT_bank_server_project2.manager.DTO.ChatMessageDto;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

@Component
public class ChatMessageListener {
    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @RabbitListener(queues= "chat.queue")
    public void receiveMessage(ChatMessageDto message){
        messagingTemplate.convertAndSend("/topic/chat.room."+message.getRoomId(), message);
    }
}
