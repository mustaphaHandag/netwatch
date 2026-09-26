package com.mustapha.netwatch.realtime;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mustapha.netwatch.event.ServiceStatusChangedEvent;
import com.mustapha.netwatch.model.MonitoredService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Handles WebSocket sessions and broadcasts service status changes to every
 * connected client in real time. Reacts to ServiceStatusChangedEvent rather
 * than being called directly, so it's just one more consumer alongside
 * email/webhook alerting (see notification package).
 */
@Component
public class StatusBroadcaster extends TextWebSocketHandler {

    private static final Logger log = LoggerFactory.getLogger(StatusBroadcaster.class);

    private final Set<WebSocketSession> sessions = new CopyOnWriteArraySet<>();
    private final ObjectMapper objectMapper;

    public StatusBroadcaster(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session);
    }

    @EventListener
    public void onStatusChanged(ServiceStatusChangedEvent event) {
        String payload;
        try {
            payload = objectMapper.writeValueAsString(
                    new StatusMessage(event.serviceName(), event.newStatus(), event.responseTimeMs())
            );
        } catch (JsonProcessingException e) {
            log.warn("Failed to serialize status message for {}: {}", event.serviceName(), e.getMessage());
            return;
        }
        broadcast(payload);
    }

    private void broadcast(String payload) {
        TextMessage message = new TextMessage(payload);
        for (WebSocketSession session : sessions) {
            try {
                if (session.isOpen()) {
                    session.sendMessage(message);
                }
            } catch (IOException e) {
                log.debug("Dropping closed WebSocket session: {}", e.getMessage());
            }
        }
    }

    private record StatusMessage(String service, MonitoredService.Status status, Long responseTimeMs) {
    }
}
