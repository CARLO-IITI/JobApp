package com.jobapp.matchingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AutoMatchResult {
    private Long applicationId;
    private Long candidateId;
    private Double matchScore;
    private Map<String, Double> criteriaBreakdown;
    private String recommendation;
    private Integer suggestedRank;
}

