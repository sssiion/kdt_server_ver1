package com.example.KDT_bank_server_project2.manager.Repository;


import com.example.KDT_bank_server_project2.manager.Entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    Optional<ChatRoom> findByRoomId(Long roomId);
    // 룸 UUID로 채팅방 찾기

    boolean existsByRoomId(Long roomId);
    //룸 UUID 존재 여부 확인

    List<ChatRoom> findByRoomNameContaining(String keyword);
    //방 이름으로 검색 -- 부분 일치

    //방이름으로 찾기 -- 정확히 일치
    Optional<ChatRoom> findByRoomName(String roomName);

    /* 최신 생성순 전체 방 목록 */
    List<ChatRoom> findAllByOrderByCreatedAtDesc();




    /* 특정 사용자가 참가한 방 목록 (user_joined_rooms 테이블 활용) */
    @Query("SELECT r FROM ChatRoom r WHERE r.roomId IN :roomIds ORDER BY r.createdAt DESC")
    List<ChatRoom> findRoomsByUserId(@Param("roomId") Set<Long> roomIds);

}
