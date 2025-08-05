package com.example.KDT_bank_server_project2.manager.Controller;

import com.example.KDT_bank_server_project2.manager.DTO.ApiResponse;
import com.example.KDT_bank_server_project2.manager.DTO.ChatRoomResponseDto;
import com.example.KDT_bank_server_project2.manager.DTO.CreateRoomDto;
import com.example.KDT_bank_server_project2.manager.DTO.UserResponseDto;
import com.example.KDT_bank_server_project2.manager.Entity.ChatRoom;
import com.example.KDT_bank_server_project2.manager.Repository.ChatRoomRepository;
import com.example.KDT_bank_server_project2.manager.Service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/chatrooms")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @PostMapping //채팅방 생성
    public ResponseEntity<ApiResponse<ChatRoomResponseDto>> createChatRoom(@PathVariable  String roomName){
        try{

            ChatRoom room = chatRoomService.createRoom(roomName);
            ChatRoomResponseDto respone = new ChatRoomResponseDto(room);

            return ResponseEntity.ok(ApiResponse.success("채팅방 생성 성공", respone));
        }catch(Exception e){
            return ResponseEntity.badRequest().body(ApiResponse.failure("채팅방 생성 실패"+e.getMessage()));
        }
    }
    @GetMapping
    public ResponseEntity<ApiResponse<List<ChatRoomResponseDto>>> getAllRoom(){
        try{
            List<ChatRoom> room = chatRoomService.getAllRooms();
            List<ChatRoomResponseDto> respones = room.stream().map(room1 -> new ChatRoomResponseDto(room1)).collect(Collectors.toList());
            return ResponseEntity.ok(ApiResponse.success("채팅방 조회 성공 ", respones));
        }catch(IllegalArgumentException e){
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{roomId}") //특정 채팅방 조회
    public ResponseEntity<ApiResponse<ChatRoomResponseDto>> getRoom(@PathVariable  String roomId ){
        try{
            ChatRoom room = chatRoomService.findByroomId(roomId);
            ChatRoomResponseDto respone = new ChatRoomResponseDto(room);
            return ResponseEntity.ok(ApiResponse.success("채팅방 조회 성공 ", respone));
        }catch(IllegalArgumentException e){
            return ResponseEntity.notFound().build();
        }

    }
    @PostMapping("/{roomId}/join")
    public ResponseEntity<ApiResponse<String>> joinRoom(@PathVariable String roomId,  @RequestParam String userId){
        try{
            chatRoomService.joinRoom(roomId,userId);
            return ResponseEntity.ok(ApiResponse.success("방참가 성공", null));

        }catch(IllegalArgumentException e){
            return ResponseEntity.badRequest()
                    .body(ApiResponse.failure(e.getMessage()));
        }
    }
    @DeleteMapping("/{roomId}/leave")
    public ResponseEntity<ApiResponse<String>> leaveRoom(@PathVariable String roomId, @RequestParam String userId){
        try{
            chatRoomService.leaveRoom(roomId,userId);
            return ResponseEntity.ok(ApiResponse.success("방 나가기 성공", null));

        }catch(IllegalArgumentException e){
            return ResponseEntity.badRequest()
                    .body(ApiResponse.failure(e.getMessage()));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<ChatRoomResponseDto>>> searchRooms(@RequestParam String keyword){
        List<ChatRoom> room = chatRoomService.searchRooms(keyword);
        List<ChatRoomResponseDto> respones = room.stream()
                .map(ChatRoomResponseDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("채팅방 검색 성공",respones));
    }
}
