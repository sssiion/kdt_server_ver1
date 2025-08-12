package com.example.KDT_bank_server_project2.manager.Service;
import com.example.KDT_bank_server_project2.manager.DTO.ChatRoomResponseDto;
import com.example.KDT_bank_server_project2.manager.Entity.User;
import com.example.KDT_bank_server_project2.manager.Repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import com.example.KDT_bank_server_project2.manager.Entity.ChatRoom;
import com.example.KDT_bank_server_project2.manager.Repository.ChatRoomRepository;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    public ChatRoom createRoom(String roomName){
        ChatRoom room = new ChatRoom(roomName);
        chatRoomRepository.save(room);
        return room;
        // 방 생성
    }

    public ChatRoom getRoom(Long roomId){
        ChatRoom room = chatRoomRepository.findByRoomId(roomId)
                .orElseThrow(()-> new IllegalArgumentException("채팅방 찾을 수 없음"));
        return room;
    }


    public void joinRoom(String userId, Long roomId){
        User user  = userRepository.findByUserId(userId)
                .orElseThrow(()-> new EntityNotFoundException(" 사용자를 찾을 수 없습니다"));
        ChatRoom room = chatRoomRepository.findByRoomId(roomId)
                .orElseThrow(()-> new IllegalArgumentException("채팅방을 찾을 수 없습니다"));

        user.joinedRooms(roomId, room.getRoomName());
        room.plusUserCount();

        userRepository.save(user);
        chatRoomRepository.save(room);
        System.out.println("joinRoom 실행됨 service임");
    }

    public void leaveRoom(Long roomId, String userId){
        User user = userRepository.findByUserId(userId)
                .orElseThrow(()-> new EntityNotFoundException("사용자 찾을 수 없음"));
        ChatRoom room = chatRoomRepository.findByRoomId(roomId)
                .orElseThrow(()-> new IllegalArgumentException("채팅방 찾을 수 없음"));

        if(!user.isInRoom(roomId)){
            throw new IllegalArgumentException("참가하지 않은 방");
        }
        user.leaveRoom(roomId);
        room.minusUserCount();
        userRepository.save(user);
        chatRoomRepository .save(room);
    }

    @Transactional(readOnly = true)
    public List<ChatRoom> getAllRooms(){
        return chatRoomRepository.findAll();
    } // 모든 방 객체 가져오기
    //특정 방 조회
    @Transactional(readOnly = true)
    public ChatRoom findByroomId(Long roomId){
        return chatRoomRepository.findByRoomId(roomId)
                .orElseThrow(()->new IllegalArgumentException("채팅방을 찾을 수 없음"));}
    // 방 검색
    @Transactional(readOnly = true)
    public List<ChatRoom> searchRooms(String keyword){
        return chatRoomRepository.findByRoomNameContaining(keyword);
    }

}
