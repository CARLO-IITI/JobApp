package com.jobapp.matchingservice.dto;

import lombok.Data;

import java.util.List;

@Data
public class CandidateMatchRequest {
    private Long candidateId;
    private List<String> skills;
    private Integer yearsOfExperience;
    private String currentLocation;
    private List<String> preferredLocations;
    private String jobPreference; // FULL_TIME, PART_TIME, etc.
    private Double expectedSalary;
    private Boolean openToRemote;
}

