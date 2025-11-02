package com.jobapp.matchingservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Advanced semantic matching service using embeddings and ML-based scoring.
 * Analyzes job descriptions and candidate profiles to find the best matches.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SemanticMatchingService {

    private final EmbeddingService embeddingService;

    /**
     * Calculate comprehensive match score between job and candidate
     */
    public Map<String, Object> calculateSemanticMatch(Map<String, Object> jobData,
                                                       Map<String, Object> candidateData) {
        log.info("Calculating semantic match between job and candidate");
        
        // Generate embeddings
        Map<String, Object> jobEmbedding = generateJobEmbedding(jobData);
        Map<String, Object> candidateEmbedding = generateCandidateEmbedding(candidateData);
        
        // Calculate various similarity scores
        double skillSimilarity = embeddingService.calculateCosineSimilarity(jobEmbedding, candidateEmbedding);
        double experienceMatch = calculateExperienceMatch(jobEmbedding, candidateEmbedding);
        double locationMatch = calculateLocationMatch(jobEmbedding, candidateEmbedding);
        double profileQuality = (double) candidateEmbedding.getOrDefault("profileCompleteness", 0.5);
        double techDensityMatch = calculateTechDensityMatch(jobEmbedding, candidateEmbedding);
        
        // Calculate weighted overall score
        double overallScore = calculateWeightedScore(
                skillSimilarity,
                experienceMatch,
                locationMatch,
                profileQuality,
                techDensityMatch
        );
        
        // Generate detailed breakdown
        Map<String, Object> result = new HashMap<>();
        result.put("overallScore", Math.round(overallScore * 100) / 100.0);
        result.put("overallPercentage", (int) (overallScore * 100));
        result.put("skillMatch", Math.round(skillSimilarity * 100));
        result.put("experienceMatch", Math.round(experienceMatch * 100));
        result.put("locationMatch", Math.round(locationMatch * 100));
        result.put("profileQuality", Math.round(profileQuality * 100));
        result.put("recommendation", generateRecommendation(overallScore));
        result.put("matchedSkills", findMatchedSkills(jobEmbedding, candidateEmbedding));
        result.put("missingSkills", findMissingSkills(jobEmbedding, candidateEmbedding));
        result.put("strengths", identifyStrengths(jobEmbedding, candidateEmbedding, skillSimilarity));
        result.put("improvementAreas", identifyImprovementAreas(jobEmbedding, candidateEmbedding));
        
        log.info("Semantic match calculated: Overall Score = {}", overallScore);
        return result;
    }

    /**
     * Find best matching candidates for a job
     */
    public List<Map<String, Object>> findBestCandidates(Map<String, Object> jobData,
                                                         List<Map<String, Object>> candidates,
                                                         int topN) {
        log.info("Finding top {} candidates for job", topN);
        
        Map<String, Object> jobEmbedding = generateJobEmbedding(jobData);
        
        return candidates.stream()
                .map(candidate -> {
                    Map<String, Object> candidateEmbedding = generateCandidateEmbedding(candidate);
                    Map<String, Object> matchResult = calculateSemanticMatch(jobData, candidate);
                    
                    Map<String, Object> result = new HashMap<>(candidate);
                    result.put("matchScore", matchResult.get("overallScore"));
                    result.put("matchPercentage", matchResult.get("overallPercentage"));
                    result.put("matchDetails", matchResult);
                    
                    return result;
                })
                .sorted((a, b) -> {
                    Double scoreA = (Double) a.get("matchScore");
                    Double scoreB = (Double) b.get("matchScore");
                    return scoreB.compareTo(scoreA);
                })
                .limit(topN)
                .collect(Collectors.toList());
    }

    /**
     * Find best matching jobs for a candidate
     */
    public List<Map<String, Object>> findBestJobs(Map<String, Object> candidateData,
                                                   List<Map<String, Object>> jobs,
                                                   int topN) {
        log.info("Finding top {} jobs for candidate", topN);
        
        Map<String, Object> candidateEmbedding = generateCandidateEmbedding(candidateData);
        
        return jobs.stream()
                .map(job -> {
                    Map<String, Object> matchResult = calculateSemanticMatch(job, candidateData);
                    
                    Map<String, Object> result = new HashMap<>(job);
                    result.put("matchScore", matchResult.get("overallScore"));
                    result.put("matchPercentage", matchResult.get("overallPercentage"));
                    result.put("matchDetails", matchResult);
                    
                    return result;
                })
                .sorted((a, b) -> {
                    Double scoreA = (Double) a.get("matchScore");
                    Double scoreB = (Double) b.get("matchScore");
                    return scoreB.compareTo(scoreA);
                })
                .limit(topN)
                .collect(Collectors.toList());
    }

    /**
     * Generate job embedding from job data
     */
    private Map<String, Object> generateJobEmbedding(Map<String, Object> jobData) {
        String title = (String) jobData.get("title");
        String description = (String) jobData.get("description");
        @SuppressWarnings("unchecked")
        List<String> requiredSkills = (List<String>) jobData.get("requiredSkills");
        String experienceLevel = (String) jobData.get("experienceLevel");
        Integer minExperience = (Integer) jobData.get("minExperience");
        String location = (String) jobData.get("location");
        Boolean remoteAllowed = (Boolean) jobData.get("remoteAllowed");
        
        return embeddingService.generateJobEmbedding(
                title != null ? title : "",
                description != null ? description : "",
                requiredSkills,
                experienceLevel,
                minExperience,
                location,
                remoteAllowed
        );
    }

    /**
     * Generate candidate embedding from candidate data
     */
    private Map<String, Object> generateCandidateEmbedding(Map<String, Object> candidateData) {
        String headline = (String) candidateData.get("headline");
        String summary = (String) candidateData.get("summary");
        @SuppressWarnings("unchecked")
        List<String> skills = (List<String>) candidateData.get("skills");
        Integer yearsOfExperience = (Integer) candidateData.get("yearsOfExperience");
        String currentLocation = (String) candidateData.get("currentLocation");
        Boolean openToRemote = (Boolean) candidateData.get("openToRemote");
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> workExperiences = (List<Map<String, Object>>) 
                candidateData.get("workExperiences");
        
        return embeddingService.generateCandidateEmbedding(
                headline,
                summary,
                skills,
                yearsOfExperience,
                currentLocation,
                openToRemote,
                workExperiences
        );
    }

    /**
     * Calculate experience match score
     */
    private double calculateExperienceMatch(Map<String, Object> jobEmbedding,
                                           Map<String, Object> candidateEmbedding) {
        Integer minRequired = (Integer) jobEmbedding.get("minExperience");
        Integer candidateYears = (Integer) candidateEmbedding.get("yearsOfExperience");
        
        if (minRequired == null || minRequired == 0) return 1.0;
        if (candidateYears == null) return 0.3;
        
        if (candidateYears >= minRequired) {
            // Bonus for more experience, but diminishing returns
            double bonus = Math.min((candidateYears - minRequired) * 0.05, 0.2);
            return Math.min(1.0, 0.8 + bonus);
        } else {
            // Penalty for less experience
            double ratio = (double) candidateYears / minRequired;
            return Math.max(0.3, ratio);
        }
    }

    /**
     * Calculate location match score
     */
    private double calculateLocationMatch(Map<String, Object> jobEmbedding,
                                         Map<String, Object> candidateEmbedding) {
        Boolean jobRemote = (Boolean) jobEmbedding.get("remoteAllowed");
        Boolean candidateRemote = (Boolean) candidateEmbedding.get("openToRemote");
        String jobLocation = (String) jobEmbedding.get("location");
        String candidateLocation = (String) candidateEmbedding.get("location");
        
        // Remote work is a perfect match
        if (Boolean.TRUE.equals(jobRemote) && Boolean.TRUE.equals(candidateRemote)) {
            return 1.0;
        }
        
        // If job is remote but candidate doesn't want remote
        if (Boolean.TRUE.equals(jobRemote)) {
            return 0.9; // Still good match
        }
        
        // Check location match
        if (jobLocation != null && candidateLocation != null) {
            if (jobLocation.equalsIgnoreCase(candidateLocation)) {
                return 1.0;
            }
            // Partial match for same city or state
            if (jobLocation.toLowerCase().contains(candidateLocation.toLowerCase()) ||
                candidateLocation.toLowerCase().contains(jobLocation.toLowerCase())) {
                return 0.7;
            }
        }
        
        // Open to relocation gives some points
        if (Boolean.TRUE.equals(candidateRemote)) {
            return 0.6;
        }
        
        return 0.4;
    }

    /**
     * Calculate tech density match
     */
    private double calculateTechDensityMatch(Map<String, Object> jobEmbedding,
                                             Map<String, Object> candidateEmbedding) {
        Double jobDensity = (Double) jobEmbedding.get("techDensity");
        Double candidateDensity = (Double) candidateEmbedding.get("techDensity");
        
        if (jobDensity == null || candidateDensity == null) return 0.5;
        
        // Similar tech density indicates similar technical focus
        double difference = Math.abs(jobDensity - candidateDensity);
        return Math.max(0.0, 1.0 - difference * 2);
    }

    /**
     * Calculate weighted overall score
     */
    private double calculateWeightedScore(double skillSimilarity,
                                          double experienceMatch,
                                          double locationMatch,
                                          double profileQuality,
                                          double techDensityMatch) {
        // Weighted scoring
        double score = 0.0;
        score += skillSimilarity * 0.40;      // 40% weight on skills
        score += experienceMatch * 0.25;      // 25% weight on experience
        score += locationMatch * 0.15;        // 15% weight on location
        score += profileQuality * 0.10;       // 10% weight on profile quality
        score += techDensityMatch * 0.10;     // 10% weight on tech focus
        
        return Math.min(1.0, Math.max(0.0, score));
    }

    /**
     * Generate recommendation based on score
     */
    private String generateRecommendation(double score) {
        if (score >= 0.85) return "EXCELLENT_MATCH";
        if (score >= 0.70) return "STRONG_MATCH";
        if (score >= 0.55) return "GOOD_MATCH";
        if (score >= 0.40) return "MODERATE_MATCH";
        return "WEAK_MATCH";
    }

    /**
     * Find matched skills between job and candidate
     */
    @SuppressWarnings("unchecked")
    private List<String> findMatchedSkills(Map<String, Object> jobEmbedding,
                                           Map<String, Object> candidateEmbedding) {
        Map<String, Double> jobSkills = (Map<String, Double>) jobEmbedding.get("skillVector");
        Map<String, Double> candidateSkills = (Map<String, Double>) candidateEmbedding.get("skillVector");
        
        if (jobSkills == null || candidateSkills == null) return new ArrayList<>();
        
        return jobSkills.keySet().stream()
                .filter(candidateSkills::containsKey)
                .collect(Collectors.toList());
    }

    /**
     * Find missing skills
     */
    @SuppressWarnings("unchecked")
    private List<String> findMissingSkills(Map<String, Object> jobEmbedding,
                                           Map<String, Object> candidateEmbedding) {
        Map<String, Double> jobSkills = (Map<String, Double>) jobEmbedding.get("skillVector");
        Map<String, Double> candidateSkills = (Map<String, Double>) candidateEmbedding.get("skillVector");
        
        if (jobSkills == null) return new ArrayList<>();
        if (candidateSkills == null) return new ArrayList<>(jobSkills.keySet());
        
        return jobSkills.keySet().stream()
                .filter(skill -> !candidateSkills.containsKey(skill))
                .limit(5) // Top 5 missing skills
                .collect(Collectors.toList());
    }

    /**
     * Identify candidate strengths
     */
    @SuppressWarnings("unchecked")
    private List<String> identifyStrengths(Map<String, Object> jobEmbedding,
                                           Map<String, Object> candidateEmbedding,
                                           double skillSimilarity) {
        List<String> strengths = new ArrayList<>();
        
        if (skillSimilarity >= 0.8) {
            strengths.add("Excellent skill match");
        }
        
        Integer candidateYears = (Integer) candidateEmbedding.get("yearsOfExperience");
        Integer minRequired = (Integer) jobEmbedding.get("minExperience");
        if (candidateYears != null && minRequired != null && candidateYears > minRequired + 2) {
            strengths.add("Exceeds experience requirements");
        }
        
        Double profileQuality = (Double) candidateEmbedding.get("profileCompleteness");
        if (profileQuality != null && profileQuality > 0.8) {
            strengths.add("Comprehensive profile");
        }
        
        List<String> matchedSkills = findMatchedSkills(jobEmbedding, candidateEmbedding);
        if (matchedSkills.size() > 5) {
            strengths.add("Strong technical foundation");
        }
        
        return strengths;
    }

    /**
     * Identify improvement areas
     */
    private List<String> identifyImprovementAreas(Map<String, Object> jobEmbedding,
                                                  Map<String, Object> candidateEmbedding) {
        List<String> improvements = new ArrayList<>();
        
        List<String> missingSkills = findMissingSkills(jobEmbedding, candidateEmbedding);
        if (!missingSkills.isEmpty()) {
            improvements.add("Consider learning: " + String.join(", ", missingSkills.subList(0, Math.min(3, missingSkills.size()))));
        }
        
        Integer candidateYears = (Integer) candidateEmbedding.get("yearsOfExperience");
        Integer minRequired = (Integer) jobEmbedding.get("minExperience");
        if (candidateYears != null && minRequired != null && candidateYears < minRequired) {
            improvements.add("Gain more years of experience");
        }
        
        Double profileQuality = (Double) candidateEmbedding.get("profileCompleteness");
        if (profileQuality != null && profileQuality < 0.6) {
            improvements.add("Complete your profile for better matches");
        }
        
        return improvements;
    }
}

