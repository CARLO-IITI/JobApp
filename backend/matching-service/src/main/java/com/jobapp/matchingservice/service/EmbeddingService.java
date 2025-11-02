package com.jobapp.matchingservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for generating embeddings from text.
 * Uses TF-IDF and semantic analysis to create vector representations.
 */
@Service
@Slf4j
public class EmbeddingService {

    // Common tech skills vocabulary for better matching
    private static final Set<String> TECH_VOCABULARY = new HashSet<>(Arrays.asList(
            "java", "python", "javascript", "typescript", "react", "angular", "vue",
            "node", "spring", "django", "flask", "express", "mongodb", "postgresql",
            "mysql", "redis", "docker", "kubernetes", "aws", "azure", "gcp",
            "microservices", "rest", "api", "graphql", "git", "ci", "cd",
            "agile", "scrum", "junit", "testing", "tdd", "machine", "learning",
            "data", "science", "backend", "frontend", "fullstack", "devops",
            "security", "cloud", "mobile", "android", "ios", "swift", "kotlin",
            "html", "css", "sass", "webpack", "gradle", "maven", "npm"
    ));

    // Experience level weights
    private static final Map<String, Double> EXPERIENCE_WEIGHTS = Map.of(
            "ENTRY", 0.5,
            "JUNIOR", 1.0,
            "MID", 1.5,
            "SENIOR", 2.0,
            "LEAD", 2.5
    );

    /**
     * Generate embedding vector from job description
     */
    public Map<String, Object> generateJobEmbedding(String title, String description, 
                                                      List<String> requiredSkills,
                                                      String experienceLevel,
                                                      Integer minExperience,
                                                      String location,
                                                      Boolean remoteAllowed) {
        log.info("Generating embedding for job: {}", title);
        
        Map<String, Object> embedding = new HashMap<>();
        
        // Combine all text for analysis
        StringBuilder fullText = new StringBuilder();
        fullText.append(title.toLowerCase()).append(" ");
        fullText.append(description.toLowerCase()).append(" ");
        if (requiredSkills != null) {
            fullText.append(String.join(" ", requiredSkills).toLowerCase());
        }
        
        // Generate TF-IDF-like vector
        Map<String, Double> skillVector = extractSkillVector(fullText.toString(), requiredSkills);
        
        // Calculate embedding features
        embedding.put("skillVector", skillVector);
        embedding.put("skillCount", requiredSkills != null ? requiredSkills.size() : 0);
        embedding.put("experienceLevel", experienceLevel);
        embedding.put("experienceWeight", EXPERIENCE_WEIGHTS.getOrDefault(experienceLevel, 1.0));
        embedding.put("minExperience", minExperience != null ? minExperience : 0);
        embedding.put("location", location != null ? location.toLowerCase() : "");
        embedding.put("remoteAllowed", remoteAllowed != null ? remoteAllowed : false);
        embedding.put("descriptionLength", description.length());
        embedding.put("techDensity", calculateTechDensity(fullText.toString()));
        
        // Extract key terms
        embedding.put("keyTerms", extractKeyTerms(fullText.toString()));
        
        log.debug("Generated embedding with {} skill dimensions", skillVector.size());
        return embedding;
    }

    /**
     * Generate embedding vector from candidate profile
     */
    public Map<String, Object> generateCandidateEmbedding(String headline,
                                                           String summary,
                                                           List<String> skills,
                                                           Integer yearsOfExperience,
                                                           String currentLocation,
                                                           Boolean openToRemote,
                                                           List<Map<String, Object>> workExperiences) {
        log.info("Generating candidate embedding");
        
        Map<String, Object> embedding = new HashMap<>();
        
        // Combine all text
        StringBuilder fullText = new StringBuilder();
        if (headline != null) fullText.append(headline.toLowerCase()).append(" ");
        if (summary != null) fullText.append(summary.toLowerCase()).append(" ");
        if (skills != null) {
            fullText.append(String.join(" ", skills).toLowerCase());
        }
        
        // Add work experience descriptions
        if (workExperiences != null) {
            for (Map<String, Object> exp : workExperiences) {
                if (exp.get("description") != null) {
                    fullText.append(exp.get("description").toString().toLowerCase()).append(" ");
                }
                if (exp.get("technologies") != null) {
                    @SuppressWarnings("unchecked")
                    List<String> techs = (List<String>) exp.get("technologies");
                    fullText.append(String.join(" ", techs).toLowerCase()).append(" ");
                }
            }
        }
        
        // Generate skill vector
        Map<String, Double> skillVector = extractSkillVector(fullText.toString(), skills);
        
        // Calculate embedding features
        embedding.put("skillVector", skillVector);
        embedding.put("skillCount", skills != null ? skills.size() : 0);
        embedding.put("yearsOfExperience", yearsOfExperience != null ? yearsOfExperience : 0);
        embedding.put("experienceLevel", inferExperienceLevel(yearsOfExperience));
        embedding.put("location", currentLocation != null ? currentLocation.toLowerCase() : "");
        embedding.put("openToRemote", openToRemote != null ? openToRemote : false);
        embedding.put("profileCompleteness", calculateProfileCompleteness(headline, summary, skills, workExperiences));
        embedding.put("techDensity", calculateTechDensity(fullText.toString()));
        
        // Extract key terms
        embedding.put("keyTerms", extractKeyTerms(fullText.toString()));
        
        log.debug("Generated candidate embedding with {} skill dimensions", skillVector.size());
        return embedding;
    }

    /**
     * Extract skill vector using TF-IDF-like approach
     */
    private Map<String, Double> extractSkillVector(String text, List<String> explicitSkills) {
        Map<String, Double> vector = new HashMap<>();
        
        // Add explicit skills with high weight
        if (explicitSkills != null) {
            for (String skill : explicitSkills) {
                String normalizedSkill = skill.toLowerCase().trim();
                vector.put(normalizedSkill, 2.0); // High weight for explicit skills
            }
        }
        
        // Extract tech terms from text
        String[] words = text.split("\\W+");
        Map<String, Integer> termFrequency = new HashMap<>();
        
        for (String word : words) {
            String normalized = word.toLowerCase().trim();
            if (TECH_VOCABULARY.contains(normalized) || 
                (explicitSkills != null && explicitSkills.stream()
                    .anyMatch(s -> s.toLowerCase().contains(normalized)))) {
                termFrequency.put(normalized, termFrequency.getOrDefault(normalized, 0) + 1);
            }
        }
        
        // Calculate TF-IDF weights
        int maxFreq = termFrequency.values().stream().max(Integer::compareTo).orElse(1);
        for (Map.Entry<String, Integer> entry : termFrequency.entrySet()) {
            double tf = (double) entry.getValue() / maxFreq;
            double weight = vector.getOrDefault(entry.getKey(), 0.0);
            vector.put(entry.getKey(), Math.max(weight, tf));
        }
        
        return vector;
    }

    /**
     * Calculate technology density in text
     */
    private double calculateTechDensity(String text) {
        String[] words = text.split("\\W+");
        if (words.length == 0) return 0.0;
        
        long techWordCount = Arrays.stream(words)
                .filter(word -> TECH_VOCABULARY.contains(word.toLowerCase()))
                .count();
        
        return (double) techWordCount / words.length;
    }

    /**
     * Extract key terms from text
     */
    private List<String> extractKeyTerms(String text) {
        String[] words = text.split("\\W+");
        Map<String, Integer> wordCount = new HashMap<>();
        
        for (String word : words) {
            String normalized = word.toLowerCase().trim();
            if (normalized.length() > 3 && TECH_VOCABULARY.contains(normalized)) {
                wordCount.put(normalized, wordCount.getOrDefault(normalized, 0) + 1);
            }
        }
        
        return wordCount.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(10)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    /**
     * Infer experience level from years of experience
     */
    private String inferExperienceLevel(Integer years) {
        if (years == null || years < 1) return "ENTRY";
        if (years < 3) return "JUNIOR";
        if (years < 6) return "MID";
        if (years < 10) return "SENIOR";
        return "LEAD";
    }

    /**
     * Calculate profile completeness score
     */
    private double calculateProfileCompleteness(String headline, String summary, 
                                                  List<String> skills,
                                                  List<Map<String, Object>> workExperiences) {
        double score = 0.0;
        
        if (headline != null && !headline.isEmpty()) score += 0.2;
        if (summary != null && summary.length() > 50) score += 0.2;
        if (skills != null && skills.size() >= 3) score += 0.3;
        if (workExperiences != null && !workExperiences.isEmpty()) score += 0.3;
        
        return score;
    }

    /**
     * Calculate cosine similarity between two embedding vectors
     */
    public double calculateCosineSimilarity(Map<String, Object> embedding1, 
                                           Map<String, Object> embedding2) {
        @SuppressWarnings("unchecked")
        Map<String, Double> vector1 = (Map<String, Double>) embedding1.get("skillVector");
        @SuppressWarnings("unchecked")
        Map<String, Double> vector2 = (Map<String, Double>) embedding2.get("skillVector");
        
        if (vector1 == null || vector2 == null || vector1.isEmpty() || vector2.isEmpty()) {
            return 0.0;
        }
        
        // Get all unique keys
        Set<String> allKeys = new HashSet<>();
        allKeys.addAll(vector1.keySet());
        allKeys.addAll(vector2.keySet());
        
        // Calculate dot product and magnitudes
        double dotProduct = 0.0;
        double magnitude1 = 0.0;
        double magnitude2 = 0.0;
        
        for (String key : allKeys) {
            double val1 = vector1.getOrDefault(key, 0.0);
            double val2 = vector2.getOrDefault(key, 0.0);
            
            dotProduct += val1 * val2;
            magnitude1 += val1 * val1;
            magnitude2 += val2 * val2;
        }
        
        magnitude1 = Math.sqrt(magnitude1);
        magnitude2 = Math.sqrt(magnitude2);
        
        if (magnitude1 == 0.0 || magnitude2 == 0.0) {
            return 0.0;
        }
        
        return dotProduct / (magnitude1 * magnitude2);
    }
}

