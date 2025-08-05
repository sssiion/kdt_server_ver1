package com.example.KDT_bank_server_project2.manager.RabbitMQ;


import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;

@Configuration // 이 클래스는 Spring 설정 클래스 > bean을 만들어서 관리 부탁
@EnableRabbit // RabbitMQ 기능 활성화 > 관련 자동 설정켜는 스위치
public class RabbitMQConfig {
    public static final String CHAT_EXCHANGE = "chat.exchange";
    public static final String CHAT_QUEUE = "chat.queue";
    public static final String CHAT_ROUTING_KEY = "chat.room.*";

    @Bean
    public TopicExchange chatExchange(){ // 메시지 교환소 > chat.exchange라는 이름의 우체국 > 메시지를 받아서 적절한 큐에 분배
        return ExchangeBuilder.topicExchange(CHAT_EXCHANGE).durable(true).build();

    }
    @Bean
    public Queue chatQueue(){  // 우편함 > 큐 생성 > 서버 재시작해도 큐 사라지지 않음, 메시지 임시 저장 장소
        return QueueBuilder.durable(CHAT_QUEUE).build();

    }
    @Bean // 이 메서드가 반환하는 객체를 Spring 컨테이너에 등록해서 관리해줘
    public Binding chatBinding(){ // 큐를, 교환소에 연결, 이 키워드로 오는 메시지만 받기
        return BindingBuilder.bind(chatQueue()).to(chatExchange()).with(CHAT_ROUTING_KEY);
    }

    // JSON 변환기
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // RabbitTemplate 설정
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }



}
