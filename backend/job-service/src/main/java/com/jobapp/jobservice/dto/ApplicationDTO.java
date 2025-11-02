package com.jobapp.jobservice.dto;

import com.jobapp.jobservice.model.Application;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationDTO {
    
    private Long id;

    @NotNull(message = "Job ID is required")
    private Long jobId;

    @NotNull(message = "Candidate ID is required")
    private Long candidateId;

    private String coverLetter;
    private String resumeUrl;
    private Application.ApplicationStatus status;
    private Double matchScore;
    private String recruiterNotes;
    private LocalDateTime reviewedAt;
    private Long reviewedBy;
    private LocalDateTime appliedAt;
    private LocalDateTime updatedAt;
}

