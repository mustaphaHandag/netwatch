package com.mustapha.netwatch.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

@Entity
@Table(name = "monitored_services")
public class MonitoredService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @NotBlank
    @URL
    @Column(nullable = false)
    private String url;

    @Column(name = "check_interval_seconds", nullable = false)
    private int checkIntervalSeconds = 60;

    @Column(nullable = false)
    private boolean active = true;

    @Enumerated(EnumType.STRING)
    @Column(name = "last_status")
    private Status lastStatus = Status.UNKNOWN;

    public enum Status {
        UP, DOWN, UNKNOWN
    }

    public MonitoredService() {
    }

    public MonitoredService(String name, String url, int checkIntervalSeconds) {
        this.name = name;
        this.url = url;
        this.checkIntervalSeconds = checkIntervalSeconds;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getCheckIntervalSeconds() {
        return checkIntervalSeconds;
    }

    public void setCheckIntervalSeconds(int checkIntervalSeconds) {
        this.checkIntervalSeconds = checkIntervalSeconds;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Status getLastStatus() {
        return lastStatus;
    }

    public void setLastStatus(Status lastStatus) {
        this.lastStatus = lastStatus;
    }
}
