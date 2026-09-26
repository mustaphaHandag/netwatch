package com.mustapha.netwatch.repository;

import com.mustapha.netwatch.model.MonitoredService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceRepository extends JpaRepository<MonitoredService, Long> {
    List<MonitoredService> findByActiveTrue();
}
