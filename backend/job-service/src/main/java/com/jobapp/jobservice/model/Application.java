package com.jobapp.jobservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "applications", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"job_id", "candidate_id"}))
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Application {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "job_id")
    private Long jobId;

    @Column(nullable = false, name = "candidate_id")
    private Long candidateId;

    @Column(length = 2000)
    private String coverLetter;

    private String resumeUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicationStatus status = ApplicationStatus.SUBMITTED;

    private Double matchScore; // AI-generated match score

    @Column(length = 1000)
    private String recruiterNotes;

    private LocalDateTime reviewedAt;

    private Long reviewedBy; // Recruiter user ID

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime appliedAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public enum ApplicationStatus {
        SUBMITTED,
        REVIEWED,
        SHORTLISTED,
        INTERVIEW_SCHEDULED,
        REJECTED,
        ACCEPTED,
        WITHDRAWN
    }
}

