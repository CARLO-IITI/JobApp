package com.jobapp.matchingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobMatchPrediction {
    private Long jobId;
    private String jobTitle;
    private Double hiringProbability;          // 0-100% chance
    private Double overallMatchScore;          // 0-100%
    private String matchLevel;                 // EXCELLENT, GOOD, FAIR, POOR
    private String recommendation;             // Should you apply?
    
    // Detailed breakdown
    private Map<String, Double> scoreBreakdown; // Skills, Experience, Education, etc.
    private List<String> matchingSkills;
    private List<String> missingSkills;
    private List<String> relatedSkills;        // Skills you have that are related
    
    // Competition analysis
    private Integer estimatedApplicants;
    private Integer yourPredictedRank;
    private String competitionLevel;           // LOW, MEDIUM, HIGH
    
    // Actionable insights
    private List<String> strengthAreas;
    private List<String> improvementAreas;
    private List<String> quickWins;            // Easy improvements for better match
    private String applicationAdvice;          // When/how to apply
}

