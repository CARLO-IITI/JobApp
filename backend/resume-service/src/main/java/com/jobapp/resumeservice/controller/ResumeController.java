package com.jobapp.resumeservice.controller;

import com.jobapp.common.dto.ApiResponse;
import com.jobapp.resumeservice.service.ResumeParserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/resume")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ResumeController {

    private final ResumeParserService resumeParserService;

    @PostMapping("/parse")
    public ResponseEntity<ApiResponse<Map<String, Object>>> parseResume(
            @RequestParam("file") MultipartFile file) {
        
        Map<String, Object> parsedData = resumeParserService.parseResume(file);
        return ResponseEntity.ok(ApiResponse.success("Resume parsed successfully", parsedData));
    }

    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> health() {
        return ResponseEntity.ok(ApiResponse.success("Resume service is running"));
    }
}

