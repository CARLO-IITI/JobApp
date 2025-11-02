package com.jobapp.matchingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchResult {
    private Long jobId;
    private String jobTitle;
    private String companyName;
    private Double overallScore;
    private Double skillsMatchScore;
    private Double experienceMatchScore;
    private Double locationMatchScore;
    private Double culturalFitScore;
    private Map<String, Object> details;
    private String recommendation;
}

