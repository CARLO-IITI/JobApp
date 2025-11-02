package com.jobapp.jobservice.controller;

import com.jobapp.common.dto.ApiResponse;
import com.jobapp.jobservice.dto.JobDTO;
import com.jobapp.jobservice.service.JobService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    @PostMapping
    public ResponseEntity<ApiResponse<JobDTO>> createJob(@Valid @RequestBody JobDTO jobDTO) {
        JobDTO created = jobService.createJob(jobDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Job posted successfully", created));
    }

    @GetMapping("/{jobId}")
    public ResponseEntity<ApiResponse<JobDTO>> getJobById(@PathVariable Long jobId) {
        JobDTO job = jobService.getJobById(jobId);
        return ResponseEntity.ok(ApiResponse.success(job));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<JobDTO>>> getAllJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("ASC") ? 
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<JobDTO> jobs = jobService.getAllActiveJobs(pageable);
        return ResponseEntity.ok(ApiResponse.success(jobs));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<JobDTO>>> searchJobs(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<JobDTO> jobs = jobService.searchJobs(keyword, pageable);
        return ResponseEntity.ok(ApiResponse.success(jobs));
    }

    @GetMapping("/location/{location}")
    public ResponseEntity<ApiResponse<Page<JobDTO>>> getJobsByLocation(
            @PathVariable String location,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<JobDTO> jobs = jobService.getJobsByLocation(location, pageable);
        return ResponseEntity.ok(ApiResponse.success(jobs));
    }

    @GetMapping("/recruiter/{recruiterId}")
    public ResponseEntity<ApiResponse<Page<JobDTO>>> getJobsByRecruiter(
            @PathVariable Long recruiterId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<JobDTO> jobs = jobService.getJobsByRecruiter(recruiterId, pageable);
        return ResponseEntity.ok(ApiResponse.success(jobs));
    }

    @GetMapping("/latest")
    public ResponseEntity<ApiResponse<List<JobDTO>>> getLatestJobs() {
        List<JobDTO> jobs = jobService.getLatestJobs();
        return ResponseEntity.ok(ApiResponse.success(jobs));
    }

    @PutMapping("/{jobId}")
    public ResponseEntity<ApiResponse<JobDTO>> updateJob(
            @PathVariable Long jobId,
            @RequestBody JobDTO jobDTO) {
        JobDTO updated = jobService.updateJob(jobId, jobDTO);
        return ResponseEntity.ok(ApiResponse.success("Job updated successfully", updated));
    }

    @DeleteMapping("/{jobId}")
    public ResponseEntity<ApiResponse<Void>> deleteJob(@PathVariable Long jobId) {
        jobService.deleteJob(jobId);
        return ResponseEntity.ok(ApiResponse.success("Job closed successfully", null));
    }
}

