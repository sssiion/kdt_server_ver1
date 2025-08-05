package com.example.KDT_bank_server_project2.manager.Controller;


import com.example.KDT_bank_server_project2.manager.DTO.ApiResponse;
import com.example.KDT_bank_server_project2.manager.DTO.UserLoginDto;
import com.example.KDT_bank_server_project2.manager.DTO.UserRegisterDto;
import com.example.KDT_bank_server_project2.manager.DTO.UserResponseDto;
import com.example.KDT_bank_server_project2.manager.Entity.User;
import com.example.KDT_bank_server_project2.manager.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController // json/xml 데이터를 직접 반환하는데 주로 사용하면 rest API 개발 방식
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class userController {
    //@Autowired // spring이 자동으로 userservice 객체를 생성해서 주입
    // 객체 생성, 필요한 의존성 함께 주입, controller 자동 연결
    private final UserService userService;

    @PostMapping("/register")
    //REquestBody 클라이언트가 보낸 JSON 데이터를 java 객체로 자동 변환하는 어노테이션
    public ResponseEntity<ApiResponse<UserResponseDto>> registerUser(@RequestBody UserRegisterDto dto){
        try{
            User user = userService.register(
                    dto.getUserName(),
                    dto.getUserPhone()

            );
            UserResponseDto response = new UserResponseDto(user);
            return ResponseEntity.ok(ApiResponse.success("회원가입 성공", response));

        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(ApiResponse.failure(e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserResponseDto>> login(@RequestBody UserLoginDto dto){
        try{
            User user = userService.login(dto.getUserId(), dto.getPassword());
            UserResponseDto response = new UserResponseDto(user);
            return ResponseEntity.ok(ApiResponse.success("로그인 성공", response));
        }catch(IllegalArgumentException e){
            return ResponseEntity.badRequest()
                    .body(ApiResponse.failure(e.getMessage()));
        }
    }
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<UserResponseDto>> logout(@RequestBody String userId){
        try{
            userService.logoutUser(userId);
            return ResponseEntity.ok(ApiResponse.success("로그아웃 성공", null));
        }catch(Exception e){
            return ResponseEntity.badRequest()
                    .body(ApiResponse.failure("로그아웃 실패"));
        }

    }
    //사용자 정보 조회
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserResponseDto>> getUser(@PathVariable String userId){
        try{
            User user = userService.findByUserId(userId);
            UserResponseDto response = new UserResponseDto(user);
            return ResponseEntity.ok(ApiResponse.success("사용자 조회 성공", response));

        }catch(IllegalArgumentException e){
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<UserResponseDto>>> searchUsers(@RequestParam String keyword){
        List<User> users = userService.findByUserIdContaining(keyword);
        List<UserResponseDto> response = users.stream()
                .map(UserResponseDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("사용자 검색 성공", response));

    }
    @GetMapping("/{userId}/rooms")
    public ResponseEntity<ApiResponse<Map<String, String>>> getuserRooms(@PathVariable String userId){
        try{
            Map<String, String> room = userService.getUserJoinedRooms(userId);
            return ResponseEntity.ok(ApiResponse.success("참가한 방 목록 조회 성공", room));
        }catch(IllegalArgumentException e){
            return ResponseEntity.notFound().build();
        }
    }



}
