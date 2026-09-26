package com.mustapha.netwatch.controller;

import com.mustapha.netwatch.dto.CheckResponse;
import com.mustapha.netwatch.dto.ServiceRequest;
import com.mustapha.netwatch.dto.ServiceResponse;
import com.mustapha.netwatch.mapper.ServiceMapper;
import com.mustapha.netwatch.service.ServiceManagementService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
public class ServiceController {

    private final ServiceManagementService serviceManagementService;

    public ServiceController(ServiceManagementService serviceManagementService) {
        this.serviceManagementService = serviceManagementService;
    }

    @GetMapping
    public List<ServiceResponse> listServices() {
        return serviceManagementService.findAll().stream()
                .map(ServiceMapper::toResponse)
                .toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ServiceResponse createService(@Valid @RequestBody ServiceRequest request) {
        return ServiceMapper.toResponse(serviceManagementService.create(request));
    }

    @GetMapping("/{id}")
    public ServiceResponse getService(@PathVariable Long id) {
        return ServiceMapper.toResponse(serviceManagementService.findByIdOrThrow(id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteService(@PathVariable Long id) {
        serviceManagementService.delete(id);
    }

    @GetMapping("/{id}/checks")
    public List<CheckResponse> getChecks(@PathVariable Long id) {
        return serviceManagementService.getRecentChecks(id).stream()
                .map(ServiceMapper::toResponse)
                .toList();
    }
}
