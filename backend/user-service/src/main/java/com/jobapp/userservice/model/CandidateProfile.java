package com.jobapp.userservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "candidate_profiles")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CandidateProfile {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(length = 2000)
    private String headline;

    @Column(length = 5000)
    private String summary;

    @ElementCollection
    @CollectionTable(name = "candidate_skills", joinColumns = @JoinColumn(name = "profile_id"))
    @Column(name = "skill")
    private List<String> skills = new ArrayList<>();

    private Integer yearsOfExperience;

    private String currentLocation;

    @ElementCollection
    @CollectionTable(name = "preferred_locations", joinColumns = @JoinColumn(name = "profile_id"))
    @Column(name = "location")
    private List<String> preferredLocations = new ArrayList<>();

    private String currentJobTitle;

    private String education; // Highest degree

    private String university;

    private LocalDate graduationDate;

    private String resumeUrl;
    
    private String resumeFileName;  // Original filename
    
    private String resumeFilePath;  // Server path to CV file

    private String portfolioUrl;

    private String githubUrl;

    private String linkedinUrl;

    @ElementCollection
    @CollectionTable(name = "candidate_languages", joinColumns = @JoinColumn(name = "profile_id"))
    @Column(name = "language")
    private List<String> languages = new ArrayList<>();

    private Double expectedSalary;

    private String noticePeriod; // Immediate, 1 month, 2 months, etc.

    @Enumerated(EnumType.STRING)
    private JobPreference jobPreference; // FULL_TIME, PART_TIME, CONTRACT, INTERNSHIP

    private Boolean openToRemote = true;

    private Boolean openToRelocation = false;

    public enum JobPreference {
        FULL_TIME, PART_TIME, CONTRACT, INTERNSHIP, ANY
    }
}

