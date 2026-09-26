package com.mustapha.netwatch.dto;

import com.mustapha.netwatch.model.MonitoredService;

import java.time.Instant;

public record CheckResponse(
        Long id,
        Instant timestamp,
        MonitoredService.Status status,
        Long responseTimeMs,
        String errorMessage
) {
}
