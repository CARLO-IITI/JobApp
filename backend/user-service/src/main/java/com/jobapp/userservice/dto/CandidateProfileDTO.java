package com.jobapp.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CandidateProfileDTO {
    private Long id;
    private Long userId;
    private String headline;
    private String summary;
    private List<String> skills;
    private Integer yearsOfExperience;
    private String currentLocation;
    private List<String> preferredLocations;
    private String currentJobTitle;
    private String education;
    private String university;
    private String resumeUrl;
    private String portfolioUrl;
    private String githubUrl;
    private String linkedinUrl;
    private List<String> languages;
    private Double expectedSalary;
    private String noticePeriod;
    private String jobPreference;
    private Boolean openToRemote;
    private Boolean openToRelocation;
    private String resumeFileUrl;  // Path to uploaded CV file
}

