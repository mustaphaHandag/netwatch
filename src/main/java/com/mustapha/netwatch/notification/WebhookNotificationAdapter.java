package com.mustapha.netwatch.notification;

import com.mustapha.netwatch.config.NotificationProperties;
import com.mustapha.netwatch.event.ServiceStatusChangedEvent;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@ConditionalOnProperty(prefix = "netwatch.notifications.webhook", name = "enabled", havingValue = "true")
public class WebhookNotificationAdapter implements NotificationPort {

    private final RestTemplate restTemplate;
    private final NotificationProperties properties;

    public WebhookNotificationAdapter(RestTemplate restTemplate, NotificationProperties properties) {
        this.restTemplate = restTemplate;
        this.properties = properties;
    }

    @Override
    public void notify(ServiceStatusChangedEvent event) {
        restTemplate.postForEntity(properties.getWebhook().getUrl(), event, Void.class);
    }
}
