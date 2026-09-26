package com.mustapha.netwatch.scheduling;

import com.mustapha.netwatch.model.MonitoredService;
import com.mustapha.netwatch.repository.ServiceRepository;
import com.mustapha.netwatch.service.HealthCheckExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

/**
 * Runs each monitored service on its own repeating task at its own
 * checkIntervalSeconds, instead of one global fixed-delay tick for every
 * service. A service created, updated or deleted at runtime is
 * (re)scheduled immediately via schedule()/cancel() — no restart needed.
 */
@Component
public class PerServiceScheduler {

    private static final Logger log = LoggerFactory.getLogger(PerServiceScheduler.class);

    private final TaskScheduler taskScheduler;
    private final ServiceRepository serviceRepository;
    private final HealthCheckExecutor healthCheckExecutor;
    private final Map<Long, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();

    public PerServiceScheduler(TaskScheduler taskScheduler,
                                ServiceRepository serviceRepository,
                                HealthCheckExecutor healthCheckExecutor) {
        this.taskScheduler = taskScheduler;
        this.serviceRepository = serviceRepository;
        this.healthCheckExecutor = healthCheckExecutor;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void scheduleAllActiveServices() {
        serviceRepository.findByActiveTrue().forEach(this::schedule);
        log.info("Scheduled {} active service(s) for health checking", scheduledTasks.size());
    }

    public void schedule(MonitoredService service) {
        cancel(service.getId());
        Duration interval = Duration.ofSeconds(service.getCheckIntervalSeconds());
        Long serviceId = service.getId();
        ScheduledFuture<?> future = taskScheduler.scheduleWithFixedDelay(
                () -> healthCheckExecutor.check(serviceId), interval);
        scheduledTasks.put(serviceId, future);
    }

    public void cancel(Long serviceId) {
        ScheduledFuture<?> existing = scheduledTasks.remove(serviceId);
        if (existing != null) {
            existing.cancel(false);
        }
    }
}
