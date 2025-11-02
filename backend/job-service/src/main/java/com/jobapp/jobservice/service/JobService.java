package com.jobapp.jobservice.service;

import com.jobapp.common.exception.ResourceNotFoundException;
import com.jobapp.jobservice.dto.JobDTO;
import com.jobapp.jobservice.model.Job;
import com.jobapp.jobservice.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;

    @Transactional
    public JobDTO createJob(JobDTO jobDTO) {
        Job job = mapToEntity(jobDTO);
        job.setStatus(Job.JobStatus.ACTIVE);
        job.setViewCount(0);
        job.setApplicationCount(0);
        job = jobRepository.save(job);
        return mapToDTO(job);
    }

    @Transactional(readOnly = true)
    public JobDTO getJobById(Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));
        
        // Increment view count
        job.setViewCount(job.getViewCount() + 1);
        jobRepository.save(job);
        
        return mapToDTO(job);
    }

    @Transactional(readOnly = true)
    public Page<JobDTO> getAllActiveJobs(Pageable pageable) {
        return jobRepository.findByStatus(Job.JobStatus.ACTIVE, pageable)
                .map(this::mapToDTO);
    }

    @Transactional(readOnly = true)
    public Page<JobDTO> getJobsByRecruiter(Long recruiterId, Pageable pageable) {
        return jobRepository.findByPostedBy(recruiterId, pageable)
                .map(this::mapToDTO);
    }

    @Transactional(readOnly = true)
    public Page<JobDTO> searchJobs(String keyword, Pageable pageable) {
        return jobRepository.searchByKeyword(keyword, pageable)
                .map(this::mapToDTO);
    }

    @Transactional(readOnly = true)
    public Page<JobDTO> getJobsByLocation(String location, Pageable pageable) {
        return jobRepository.findByLocation(location, pageable)
                .map(this::mapToDTO);
    }

    @Transactional
    public JobDTO updateJob(Long jobId, JobDTO jobDTO) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));

        // Update fields
        if (jobDTO.getTitle() != null) job.setTitle(jobDTO.getTitle());
        if (jobDTO.getDescription() != null) job.setDescription(jobDTO.getDescription());
        if (jobDTO.getRequiredSkills() != null) job.setRequiredSkills(jobDTO.getRequiredSkills());
        if (jobDTO.getLocation() != null) job.setLocation(jobDTO.getLocation());
        if (jobDTO.getJobType() != null) job.setJobType(jobDTO.getJobType());
        if (jobDTO.getExperienceLevel() != null) job.setExperienceLevel(jobDTO.getExperienceLevel());
        if (jobDTO.getMinExperience() != null) job.setMinExperience(jobDTO.getMinExperience());
        if (jobDTO.getMaxExperience() != null) job.setMaxExperience(jobDTO.getMaxExperience());
        if (jobDTO.getMinSalary() != null) job.setMinSalary(jobDTO.getMinSalary());
        if (jobDTO.getMaxSalary() != null) job.setMaxSalary(jobDTO.getMaxSalary());
        if (jobDTO.getRemoteAllowed() != null) job.setRemoteAllowed(jobDTO.getRemoteAllowed());
        if (jobDTO.getOpenings() != null) job.setOpenings(jobDTO.getOpenings());
        if (jobDTO.getStatus() != null) job.setStatus(jobDTO.getStatus());

        job = jobRepository.save(job);
        return mapToDTO(job);
    }

    @Transactional
    public void deleteJob(Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));
        job.setStatus(Job.JobStatus.CLOSED);
        jobRepository.save(job);
    }

    @Transactional(readOnly = true)
    public List<JobDTO> getLatestJobs() {
        return jobRepository.findTop10ByStatusOrderByCreatedAtDesc(Job.JobStatus.ACTIVE)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private JobDTO mapToDTO(Job job) {
        return JobDTO.builder()
                .id(job.getId())
                .title(job.getTitle())
                .description(job.getDescription())
                .companyId(job.getCompanyId())
                .companyName(job.getCompanyName())
                .companyDescription(job.getCompanyDescription())
                .requiredSkills(job.getRequiredSkills())
                .location(job.getLocation())
                .jobType(job.getJobType())
                .experienceLevel(job.getExperienceLevel())
                .minExperience(job.getMinExperience())
                .maxExperience(job.getMaxExperience())
                .minSalary(job.getMinSalary())
                .maxSalary(job.getMaxSalary())
                .currency(job.getCurrency())
                .remoteAllowed(job.getRemoteAllowed())
                .openings(job.getOpenings())
                .responsibilities(job.getResponsibilities())
                .qualifications(job.getQualifications())
                .benefits(job.getBenefits())
                .applicationDeadline(job.getApplicationDeadline())
                .status(job.getStatus())
                .postedBy(job.getPostedBy())
                .viewCount(job.getViewCount())
                .applicationCount(job.getApplicationCount())
                .createdAt(job.getCreatedAt())
                .updatedAt(job.getUpdatedAt())
                .build();
    }

    private Job mapToEntity(JobDTO dto) {
        return Job.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .companyId(dto.getCompanyId())
                .companyName(dto.getCompanyName())
                .companyDescription(dto.getCompanyDescription())
                .requiredSkills(dto.getRequiredSkills() != null ? dto.getRequiredSkills() : new java.util.ArrayList<>())
                .location(dto.getLocation())
                .jobType(dto.getJobType())
                .experienceLevel(dto.getExperienceLevel())
                .minExperience(dto.getMinExperience())
                .maxExperience(dto.getMaxExperience())
                .minSalary(dto.getMinSalary())
                .maxSalary(dto.getMaxSalary())
                .currency(dto.getCurrency() != null ? dto.getCurrency() : "USD")
                .remoteAllowed(dto.getRemoteAllowed() != null ? dto.getRemoteAllowed() : false)
                .openings(dto.getOpenings() != null ? dto.getOpenings() : 1)
                .responsibilities(dto.getResponsibilities())
                .qualifications(dto.getQualifications())
                .benefits(dto.getBenefits())
                .applicationDeadline(dto.getApplicationDeadline())
                .postedBy(dto.getPostedBy())
                .build();
    }
}

