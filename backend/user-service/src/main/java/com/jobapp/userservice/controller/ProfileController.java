package com.jobapp.userservice.controller;

import com.jobapp.common.dto.ApiResponse;
import com.jobapp.userservice.dto.CandidateProfileDTO;
import com.jobapp.userservice.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("/candidate/{userId}")
    public ResponseEntity<ApiResponse<CandidateProfileDTO>> getCandidateProfile(@PathVariable Long userId) {
        CandidateProfileDTO profile = profileService.getCandidateProfile(userId);
        return ResponseEntity.ok(ApiResponse.success(profile));
    }

    @PutMapping("/candidate/{userId}")
    public ResponseEntity<ApiResponse<CandidateProfileDTO>> updateCandidateProfile(
            @PathVariable Long userId,
            @RequestBody CandidateProfileDTO profileDTO) {
        CandidateProfileDTO updated = profileService.updateCandidateProfile(userId, profileDTO);
        return ResponseEntity.ok(ApiResponse.success("Profile updated successfully", updated));
    }
}

