package com.jobapp.matchingservice.algorithm;

import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Semantic similarity using word embeddings and vector space models
 * Simulates Word2Vec-style embeddings for skill matching
 */
@Component
public class EmbeddingMatcher {

    // Simulated skill embeddings (in production, use pre-trained Word2Vec/GloVe)
    private static final Map<String, double[]> SKILL_EMBEDDINGS = new HashMap<>();
    
    static {
        // Initialize skill embeddings (simplified 10-dimensional vectors)
        // In production: Load from pre-trained Word2Vec model
        
        // Programming Languages (similar to each other)
        SKILL_EMBEDDINGS.put("java", new double[]{0.9, 0.8, 0.1, 0.2, 0.7, 0.3, 0.1, 0.4, 0.8, 0.6});
        SKILL_EMBEDDINGS.put("python", new double[]{0.8, 0.7, 0.2, 0.3, 0.6, 0.4, 0.2, 0.5, 0.7, 0.5});
        SKILL_EMBEDDINGS.put("javascript", new double[]{0.7, 0.6, 0.3, 0.4, 0.5, 0.5, 0.3, 0.6, 0.6, 0.4});
        SKILL_EMBEDDINGS.put("c++", new double[]{0.9, 0.9, 0.1, 0.1, 0.8, 0.2, 0.1, 0.3, 0.9, 0.7});
        SKILL_EMBEDDINGS.put("go", new double[]{0.8, 0.7, 0.2, 0.2, 0.7, 0.3, 0.2, 0.4, 0.8, 0.6});
        
        // Frameworks (related to their languages)
        SKILL_EMBEDDINGS.put("spring", new double[]{0.9, 0.8, 0.1, 0.3, 0.7, 0.4, 0.1, 0.5, 0.8, 0.6});
        SKILL_EMBEDDINGS.put("django", new double[]{0.8, 0.7, 0.2, 0.4, 0.6, 0.5, 0.2, 0.6, 0.7, 0.5});
        SKILL_EMBEDDINGS.put("react", new double[]{0.7, 0.6, 0.3, 0.5, 0.5, 0.6, 0.3, 0.7, 0.6, 0.4});
        SKILL_EMBEDDINGS.put("angular", new double[]{0.7, 0.6, 0.3, 0.5, 0.5, 0.6, 0.3, 0.7, 0.6, 0.5});
        SKILL_EMBEDDINGS.put("vue", new double[]{0.7, 0.6, 0.3, 0.5, 0.5, 0.6, 0.3, 0.7, 0.6, 0.4});
        
        // Databases
        SKILL_EMBEDDINGS.put("sql", new double[]{0.5, 0.4, 0.8, 0.7, 0.3, 0.6, 0.7, 0.4, 0.3, 0.5});
        SKILL_EMBEDDINGS.put("postgresql", new double[]{0.5, 0.4, 0.8, 0.7, 0.3, 0.6, 0.7, 0.4, 0.3, 0.5});
        SKILL_EMBEDDINGS.put("mysql", new double[]{0.5, 0.4, 0.8, 0.7, 0.3, 0.6, 0.7, 0.4, 0.3, 0.5});
        SKILL_EMBEDDINGS.put("mongodb", new double[]{0.4, 0.3, 0.7, 0.6, 0.2, 0.5, 0.8, 0.5, 0.2, 0.4});
        SKILL_EMBEDDINGS.put("redis", new double[]{0.4, 0.3, 0.7, 0.6, 0.2, 0.5, 0.8, 0.5, 0.2, 0.4});
        
        // Cloud & DevOps
        SKILL_EMBEDDINGS.put("aws", new double[]{0.3, 0.2, 0.4, 0.3, 0.2, 0.8, 0.9, 0.8, 0.2, 0.3});
        SKILL_EMBEDDINGS.put("azure", new double[]{0.3, 0.2, 0.4, 0.3, 0.2, 0.8, 0.9, 0.8, 0.2, 0.3});
        SKILL_EMBEDDINGS.put("gcp", new double[]{0.3, 0.2, 0.4, 0.3, 0.2, 0.8, 0.9, 0.8, 0.2, 0.3});
        SKILL_EMBEDDINGS.put("docker", new double[]{0.4, 0.3, 0.3, 0.2, 0.3, 0.7, 0.9, 0.9, 0.3, 0.4});
        SKILL_EMBEDDINGS.put("kubernetes", new double[]{0.4, 0.3, 0.3, 0.2, 0.3, 0.7, 0.9, 0.9, 0.3, 0.4});
        
        // AI/ML
        SKILL_EMBEDDINGS.put("machine learning", new double[]{0.2, 0.3, 0.2, 0.8, 0.1, 0.4, 0.2, 0.3, 0.1, 0.9});
        SKILL_EMBEDDINGS.put("deep learning", new double[]{0.2, 0.3, 0.2, 0.9, 0.1, 0.4, 0.2, 0.3, 0.1, 0.9});
        SKILL_EMBEDDINGS.put("tensorflow", new double[]{0.2, 0.3, 0.2, 0.9, 0.1, 0.4, 0.2, 0.3, 0.1, 0.9});
    }

    /**
     * Calculate semantic similarity between two skills using embeddings
     */
    public double calculateSemanticSimilarity(String skill1, String skill2) {
        String s1 = skill1.toLowerCase().trim();
        String s2 = skill2.toLowerCase().trim();
        
        // Exact match
        if (s1.equals(s2)) return 1.0;
        
        // Get embeddings
        double[] vec1 = SKILL_EMBEDDINGS.getOrDefault(s1, generateDefaultEmbedding());
        double[] vec2 = SKILL_EMBEDDINGS.getOrDefault(s2, generateDefaultEmbedding());
        
        // Calculate cosine similarity
        return cosineSimilarity(vec1, vec2);
    }

    /**
     * Enhanced skill matching using embeddings
     * Finds related skills, not just exact matches
     */
    public double enhancedSkillMatching(List<String> candidateSkills, List<String> requiredSkills) {
        if (candidateSkills == null || candidateSkills.isEmpty() || 
            requiredSkills == null || requiredSkills.isEmpty()) {
            return 0.0;
        }

        double totalSimilarity = 0.0;
        
        for (String requiredSkill : requiredSkills) {
            double maxSimilarity = 0.0;
            
            // Find best matching skill from candidate
            for (String candidateSkill : candidateSkills) {
                double similarity = calculateSemanticSimilarity(requiredSkill, candidateSkill);
                maxSimilarity = Math.max(maxSimilarity, similarity);
            }
            
            totalSimilarity += maxSimilarity;
        }
        
        // Average similarity
        return totalSimilarity / requiredSkills.size();
    }

    /**
     * Cosine similarity between two vectors
     */
    private double cosineSimilarity(double[] vec1, double[] vec2) {
        if (vec1.length != vec2.length) return 0.0;
        
        double dotProduct = 0.0;
        double norm1 = 0.0;
        double norm2 = 0.0;
        
        for (int i = 0; i < vec1.length; i++) {
            dotProduct += vec1[i] * vec2[i];
            norm1 += vec1[i] * vec1[i];
            norm2 += vec2[i] * vec2[i];
        }
        
        if (norm1 == 0.0 || norm2 == 0.0) return 0.0;
        
        return dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }

    /**
     * Generate default embedding for unknown skills
     */
    private double[] generateDefaultEmbedding() {
        return new double[]{0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5};
    }

    /**
     * Calculate hiring probability (0-100%)
     * Based on historical data and current match
     */
    public double calculateHiringProbability(
            double skillsMatch,
            double experienceMatch,
            double educationMatch,
            double locationMatch,
            int totalApplicants,
            int candidateRank) {
        
        // Base probability from match scores
        double baseProb = (skillsMatch * 0.4) + 
                         (experienceMatch * 0.3) + 
                         (educationMatch * 0.2) + 
                         (locationMatch * 0.1);
        
        // Adjust for competition
        double competitionFactor = 1.0;
        if (totalApplicants > 0 && candidateRank > 0) {
            // Top 10% gets boost, bottom 50% gets penalty
            double percentile = (double) candidateRank / totalApplicants;
            
            if (percentile <= 0.1) competitionFactor = 1.3;      // Top 10%
            else if (percentile <= 0.25) competitionFactor = 1.1; // Top 25%
            else if (percentile <= 0.5) competitionFactor = 1.0;  // Top 50%
            else competitionFactor = 0.7;                         // Bottom 50%
        }
        
        double probability = baseProb * competitionFactor;
        
        // Cap at realistic ranges
        probability = Math.min(0.95, Math.max(0.05, probability));
        
        return Math.round(probability * 100.0) / 100.0;
    }

    /**
     * Analyze job description text semantically
     */
    public List<String> extractImpliedSkills(String jobDescription) {
        List<String> impliedSkills = new ArrayList<>();
        String lower = jobDescription.toLowerCase();
        
        // Map phrases to skills
        Map<String, String> phraseToSkill = Map.of(
            "microservices", "Microservices",
            "rest api", "REST",
            "cloud", "AWS",
            "containers", "Docker",
            "orchestration", "Kubernetes",
            "version control", "Git",
            "ci/cd", "Jenkins",
            "agile", "Agile"
        );
        
        for (Map.Entry<String, String> entry : phraseToSkill.entrySet()) {
            if (lower.contains(entry.getKey())) {
                impliedSkills.add(entry.getValue());
            }
        }
        
        return impliedSkills;
    }
}

