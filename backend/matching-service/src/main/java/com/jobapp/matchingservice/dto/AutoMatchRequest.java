package com.jobapp.matchingservice.dto;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class AutoMatchRequest {
    private Long jobId;
    private Map<String, Object> jobRequirements;
    private List<Long> applicationIds;
}

