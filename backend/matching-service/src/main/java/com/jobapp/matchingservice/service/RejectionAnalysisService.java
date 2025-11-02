package com.jobapp.matchingservice.service;

import com.jobapp.matchingservice.algorithm.SimilarityCalculator;
import com.jobapp.matchingservice.dto.RejectionAnalysis;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * AI-powered rejection analysis and constructive feedback
 */
@Service
@RequiredArgsConstructor
public class RejectionAnalysisService {

    private final SimilarityCalculator similarityCalculator;

    public RejectionAnalysis analyzeRejection(
            Map<String, Object> candidateProfile,
            Map<String, Object> jobRequirements) {

        // Extract data
        @SuppressWarnings("unchecked")
        List<String> candidateSkills = (List<String>) candidateProfile.getOrDefault("skills", new ArrayList<>());
        Integer candidateExp = (Integer) candidateProfile.get("yearsOfExperience");
        Double expectedSalary = candidateProfile.get("expectedSalary") != null ? 
                ((Number) candidateProfile.get("expectedSalary")).doubleValue() : null;

        @SuppressWarnings("unchecked")
        List<String> requiredSkills = (List<String>) jobRequirements.getOrDefault("requiredSkills", new ArrayList<>());
        Integer minExp = (Integer) jobRequirements.get("minExperience");
        Integer maxExp = (Integer) jobRequirements.get("maxExperience");
        Double maxSalary = jobRequirements.get("maxSalary") != null ? 
                ((Number) jobRequirements.get("maxSalary")).doubleValue() : null;

        // Calculate scores
        double skillScore = similarityCalculator.calculateSkillSimilarity(candidateSkills, requiredSkills);
        double expScore = similarityCalculator.calculateExperienceMatch(candidateExp, minExp, maxExp);
        double salaryScore = similarityCalculator.calculateSalaryMatch(expectedSalary, null, maxSalary);

        // Overall match
        double overallScore = (skillScore * 0.5) + (expScore * 0.3) + (salaryScore * 0.2);

        // Find strengths
        List<String> strengths = findStrengths(candidateSkills, requiredSkills, candidateExp, minExp);

        // Find missing skills
        List<String> missingSkills = findMissingSkills(candidateSkills, requiredSkills);

        // Areas for improvement
        List<String> improvements = generateImprovements(skillScore, expScore, salaryScore, candidateExp, minExp);

        // Generate recommendations
        List<String> recommendations = generateRecommendations(missingSkills, expScore, candidateExp, minExp);

        // Suggest courses
        List<String> courses = suggestCourses(missingSkills);

        // Experience gap analysis
        String expGapAnalysis = analyzeExperienceGap(candidateExp, minExp, maxExp);

        // Salary feedback
        String salaryFeedback = analyzeSalaryExpectation(expectedSalary, maxSalary);

        // Encouraging message
        String encouragement = generateEncouragingMessage(overallScore, strengths);

        // Summary
        String summary = generateSummary(overallScore, skillScore, expScore);

        return RejectionAnalysis.builder()
                .summary(summary)
                .overallMatchScore(Math.round(overallScore * 100.0) / 100.0)
                .strengths(strengths)
                .areasForImprovement(improvements)
                .missingSkills(missingSkills)
                .experienceGapAnalysis(expGapAnalysis)
                .salaryExpectationFeedback(salaryFeedback)
                .recommendations(recommendations)
                .suggestedCourses(courses)
                .encouragingMessage(encouragement)
                .build();
    }

    private List<String> findStrengths(List<String> candidateSkills, List<String> requiredSkills,
                                       Integer candidateExp, Integer minExp) {
        List<String> strengths = new ArrayList<>();

        // Matching skills
        Set<String> candidateSet = new HashSet<>();
        if (candidateSkills != null) {
            candidateSkills.forEach(s -> candidateSet.add(s.toLowerCase()));
        }

        if (requiredSkills != null) {
            for (String skill : requiredSkills) {
                if (candidateSet.contains(skill.toLowerCase())) {
                    strengths.add("Strong proficiency in " + skill);
                }
            }
        }

        // Experience
        if (candidateExp != null && minExp != null && candidateExp >= minExp) {
            strengths.add("Meets experience requirements (" + candidateExp + " years)");
        }

        if (strengths.isEmpty()) {
            strengths.add("Enthusiasm and willingness to learn");
            strengths.add("Potential for growth in this role");
        }

        return strengths;
    }

    private List<String> findMissingSkills(List<String> candidateSkills, List<String> requiredSkills) {
        List<String> missing = new ArrayList<>();
        
        Set<String> candidateSet = new HashSet<>();
        if (candidateSkills != null) {
            candidateSkills.forEach(s -> candidateSet.add(s.toLowerCase()));
        }

        if (requiredSkills != null) {
            for (String skill : requiredSkills) {
                if (!candidateSet.contains(skill.toLowerCase())) {
                    missing.add(skill);
                }
            }
        }

        return missing;
    }

    private List<String> generateImprovements(double skillScore, double expScore, double salaryScore,
                                              Integer candidateExp, Integer minExp) {
        List<String> improvements = new ArrayList<>();

        if (skillScore < 0.7) {
            improvements.add("Expand your technical skill set to better match job requirements");
            improvements.add("Consider taking online courses or certifications in key technologies");
        }

        if (expScore < 0.7 && candidateExp != null && minExp != null && candidateExp < minExp) {
            int gap = minExp - candidateExp;
            improvements.add("Gain " + gap + " more year(s) of relevant experience through projects or internships");
            improvements.add("Highlight transferable skills from your current experience");
        }

        if (salaryScore < 0.5) {
            improvements.add("Research market salary ranges for similar positions");
            improvements.add("Consider the total compensation package, not just base salary");
        }

        return improvements;
    }

    private List<String> generateRecommendations(List<String> missingSkills, double expScore,
                                                 Integer candidateExp, Integer minExp) {
        List<String> recommendations = new ArrayList<>();

        if (!missingSkills.isEmpty()) {
            recommendations.add("Focus on learning: " + String.join(", ", missingSkills.subList(0, Math.min(3, missingSkills.size()))));
            recommendations.add("Build projects using these technologies to demonstrate practical skills");
        }

        if (expScore < 0.7) {
            recommendations.add("Look for junior or mid-level positions that match your experience level");
            recommendations.add("Contribute to open-source projects to build your portfolio");
        }

        recommendations.add("Tailor your resume to highlight relevant skills and experiences");
        recommendations.add("Practice common interview questions for your target role");
        recommendations.add("Network with professionals in your desired field");

        return recommendations;
    }

    private List<String> suggestCourses(List<String> missingSkills) {
        List<String> courses = new ArrayList<>();
        Map<String, String> skillToCourse = new HashMap<>();

        // Map skills to learning resources
        skillToCourse.put("java", "Oracle Java SE Certification, Udemy Java Masterclass");
        skillToCourse.put("python", "Python for Everybody (Coursera), Real Python");
        skillToCourse.put("javascript", "JavaScript: The Complete Guide (Udemy)");
        skillToCourse.put("react", "React - The Complete Guide (Udemy), React Official Tutorial");
        skillToCourse.put("spring", "Spring Framework Master Class (Udemy)");
        skillToCourse.put("docker", "Docker Mastery (Udemy), Docker Official Docs");
        skillToCourse.put("kubernetes", "Kubernetes for Beginners (KodeKloud)");
        skillToCourse.put("aws", "AWS Certified Solutions Architect");
        skillToCourse.put("machine learning", "Andrew Ng's ML Course (Coursera)");
        skillToCourse.put("sql", "SQL Bootcamp (Udemy), Mode Analytics SQL Tutorial");

        for (String skill : missingSkills) {
            String skillLower = skill.toLowerCase();
            if (skillToCourse.containsKey(skillLower)) {
                courses.add(skill + ": " + skillToCourse.get(skillLower));
            }
        }

        if (courses.isEmpty()) {
            courses.add("Search for relevant courses on Coursera, Udemy, or freeCodeCamp");
        }

        return courses;
    }

    private String analyzeExperienceGap(Integer candidateExp, Integer minExp, Integer maxExp) {
        if (candidateExp == null || minExp == null) {
            return "Experience information not available for comparison.";
        }

        if (candidateExp >= minExp && (maxExp == null || candidateExp <= maxExp)) {
            return "Your experience level matches the requirement perfectly!";
        } else if (candidateExp < minExp) {
            int gap = minExp - candidateExp;
            return "You have " + gap + " year(s) less experience than required. Focus on building relevant projects and contributing to open-source to demonstrate your capabilities. Many companies value practical skills over years on paper.";
        } else {
            return "You exceed the experience requirements, which is excellent! However, the role might be looking for someone at a specific level. Consider applying for senior positions.";
        }
    }

    private String analyzeSalaryExpectation(Double expectedSalary, Double maxSalary) {
        if (expectedSalary == null || maxSalary == null) {
            return "No salary mismatch detected.";
        }

        if (expectedSalary <= maxSalary) {
            return "Your salary expectation aligns with the budget.";
        } else {
            double excess = expectedSalary - maxSalary;
            double percentOver = (excess / maxSalary) * 100;
            return String.format("Your salary expectation is %.1f%% above the role's budget. " +
                    "Consider the total compensation package including benefits, growth opportunities, " +
                    "and learning experiences when evaluating offers.", percentOver);
        }
    }

    private String generateEncouragingMessage(double overallScore, List<String> strengths) {
        if (overallScore >= 0.7) {
            return "You were a strong candidate! Sometimes hiring decisions come down to very specific team needs or timing. Keep applying - the right opportunity is out there!";
        } else if (overallScore >= 0.5) {
            return "You showed promise in several areas! With some focused skill development, you'll be an even stronger candidate. Don't give up - every application is a learning opportunity!";
        } else {
            return "Thank you for your interest! While this particular role wasn't the right fit, we encourage you to keep developing your skills and applying for positions that match your current experience level. Growth takes time, and you're on the right path!";
        }
    }

    private String generateSummary(double overallScore, double skillScore, double expScore) {
        if (overallScore >= 0.7) {
            return "You were a competitive candidate with strong qualifications. The decision was difficult and came down to very specific requirements or another candidate's unique fit.";
        } else if (skillScore < 0.5) {
            return "While you have potential, there was a significant gap in the technical skills required for this role. We encourage you to focus on developing these skills.";
        } else if (expScore < 0.5) {
            return "Your skills are promising, but the role requires more experience. Consider positions that match your current experience level while you continue to grow.";
        } else {
            return "After careful consideration, we've decided to move forward with other candidates who more closely match our current needs. We appreciate your interest and encourage continued growth.";
        }
    }
}

