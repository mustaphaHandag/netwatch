package com.mustapha.netwatch.notification;

import com.mustapha.netwatch.event.ServiceStatusChangedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Fans a status-change event out to every configured NotificationPort
 * (empty list if none are enabled). Runs off the check thread so a slow
 * SMTP server or webhook endpoint never delays the next health check.
 */
@Component
public class NotificationDispatcher {

    private static final Logger log = LoggerFactory.getLogger(NotificationDispatcher.class);

    private final List<NotificationPort> notifiers;

    public NotificationDispatcher(List<NotificationPort> notifiers) {
        this.notifiers = notifiers;
    }

    @Async("notificationExecutor")
    @EventListener
    public void onStatusChanged(ServiceStatusChangedEvent event) {
        for (NotificationPort notifier : notifiers) {
            try {
                notifier.notify(event);
            } catch (Exception e) {
                log.warn("Notification adapter {} failed for service {}: {}",
                        notifier.getClass().getSimpleName(), event.serviceName(), e.getMessage());
            }
        }
    }
}
