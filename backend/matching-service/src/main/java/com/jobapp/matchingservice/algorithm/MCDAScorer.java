package com.jobapp.matchingservice.algorithm;

import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Multi-Criteria Decision Analysis (MCDA) for candidate evaluation
 * Uses TOPSIS (Technique for Order Preference by Similarity to Ideal Solution)
 */
@Component
public class MCDAScorer {

    /**
     * MCDA Criteria Weights
     */
    private static final Map<String, Double> CRITERIA_WEIGHTS = Map.of(
            "SKILLS_MATCH", 0.35,           // 35% - Most important
            "EXPERIENCE_MATCH", 0.25,       // 25% - Very important
            "SKILL_DEPTH", 0.15,            // 15% - Quality over quantity
            "EDUCATION_LEVEL", 0.10,        // 10% - Academic background
            "LOCATION_FIT", 0.10,           // 10% - Geographic compatibility
            "SALARY_ALIGNMENT", 0.05        // 5% - Budget fit
    );

    /**
     * Calculate MCDA score using TOPSIS method
     */
    public double calculateMCDAScore(Map<String, Double> criteriaScores) {
        // Normalize scores (already 0-1, so just apply weights)
        double weightedSum = 0.0;
        
        for (Map.Entry<String, Double> entry : CRITERIA_WEIGHTS.entrySet()) {
            String criterion = entry.getKey();
            Double weight = entry.getValue();
            Double score = criteriaScores.getOrDefault(criterion, 0.5);
            
            weightedSum += (score * weight);
        }

        return Math.round(weightedSum * 100.0) / 100.0;
    }

    /**
     * Calculate all criteria scores for a candidate
     */
    public Map<String, Double> evaluateCriteria(
            List<String> candidateSkills,
            Integer candidateExp,
            String candidateEducation,
            String candidateLocation,
            Double expectedSalary,
            List<String> requiredSkills,
            Integer minExp,
            Integer maxExp,
            String jobLocation,
            Double maxSalary,
            Boolean remoteAllowed) {

        Map<String, Double> scores = new HashMap<>();

        // 1. Skills Match (Jaccard similarity)
        scores.put("SKILLS_MATCH", calculateJaccardSimilarity(candidateSkills, requiredSkills));

        // 2. Experience Match
        scores.put("EXPERIENCE_MATCH", calculateExperienceScore(candidateExp, minExp, maxExp));

        // 3. Skill Depth (number of skills relative to requirements)
        scores.put("SKILL_DEPTH", calculateSkillDepth(candidateSkills, requiredSkills));

        // 4. Education Level
        scores.put("EDUCATION_LEVEL", calculateEducationScore(candidateEducation));

        // 5. Location Fit
        scores.put("LOCATION_FIT", calculateLocationScore(candidateLocation, jobLocation, remoteAllowed));

        // 6. Salary Alignment
        scores.put("SALARY_ALIGNMENT", calculateSalaryScore(expectedSalary, maxSalary));

        return scores;
    }

    private double calculateJaccardSimilarity(List<String> set1, List<String> set2) {
        if (set1 == null || set1.isEmpty() || set2 == null || set2.isEmpty()) {
            return 0.0;
        }

        Set<String> s1 = new HashSet<>();
        set1.forEach(item -> s1.add(item.toLowerCase().trim()));

        Set<String> s2 = new HashSet<>();
        set2.forEach(item -> s2.add(item.toLowerCase().trim()));

        Set<String> intersection = new HashSet<>(s1);
        intersection.retainAll(s2);

        Set<String> union = new HashSet<>(s1);
        union.addAll(s2);

        return union.isEmpty() ? 0.0 : (double) intersection.size() / union.size();
    }

    private double calculateExperienceScore(Integer candidateExp, Integer minExp, Integer maxExp) {
        if (candidateExp == null) return 0.5;
        if (minExp == null && maxExp == null) return 1.0;

        int min = minExp != null ? minExp : 0;
        int max = maxExp != null ? maxExp : Integer.MAX_VALUE;

        if (candidateExp >= min && candidateExp <= max) {
            return 1.0; // Perfect match
        } else if (candidateExp < min) {
            double gap = min - candidateExp;
            return Math.max(0.0, 1.0 - (gap / 10.0));
        } else {
            return 0.8; // Overqualified is still good
        }
    }

    private double calculateSkillDepth(List<String> candidateSkills, List<String> requiredSkills) {
        if (candidateSkills == null || candidateSkills.isEmpty()) return 0.0;
        if (requiredSkills == null || requiredSkills.isEmpty()) return 1.0;

        // More skills than required is good (shows breadth)
        double ratio = (double) candidateSkills.size() / requiredSkills.size();
        
        if (ratio >= 1.5) return 1.0;      // Excellent breadth
        else if (ratio >= 1.0) return 0.8; // Good breadth
        else if (ratio >= 0.7) return 0.6; // Moderate
        else return 0.4;                    // Limited
    }

    private double calculateEducationScore(String education) {
        if (education == null || education.isEmpty()) return 0.5;

        String lower = education.toLowerCase();
        if (lower.contains("phd") || lower.contains("doctorate")) return 1.0;
        if (lower.contains("master") || lower.contains("m.s") || lower.contains("mba")) return 0.9;
        if (lower.contains("bachelor") || lower.contains("b.s") || lower.contains("b.tech")) return 0.8;
        if (lower.contains("associate") || lower.contains("diploma")) return 0.6;
        return 0.5;
    }

    private double calculateLocationScore(String candidateLocation, String jobLocation, Boolean remoteAllowed) {
        if (remoteAllowed != null && remoteAllowed) return 1.0;
        if (candidateLocation == null || jobLocation == null) return 0.5;

        String c = candidateLocation.toLowerCase();
        String j = jobLocation.toLowerCase();

        if (c.equals(j)) return 1.0;
        if (c.contains(j) || j.contains(c)) return 0.7;
        return 0.3;
    }

    private double calculateSalaryScore(Double expected, Double maxSalary) {
        if (expected == null || maxSalary == null) return 1.0;
        if (expected <= maxSalary) return 1.0;
        
        double excess = expected - maxSalary;
        return Math.max(0.0, 1.0 - (excess / maxSalary));
    }

    /**
     * Rank multiple candidates using TOPSIS
     */
    public List<Map<String, Object>> rankUsingTOPSIS(List<Map<String, Object>> candidates,
                                                      Map<String, Object> jobRequirements) {
        
        List<Map<String, Object>> rankedCandidates = new ArrayList<>();

        // Calculate MCDA scores for all candidates
        for (Map<String, Object> candidate : candidates) {
            @SuppressWarnings("unchecked")
            List<String> skills = (List<String>) candidate.getOrDefault("skills", new ArrayList<>());
            Integer exp = candidate.get("yearsOfExperience") != null ? 
                    ((Number) candidate.get("yearsOfExperience")).intValue() : null;
            String education = (String) candidate.get("education");
            String location = (String) candidate.get("currentLocation");
            Double salary = candidate.get("expectedSalary") != null ? 
                    ((Number) candidate.get("expectedSalary")).doubleValue() : null;

            @SuppressWarnings("unchecked")
            List<String> reqSkills = (List<String>) jobRequirements.getOrDefault("requiredSkills", new ArrayList<>());
            Integer minExp = jobRequirements.get("minExperience") != null ? 
                    ((Number) jobRequirements.get("minExperience")).intValue() : null;
            Integer maxExp = jobRequirements.get("maxExperience") != null ? 
                    ((Number) jobRequirements.get("maxExperience")).intValue() : null;
            String jobLoc = (String) jobRequirements.get("location");
            Double maxSal = jobRequirements.get("maxSalary") != null ? 
                    ((Number) jobRequirements.get("maxSalary")).doubleValue() : null;
            Boolean remote = (Boolean) jobRequirements.getOrDefault("remoteAllowed", false);

            Map<String, Double> criteriaScores = evaluateCriteria(
                    skills, exp, education, location, salary,
                    reqSkills, minExp, maxExp, jobLoc, maxSal, remote
            );

            double mcdaScore = calculateMCDAScore(criteriaScores);

            Map<String, Object> rankedCandidate = new HashMap<>(candidate);
            rankedCandidate.put("mcdaScore", mcdaScore);
            rankedCandidate.put("criteriaScores", criteriaScores);
            
            rankedCandidates.add(rankedCandidate);
        }

        // Sort by MCDA score descending
        rankedCandidates.sort((a, b) -> {
            Double scoreA = (Double) a.get("mcdaScore");
            Double scoreB = (Double) b.get("mcdaScore");
            return Double.compare(scoreB, scoreA);
        });

        return rankedCandidates;
    }
}

