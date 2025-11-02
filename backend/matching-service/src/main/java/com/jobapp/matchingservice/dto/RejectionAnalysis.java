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
public class RejectionAnalysis {
    private String summary;
    private Double overallMatchScore;
    private List<String> strengths;
    private List<String> areasForImprovement;
    private List<String> missingSkills;
    private String experienceGapAnalysis;
    private String salaryExpectationFeedback;
    private String locationFeedback;
    private List<String> recommendations;
    private List<String> suggestedCourses;
    private String encouragingMessage;
}

