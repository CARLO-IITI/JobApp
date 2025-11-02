package com.jobapp.matchingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticityAnalysis {
    private Double authenticityScore;        // 0-100%
    private String trustLevel;               // HIGH, MEDIUM, LOW, VERY_LOW
    private List<String> redFlags;          // Warning signs
    private List<String> greenFlags;        // Genuine indicators
    private String recommendation;          // For recruiters
    private List<String> verificationSuggestions;
    private Double verifiabilityScore;      // How easy to verify (GitHub, LinkedIn, etc.)
    private Boolean hasGitHub;
    private Boolean hasLinkedIn;
    private Boolean hasPortfolio;
}

