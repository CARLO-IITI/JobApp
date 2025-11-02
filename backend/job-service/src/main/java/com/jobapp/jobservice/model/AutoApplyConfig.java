package com.jobapp.jobservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Auto-Apply Agent Configuration
 */
@Entity
@Table(name = "auto_apply_configs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AutoApplyConfig {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long userId;

    @Column(nullable = false)
    private Boolean enabled = false;

    private Double minSalary;
    private Double maxSalary;

    @ElementCollection
    @CollectionTable(name = "auto_apply_locations", joinColumns = @JoinColumn(name = "config_id"))
    @Column(name = "location")
    private List<String> preferredLocations = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "auto_apply_job_types", joinColumns = @JoinColumn(name = "config_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "job_type")
    private List<Job.JobType> preferredJobTypes = new ArrayList<>();

    private Double minMatchThreshold = 0.70; // Only apply if match > 70%

    private Boolean remoteOnly = false;

    private Integer maxApplicationsPerDay = 10;

    @Column(length = 2000)
    private String coverLetterTemplate;

    @Column(nullable = false)
    private Boolean customizeCoverLetter = true;
}

