package com.jobapp.jobservice.dto;

import com.jobapp.jobservice.model.Job;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobDTO {
    
    private Long id;

    @NotBlank(message = "Job title is required")
    private String title;

    @NotBlank(message = "Job description is required")
    private String description;

    @NotNull(message = "Company ID is required")
    private Long companyId;

    @NotBlank(message = "Company name is required")
    private String companyName;

    private String companyDescription;

    private List<String> requiredSkills;

    @NotBlank(message = "Location is required")
    private String location;

    @NotNull(message = "Job type is required")
    private Job.JobType jobType;

    @NotNull(message = "Experience level is required")
    private Job.ExperienceLevel experienceLevel;

    private Integer minExperience;
    private Integer maxExperience;
    private Double minSalary;
    private Double maxSalary;
    private String currency;
    private Boolean remoteAllowed;
    private Integer openings;
    private String responsibilities;
    private String qualifications;
    private String benefits;
    private LocalDate applicationDeadline;
    private Job.JobStatus status;
    private Long postedBy;
    private Integer viewCount;
    private Integer applicationCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

