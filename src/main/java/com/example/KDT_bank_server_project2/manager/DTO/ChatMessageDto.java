package com.example.KDT_bank_server_project2.manager.DTO;

import com.example.KDT_bank_server_project2.manager.Entity.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Data
@Getter
@Setter
public class ChatMessageDto {
    private String roomId;
    private String userId;
    private String content;
    private String type;
    private LocalDateTime sendAt;

    public ChatMessageDto() {}
    public ChatMessageDto(ChatMessage chatMessage) {
        this.roomId = chatMessage.getRoomUuid();
        this.userId = chatMessage.getSenderId();
        this.content = chatMessage.getContent();
        this.type = chatMessage.getType().toString();
        this.sendAt = chatMessage.getSentAt();


    }



}

