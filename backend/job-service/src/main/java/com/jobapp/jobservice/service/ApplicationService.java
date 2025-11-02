package com.jobapp.jobservice.service;

import com.jobapp.common.exception.BadRequestException;
import com.jobapp.common.exception.ResourceNotFoundException;
import com.jobapp.jobservice.dto.ApplicationDTO;
import com.jobapp.jobservice.model.Application;
import com.jobapp.jobservice.model.Job;
import com.jobapp.jobservice.repository.ApplicationRepository;
import com.jobapp.jobservice.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final JobRepository jobRepository;

    @Transactional
    public ApplicationDTO applyForJob(ApplicationDTO applicationDTO) {
        // Check if job exists and is active
        Job job = jobRepository.findById(applicationDTO.getJobId())
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));

        if (job.getStatus() != Job.JobStatus.ACTIVE) {
            throw new BadRequestException("This job is no longer accepting applications");
        }

        // Check if already applied
        if (applicationRepository.existsByJobIdAndCandidateId(
                applicationDTO.getJobId(), applicationDTO.getCandidateId())) {
            throw new BadRequestException("You have already applied for this job");
        }

        Application application = mapToEntity(applicationDTO);
        application.setStatus(Application.ApplicationStatus.SUBMITTED);
        application = applicationRepository.save(application);

        // Update job application count
        job.setApplicationCount(job.getApplicationCount() + 1);
        jobRepository.save(job);

        return mapToDTO(application);
    }

    @Transactional(readOnly = true)
    public ApplicationDTO getApplicationById(Long applicationId) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));
        return mapToDTO(application);
    }

    @Transactional(readOnly = true)
    public Page<ApplicationDTO> getApplicationsByCandidate(Long candidateId, Pageable pageable) {
        return applicationRepository.findByCandidateId(candidateId, pageable)
                .map(this::mapToDTO);
    }

    @Transactional(readOnly = true)
    public Page<ApplicationDTO> getApplicationsByJob(Long jobId, Pageable pageable) {
        return applicationRepository.findByJobId(jobId, pageable)
                .map(this::mapToDTO);
    }

    @Transactional
    public ApplicationDTO updateApplicationStatus(Long applicationId, Application.ApplicationStatus status, String notes) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));

        application.setStatus(status);
        if (notes != null) {
            application.setRecruiterNotes(notes);
        }
        
        application = applicationRepository.save(application);
        return mapToDTO(application);
    }

    @Transactional
    public void withdrawApplication(Long applicationId, Long candidateId) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));

        if (!application.getCandidateId().equals(candidateId)) {
            throw new BadRequestException("You can only withdraw your own applications");
        }

        application.setStatus(Application.ApplicationStatus.WITHDRAWN);
        applicationRepository.save(application);
    }

    private ApplicationDTO mapToDTO(Application application) {
        return ApplicationDTO.builder()
                .id(application.getId())
                .jobId(application.getJobId())
                .candidateId(application.getCandidateId())
                .coverLetter(application.getCoverLetter())
                .resumeUrl(application.getResumeUrl())
                .status(application.getStatus())
                .matchScore(application.getMatchScore())
                .recruiterNotes(application.getRecruiterNotes())
                .reviewedAt(application.getReviewedAt())
                .reviewedBy(application.getReviewedBy())
                .appliedAt(application.getAppliedAt())
                .updatedAt(application.getUpdatedAt())
                .build();
    }

    private Application mapToEntity(ApplicationDTO dto) {
        return Application.builder()
                .jobId(dto.getJobId())
                .candidateId(dto.getCandidateId())
                .coverLetter(dto.getCoverLetter())
                .resumeUrl(dto.getResumeUrl())
                .matchScore(dto.getMatchScore())
                .build();
    }
}

