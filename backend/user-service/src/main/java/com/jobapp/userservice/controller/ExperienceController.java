package com.jobapp.userservice.controller;

import com.jobapp.common.dto.ApiResponse;
import com.jobapp.userservice.dto.WorkExperienceDTO;
import com.jobapp.userservice.dto.ProjectShowcaseDTO;
import com.jobapp.userservice.service.ExperienceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/experience")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ExperienceController {

    private final ExperienceService experienceService;

    @PostMapping("/work")
    public ResponseEntity<ApiResponse<WorkExperienceDTO>> addWorkExperience(@RequestBody WorkExperienceDTO dto) {
        WorkExperienceDTO created = experienceService.addWorkExperience(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Work experience added", created));
    }

    @GetMapping("/work/{userId}")
    public ResponseEntity<ApiResponse<List<WorkExperienceDTO>>> getUserExperiences(@PathVariable Long userId) {
        List<WorkExperienceDTO> experiences = experienceService.getUserExperiences(userId);
        return ResponseEntity.ok(ApiResponse.success(experiences));
    }

    @PostMapping("/projects")
    public ResponseEntity<ApiResponse<ProjectShowcaseDTO>> addProject(@RequestBody ProjectShowcaseDTO dto) {
        ProjectShowcaseDTO created = experienceService.addProject(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Project added", created));
    }

    @GetMapping("/projects/{userId}")
    public ResponseEntity<ApiResponse<List<ProjectShowcaseDTO>>> getUserProjects(@PathVariable Long userId) {
        List<ProjectShowcaseDTO> projects = experienceService.getUserProjects(userId);
        return ResponseEntity.ok(ApiResponse.success(projects));
    }
}

