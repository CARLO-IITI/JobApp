package com.jobapp.matchingservice.service;

import com.jobapp.matchingservice.algorithm.SimilarityCalculator;
import com.jobapp.matchingservice.dto.RankedCandidate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * AI-powered candidate ranking for recruiters
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CandidateRankingService {

    private final SimilarityCalculator similarityCalculator;

    /**
     * Rank candidates by fit for a job
     */
    public List<RankedCandidate> rankCandidates(Map<String, Object> jobRequirements, 
                                                 List<Map<String, Object>> candidates) {
        
        log.info("Ranking {} candidates for job", candidates.size());

        List<RankedCandidate> rankedCandidates = new ArrayList<>();

        for (Map<String, Object> candidate : candidates) {
            RankedCandidate ranked = calculateCandidateScore(candidate, jobRequirements);
            rankedCandidates.add(ranked);
        }

        // Sort by overall score descending
        rankedCandidates.sort((a, b) -> Double.compare(b.getOverallScore(), a.getOverallScore()));

        // Assign ranks
        for (int i = 0; i < rankedCandidates.size(); i++) {
            rankedCandidates.get(i).setRank(i + 1);
        }

        return rankedCandidates;
    }

    private RankedCandidate calculateCandidateScore(Map<String, Object> candidate, 
                                                    Map<String, Object> jobRequirements) {
        // Extract candidate data
        Long candidateId = candidate.get("candidateId") != null ? 
                ((Number) candidate.get("candidateId")).longValue() : null;
        String candidateName = (String) candidate.getOrDefault("candidateName", "Unknown");
        
        @SuppressWarnings("unchecked")
        List<String> candidateSkills = (List<String>) candidate.getOrDefault("skills", new ArrayList<>());
        Integer candidateExp = candidate.get("yearsOfExperience") != null ? 
                ((Number) candidate.get("yearsOfExperience")).intValue() : null;
        String candidateLocation = (String) candidate.get("currentLocation");
        
        // Extract job requirements
        @SuppressWarnings("unchecked")
        List<String> requiredSkills = (List<String>) jobRequirements.getOrDefault("requiredSkills", new ArrayList<>());
        Integer minExp = jobRequirements.get("minExperience") != null ? 
                ((Number) jobRequirements.get("minExperience")).intValue() : null;
        Integer maxExp = jobRequirements.get("maxExperience") != null ? 
                ((Number) jobRequirements.get("maxExperience")).intValue() : null;
        String jobLocation = (String) jobRequirements.get("location");
        Boolean remoteAllowed = (Boolean) jobRequirements.getOrDefault("remoteAllowed", false);

        // Calculate scores
        double skillScore = similarityCalculator.calculateSkillSimilarity(candidateSkills, requiredSkills);
        double expScore = similarityCalculator.calculateExperienceMatch(candidateExp, minExp, maxExp);
        double locationScore = similarityCalculator.calculateLocationMatch(
                jobLocation, candidateLocation, null, remoteAllowed, true);

        // Weighted overall score
        double overallScore = (skillScore * 0.5) + (expScore * 0.3) + (locationScore * 0.2);

        // Round scores
        overallScore = Math.round(overallScore * 100.0) / 100.0;
        skillScore = Math.round(skillScore * 100.0) / 100.0;
        expScore = Math.round(expScore * 100.0) / 100.0;
        locationScore = Math.round(locationScore * 100.0) / 100.0;

        // Find matching and missing skills
        List<String> matchingSkills = findMatchingSkills(candidateSkills, requiredSkills);
        List<String> missingSkills = findMissingSkills(candidateSkills, requiredSkills);

        // Generate recommendation
        String recommendation = generateRecruiterRecommendation(overallScore, skillScore, expScore);

        // Determine fit level
        String fitLevel = determineFitLevel(overallScore);

        return RankedCandidate.builder()
                .candidateId(candidateId)
                .candidateName(candidateName)
                .overallScore(overallScore)
                .skillsMatchScore(skillScore)
                .experienceMatchScore(expScore)
                .locationMatchScore(locationScore)
                .matchingSkills(matchingSkills)
                .missingSkills(missingSkills)
                .recommendation(recommendation)
                .fitLevel(fitLevel)
                .build();
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

    private String generateRecruiterRecommendation(double overallScore, double skillScore, double expScore) {
        if (overallScore >= 0.85) {
            return "🌟 HIGHLY RECOMMENDED - Excellent fit! Strong match across all criteria.";
        } else if (overallScore >= 0.7) {
            return "✅ RECOMMENDED - Good fit. Meets most requirements.";
        } else if (overallScore >= 0.55) {
            if (skillScore >= 0.7) {
                return "⚠️ POTENTIAL - Strong skills but may need training in other areas.";
            } else {
                return "⚠️ CONSIDER - Moderate fit. May require skill development.";
            }
        } else {
            return "❌ NOT RECOMMENDED - Significant gaps in requirements.";
        }
    }

    private String determineFitLevel(double overallScore) {
        if (overallScore >= 0.8) return "EXCELLENT";
        else if (overallScore >= 0.65) return "GOOD";
        else if (overallScore >= 0.45) return "MODERATE";
        else return "WEAK";
    }
}

