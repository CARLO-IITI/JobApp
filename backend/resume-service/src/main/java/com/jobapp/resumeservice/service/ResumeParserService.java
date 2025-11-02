package com.jobapp.resumeservice.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class ResumeParserService {

    private final Tika tika = new Tika();

    // Common skill patterns
    private static final List<String> TECH_SKILLS = Arrays.asList(
            "Java", "Python", "JavaScript", "TypeScript", "C\\+\\+", "C#", "Ruby", "Go", "Rust",
            "React", "Angular", "Vue", "Node\\.js", "Spring", "Django", "Flask", "Express",
            "SQL", "MySQL", "PostgreSQL", "MongoDB", "Redis", "Elasticsearch",
            "AWS", "Azure", "GCP", "Docker", "Kubernetes", "Jenkins", "Git",
            "Machine Learning", "Deep Learning", "AI", "Data Science", "Big Data",
            "HTML", "CSS", "REST", "GraphQL", "Microservices", "Agile", "Scrum"
    );

    public Map<String, Object> parseResume(MultipartFile file) {
        try {
            log.info("Parsing resume: {}", file.getOriginalFilename());
            
            // Extract text from file
            String text;
            try (InputStream inputStream = file.getInputStream()) {
                text = tika.parseToString(inputStream);
            }

            // Parse various sections
            Map<String, Object> resumeData = new HashMap<>();
            resumeData.put("rawText", text);
            resumeData.put("fileName", file.getOriginalFilename());
            resumeData.put("skills", extractSkills(text));
            resumeData.put("email", extractEmail(text));
            resumeData.put("phone", extractPhone(text));
            resumeData.put("education", extractEducation(text));
            resumeData.put("experience", extractExperience(text));
            resumeData.put("name", extractName(text));

            log.info("Resume parsed successfully");
            return resumeData;

        } catch (Exception e) {
            log.error("Error parsing resume", e);
            throw new RuntimeException("Failed to parse resume: " + e.getMessage());
        }
    }

    private List<String> extractSkills(String text) {
        List<String> skills = new ArrayList<>();
        String lowerText = text.toLowerCase();

        for (String skill : TECH_SKILLS) {
            Pattern pattern = Pattern.compile("\\b" + skill.toLowerCase() + "\\b", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(lowerText);
            if (matcher.find()) {
                skills.add(skill.replace("\\", "")); // Remove regex escape characters
            }
        }

        return skills;
    }

    private String extractEmail(String text) {
        Pattern emailPattern = Pattern.compile(
                "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}",
                Pattern.CASE_INSENSITIVE
        );
        Matcher matcher = emailPattern.matcher(text);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    private String extractPhone(String text) {
        // Match various phone formats
        Pattern phonePattern = Pattern.compile(
                "(?:\\+?\\d{1,3}[-.\\s]?)?\\(?\\d{3}\\)?[-.\\s]?\\d{3}[-.\\s]?\\d{4}"
        );
        Matcher matcher = phonePattern.matcher(text);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    private String extractEducation(String text) {
        // Look for common degree patterns
        Pattern degreePattern = Pattern.compile(
                "(Bachelor|Master|PhD|B\\.?S\\.?|M\\.?S\\.?|B\\.?Tech|M\\.?Tech|MBA)[^\\n]*",
                Pattern.CASE_INSENSITIVE
        );
        Matcher matcher = degreePattern.matcher(text);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    private Integer extractExperience(String text) {
        // Try to find years of experience mentioned
        Pattern expPattern = Pattern.compile(
                "(\\d+)\\+?\\s*years?\\s*(?:of\\s*)?experience",
                Pattern.CASE_INSENSITIVE
        );
        Matcher matcher = expPattern.matcher(text);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        return 0;
    }

    private String extractName(String text) {
        // Name is typically at the beginning
        // This is a simple heuristic - get first 2-3 capitalized words
        String[] lines = text.split("\\n");
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;
            
            Pattern namePattern = Pattern.compile(
                    "^([A-Z][a-z]+(?:\\s+[A-Z][a-z]+){1,2})$"
            );
            Matcher matcher = namePattern.matcher(line);
            if (matcher.find()) {
                return matcher.group(1);
            }
        }
        return null;
    }
}

