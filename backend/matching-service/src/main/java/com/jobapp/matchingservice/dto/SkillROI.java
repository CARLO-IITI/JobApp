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
public class SkillROI {
    private String skillName;
    private Integer currentMatchingJobs;
    private Integer jobsAfterLearning;
    private Integer additionalJobs;
    private Double currentAvgSalary;
    private Double salaryAfterLearning;
    private Double salaryIncrease;
    private Double currentHiringProbability;
    private Double hiringProbabilityAfterLearning;
    private Integer learningTimeHours;
    private String learningDifficulty;  // EASY, MODERATE, HARD
    private Double roi;  // Dollars per hour
    private String priority;  // HIGHEST, HIGH, MEDIUM, LOW
    private List<String> recommendedCourses;
    private String marketTrend;  // RISING, STABLE, DECLINING
    private Integer demandGrowth;  // Percentage
}

