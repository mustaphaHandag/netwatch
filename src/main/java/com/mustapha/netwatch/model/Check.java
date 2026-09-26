package com.mustapha.netwatch.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "checks")
public class Check {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "service_id", nullable = false)
    private MonitoredService service;

    @Column(nullable = false)
    private Instant timestamp = Instant.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MonitoredService.Status status;

    @Column(name = "response_time_ms")
    private Long responseTimeMs;

    @Column(name = "error_message")
    private String errorMessage;

    public Check() {
    }

    public Check(MonitoredService service, MonitoredService.Status status, Long responseTimeMs, String errorMessage) {
        this.service = service;
        this.status = status;
        this.responseTimeMs = responseTimeMs;
        this.errorMessage = errorMessage;
    }

    public Long getId() {
        return id;
    }

    public MonitoredService getService() {
        return service;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public MonitoredService.Status getStatus() {
        return status;
    }

    public Long getResponseTimeMs() {
        return responseTimeMs;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
