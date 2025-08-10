package com.example.KDT_bank_server_project2.manager.Service;

import com.example.KDT_bank_server_project2.manager.DTO.UserDataDto;
import com.example.KDT_bank_server_project2.manager.DTO.UserResponseDto;
import com.example.KDT_bank_server_project2.manager.Entity.User;
import com.example.KDT_bank_server_project2.manager.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.usertype.UserType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public List<UserDataDto> getUserData(){
        List<User> user = userRepository.findAll();
        System.out.println(user+"사람들 모두 가져오기");
        List<UserDataDto> userDataDtos = new ArrayList<>();
        for(User u : user){
            userDataDtos.add(new UserDataDto(u));
        }
        return  userDataDtos;
    }

    public User PwChange(User user, String password){
        user.setPassword(password);
        return userRepository.save(user);
    }
    public User dataChange(User user, UserResponseDto dto){
        user.setUserName(dto.getUserName());
        user.setUserPhone(dto.getUserPhone());
        return userRepository.save(user);
    }
    public User TypeChange(User user, String type){
        user.setUser_authority(User.UserType.valueOf(type));
        return user;
    }

    public User register(String name, String userPhone){

        User user = new User( name, userPhone);
        return userRepository.save(user);
    } // 유저 생성
    public User login(String userId, String password){

        User user =  userRepository.findByUserIdAndPassword(userId, password)
                .orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호가 다릅니다"));
        user.setOnline();
        return user;
    }// 유저 로그인.
    public void logoutUser(String userId){
        userRepository.updateOnlineStatus(userId, false);

    } // 유저 로그아웃

    @Transactional(readOnly = true)  // 데이터 베이스 트랜잭션을 자동으로 관리해주는 spring (트랜잭션 : 모든 작업이 성공, 모든 작업 실패 단위)
    public User findByUserId(String userId){
        return userRepository.findByUserId(userId)
                .orElseThrow(()-> new IllegalArgumentException("사용자를 찾을 수 없습니다" + userId));
    } // 사용자 Id 로 찾기 >> 사용자 검색
    // orElesThrow : optional 객체에서 사용하는 메서드로 값이 잇으면 그값 반환, 없으면 예외를 던져라


    // 모든 유저 리턴
    @Transactional(readOnly = true)
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public List<User> findByUserIdContaining(String keyword) {
        return userRepository.findByUserIdContaining(keyword);
    }

    //사용자가 참가한 방 목록 조회
    public Map<String, String> getUserByJoinedRooms(String userId){
        User user = userRepository.findByUserId(userId) //발신자 조회
                .orElseThrow(()-> new RuntimeException("발신자 찾을 수 없음"));
        System.out.println("사용자 ID"+user.getUserId());

        Map<String, String> roomdata= user.getJoinedRooms();
        // ✅ 강제 초기화 후 안전한 출력
        if (roomdata != null) {
            roomdata.size(); // 컬렉션 강제 초기화
            System.out.println("참가한 방 개수: " + roomdata.size());
            System.out.println("방 ID들: " + new ArrayList<>(roomdata.keySet()));
            System.out.println("전체 방 정보: " + roomdata);
        }
        System.out.println(roomdata.keySet()+ "getjoinRooms");
        if(!roomdata.isEmpty()){
            return new HashMap<>(roomdata);
        }
        System.out.println("룸데이터 없음");
        return Map.of();
    }

    @Transactional(readOnly = true)
    public List<String> getUserType(){
        User user = new User();
        return user.getAllUserTypes();
    }
}
