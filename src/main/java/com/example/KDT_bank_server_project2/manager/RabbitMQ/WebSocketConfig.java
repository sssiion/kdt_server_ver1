package com.example.KDT_bank_server_project2.manager.RabbitMQ;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Value("${spring.rabbitmq.host}")
    private String rabbitHost;

    @Value("${spring.rabbitmq.stomp.port}")
    private int stompPort;

    @Value("${spring.rabbitmq.stomp.login}")
    private String stompLogin;

    @Value("${spring.rabbitmq.stomp.passcode}")
    private String stompPasscode;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config){
        //클라이언트에서 서버로 메시지 보낼때 prefix
        config.setApplicationDestinationPrefixes("/app");

        //RabbitMQ를 외부 브로커로 사용
        config.enableStompBrokerRelay("/topic","/queue")
                .setRelayHost(rabbitHost)
                .setRelayPort(stompPort)
                .setSystemLogin(stompLogin)
                .setSystemPasscode(stompPasscode)
                .setClientLogin(stompLogin)
                .setClientPasscode(stompPasscode);
        //개별 사용자 메시지 prefix
        config.setUserDestinationPrefix("/user");

    }
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOrigins("*")
                .withSockJS();
    }


}
