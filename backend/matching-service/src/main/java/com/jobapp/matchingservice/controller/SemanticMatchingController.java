package com.jobapp.matchingservice.controller;

import com.jobapp.common.dto.ApiResponse;
import com.jobapp.matchingservice.service.SemanticMatchingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST API for semantic job-candidate matching using embeddings and ML.
 * Provides intelligent matching based on deep analysis of job descriptions and candidate profiles.
 */
@RestController
@RequestMapping("/api/semantic-matching")
@RequiredArgsConstructor
@Slf4j
public class SemanticMatchingController {

    private final SemanticMatchingService semanticMatchingService;

    /**
     * Calculate semantic match score between a job and candidate
     * 
     * POST /api/semantic-matching/calculate-match
     * 
     * Request Body:
     * {
     *   "job": { job data },
     *   "candidate": { candidate data }
     * }
     * 
     * Response:
     * {
     *   "overallScore": 0.85,
     *   "overallPercentage": 85,
     *   "skillMatch": 90,
     *   "experienceMatch": 85,
     *   "locationMatch": 80,
     *   "recommendation": "EXCELLENT_MATCH",
     *   "matchedSkills": ["Java", "Spring", "React"],
     *   "missingSkills": ["Kubernetes", "Docker"],
     *   "strengths": [...],
     *   "improvementAreas": [...]
     * }
     */
    @PostMapping("/calculate-match")
    public ResponseEntity<ApiResponse<Map<String, Object>>> calculateSemanticMatch(
            @RequestBody Map<String, Object> request) {
        
        log.info("Calculating semantic match");
        
        @SuppressWarnings("unchecked")
        Map<String, Object> jobData = (Map<String, Object>) request.get("job");
        @SuppressWarnings("unchecked")
        Map<String, Object> candidateData = (Map<String, Object>) request.get("candidate");
        
        if (jobData == null || candidateData == null) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Job and candidate data are required"));
        }
        
        Map<String, Object> matchResult = semanticMatchingService.calculateSemanticMatch(
                jobData, candidateData);
        
        return ResponseEntity.ok(ApiResponse.success(
                "Semantic match calculated successfully", matchResult));
    }

    /**
     * Find best matching candidates for a job
     * 
     * POST /api/semantic-matching/find-candidates
     * 
     * Request Body:
     * {
     *   "job": { job data },
     *   "candidates": [ { candidate1 }, { candidate2 }, ... ],
     *   "topN": 10
     * }
     * 
     * Response: List of top N candidates with match scores
     */
    @PostMapping("/find-candidates")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> findBestCandidates(
            @RequestBody Map<String, Object> request) {
        
        log.info("Finding best candidates for job");
        
        @SuppressWarnings("unchecked")
        Map<String, Object> jobData = (Map<String, Object>) request.get("job");
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> candidates = (List<Map<String, Object>>) request.get("candidates");
        Integer topN = request.get("topN") != null ? (Integer) request.get("topN") : 10;
        
        if (jobData == null || candidates == null) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Job and candidates data are required"));
        }
        
        List<Map<String, Object>> topCandidates = semanticMatchingService.findBestCandidates(
                jobData, candidates, topN);
        
        return ResponseEntity.ok(ApiResponse.success(
                String.format("Found top %d matching candidates", topCandidates.size()),
                topCandidates));
    }

    /**
     * Find best matching jobs for a candidate
     * 
     * POST /api/semantic-matching/find-jobs
     * 
     * Request Body:
     * {
     *   "candidate": { candidate data },
     *   "jobs": [ { job1 }, { job2 }, ... ],
     *   "topN": 10
     * }
     * 
     * Response: List of top N jobs with match scores
     */
    @PostMapping("/find-jobs")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> findBestJobs(
            @RequestBody Map<String, Object> request) {
        
        log.info("Finding best jobs for candidate");
        
        @SuppressWarnings("unchecked")
        Map<String, Object> candidateData = (Map<String, Object>) request.get("candidate");
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> jobs = (List<Map<String, Object>>) request.get("jobs");
        Integer topN = request.get("topN") != null ? (Integer) request.get("topN") : 10;
        
        if (candidateData == null || jobs == null) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Candidate and jobs data are required"));
        }
        
        List<Map<String, Object>> topJobs = semanticMatchingService.findBestJobs(
                candidateData, jobs, topN);
        
        return ResponseEntity.ok(ApiResponse.success(
                String.format("Found top %d matching jobs", topJobs.size()),
                topJobs));
    }

    /**
     * Batch calculate matches for multiple job-candidate pairs
     * 
     * POST /api/semantic-matching/batch-match
     * 
     * Request Body:
     * {
     *   "pairs": [
     *     { "job": {...}, "candidate": {...} },
     *     { "job": {...}, "candidate": {...} }
     *   ]
     * }
     * 
     * Response: List of match results for each pair
     */
    @PostMapping("/batch-match")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> batchCalculateMatches(
            @RequestBody Map<String, Object> request) {
        
        log.info("Batch calculating semantic matches");
        
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> pairs = (List<Map<String, Object>>) request.get("pairs");
        
        if (pairs == null || pairs.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Pairs array is required"));
        }
        
        List<Map<String, Object>> results = pairs.stream()
                .map(pair -> {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> job = (Map<String, Object>) pair.get("job");
                    @SuppressWarnings("unchecked")
                    Map<String, Object> candidate = (Map<String, Object>) pair.get("candidate");
                    
                    if (job != null && candidate != null) {
                        return semanticMatchingService.calculateSemanticMatch(job, candidate);
                    }
                    return null;
                })
                .filter(result -> result != null)
                .toList();
        
        return ResponseEntity.ok(ApiResponse.success(
                String.format("Calculated %d matches", results.size()), results));
    }

    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> healthCheck() {
        return ResponseEntity.ok(ApiResponse.success("Semantic matching service is running"));
    }
}

