package com.mustapha.netwatch.event;

import com.mustapha.netwatch.model.MonitoredService;

import java.time.Instant;

/**
 * Published whenever a monitored service's status flips (UP/DOWN/UNKNOWN).
 * Decouples the check execution from anything reacting to it (WebSocket
 * broadcast, email/webhook alerting) so new reactions can be added as new
 * listeners without touching the scheduler or the check logic.
 */
public record ServiceStatusChangedEvent(
        Long serviceId,
        String serviceName,
        MonitoredService.Status previousStatus,
        MonitoredService.Status newStatus,
        Long responseTimeMs,
        String errorMessage,
        Instant occurredAt
) {
}
