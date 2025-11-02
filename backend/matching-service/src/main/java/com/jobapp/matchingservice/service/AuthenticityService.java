package com.jobapp.matchingservice.service;

import com.jobapp.matchingservice.algorithm.AuthenticityDetector;
import com.jobapp.matchingservice.dto.AuthenticityAnalysis;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticityService {

    private final AuthenticityDetector authenticityDetector;

    public AuthenticityAnalysis analyzeCandidate(Map<String, Object> profile) {
        // Analyze authenticity
        Map<String, Object> analysis = authenticityDetector.analyzeAuthenticity(profile, profile);
        
        // Cross-reference validation
        Map<String, Object> crossRef = authenticityDetector.crossReferenceValidation(profile);

        return AuthenticityAnalysis.builder()
                .authenticityScore(((Number) analysis.get("authenticityScore")).doubleValue())
                .trustLevel((String) analysis.get("trustLevel"))
                .redFlags((List<String>) analysis.get("redFlags"))
                .greenFlags((List<String>) analysis.get("greenFlags"))
                .recommendation((String) analysis.get("recommendation"))
                .verificationSuggestions((List<String>) analysis.get("verificationSuggestions"))
                .verifiabilityScore(((Number) crossRef.get("verifiabilityScore")).doubleValue())
                .hasGitHub((Boolean) crossRef.get("hasGitHub"))
                .hasLinkedIn((Boolean) crossRef.get("hasLinkedIn"))
                .hasPortfolio((Boolean) crossRef.get("hasPortfolio"))
                .build();
    }
}

