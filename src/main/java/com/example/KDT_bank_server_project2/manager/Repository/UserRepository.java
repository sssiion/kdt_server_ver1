package com.example.KDT_bank_server_project2.manager.Repository;
//repository 인터페이스 만드는 이유 : 데베 접근을 위한 통로
//repository 역할 :기본 CRUD 자동 제공, 커스텀 메서드 자동 구현 ,SQL 자동 생성
//없는 경우 manager, transaction 객체 생성해서 begin, persist, commit, close를 여러줄 작성
//있는 경우 userRepository.save(user) 한줄로 해결

//entity 데이터 구조 정의 > repository 데이터 접근방법 정의 > service 비즈니스 로직 구현 > controller 외부 요청 처리
//entity : 어떤 데이터를 저장?
//repository : 그 데이터를 어떻게 저장/조회할지 정의
//Service : 비즈니스 로직(회원가입, 로그인 등) 을 repository를 사용해서 구현

import com.example.KDT_bank_server_project2.manager.Entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // 기본 메서드들 JpaRepository가 자동 제공
    // save, findById, findAll, delete

    // 커스텀 메서드 : JPA가 메서드명을 보고 자동으로 SQL 생성


    // 로그인 회원가입용
    Optional<User> findByUserId(String userId);
    // 사용자 Id로 찾기
    boolean existsByUserId(String userId);
    //사용자 Id 존재 여부 확인



    List<User> findByUserIdContaining(String keyword);
    // 특젇 사용자명 패턴으로 검색

    Optional<User> findByUserPhone(String userPhone);
    //전화번호로 찾기


    List<User> findByIsOnlineTrue();
    // 온라인 사용자 목록 조회

    //Optional<User> findByIdAndRooms_Id(String roomId);




    //온라인 상태 업데이트 (커스텀 쿼리)
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.isOnline = :isOnline WHERE u.userId = :userId")
    void updateOnlineStatus(@Param("userId") String userId, @Param("isOnline") boolean isOnline);

    //사용자 ID와 비밀번호로 인증
    Optional<User> findByUserIdAndPassword(String userid, String password);

    //방 참가자 조회
    @Query("SELECT u FROM User u JOIN u.joinedRooms jr WHERE KEY(jr) = :roomId")
    List<User> isUserInRoom(@Param("roomId") String roomId);





}
