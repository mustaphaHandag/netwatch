package com.mustapha.netwatch.config;

import com.mustapha.netwatch.realtime.StatusBroadcaster;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final StatusBroadcaster statusBroadcaster;

    public WebSocketConfig(StatusBroadcaster statusBroadcaster) {
        this.statusBroadcaster = statusBroadcaster;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(statusBroadcaster, "/ws/status")
                .setAllowedOrigins("*");
    }
}
