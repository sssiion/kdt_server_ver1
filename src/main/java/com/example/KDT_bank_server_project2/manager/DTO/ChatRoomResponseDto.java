package com.example.KDT_bank_server_project2.manager.DTO;

import com.example.KDT_bank_server_project2.manager.Entity.ChatRoom;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
public class ChatRoomResponseDto {
    private String roomId;
    private String roomName;
    private Integer userCount;
    private LocalDateTime createdAt;

    public ChatRoomResponseDto(ChatRoom chatRoom) {
        this.roomId = chatRoom.getRoomId();
        this.roomName = chatRoom.getRoomName();
        this.userCount = chatRoom.getUserCount();
        this.createdAt = chatRoom.getCreatedAt();
    }
}
