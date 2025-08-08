package com.example.KDT_bank_server_project2.manager.RabbitMQ;

import com.example.KDT_bank_server_project2.manager.Service.ChatRoomService;
import com.example.KDT_bank_server_project2.manager.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final UserService userService;
    private final ChatRoomService chatRoomService;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        System.out.println("새로운 WebSocket 연결: " + event.getMessage());
    }

    @EventListener
    public void handleWebsocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        String userId = (String) headerAccessor.getSessionAttributes().get("userId");

        if (userId != null) {
            System.out.println("사용자 연결 해제: " + userId);
            userService.logoutUser(userId);
        }
    }

}
