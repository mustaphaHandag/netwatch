package com.mustapha.netwatch.service;

import com.mustapha.netwatch.dto.ServiceRequest;
import com.mustapha.netwatch.exception.ResourceNotFoundException;
import com.mustapha.netwatch.model.Check;
import com.mustapha.netwatch.model.MonitoredService;
import com.mustapha.netwatch.repository.CheckRepository;
import com.mustapha.netwatch.repository.ServiceRepository;
import com.mustapha.netwatch.scheduling.PerServiceScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Application service for the monitored-service aggregate: owns the CRUD
 * business logic that used to live in the controller, and keeps the
 * scheduler in sync whenever a service is created or removed.
 */
@Service
public class ServiceManagementService {

    private final ServiceRepository serviceRepository;
    private final CheckRepository checkRepository;
    private final PerServiceScheduler scheduler;

    public ServiceManagementService(ServiceRepository serviceRepository,
                                     CheckRepository checkRepository,
                                     PerServiceScheduler scheduler) {
        this.serviceRepository = serviceRepository;
        this.checkRepository = checkRepository;
        this.scheduler = scheduler;
    }

    @Transactional
    public MonitoredService create(ServiceRequest request) {
        MonitoredService service = new MonitoredService(
                request.getName(), request.getUrl(), request.getCheckIntervalSeconds()
        );
        MonitoredService saved = serviceRepository.save(service);
        scheduler.schedule(saved);
        return saved;
    }

    @Transactional(readOnly = true)
    public List<MonitoredService> findAll() {
        return serviceRepository.findAll();
    }

    @Transactional(readOnly = true)
    public MonitoredService findByIdOrThrow(Long id) {
        return serviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service %d not found".formatted(id)));
    }

    @Transactional
    public MonitoredService update(Long id, ServiceRequest request) {
        MonitoredService service = findByIdOrThrow(id);
        service.setName(request.getName());
        service.setUrl(request.getUrl());
        service.setCheckIntervalSeconds(request.getCheckIntervalSeconds());
        MonitoredService saved = serviceRepository.save(service);
        if (saved.isActive()) {
            scheduler.schedule(saved);
        }
        return saved;
    }

    @Transactional
    public void delete(Long id) {
        MonitoredService service = findByIdOrThrow(id);
        scheduler.cancel(service.getId());
        serviceRepository.delete(service);
    }

    @Transactional(readOnly = true)
    public List<Check> getRecentChecks(Long serviceId) {
        findByIdOrThrow(serviceId);
        return checkRepository.findTop50ByServiceIdOrderByTimestampDesc(serviceId);
    }
}
