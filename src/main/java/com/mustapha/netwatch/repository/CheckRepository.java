package com.mustapha.netwatch.repository;

import com.mustapha.netwatch.model.Check;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CheckRepository extends JpaRepository<Check, Long> {
    List<Check> findTop50ByServiceIdOrderByTimestampDesc(Long serviceId);
}
