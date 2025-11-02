package com.jobapp.jobservice.controller;

import com.jobapp.common.dto.ApiResponse;
import com.jobapp.jobservice.dto.ApplicationDTO;
import com.jobapp.jobservice.model.Application;
import com.jobapp.jobservice.service.ApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    @PostMapping
    public ResponseEntity<ApiResponse<ApplicationDTO>> applyForJob(@Valid @RequestBody ApplicationDTO applicationDTO) {
        ApplicationDTO created = applicationService.applyForJob(applicationDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Application submitted successfully", created));
    }

    @GetMapping("/{applicationId}")
    public ResponseEntity<ApiResponse<ApplicationDTO>> getApplicationById(@PathVariable Long applicationId) {
        ApplicationDTO application = applicationService.getApplicationById(applicationId);
        return ResponseEntity.ok(ApiResponse.success(application));
    }

    @GetMapping("/candidate/{candidateId}")
    public ResponseEntity<ApiResponse<Page<ApplicationDTO>>> getApplicationsByCandidate(
            @PathVariable Long candidateId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<ApplicationDTO> applications = applicationService.getApplicationsByCandidate(candidateId, pageable);
        return ResponseEntity.ok(ApiResponse.success(applications));
    }

    @GetMapping("/job/{jobId}")
    public ResponseEntity<ApiResponse<Page<ApplicationDTO>>> getApplicationsByJob(
            @PathVariable Long jobId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<ApplicationDTO> applications = applicationService.getApplicationsByJob(jobId, pageable);
        return ResponseEntity.ok(ApiResponse.success(applications));
    }

    @PutMapping("/{applicationId}/status")
    public ResponseEntity<ApiResponse<ApplicationDTO>> updateApplicationStatus(
            @PathVariable Long applicationId,
            @RequestParam Application.ApplicationStatus status,
            @RequestParam(required = false) String notes) {
        
        ApplicationDTO updated = applicationService.updateApplicationStatus(applicationId, status, notes);
        return ResponseEntity.ok(ApiResponse.success("Application status updated", updated));
    }

    @DeleteMapping("/{applicationId}/withdraw")
    public ResponseEntity<ApiResponse<Void>> withdrawApplication(
            @PathVariable Long applicationId,
            @RequestParam Long candidateId) {
        
        applicationService.withdrawApplication(applicationId, candidateId);
        return ResponseEntity.ok(ApiResponse.success("Application withdrawn successfully", null));
    }
}

