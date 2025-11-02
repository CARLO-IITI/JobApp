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
public class SalaryNegotiation {
    private Double offeredSalary;
    private Double marketValue;
    private Double suggestedCounterOffer;
    private Double minAcceptable;
    private Double maxRealistic;
    private String verdict;  // UNDERPAID, FAIR, OVERPAID
    private Double percentageUnderpaid;
    private List<String> negotiationTips;
    private List<String> justifications;
    private String emailTemplate;
    private String phoneScript;
    private List<String> alternatives;  // If they won't budge on salary
    private Integer successProbability;  // 0-100%
    private String recommendedAction;
}

