package com.jobapp.matchingservice.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class CandidateRankingRequest {
    private Map<String, Object> jobRequirements;
    private List<Map<String, Object>> candidates;
}

