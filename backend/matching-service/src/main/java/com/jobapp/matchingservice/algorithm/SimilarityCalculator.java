package com.jobapp.matchingservice.algorithm;

import org.apache.commons.text.similarity.CosineSimilarity;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Advanced similarity calculation using various algorithms
 */
@Component
public class SimilarityCalculator {

    /**
     * Calculate cosine similarity between two skill sets
     * Returns value between 0 and 1
     */
    public double calculateSkillSimilarity(List<String> candidateSkills, List<String> requiredSkills) {
        if (candidateSkills == null || candidateSkills.isEmpty() || 
            requiredSkills == null || requiredSkills.isEmpty()) {
            return 0.0;
        }

        // Normalize skills to lowercase
        Set<String> normalizedCandidateSkills = new HashSet<>();
        candidateSkills.forEach(skill -> normalizedCandidateSkills.add(skill.toLowerCase().trim()));

        Set<String> normalizedRequiredSkills = new HashSet<>();
        requiredSkills.forEach(skill -> normalizedRequiredSkills.add(skill.toLowerCase().trim()));

        // Create vectors for cosine similarity
        Set<String> allSkills = new HashSet<>();
        allSkills.addAll(normalizedCandidateSkills);
        allSkills.addAll(normalizedRequiredSkills);

        Map<CharSequence, Integer> candidateVector = new HashMap<>();
        Map<CharSequence, Integer> requiredVector = new HashMap<>();

        for (String skill : allSkills) {
            candidateVector.put(skill, normalizedCandidateSkills.contains(skill) ? 1 : 0);
            requiredVector.put(skill, normalizedRequiredSkills.contains(skill) ? 1 : 0);
        }

        CosineSimilarity cosineSimilarity = new CosineSimilarity();
        return cosineSimilarity.cosineSimilarity(candidateVector, requiredVector);
    }

    /**
     * Calculate Jaccard similarity coefficient
     */
    public double calculateJaccardSimilarity(List<String> set1, List<String> set2) {
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

        if (union.isEmpty()) {
            return 0.0;
        }

        return (double) intersection.size() / union.size();
    }

    /**
     * Calculate experience match score
     */
    public double calculateExperienceMatch(Integer candidateExperience, 
                                          Integer minRequired, 
                                          Integer maxRequired) {
        if (candidateExperience == null) {
            return 0.5; // Default moderate score if not specified
        }

        if (minRequired == null && maxRequired == null) {
            return 1.0; // No requirement specified
        }

        int min = minRequired != null ? minRequired : 0;
        int max = maxRequired != null ? maxRequired : Integer.MAX_VALUE;

        if (candidateExperience >= min && candidateExperience <= max) {
            return 1.0; // Perfect match
        } else if (candidateExperience < min) {
            // Below minimum - calculate how close they are
            double gap = min - candidateExperience;
            return Math.max(0.0, 1.0 - (gap / 5.0)); // Reduce score based on gap
        } else {
            // Above maximum - slight preference for more experience
            double excess = candidateExperience - max;
            return Math.max(0.7, 1.0 - (excess / 10.0));
        }
    }

    /**
     * Calculate location match score
     */
    public double calculateLocationMatch(String jobLocation, 
                                        String candidateLocation,
                                        List<String> preferredLocations,
                                        Boolean remoteAllowed,
                                        Boolean openToRemote) {
        if (remoteAllowed && openToRemote) {
            return 1.0; // Perfect match for remote
        }

        if (jobLocation == null || candidateLocation == null) {
            return 0.5; // Default moderate score
        }

        String normalizedJobLocation = jobLocation.toLowerCase().trim();
        String normalizedCandidateLocation = candidateLocation.toLowerCase().trim();

        // Exact location match
        if (normalizedJobLocation.equals(normalizedCandidateLocation)) {
            return 1.0;
        }

        // Check preferred locations
        if (preferredLocations != null) {
            for (String preferred : preferredLocations) {
                if (normalizedJobLocation.equals(preferred.toLowerCase().trim())) {
                    return 0.9; // High score for preferred location
                }
            }
        }

        // Check if same city/state (partial match)
        if (normalizedJobLocation.contains(normalizedCandidateLocation) || 
            normalizedCandidateLocation.contains(normalizedJobLocation)) {
            return 0.6;
        }

        return 0.3; // Different location, lower score
    }

    /**
     * Calculate salary expectation match
     */
    public double calculateSalaryMatch(Double expectedSalary, 
                                      Double minSalary, 
                                      Double maxSalary) {
        if (expectedSalary == null || (minSalary == null && maxSalary == null)) {
            return 1.0; // No constraints
        }

        double min = minSalary != null ? minSalary : 0;
        double max = maxSalary != null ? maxSalary : Double.MAX_VALUE;

        if (expectedSalary >= min && expectedSalary <= max) {
            return 1.0; // Perfect match
        } else if (expectedSalary < min) {
            return 0.9; // Candidate expects less - good for employer
        } else {
            // Candidate expects more
            double excess = expectedSalary - max;
            double range = max - min;
            if (range > 0) {
                return Math.max(0.0, 1.0 - (excess / range));
            }
            return 0.5;
        }
    }
}

