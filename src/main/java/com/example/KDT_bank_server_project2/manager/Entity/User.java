package com.example.KDT_bank_server_project2.manager.Entity;

import lombok.Getter;
import org.apache.commons.lang3.RandomStringUtils;
import jakarta.persistence.*; //

import java.time.LocalDateTime;
import java.util.Map;
import java.util.HashMap;

@Entity //JPA에게 user 클래스를 DB 테이블과 매핑해줘
@Table(name="users") // 실제 DB 에서는 user 테이블을 사용해
public class User {

    @Getter
    @Id
    @Column(name = "user_id", unique = true, nullable = false)
    private String userId;

    @Column(name="user_authority")
    private String user_authority ="USER";
    //권한 유무

    @Getter
    @Column(name="user_phone", unique = true)
    private String userPhone;

    @Getter
    @Column(name="user_name", unique = true)
    private String userName;


    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "is_online")
    private Boolean isOnline = false;

    @Column(name = "create_at")
    private LocalDateTime createAt;

    @Getter
    @ElementCollection
    @CollectionTable(name = "user_joined_rooms", joinColumns = @JoinColumn(name = "user_id"))
    @MapKeyColumn(name = "room_id")
    @Column(name = "room_name")
    private Map<String, String> joinedRooms = new HashMap<>();


    public User(){ }

    public User(String name, String userPhone){
        this.userId=  RandomStringUtils.randomAlphanumeric(6);
        this.userName = name;
        this.password = "1234";
        this.userPhone = userPhone;
        this.user_authority = "USER";
        this.createAt = LocalDateTime.now();

        this.isOnline = false;
    }
    public boolean checkPassword(String password){
        return this.password != null && this.password.equals(password);
    }

    public void setOnline(){
        this.isOnline = true;
    }
    public void setOffline(){
        this.isOnline = false;
    }


    public boolean isOnline() { return isOnline; }

    public void joinedRooms(String roomId, String roomName){
        this.joinedRooms.put(roomId,roomName);
    }
    public void leaveRoom(String roomId){
        joinedRooms.remove(roomId);
    }

    public boolean isInRoom(String roomId){
        Map<String,String> joinedRooms = getJoinedRooms();
        return joinedRooms.containsKey(roomId);
    }
    public String getUserAuthority() { return user_authority; }
    public void setUserAuthority(String user_authority) { this.user_authority = user_authority; }


}
