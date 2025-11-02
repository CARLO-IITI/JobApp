package com.jobapp.matchingservice.algorithm;

import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Pattern;

/**
 * AI-powered Resume Authenticity Detector
 * Uses psychological profiling, consistency analysis, and pattern recognition
 * to identify potential resume fraud and find genuine candidates
 */
@Component
public class AuthenticityDetector {

    /**
     * Calculate authenticity score (0-100%)
     * Higher = More authentic/genuine
     */
    public Map<String, Object> analyzeAuthenticity(Map<String, Object> profile, Map<String, Object> resumeData) {
        Map<String, Object> analysis = new HashMap<>();
        
        List<String> redFlags = new ArrayList<>();
        List<String> greenFlags = new ArrayList<>();
        double authenticityScore = 100.0; // Start with perfect score, deduct for issues

        // 1. Skill-Experience Consistency Check
        Map<String, Object> skillExpCheck = checkSkillExperienceConsistency(profile);
        if (!(Boolean) skillExpCheck.get("consistent")) {
            redFlags.addAll((List<String>) skillExpCheck.get("issues"));
            authenticityScore -= 15;
        } else {
            greenFlags.add("Skills match experience level appropriately");
        }

        // 2. Skill Inflation Detection
        Map<String, Object> inflationCheck = detectSkillInflation(profile);
        if ((Boolean) inflationCheck.get("inflated")) {
            redFlags.addAll((List<String>) inflationCheck.get("reasons"));
            authenticityScore -= 20;
        } else {
            greenFlags.add("Realistic skill set for experience level");
        }

        // 3. Timeline Consistency
        Map<String, Object> timelineCheck = checkTimelineConsistency(profile);
        if (!(Boolean) timelineCheck.get("consistent")) {
            redFlags.addAll((List<String>) timelineCheck.get("issues"));
            authenticityScore -= 15;
        } else {
            greenFlags.add("Career timeline is logical and consistent");
        }

        // 4. Psychological Red Flags
        List<String> psychFlags = detectPsychologicalRedFlags(profile);
        if (!psychFlags.isEmpty()) {
            redFlags.addAll(psychFlags);
            authenticityScore -= (psychFlags.size() * 10);
        }

        // 5. Skill Clustering Analysis
        Map<String, Object> clusterCheck = analyzeSkillClusters(profile);
        if ((Boolean) clusterCheck.get("suspicious")) {
            redFlags.addAll((List<String>) clusterCheck.get("reasons"));
            authenticityScore -= 10;
        } else {
            greenFlags.add("Skills show coherent expertise in related areas");
        }

        // 6. Genuine Candidate Indicators
        List<String> genuineIndicators = detectGenuineIndicators(profile);
        greenFlags.addAll(genuineIndicators);
        authenticityScore += (genuineIndicators.size() * 5); // Bonus for genuine signs

        // Cap score between 0-100
        authenticityScore = Math.max(0, Math.min(100, authenticityScore));

        // Determine trust level
        String trustLevel = determineTrustLevel(authenticityScore);
        String recommendation = generateRecommendation(authenticityScore, redFlags.size());

        analysis.put("authenticityScore", Math.round(authenticityScore * 10.0) / 10.0);
        analysis.put("trustLevel", trustLevel);
        analysis.put("redFlags", redFlags);
        analysis.put("greenFlags", greenFlags);
        analysis.put("recommendation", recommendation);
        analysis.put("verificationSuggestions", generateVerificationSteps(redFlags));
        
        return analysis;
    }

    /**
     * Check if skills match experience level (Psychological consistency)
     */
    private Map<String, Object> checkSkillExperienceConsistency(Map<String, Object> profile) {
        Map<String, Object> result = new HashMap<>();
        List<String> issues = new ArrayList<>();
        
        Integer experience = profile.get("yearsOfExperience") != null ? 
                ((Number) profile.get("yearsOfExperience")).intValue() : 0;
        
        @SuppressWarnings("unchecked")
        List<String> skills = (List<String>) profile.getOrDefault("skills", new ArrayList<>());

        // Red Flag: Too many skills for experience level
        if (experience < 2 && skills.size() > 15) {
            issues.add("🚩 Unrealistic: " + skills.size() + " skills claimed with only " + experience + " year(s) experience");
        } else if (experience < 5 && skills.size() > 25) {
            issues.add("🚩 Suspicious: Too many skills for " + experience + " years experience");
        }

        // Red Flag: Advanced skills with minimal experience
        if (experience < 2) {
            List<String> advancedSkills = Arrays.asList("kubernetes", "aws", "system design", "microservices", "machine learning");
            long advancedCount = skills.stream()
                    .filter(s -> advancedSkills.stream().anyMatch(a -> s.toLowerCase().contains(a)))
                    .count();
            
            if (advancedCount > 2) {
                issues.add("🚩 Inconsistent: Claims advanced skills (Kubernetes, AWS) with <2 years experience");
            }
        }

        result.put("consistent", issues.isEmpty());
        result.put("issues", issues);
        return result;
    }

    /**
     * Detect skill list inflation (Psychological: Overconfidence/Dishonesty)
     */
    private Map<String, Object> detectSkillInflation(Map<String, Object> profile) {
        Map<String, Object> result = new HashMap<>();
        List<String> reasons = new ArrayList<>();
        
        @SuppressWarnings("unchecked")
        List<String> skills = (List<String>) profile.getOrDefault("skills", new ArrayList<>());

        // Red Flag 1: Too many unrelated skills (Jack of all trades, master of none?)
        if (skills.size() > 30) {
            reasons.add("🚩 Over-inflation: " + skills.size() + " skills listed (typical: 8-15)");
        }

        // Red Flag 2: Claiming ALL popular technologies
        List<String> trendingSkills = Arrays.asList("react", "kubernetes", "aws", "machine learning", 
                "blockchain", "ai", "cloud", "microservices");
        long trendingCount = skills.stream()
                .filter(s -> trendingSkills.stream().anyMatch(t -> s.toLowerCase().contains(t)))
                .count();
        
        if (trendingCount >= 6) {
            reasons.add("🚩 Suspicious: Claims expertise in ALL trending technologies");
        }

        // Red Flag 3: Conflicting technology stacks
        boolean hasJava = skills.stream().anyMatch(s -> s.toLowerCase().contains("java"));
        boolean hasDotNet = skills.stream().anyMatch(s -> s.toLowerCase().contains(".net") || s.toLowerCase().contains("c#"));
        boolean hasPHP = skills.stream().anyMatch(s -> s.toLowerCase().contains("php"));
        
        int backendStacks = (hasJava ? 1 : 0) + (hasDotNet ? 1 : 0) + (hasPHP ? 1 : 0);
        if (backendStacks >= 3) {
            reasons.add("⚠️ Unusual: Claims expertise in multiple competing backend stacks");
        }

        result.put("inflated", !reasons.isEmpty());
        result.put("reasons", reasons);
        return result;
    }

    /**
     * Check career timeline logic
     */
    private Map<String, Object> checkTimelineConsistency(Map<String, Object> profile) {
        Map<String, Object> result = new HashMap<>();
        List<String> issues = new ArrayList<>();

        Integer experience = profile.get("yearsOfExperience") != null ? 
                ((Number) profile.get("yearsOfExperience")).intValue() : 0;
        String education = (String) profile.get("education");

        // Check if experience matches education timeline
        if (education != null) {
            String lower = education.toLowerCase();
            
            // Red Flag: PhD with only 1 year experience (impossible)
            if (lower.contains("phd") && experience < 3) {
                issues.add("🚩 Timeline Error: PhD typically requires 4-6 years, but only " + experience + " years total experience");
            }
            
            // Red Flag: Master's with 0-1 year
            if (lower.contains("master") && experience < 1) {
                issues.add("🚩 Inconsistent: Master's degree but no work experience listed");
            }
        }

        result.put("consistent", issues.isEmpty());
        result.put("issues", issues);
        return result;
    }

    /**
     * Psychological red flags (Dishonesty patterns)
     */
    private List<String> detectPsychologicalRedFlags(Map<String, Object> profile) {
        List<String> flags = new ArrayList<>();
        
        String summary = (String) profile.get("summary");
        
        if (summary != null) {
            String lower = summary.toLowerCase();
            
            // Red Flag: Excessive superlatives (overcompensation)
            long superlatives = countSuperlatives(lower);
            if (superlatives > 5) {
                flags.add("⚠️ Over-confident language: Excessive use of superlatives (best, expert, master)");
            }

            // Red Flag: Vague claims without specifics
            if (lower.contains("many projects") || lower.contains("various companies") || 
                lower.contains("numerous") || lower.contains("extensive")) {
                if (!lower.contains("at ") && !lower.contains("for ")) { // No specific names
                    flags.add("⚠️ Vague claims: Uses broad terms without specific examples");
                }
            }

            // Green indicator: Specific numbers and metrics
            if (Pattern.compile("\\d+%|\\d+ projects|\\d+ years").matcher(lower).find()) {
                // Good sign - specific metrics (not a red flag)
            }
        }

        return flags;
    }

    /**
     * Analyze skill clustering (Coherence check)
     */
    private Map<String, Object> analyzeSkillClusters(Map<String, Object> profile) {
        Map<String, Object> result = new HashMap<>();
        List<String> reasons = new ArrayList<>();
        
        @SuppressWarnings("unchecked")
        List<String> skills = (List<String>) profile.getOrDefault("skills", new ArrayList<>());

        // Check if skills form coherent groups
        boolean hasFrontend = hasAnySkill(skills, Arrays.asList("react", "angular", "vue", "html", "css"));
        boolean hasBackend = hasAnySkill(skills, Arrays.asList("java", "python", "node", "spring", "django"));
        boolean hasDatabase = hasAnySkill(skills, Arrays.asList("sql", "mongodb", "postgresql", "mysql"));
        boolean hasDevOps = hasAnySkill(skills, Arrays.asList("docker", "kubernetes", "jenkins", "aws"));
        boolean hasML = hasAnySkill(skills, Arrays.asList("machine learning", "tensorflow", "pytorch"));
        boolean hasMobile = hasAnySkill(skills, Arrays.asList("android", "ios", "react native", "flutter"));

        int domains = (hasFrontend ? 1 : 0) + (hasBackend ? 1 : 0) + (hasML ? 1 : 0) + (hasMobile ? 1 : 0);

        // Red Flag: Claims expertise in too many unrelated domains
        if (domains >= 4) {
            reasons.add("⚠️ Scattered expertise: Claims skills across " + domains + " different domains (Frontend, Backend, ML, Mobile)");
        }

        result.put("suspicious", !reasons.isEmpty());
        result.put("reasons", reasons);
        return result;
    }

    /**
     * Detect genuine candidate indicators (Positive signs)
     */
    private List<String> detectGenuineIndicators(Map<String, Object> profile) {
        List<String> indicators = new ArrayList<>();

        String portfolio = (String) profile.get("portfolioUrl");
        String github = (String) profile.get("githubUrl");
        String linkedin = (String) profile.get("linkedinUrl");
        
        @SuppressWarnings("unchecked")
        List<String> skills = (List<String>) profile.getOrDefault("skills", new ArrayList<>());

        // Genuine Indicator 1: Has portfolio/GitHub
        if (portfolio != null && !portfolio.isEmpty()) {
            indicators.add("✅ Has portfolio website (shows real work)");
        }
        if (github != null && !github.isEmpty()) {
            indicators.add("✅ Has GitHub profile (verifiable code)");
        }
        if (linkedin != null && !linkedin.isEmpty()) {
            indicators.add("✅ Has LinkedIn profile (social proof)");
        }

        // Genuine Indicator 2: Focused skill set (specialist)
        if (skills.size() >= 5 && skills.size() <= 15) {
            indicators.add("✅ Focused expertise (realistic skill count)");
        }

        // Genuine Indicator 3: Coherent tech stack
        boolean hasFrontend = hasAnySkill(skills, Arrays.asList("react", "vue", "angular"));
        boolean hasBackend = hasAnySkill(skills, Arrays.asList("node", "express", "spring"));
        boolean hasDatabase = hasAnySkill(skills, Arrays.asList("mongodb", "postgresql", "sql"));
        
        if (hasFrontend && hasBackend && hasDatabase) {
            indicators.add("✅ Coherent full-stack skill set (logical combination)");
        }

        return indicators;
    }

    /**
     * Generate verification steps for recruiters
     */
    private List<String> generateVerificationSteps(List<String> redFlags) {
        List<String> steps = new ArrayList<>();

        if (redFlags.isEmpty()) {
            steps.add("Profile appears authentic - standard interview process recommended");
            return steps;
        }

        steps.add("🔍 Recommended Verification Steps:");
        
        if (redFlags.stream().anyMatch(f -> f.contains("skill"))) {
            steps.add("• Ask specific technical questions about claimed skills");
            steps.add("• Request code samples or GitHub repositories");
            steps.add("• Conduct technical assessment test");
        }

        if (redFlags.stream().anyMatch(f -> f.contains("experience"))) {
            steps.add("• Ask for detailed project descriptions");
            steps.add("• Verify employment dates with references");
            steps.add("• Ask behavioral questions about experience");
        }

        if (redFlags.stream().anyMatch(f -> f.contains("Timeline"))) {
            steps.add("• Request academic transcripts");
            steps.add("• Verify employment history");
        }

        steps.add("• LinkedIn profile cross-verification");
        steps.add("• Reference checks (if shortlisted)");

        return steps;
    }

    private String determineTrustLevel(double score) {
        if (score >= 85) return "HIGH";
        if (score >= 70) return "MEDIUM";
        if (score >= 50) return "LOW";
        return "VERY_LOW";
    }

    private String generateRecommendation(double score, int redFlagCount) {
        if (score >= 90 && redFlagCount == 0) {
            return "✅ VERIFIED GENUINE - Profile appears highly authentic with no red flags. Proceed with confidence.";
        } else if (score >= 75 && redFlagCount <= 1) {
            return "✅ LIKELY GENUINE - Profile appears mostly authentic with minor inconsistencies. Proceed with standard verification.";
        } else if (score >= 60 && redFlagCount <= 3) {
            return "⚠️ REQUIRES VERIFICATION - Some inconsistencies detected. Conduct thorough technical assessment before proceeding.";
        } else {
            return "🚨 HIGH RISK - Multiple red flags detected. Extensive verification required. Consider technical test, code review, and reference checks.";
        }
    }

    private long countSuperlatives(String text) {
        List<String> superlatives = Arrays.asList("best", "expert", "master", "guru", "ninja", 
                "rockstar", "excellent", "outstanding", "exceptional", "world-class");
        
        return superlatives.stream()
                .filter(text::contains)
                .count();
    }

    private boolean hasAnySkill(List<String> skills, List<String> keywords) {
        return skills.stream()
                .anyMatch(s -> keywords.stream()
                        .anyMatch(k -> s.toLowerCase().contains(k)));
    }

    /**
     * Cross-reference analysis (Future: Check GitHub, LinkedIn APIs)
     */
    public Map<String, Object> crossReferenceValidation(Map<String, Object> profile) {
        Map<String, Object> validation = new HashMap<>();
        List<String> suggestions = new ArrayList<>();

        String github = (String) profile.get("githubUrl");
        String linkedin = (String) profile.get("linkedinUrl");
        String portfolio = (String) profile.get("portfolioUrl");

        if (github == null || github.isEmpty()) {
            suggestions.add("Recommend requesting GitHub profile for code verification");
        }

        if (linkedin == null || linkedin.isEmpty()) {
            suggestions.add("Recommend LinkedIn profile for employment history verification");
        }

        if (portfolio == null || portfolio.isEmpty()) {
            suggestions.add("Recommend portfolio for project showcases");
        }

        validation.put("hasGitHub", github != null && !github.isEmpty());
        validation.put("hasLinkedIn", linkedin != null && !linkedin.isEmpty());
        validation.put("hasPortfolio", portfolio != null && !portfolio.isEmpty());
        validation.put("suggestions", suggestions);
        validation.put("verifiabilityScore", calculateVerifiabilityScore(github, linkedin, portfolio));

        return validation;
    }

    private double calculateVerifiabilityScore(String github, String linkedin, String portfolio) {
        int score = 0;
        if (github != null && !github.isEmpty()) score += 40;
        if (linkedin != null && !linkedin.isEmpty()) score += 30;
        if (portfolio != null && !portfolio.isEmpty()) score += 30;
        return score;
    }
}

