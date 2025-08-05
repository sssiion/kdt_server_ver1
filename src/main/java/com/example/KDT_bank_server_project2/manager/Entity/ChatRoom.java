package com.example.KDT_bank_server_project2.manager.Entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name="chat_room")
public class ChatRoom {

    @Id
    private String roomId;  //방 id

    @Column(name = "room_name", nullable = false)
    private String roomName;

    @Column(name = "create_at")
    private LocalDateTime createdAt;

    @Column(name = "user_count")
    private Integer userCount = 0;


    @PrePersist
    protected void onCreate() {
        if (roomId == null) {
            roomId = UUID.randomUUID().toString();
        }
        createdAt = LocalDateTime.now();
    }
    public ChatRoom() {}
    public ChatRoom(String roomName){
        this.roomName = roomName;
        this.roomId = UUID.randomUUID().toString();
        this.userCount = 0;
    }
    // 참가자 수 관리
    public void plusUserCount() {
        this.userCount++;
    }
    public void minusUserCount() {
        if (this.userCount > 0) {
            this.userCount--;
        }
    }


}
