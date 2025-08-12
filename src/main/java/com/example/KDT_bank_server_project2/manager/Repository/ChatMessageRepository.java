package com.example.KDT_bank_server_project2.manager.Repository;

import com.example.KDT_bank_server_project2.manager.Entity.ChatMessage;
import com.example.KDT_bank_server_project2.manager.Entity.ChatRoom;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {


    List<ChatMessage> findByRoomIdOrderBySentAtAsc(Long roomId);
    // 특정 채팅방의 모든 메시지 (시간순 정렬)

    List<ChatMessage> findByRoomIdOrderBySentAtDesc(Long roomId, Pageable pageable);
    // 특정 채팅방의 최근 메시지 ( 페이지네이션)

    List<ChatMessage> findBySenderIdOrderBySentAtDesc(String senderId);
    // 특정 사용자가 보낸 메시지




    @Query("SELECT cm FROM ChatMessage cm WHERE cm.roomId = :roomId AND cm.content LIKE %:keyword% ORDER BY cm.sentAt DESC")
    List<ChatMessage> searchMessageByContent(@Param("roomId") Long roomId, @Param("keyword") String keyword);
    // 메시지 내용으로 검색

    @Query("SELECT cm FROM ChatMessage  cm WHERE cm.roomId = :roomId ORDER BY cm.sentAt DESC")
    List<ChatMessage> findLatestMessagesByRoomId(@Param("roomId") String roomId, Pageable pageable);
    //특정 방의 최근 메시지 1개



}
