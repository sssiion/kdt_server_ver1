package com.example.KDT_bank_server_project2.manager.Service;

import com.example.KDT_bank_server_project2.manager.Entity.ChatMessage;
import com.example.KDT_bank_server_project2.manager.Entity.ChatRoom;
import com.example.KDT_bank_server_project2.manager.Entity.User;
import com.example.KDT_bank_server_project2.manager.Repository.ChatMessageRepository;
import com.example.KDT_bank_server_project2.manager.Repository.ChatRoomRepository;
import com.example.KDT_bank_server_project2.manager.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    public ChatMessage saveMessage(String senderId, String roomId, String content, ChatMessage.MessageType type ){
        User sender = userRepository.findByUserId(senderId) //발신자 조회
                .orElseThrow(()-> new RuntimeException("발신자 찾을 수 없음"));
        ChatRoom room = chatRoomRepository.findByRoomId(roomId)
                .orElseThrow(()-> new IllegalArgumentException("채팅방 찾을수 없음"));//채팅방 조회
        ChatMessage message = new ChatMessage(senderId, roomId, content, type);
        return chatMessageRepository.save(message);
    } // 메시지 저장
    //일반 채팅 메시지
    public ChatMessage saveChatMessage(String senderId, String roomId, String message,String receiverId){
        return saveMessage(senderId, roomId, message, ChatMessage.MessageType.CHAT);
    }
    public ChatMessage saveJoinMessage(String senderId, String roomId){
        User sender = userRepository.findByUserId(senderId)
                .orElseThrow(()-> new IllegalArgumentException("발신자를 찾을 수 없습니다"+senderId));
        String message = sender.getUserName() +"님이 입장";
        return saveMessage(senderId, roomId, message, ChatMessage.MessageType.JOIN);

    }
    public ChatMessage saveLeaveMessage(String senderId, String roomId){
        User user = userRepository.findByUserId(senderId)
                .orElseThrow(()->new IllegalArgumentException("발신자를 찾을 수 없습니다."));
        String message = user.getUserName()+"님이 나감";
        return saveMessage(senderId, roomId, message, ChatMessage.MessageType.LEAVE);

    }
    // 특정 방의 모든 메시지 조회 ( 시간 순)
    @Transactional(readOnly = true)
    public List<ChatMessage> getRoomMessages(String roomId){
        return chatMessageRepository.findByRoomUuidOrderBySentAtAsc(roomId);

    } // 과거 메시지 가져오기.
    // 특정 방의 최근 메시지 조회 ( 페이징)
    @Transactional(readOnly = true)
    public List<ChatMessage> getRecentRoomMessage(String roomId, int size){
        chatRoomRepository.findByRoomId(roomId)
                .orElseThrow(()-> new IllegalArgumentException("채팅방을 찾을 수 없음"+roomId));
        Pageable pageable = PageRequest.of(0, size);
        return chatMessageRepository.findByRoomUuidOrderBySentAtDesc(roomId, pageable);
    }
    @Transactional(readOnly = true)
    public List<ChatMessage> getUserMessages(String senderId, String roomId){
        userRepository.findByUserId(senderId)
                .orElseThrow(()-> new IllegalArgumentException("사용자 찾을 수 없음"));
        return chatMessageRepository.findBySenderIdOrderBySentAtDesc(senderId);
    }
    @Transactional(readOnly = true)
    public List<ChatMessage> searchMessages(String roomUuid, String keyword){
        return chatMessageRepository.searchMessageByContent(roomUuid,keyword);
    } // 키워드로 메시지 찾기



}
