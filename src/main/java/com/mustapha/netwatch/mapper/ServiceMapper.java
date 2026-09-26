package com.mustapha.netwatch.mapper;

import com.mustapha.netwatch.dto.CheckResponse;
import com.mustapha.netwatch.dto.ServiceResponse;
import com.mustapha.netwatch.model.Check;
import com.mustapha.netwatch.model.MonitoredService;

public final class ServiceMapper {

    private ServiceMapper() {
    }

    public static ServiceResponse toResponse(MonitoredService service) {
        return new ServiceResponse(
                service.getId(),
                service.getName(),
                service.getUrl(),
                service.getCheckIntervalSeconds(),
                service.isActive(),
                service.getLastStatus()
        );
    }

    public static CheckResponse toResponse(Check check) {
        return new CheckResponse(
                check.getId(),
                check.getTimestamp(),
                check.getStatus(),
                check.getResponseTimeMs(),
                check.getErrorMessage()
        );
    }
}
