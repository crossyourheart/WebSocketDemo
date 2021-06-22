package com.example.websocketdemo;

/**
 * @author wang wei
 * @description
 * @create 2021/6/22 10:43
 */

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 *
 */
@EnableWebSocket //开启SpringBoot对WebSocket的支持
@Configuration //声明该类是一个配置类
public class ChatConfig {

    /**
     * 配置ServerEndpointExporter的bean
     * 该Bean会自动注册使用了@ServerEndpoint注解声明的Websocket endpoint
     * @return
     */
    @Bean
    public ServerEndpointExporter serverEndpoint() {
        return new ServerEndpointExporter();
    }
}