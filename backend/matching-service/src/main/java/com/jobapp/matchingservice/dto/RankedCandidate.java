package com.jobapp.matchingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RankedCandidate {
    private Long candidateId;
    private String candidateName;
    private Double overallScore;
    private Double skillsMatchScore;
    private Double experienceMatchScore;
    private Double locationMatchScore;
    private Integer rank;
    private List<String> matchingSkills;
    private List<String> missingSkills;
    private String recommendation;
    private String fitLevel; // EXCELLENT, GOOD, MODERATE, WEAK
}

