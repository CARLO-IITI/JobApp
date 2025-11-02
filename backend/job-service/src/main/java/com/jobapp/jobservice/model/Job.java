package com.jobapp.jobservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "jobs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Job {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 10000, nullable = false)
    private String description;

    @Column(nullable = false)
    private Long companyId; // Reference to recruiter's company

    @Column(nullable = false)
    private String companyName;

    @Column(length = 2000)
    private String companyDescription;

    @ElementCollection
    @CollectionTable(name = "job_skills", joinColumns = @JoinColumn(name = "job_id"))
    @Column(name = "skill")
    private List<String> requiredSkills = new ArrayList<>();

    @Column(nullable = false)
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobType jobType; // FULL_TIME, PART_TIME, CONTRACT, INTERNSHIP

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExperienceLevel experienceLevel; // ENTRY, JUNIOR, MID, SENIOR, LEAD

    private Integer minExperience; // in years

    private Integer maxExperience; // in years

    private Double minSalary;

    private Double maxSalary;

    private String currency = "USD";

    private Boolean remoteAllowed = false;

    @Column(nullable = false)
    private Integer openings = 1;

    @Column(length = 2000)
    private String responsibilities;

    @Column(length = 2000)
    private String qualifications;

    @Column(length = 1000)
    private String benefits;

    private LocalDate applicationDeadline;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobStatus status = JobStatus.ACTIVE; // ACTIVE, CLOSED, DRAFT

    @Column(nullable = false)
    private Long postedBy; // User ID of recruiter

    private Integer viewCount = 0;

    private Integer applicationCount = 0;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public enum JobType {
        FULL_TIME, PART_TIME, CONTRACT, INTERNSHIP
    }

    public enum ExperienceLevel {
        ENTRY, JUNIOR, MID, SENIOR, LEAD
    }

    public enum JobStatus {
        ACTIVE, CLOSED, DRAFT
    }
}

