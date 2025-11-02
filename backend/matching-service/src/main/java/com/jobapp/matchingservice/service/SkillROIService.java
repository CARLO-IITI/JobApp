package com.jobapp.matchingservice.service;

import com.jobapp.matchingservice.dto.SkillROI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Calculates ROI for learning new skills
 * Helps candidates make data-driven learning decisions
 */
@Slf4j
@Service
public class SkillROIService {

    // Market data for popular skills (in production, fetch from real job data)
    private static final Map<String, Map<String, Object>> SKILL_MARKET_DATA = new HashMap<>();
    
    static {
        // Kubernetes
        SKILL_MARKET_DATA.put("kubernetes", Map.of(
            "avgSalary", 135000.0,
            "jobCount", 250,
            "demandGrowth", 85,
            "learningHours", 40,
            "difficulty", "MODERATE",
            "trend", "RISING",
            "courses", List.of("Kubernetes for Beginners (KodeKloud)", "CKA Certification")
        ));
        
        // AWS
        SKILL_MARKET_DATA.put("aws", Map.of(
            "avgSalary", 140000.0,
            "jobCount", 350,
            "demandGrowth", 65,
            "learningHours", 60,
            "difficulty", "MODERATE",
            "trend", "RISING",
            "courses", List.of("AWS Certified Solutions Architect", "AWS Fundamentals")
        ));
        
        // React
        SKILL_MARKET_DATA.put("react", Map.of(
            "avgSalary", 125000.0,
            "jobCount", 400,
            "demandGrowth", 45,
            "learningHours", 50,
            "difficulty", "EASY",
            "trend", "STABLE",
            "courses", List.of("React - The Complete Guide (Udemy)", "React Official Docs")
        ));
        
        // Python
        SKILL_MARKET_DATA.put("python", Map.of(
            "avgSalary", 120000.0,
            "jobCount", 500,
            "demandGrowth", 50,
            "learningHours", 80,
            "difficulty", "EASY",
            "trend", "RISING",
            "courses", List.of("Python for Data Science", "Complete Python Bootcamp")
        ));
        
        // Machine Learning
        SKILL_MARKET_DATA.put("machine learning", Map.of(
            "avgSalary", 155000.0,
            "jobCount", 180,
            "demandGrowth", 120,
            "learningHours", 200,
            "difficulty", "HARD",
            "trend", "RISING",
            "courses", List.of("Andrew Ng ML Course", "Deep Learning Specialization")
        ));
        
        // TypeScript
        SKILL_MARKET_DATA.put("typescript", Map.of(
            "avgSalary", 128000.0,
            "jobCount", 300,
            "demandGrowth", 75,
            "learningHours", 30,
            "difficulty", "EASY",
            "trend", "RISING",
            "courses", List.of("TypeScript Complete Guide", "Understanding TypeScript")
        ));
        
        // Docker
        SKILL_MARKET_DATA.put("docker", Map.of(
            "avgSalary", 130000.0,
            "jobCount", 280,
            "demandGrowth", 70,
            "learningHours", 25,
            "difficulty", "EASY",
            "trend", "STABLE",
            "courses", List.of("Docker Mastery (Udemy)", "Docker Official Tutorial")
        ));
    }

    /**
     * Calculate ROI for learning a specific skill
     */
    public SkillROI calculateSkillROI(
            String skillName,
            List<String> currentSkills,
            Integer currentMatchingJobs,
            Double currentAvgSalary,
            Double currentHiringProbability) {

        String skillLower = skillName.toLowerCase().trim();
        
        // Get market data for this skill
        Map<String, Object> marketData = SKILL_MARKET_DATA.getOrDefault(skillLower, getDefaultMarketData());
        
        Double skillAvgSalary = (Double) marketData.get("avgSalary");
        Integer skillJobCount = (Integer) marketData.get("jobCount");
        Integer demandGrowth = (Integer) marketData.get("demandGrowth");
        Integer learningHours = (Integer) marketData.get("learningHours");
        String difficulty = (String) marketData.get("difficulty");
        String trend = (String) marketData.get("trend");
        @SuppressWarnings("unchecked")
        List<String> courses = (List<String>) marketData.get("courses");

        // Calculate impact
        int additionalJobs = (int) (skillJobCount * 0.7); // 70% of jobs with this skill
        int totalJobsAfter = currentMatchingJobs + additionalJobs;
        
        // Salary increase estimate
        double salaryIncrease = Math.max(0, skillAvgSalary - (currentAvgSalary != null ? currentAvgSalary : 100000));
        double salaryAfter = (currentAvgSalary != null ? currentAvgSalary : 100000) + (salaryIncrease * 0.6);
        
        // Hiring probability improvement
        double probAfter = Math.min(0.95, (currentHiringProbability != null ? currentHiringProbability : 0.6) + 0.15);
        
        // Calculate ROI
        double annualValueIncrease = salaryIncrease * 0.6; // Conservative estimate
        double roi = annualValueIncrease / learningHours;
        
        // Determine priority
        String priority = determinePriority(roi, demandGrowth, difficulty);

        return SkillROI.builder()
                .skillName(skillName)
                .currentMatchingJobs(currentMatchingJobs)
                .jobsAfterLearning(totalJobsAfter)
                .additionalJobs(additionalJobs)
                .currentAvgSalary(currentAvgSalary != null ? currentAvgSalary : 100000.0)
                .salaryAfterLearning(Math.round(salaryAfter * 100.0) / 100.0)
                .salaryIncrease(Math.round(salaryIncrease * 0.6 * 100.0) / 100.0)
                .currentHiringProbability(currentHiringProbability != null ? currentHiringProbability : 0.6)
                .hiringProbabilityAfterLearning(Math.round(probAfter * 100.0) / 100.0)
                .learningTimeHours(learningHours)
                .learningDifficulty(difficulty)
                .roi(Math.round(roi * 100.0) / 100.0)
                .priority(priority)
                .recommendedCourses(courses)
                .marketTrend(trend)
                .demandGrowth(demandGrowth)
                .build();
    }

    /**
     * Calculate ROI for multiple skills, ranked by value
     */
    public List<SkillROI> calculateMultipleSkillROI(
            List<String> potentialSkills,
            List<String> currentSkills,
            Integer currentMatchingJobs,
            Double currentAvgSalary,
            Double currentHiringProbability) {

        List<SkillROI> roiList = new ArrayList<>();

        for (String skill : potentialSkills) {
            // Don't calculate for skills they already have
            if (currentSkills != null && 
                currentSkills.stream().anyMatch(s -> s.equalsIgnoreCase(skill))) {
                continue;
            }

            SkillROI roi = calculateSkillROI(
                    skill,
                    currentSkills,
                    currentMatchingJobs,
                    currentAvgSalary,
                    currentHiringProbability
            );
            
            roiList.add(roi);
        }

        // Sort by ROI descending
        roiList.sort((a, b) -> Double.compare(b.getRoi(), a.getRoi()));

        return roiList;
    }

    private String determinePriority(double roi, int demandGrowth, String difficulty) {
        // High ROI + High demand + Easy = HIGHEST
        if (roi > 500 && demandGrowth > 60 && difficulty.equals("EASY")) {
            return "HIGHEST";
        }
        
        if (roi > 400 || demandGrowth > 80) {
            return "HIGH";
        }
        
        if (roi > 200 || demandGrowth > 40) {
            return "MEDIUM";
        }
        
        return "LOW";
    }

    private Map<String, Object> getDefaultMarketData() {
        return Map.of(
            "avgSalary", 110000.0,
            "jobCount", 100,
            "demandGrowth", 30,
            "learningHours", 50,
            "difficulty", "MODERATE",
            "trend", "STABLE",
            "courses", List.of("Search online courses for this skill")
        );
    }
}

