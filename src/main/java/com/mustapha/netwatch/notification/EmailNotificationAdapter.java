package com.mustapha.netwatch.notification;

import com.mustapha.netwatch.config.NotificationProperties;
import com.mustapha.netwatch.event.ServiceStatusChangedEvent;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(prefix = "netwatch.notifications.email", name = "enabled", havingValue = "true")
public class EmailNotificationAdapter implements NotificationPort {

    private final JavaMailSender mailSender;
    private final NotificationProperties properties;

    public EmailNotificationAdapter(JavaMailSender mailSender, NotificationProperties properties) {
        this.mailSender = mailSender;
        this.properties = properties;
    }

    @Override
    public void notify(ServiceStatusChangedEvent event) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(properties.getEmail().getTo());
        message.setSubject("[NetWatch] %s is now %s".formatted(event.serviceName(), event.newStatus()));
        message.setText("""
                Service '%s' changed from %s to %s at %s.
                Response time: %s ms
                Error: %s
                """.formatted(
                event.serviceName(),
                event.previousStatus(),
                event.newStatus(),
                event.occurredAt(),
                event.responseTimeMs(),
                event.errorMessage() != null ? event.errorMessage() : "n/a"
        ));
        mailSender.send(message);
    }
}
