package com.example.KDT_bank_server_project2.manager.RabbitMQ;

import com.example.KDT_bank_server_project2.manager.DTO.ChatMessageDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RabbitChatConsumer {

    private final SimpMessagingTemplate messagingTemplate;
    private final ObjectMapper objectMapper;

    // RabbitMQ에서 메시지 수신하여 WebSocket으로 브로드캐스트
    @RabbitListener(queues = RabbitMQConfig.CHAT_QUEUE)
    public void consumeMessage(String messageJson) {
        try {
            // JSON을 DTO로 변환
            ChatMessageDto dto = objectMapper.readValue(messageJson, ChatMessageDto.class);

            // 해당 방을 구독한 모든 클라이언트에게 브로드캐스트
            messagingTemplate.convertAndSend(
                    "/topic/room." + dto.getRoomId(),
                    dto
            );

            System.out.println("메시지 브로드캐스트 완료: " + dto.getContent());

        } catch (Exception e) {
            System.err.println("메시지 소비 실패: " + e.getMessage());
        }
    }
}
