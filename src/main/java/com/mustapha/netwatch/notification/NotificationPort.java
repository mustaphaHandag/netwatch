package com.mustapha.netwatch.notification;

import com.mustapha.netwatch.event.ServiceStatusChangedEvent;

/**
 * A channel that can be alerted when a monitored service's status changes.
 * Adding a new channel (Slack, SMS, PagerDuty...) means implementing this
 * interface and nothing else — the dispatcher picks up every bean of this
 * type automatically.
 */
public interface NotificationPort {

    void notify(ServiceStatusChangedEvent event);
}
