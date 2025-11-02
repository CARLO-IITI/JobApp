package com.jobapp.matchingservice.service;

import com.jobapp.matchingservice.algorithm.SimilarityCalculator;
import com.jobapp.matchingservice.dto.CandidateMatchRequest;
import com.jobapp.matchingservice.dto.MatchResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatchingService {

    private final SimilarityCalculator similarityCalculator;
    private final RestTemplate restTemplate = new RestTemplate();

    // Weight configuration for different factors
    private static final double SKILL_WEIGHT = 0.4;
    private static final double EXPERIENCE_WEIGHT = 0.3;
    private static final double LOCATION_WEIGHT = 0.2;
    private static final double SALARY_WEIGHT = 0.1;

    /**
     * Find best matching jobs for a candidate
     */
    public List<MatchResult> findMatchingJobs(CandidateMatchRequest request, List<Map<String, Object>> jobs) {
        log.info("Finding matches for candidate: {}", request.getCandidateId());

        List<MatchResult> matches = new ArrayList<>();

        for (Map<String, Object> job : jobs) {
            MatchResult match = calculateMatch(request, job);
            if (match.getOverallScore() > 0.3) { // Only include reasonable matches
                matches.add(match);
            }
        }

        // Sort by overall score descending
        matches.sort((a, b) -> Double.compare(b.getOverallScore(), a.getOverallScore()));

        log.info("Found {} matches for candidate {}", matches.size(), request.getCandidateId());
        return matches;
    }

    /**
     * Calculate match score between candidate and job
     */
    private MatchResult calculateMatch(CandidateMatchRequest candidate, Map<String, Object> job) {
        // Extract job details
        Long jobId = ((Number) job.get("id")).longValue();
        String jobTitle = (String) job.get("title");
        String companyName = (String) job.get("companyName");
        
        @SuppressWarnings("unchecked")
        List<String> requiredSkills = (List<String>) job.getOrDefault("requiredSkills", new ArrayList<>());
        
        Integer minExperience = job.get("minExperience") != null ? 
                ((Number) job.get("minExperience")).intValue() : null;
        Integer maxExperience = job.get("maxExperience") != null ? 
                ((Number) job.get("maxExperience")).intValue() : null;
        
        String jobLocation = (String) job.get("location");
        Boolean remoteAllowed = (Boolean) job.getOrDefault("remoteAllowed", false);
        
        Double minSalary = job.get("minSalary") != null ? 
                ((Number) job.get("minSalary")).doubleValue() : null;
        Double maxSalary = job.get("maxSalary") != null ? 
                ((Number) job.get("maxSalary")).doubleValue() : null;

        // Calculate individual scores
        double skillScore = similarityCalculator.calculateSkillSimilarity(
                candidate.getSkills(), requiredSkills);
        
        double experienceScore = similarityCalculator.calculateExperienceMatch(
                candidate.getYearsOfExperience(), minExperience, maxExperience);
        
        double locationScore = similarityCalculator.calculateLocationMatch(
                jobLocation, 
                candidate.getCurrentLocation(),
                candidate.getPreferredLocations(),
                remoteAllowed,
                candidate.getOpenToRemote());
        
        double salaryScore = similarityCalculator.calculateSalaryMatch(
                candidate.getExpectedSalary(), minSalary, maxSalary);

        // Calculate weighted overall score
        double overallScore = (skillScore * SKILL_WEIGHT) +
                             (experienceScore * EXPERIENCE_WEIGHT) +
                             (locationScore * LOCATION_WEIGHT) +
                             (salaryScore * SALARY_WEIGHT);

        // Round to 2 decimal places
        overallScore = Math.round(overallScore * 100.0) / 100.0;
        skillScore = Math.round(skillScore * 100.0) / 100.0;
        experienceScore = Math.round(experienceScore * 100.0) / 100.0;
        locationScore = Math.round(locationScore * 100.0) / 100.0;

        // Generate recommendation
        String recommendation = generateRecommendation(overallScore, skillScore, experienceScore);

        // Build details map
        Map<String, Object> details = new HashMap<>();
        details.put("matchingSkills", findMatchingSkills(candidate.getSkills(), requiredSkills));
        details.put("missingSkills", findMissingSkills(candidate.getSkills(), requiredSkills));
        details.put("experienceGap", calculateExperienceGap(candidate.getYearsOfExperience(), minExperience));

        return MatchResult.builder()
                .jobId(jobId)
                .jobTitle(jobTitle)
                .companyName(companyName)
                .overallScore(overallScore)
                .skillsMatchScore(skillScore)
                .experienceMatchScore(experienceScore)
                .locationMatchScore(locationScore)
                .culturalFitScore(salaryScore) // Using salary as cultural fit proxy
                .details(details)
                .recommendation(recommendation)
                .build();
    }

    private String generateRecommendation(double overallScore, double skillScore, double experienceScore) {
        if (overallScore >= 0.8) {
            return "Excellent match! Strongly recommended to apply.";
        } else if (overallScore >= 0.6) {
            return "Good match. You meet most requirements.";
        } else if (overallScore >= 0.4) {
            if (skillScore < 0.5) {
                return "Moderate match. Consider upskilling in required areas.";
            } else if (experienceScore < 0.5) {
                return "Moderate match. Highlight relevant projects to compensate for experience.";
            }
            return "Moderate match. Worth considering if interested.";
        } else {
            return "Lower match. Apply if very interested and willing to learn.";
        }
    }

    private List<String> findMatchingSkills(List<String> candidateSkills, List<String> requiredSkills) {
        if (candidateSkills == null || requiredSkills == null) {
            return new ArrayList<>();
        }

        Set<String> candidateSet = candidateSkills.stream()
                .map(s -> s.toLowerCase().trim())
                .collect(Collectors.toSet());

        return requiredSkills.stream()
                .filter(skill -> candidateSet.contains(skill.toLowerCase().trim()))
                .collect(Collectors.toList());
    }

    private List<String> findMissingSkills(List<String> candidateSkills, List<String> requiredSkills) {
        if (requiredSkills == null) {
            return new ArrayList<>();
        }

        Set<String> candidateSet = candidateSkills != null ? 
                candidateSkills.stream()
                        .map(s -> s.toLowerCase().trim())
                        .collect(Collectors.toSet()) : new HashSet<>();

        return requiredSkills.stream()
                .filter(skill -> !candidateSet.contains(skill.toLowerCase().trim()))
                .collect(Collectors.toList());
    }

    private Integer calculateExperienceGap(Integer candidateExp, Integer requiredExp) {
        if (candidateExp == null || requiredExp == null) {
            return 0;
        }
        return Math.max(0, requiredExp - candidateExp);
    }
}

