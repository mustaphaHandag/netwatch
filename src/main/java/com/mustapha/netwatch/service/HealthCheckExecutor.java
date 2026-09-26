package com.mustapha.netwatch.service;

import com.mustapha.netwatch.event.ServiceStatusChangedEvent;
import com.mustapha.netwatch.model.Check;
import com.mustapha.netwatch.model.MonitoredService;
import com.mustapha.netwatch.repository.CheckRepository;
import com.mustapha.netwatch.repository.ServiceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;

/**
 * Executes a single health check for a single service. Reloads the service
 * by id on every run (rather than closing over a stale entity) so an
 * in-flight scheduled task always sees the latest URL/active flag.
 */
@Component
public class HealthCheckExecutor {

    private static final Logger log = LoggerFactory.getLogger(HealthCheckExecutor.class);

    private final ServiceRepository serviceRepository;
    private final CheckRepository checkRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final RestTemplate restTemplate;

    public HealthCheckExecutor(ServiceRepository serviceRepository,
                                CheckRepository checkRepository,
                                ApplicationEventPublisher eventPublisher,
                                RestTemplate restTemplate) {
        this.serviceRepository = serviceRepository;
        this.checkRepository = checkRepository;
        this.eventPublisher = eventPublisher;
        this.restTemplate = restTemplate;
    }

    public void check(Long serviceId) {
        serviceRepository.findById(serviceId)
                .filter(MonitoredService::isActive)
                .ifPresent(this::performCheck);
    }

    private void performCheck(MonitoredService service) {
        long start = System.currentTimeMillis();
        MonitoredService.Status status;
        String errorMessage = null;

        try {
            restTemplate.getForEntity(service.getUrl(), String.class);
            status = MonitoredService.Status.UP;
        } catch (RestClientException e) {
            status = MonitoredService.Status.DOWN;
            errorMessage = e.getMessage();
        }

        long responseTime = System.currentTimeMillis() - start;
        MonitoredService.Status previousStatus = service.getLastStatus();
        boolean statusChanged = previousStatus != status;

        checkRepository.save(new Check(service, status, responseTime, errorMessage));

        service.setLastStatus(status);
        serviceRepository.save(service);

        if (statusChanged) {
            log.info("Service {} switched from {} to {}", service.getName(), previousStatus, status);
            eventPublisher.publishEvent(new ServiceStatusChangedEvent(
                    service.getId(), service.getName(), previousStatus, status,
                    responseTime, errorMessage, Instant.now()
            ));
        }
    }
}
