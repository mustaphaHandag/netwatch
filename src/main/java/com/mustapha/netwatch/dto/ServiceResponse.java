package com.mustapha.netwatch.dto;

import com.mustapha.netwatch.model.MonitoredService;

public record ServiceResponse(
        Long id,
        String name,
        String url,
        int checkIntervalSeconds,
        boolean active,
        MonitoredService.Status lastStatus
) {
}
