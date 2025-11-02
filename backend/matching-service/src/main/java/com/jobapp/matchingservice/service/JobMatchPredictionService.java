package com.jobapp.matchingservice.service;

import com.jobapp.matchingservice.algorithm.EmbeddingMatcher;
import com.jobapp.matchingservice.algorithm.SimilarityCalculator;
import com.jobapp.matchingservice.dto.JobMatchPrediction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Predicts candidate's chances of getting hired for a specific job
 * Uses embeddings and multi-factor analysis
 */
@Service
@RequiredArgsConstructor
public class JobMatchPredictionService {

    private final SimilarityCalculator similarityCalculator;
    private final EmbeddingMatcher embeddingMatcher;

    public JobMatchPrediction predictJobMatch(
            Map<String, Object> candidateProfile,
            Map<String, Object> jobDetails) {

        // Extract candidate data
        @SuppressWarnings("unchecked")
        List<String> candidateSkills = (List<String>) candidateProfile.getOrDefault("skills", new ArrayList<>());
        Integer candidateExp = candidateProfile.get("yearsOfExperience") != null ? 
                ((Number) candidateProfile.get("yearsOfExperience")).intValue() : 0;
        String candidateEducation = (String) candidateProfile.get("education");
        String candidateLocation = (String) candidateProfile.get("currentLocation");
        Double expectedSalary = candidateProfile.get("expectedSalary") != null ? 
                ((Number) candidateProfile.get("expectedSalary")).doubleValue() : null;

        // Extract job data
        Long jobId = jobDetails.get("id") != null ? ((Number) jobDetails.get("id")).longValue() : null;
        String jobTitle = (String) jobDetails.get("title");
        String jobDescription = (String) jobDetails.get("description");
        
        @SuppressWarnings("unchecked")
        List<String> requiredSkills = (List<String>) jobDetails.getOrDefault("requiredSkills", new ArrayList<>());
        Integer minExp = jobDetails.get("minExperience") != null ? 
                ((Number) jobDetails.get("minExperience")).intValue() : null;
        Integer maxExp = jobDetails.get("maxExperience") != null ? 
                ((Number) jobDetails.get("maxExperience")).intValue() : null;
        String jobLocation = (String) jobDetails.get("location");
        Double maxSalary = jobDetails.get("maxSalary") != null ? 
                ((Number) jobDetails.get("maxSalary")).doubleValue() : null;
        Boolean remoteAllowed = (Boolean) jobDetails.getOrDefault("remoteAllowed", false);
        Integer applicationCount = jobDetails.get("applicationCount") != null ?
                ((Number) jobDetails.get("applicationCount")).intValue() : 10;

        // Extract implied skills from job description
        List<String> impliedSkills = embeddingMatcher.extractImpliedSkills(jobDescription != null ? jobDescription : "");
        List<String> allRequiredSkills = new ArrayList<>(requiredSkills);
        allRequiredSkills.addAll(impliedSkills);

        // Calculate individual scores using embeddings
        double skillsScore = embeddingMatcher.enhancedSkillMatching(candidateSkills, allRequiredSkills);
        double experienceScore = similarityCalculator.calculateExperienceMatch(candidateExp, minExp, maxExp);
        double educationScore = calculateEducationScore(candidateEducation);
        double locationScore = similarityCalculator.calculateLocationMatch(
                jobLocation, candidateLocation, null, remoteAllowed, true);
        double salaryScore = similarityCalculator.calculateSalaryMatch(expectedSalary, null, maxSalary);

        // Overall match score
        double overallScore = (skillsScore * 0.40) + 
                             (experienceScore * 0.30) + 
                             (educationScore * 0.15) + 
                             (locationScore * 0.10) + 
                             (salaryScore * 0.05);

        // Find matching, missing, and related skills
        List<String> matchingSkills = findExactMatches(candidateSkills, requiredSkills);
        List<String> missingSkills = findMissingSkills(candidateSkills, requiredSkills);
        List<String> relatedSkills = findRelatedSkills(candidateSkills, requiredSkills);

        // Estimate rank (based on score and competition)
        int estimatedRank = estimateRank(overallScore, applicationCount);

        // Calculate hiring probability
        double hiringProbability = embeddingMatcher.calculateHiringProbability(
                skillsScore, experienceScore, educationScore, locationScore,
                applicationCount, estimatedRank);

        // Determine match level
        String matchLevel = determineMatchLevel(overallScore);

        // Generate recommendation
        String recommendation = generateRecommendation(overallScore, hiringProbability);

        // Competition analysis
        String competitionLevel = analyzeCompetition(applicationCount);

        // Score breakdown
        Map<String, Double> breakdown = new LinkedHashMap<>();
        breakdown.put("Skills Match", Math.round(skillsScore * 100.0) / 100.0);
        breakdown.put("Experience Match", Math.round(experienceScore * 100.0) / 100.0);
        breakdown.put("Education Match", Math.round(educationScore * 100.0) / 100.0);
        breakdown.put("Location Fit", Math.round(locationScore * 100.0) / 100.0);
        breakdown.put("Salary Alignment", Math.round(salaryScore * 100.0) / 100.0);

        // Strength areas
        List<String> strengthAreas = identifyStrengths(breakdown);

        // Improvement areas
        List<String> improvementAreas = identifyImprovements(breakdown, missingSkills);

        // Quick wins
        List<String> quickWins = generateQuickWins(missingSkills, skillsScore);

        // Application advice
        String advice = generateApplicationAdvice(hiringProbability, competitionLevel);

        return JobMatchPrediction.builder()
                .jobId(jobId)
                .jobTitle(jobTitle)
                .hiringProbability(Math.round(hiringProbability * 100.0) / 100.0)
                .overallMatchScore(Math.round(overallScore * 100.0) / 100.0)
                .matchLevel(matchLevel)
                .recommendation(recommendation)
                .scoreBreakdown(breakdown)
                .matchingSkills(matchingSkills)
                .missingSkills(missingSkills)
                .relatedSkills(relatedSkills)
                .estimatedApplicants(applicationCount)
                .yourPredictedRank(estimatedRank)
                .competitionLevel(competitionLevel)
                .strengthAreas(strengthAreas)
                .improvementAreas(improvementAreas)
                .quickWins(quickWins)
                .applicationAdvice(advice)
                .build();
    }

    private double calculateEducationScore(String education) {
        if (education == null) return 0.5;
        String lower = education.toLowerCase();
        if (lower.contains("phd")) return 1.0;
        if (lower.contains("master") || lower.contains("m.s")) return 0.9;
        if (lower.contains("bachelor") || lower.contains("b.s") || lower.contains("b.tech")) return 0.8;
        return 0.6;
    }

    private List<String> findExactMatches(List<String> candidateSkills, List<String> requiredSkills) {
        if (candidateSkills == null || requiredSkills == null) return new ArrayList<>();
        
        Set<String> candidateSet = candidateSkills.stream()
                .map(s -> s.toLowerCase().trim())
                .collect(Collectors.toSet());

        return requiredSkills.stream()
                .filter(skill -> candidateSet.contains(skill.toLowerCase().trim()))
                .collect(Collectors.toList());
    }

    private List<String> findMissingSkills(List<String> candidateSkills, List<String> requiredSkills) {
        List<String> exact = findExactMatches(candidateSkills, requiredSkills);
        return requiredSkills.stream()
                .filter(skill -> !exact.contains(skill))
                .collect(Collectors.toList());
    }

    private List<String> findRelatedSkills(List<String> candidateSkills, List<String> requiredSkills) {
        List<String> related = new ArrayList<>();
        
        if (candidateSkills == null || requiredSkills == null) return related;

        for (String candidateSkill : candidateSkills) {
            for (String requiredSkill : requiredSkills) {
                double similarity = embeddingMatcher.calculateSemanticSimilarity(candidateSkill, requiredSkill);
                if (similarity > 0.6 && similarity < 0.95) { // Related but not exact
                    related.add(candidateSkill + " (similar to " + requiredSkill + ")");
                }
            }
        }

        return related.stream().distinct().limit(3).collect(Collectors.toList());
    }

    private int estimateRank(double overallScore, int totalApplicants) {
        // Estimate rank based on score
        if (overallScore >= 0.85) return Math.max(1, totalApplicants / 10);      // Top 10%
        if (overallScore >= 0.70) return Math.max(1, totalApplicants / 4);       // Top 25%
        if (overallScore >= 0.55) return Math.max(1, totalApplicants / 2);       // Top 50%
        return Math.max(1, (int)(totalApplicants * 0.75));                       // Bottom 25%
    }

    private String determineMatchLevel(double score) {
        if (score >= 0.85) return "EXCELLENT";
        if (score >= 0.70) return "GOOD";
        if (score >= 0.50) return "FAIR";
        return "POOR";
    }

    private String generateRecommendation(double matchScore, double probability) {
        if (probability >= 0.70) {
            return "🌟 Highly Recommended! You're a strong match - apply immediately!";
        } else if (probability >= 0.50) {
            return "✅ Recommended. Good match - definitely apply!";
        } else if (probability >= 0.30) {
            return "⚠️ Moderate Chance. Apply if very interested, but focus on skill development.";
        } else {
            return "⚡ Low Match. Consider building missing skills before applying.";
        }
    }

    private String analyzeCompetition(int applicants) {
        if (applicants > 50) return "HIGH";
        if (applicants > 20) return "MEDIUM";
        return "LOW";
    }

    private List<String> identifyStrengths(Map<String, Double> breakdown) {
        List<String> strengths = new ArrayList<>();
        
        breakdown.forEach((criterion, score) -> {
            if (score >= 0.8) {
                strengths.add(criterion + " (" + Math.round(score * 100) + "% match)");
            }
        });

        return strengths.isEmpty() ? List.of("Willingness to learn and grow") : strengths;
    }

    private List<String> identifyImprovements(Map<String, Double> breakdown, List<String> missingSkills) {
        List<String> improvements = new ArrayList<>();
        
        breakdown.forEach((criterion, score) -> {
            if (score < 0.6) {
                improvements.add("Improve your " + criterion.toLowerCase());
            }
        });

        if (!missingSkills.isEmpty()) {
            improvements.add("Learn missing skills: " + String.join(", ", missingSkills.subList(0, Math.min(3, missingSkills.size()))));
        }

        return improvements;
    }

    private List<String> generateQuickWins(List<String> missingSkills, double skillScore) {
        List<String> quickWins = new ArrayList<>();
        
        if (skillScore >= 0.7) {
            quickWins.add("You're already a strong match! Polish your resume and apply.");
        }
        
        if (!missingSkills.isEmpty() && missingSkills.size() <= 2) {
            quickWins.add("Learn just " + String.join(" and ", missingSkills) + " to become an excellent match!");
        }
        
        quickWins.add("Highlight your matching skills prominently in your cover letter");
        quickWins.add("Mention specific projects using required technologies");
        
        return quickWins;
    }

    private String generateApplicationAdvice(double probability, String competition) {
        if (probability >= 0.70) {
            return "Apply now! You're in the top tier. Tailor your cover letter to highlight matching skills.";
        } else if (probability >= 0.50) {
            if (competition.equals("HIGH")) {
                return "Apply soon - competition is high. Make your application stand out with specific examples.";
            }
            return "Good timing to apply. Emphasize your relevant experience and willingness to learn.";
        } else if (probability >= 0.30) {
            return "Consider applying if very interested, but also work on missing skills. Apply to similar roles with lower requirements.";
        } else {
            return "Build missing skills first (2-3 months), then apply. Look for junior positions that match your current level.";
        }
    }
}

