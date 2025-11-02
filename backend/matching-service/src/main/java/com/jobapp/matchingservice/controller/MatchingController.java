package com.jobapp.matchingservice.controller;

import com.jobapp.common.dto.ApiResponse;
import com.jobapp.matchingservice.dto.*;
import com.jobapp.matchingservice.service.*;
import com.jobapp.matchingservice.service.CandidateRankingService;
import com.jobapp.matchingservice.service.MatchingService;
import com.jobapp.matchingservice.service.RejectionAnalysisService;
import com.jobapp.matchingservice.service.JobMatchPredictionService;
import com.jobapp.matchingservice.service.AuthenticityService;
import com.jobapp.matchingservice.service.SkillROIService;
import com.jobapp.matchingservice.service.SalaryNegotiationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/matching")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class MatchingController {

    private final MatchingService matchingService;
    private final CandidateRankingService candidateRankingService;
    private final RejectionAnalysisService rejectionAnalysisService;
    private final JobMatchPredictionService jobMatchPredictionService;
    private final AuthenticityService authenticityService;
    private final SkillROIService skillROIService;
    private final SalaryNegotiationService salaryNegotiationService;

    @PostMapping("/find-jobs")
    public ResponseEntity<ApiResponse<List<MatchResult>>> findMatchingJobs(
            @RequestBody CandidateMatchRequest request,
            @RequestParam(required = false) List<Map<String, Object>> jobs) {
        
        List<MatchResult> matches = matchingService.findMatchingJobs(request, jobs);
        return ResponseEntity.ok(ApiResponse.success("Matches found successfully", matches));
    }

    @PostMapping("/rank-candidates")
    public ResponseEntity<ApiResponse<List<RankedCandidate>>> rankCandidates(
            @RequestBody CandidateRankingRequest request) {
        
        List<RankedCandidate> rankedCandidates = candidateRankingService.rankCandidates(
                request.getJobRequirements(), 
                request.getCandidates()
        );
        
        return ResponseEntity.ok(ApiResponse.success("Candidates ranked successfully", rankedCandidates));
    }

    @PostMapping("/analyze-rejection")
    public ResponseEntity<ApiResponse<RejectionAnalysis>> analyzeRejection(
            @RequestBody Map<String, Object> request) {
        
        @SuppressWarnings("unchecked")
        Map<String, Object> candidateProfile = (Map<String, Object>) request.get("candidateProfile");
        @SuppressWarnings("unchecked")
        Map<String, Object> jobRequirements = (Map<String, Object>) request.get("jobRequirements");
        
        RejectionAnalysis analysis = rejectionAnalysisService.analyzeRejection(
                candidateProfile, 
                jobRequirements
        );
        
        return ResponseEntity.ok(ApiResponse.success("Analysis completed", analysis));
    }

    @PostMapping("/auto-score-applications")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> autoScoreApplications(
            @RequestBody Map<String, Object> request) {
        
        @SuppressWarnings("unchecked")
        Map<String, Object> jobRequirements = (Map<String, Object>) request.get("jobRequirements");
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> applications = (List<Map<String, Object>>) request.get("applications");
        
        List<Map<String, Object>> scoredApplications = new ArrayList<>();
        
        for (Map<String, Object> app : applications) {
            // Calculate match score for this application
            Map<String, Object> scored = new HashMap<>(app);
            
            @SuppressWarnings("unchecked")
            List<String> candidateSkills = (List<String>) app.getOrDefault("skills", new ArrayList<>());
            @SuppressWarnings("unchecked")
            List<String> requiredSkills = (List<String>) jobRequirements.getOrDefault("requiredSkills", new ArrayList<>());
            
            // Calculate scores
            double skillScore = candidateSkills != null && requiredSkills != null ?
                    (double) candidateSkills.stream()
                            .filter(s -> requiredSkills.stream()
                                    .anyMatch(r -> r.equalsIgnoreCase(s)))
                            .count() / Math.max(requiredSkills.size(), 1) : 0.0;
            
            scored.put("skillsMatchScore", Math.round(skillScore * 100.0) / 100.0);
            scored.put("overallScore", Math.round(skillScore * 100.0) / 100.0);
            
            scoredApplications.add(scored);
        }
        
        // Sort by score descending
        scoredApplications.sort((a, b) -> {
            Double scoreA = ((Number) a.getOrDefault("overallScore", 0)).doubleValue();
            Double scoreB = ((Number) b.getOrDefault("overallScore", 0)).doubleValue();
            return Double.compare(scoreB, scoreA);
        });
        
        // Add ranks
        for (int i = 0; i < scoredApplications.size(); i++) {
            scoredApplications.get(i).put("rank", i + 1);
        }
        
        return ResponseEntity.ok(ApiResponse.success("Applications scored", scoredApplications));
    }

    @PostMapping("/predict-job-match")
    public ResponseEntity<ApiResponse<JobMatchPrediction>> predictJobMatch(
            @RequestBody Map<String, Object> request) {
        
        @SuppressWarnings("unchecked")
        Map<String, Object> candidateProfile = (Map<String, Object>) request.get("candidateProfile");
        @SuppressWarnings("unchecked")
        Map<String, Object> jobDetails = (Map<String, Object>) request.get("jobDetails");
        
        JobMatchPrediction prediction = jobMatchPredictionService.predictJobMatch(
                candidateProfile,
                jobDetails
        );
        
        return ResponseEntity.ok(ApiResponse.success("Match prediction completed", prediction));
    }

    @PostMapping("/verify-authenticity")
    public ResponseEntity<ApiResponse<AuthenticityAnalysis>> verifyAuthenticity(
            @RequestBody Map<String, Object> candidateProfile) {
        
        AuthenticityAnalysis analysis = authenticityService.analyzeCandidate(candidateProfile);
        
        return ResponseEntity.ok(ApiResponse.success("Authenticity analysis completed", analysis));
    }

    @PostMapping("/calculate-skill-roi")
    public ResponseEntity<ApiResponse<List<SkillROI>>> calculateSkillROI(
            @RequestBody Map<String, Object> request) {
        
        @SuppressWarnings("unchecked")
        List<String> potentialSkills = (List<String>) request.get("potentialSkills");
        @SuppressWarnings("unchecked")
        List<String> currentSkills = (List<String>) request.get("currentSkills");
        Integer currentJobs = request.get("currentMatchingJobs") != null ? 
                ((Number) request.get("currentMatchingJobs")).intValue() : 50;
        Double currentSalary = request.get("currentAvgSalary") != null ? 
                ((Number) request.get("currentAvgSalary")).doubleValue() : 100000.0;
        Double hiringProb = request.get("currentHiringProbability") != null ? 
                ((Number) request.get("currentHiringProbability")).doubleValue() : 0.6;
        
        List<SkillROI> roiList = skillROIService.calculateMultipleSkillROI(
                potentialSkills, currentSkills, currentJobs, currentSalary, hiringProb);
        
        return ResponseEntity.ok(ApiResponse.success("Skill ROI calculated", roiList));
    }

    @PostMapping("/negotiate-salary")
    public ResponseEntity<ApiResponse<SalaryNegotiation>> negotiateSalary(
            @RequestBody Map<String, Object> request) {
        
        Double offeredSalary = request.get("offeredSalary") != null ? 
                ((Number) request.get("offeredSalary")).doubleValue() : 100000.0;
        @SuppressWarnings("unchecked")
        List<String> skills = (List<String>) request.getOrDefault("skills", new ArrayList<>());
        Integer experience = request.get("yearsOfExperience") != null ? 
                ((Number) request.get("yearsOfExperience")).intValue() : 0;
        String location = (String) request.get("location");
        String jobTitle = (String) request.get("jobTitle");
        
        SalaryNegotiation negotiation = salaryNegotiationService.analyzeSalaryOffer(
                offeredSalary, skills, experience, location, jobTitle);
        
        return ResponseEntity.ok(ApiResponse.success("Salary analysis completed", negotiation));
    }

    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> health() {
        return ResponseEntity.ok(ApiResponse.success("Matching service is running"));
    }
}





