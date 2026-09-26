package com.mustapha.netwatch.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * Dedicated thread pool for per-service health checks. Sized independently
 * from the notification executor so a slow email/webhook call can never
 * delay a check, and a slow check can never delay a notification.
 */
@Configuration
public class SchedulingConfig {

    @Bean
    public TaskScheduler taskScheduler(@Value("${netwatch.scheduler.pool-size:10}") int poolSize) {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(poolSize);
        scheduler.setThreadNamePrefix("netwatch-check-");
        scheduler.setRemoveOnCancelPolicy(true);
        scheduler.initialize();
        return scheduler;
    }
}
