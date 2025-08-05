package com.example.KDT_bank_server_project2.manager.DTO;

public class CreateRoomDto {
    private String roomName;
    private String userId;

    public CreateRoomDto(String roomName) {
        this.roomName = roomName;
    }
    public String getRoomName() {return roomName;}

}
