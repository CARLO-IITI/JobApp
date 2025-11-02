package com.jobapp.userservice.service;

import com.jobapp.common.exception.ResourceNotFoundException;
import com.jobapp.userservice.dto.CandidateProfileDTO;
import com.jobapp.userservice.model.CandidateProfile;
import com.jobapp.userservice.model.User;
import com.jobapp.userservice.repository.CandidateProfileRepository;
import com.jobapp.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final CandidateProfileRepository candidateProfileRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public CandidateProfileDTO getCandidateProfile(Long userId) {
        CandidateProfile profile = candidateProfileRepository.findByUserId(userId)
                .orElse(null);
        
        if (profile == null) {
            // Return empty profile
            return CandidateProfileDTO.builder()
                    .userId(userId)
                    .skills(new ArrayList<>())
                    .preferredLocations(new ArrayList<>())
                    .languages(new ArrayList<>())
                    .openToRemote(true)
                    .openToRelocation(false)
                    .build();
        }
        
        return mapToDTO(profile);
    }

    @Transactional
    public CandidateProfileDTO updateCandidateProfile(Long userId, CandidateProfileDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        CandidateProfile profile = candidateProfileRepository.findByUserId(userId)
                .orElse(CandidateProfile.builder()
                        .user(user)
                        .skills(new ArrayList<>())
                        .preferredLocations(new ArrayList<>())
                        .languages(new ArrayList<>())
                        .build());

        // Update fields
        if (dto.getHeadline() != null) profile.setHeadline(dto.getHeadline());
        if (dto.getSummary() != null) profile.setSummary(dto.getSummary());
        if (dto.getSkills() != null) profile.setSkills(dto.getSkills());
        if (dto.getYearsOfExperience() != null) profile.setYearsOfExperience(dto.getYearsOfExperience());
        if (dto.getCurrentLocation() != null) profile.setCurrentLocation(dto.getCurrentLocation());
        if (dto.getPreferredLocations() != null) profile.setPreferredLocations(dto.getPreferredLocations());
        if (dto.getCurrentJobTitle() != null) profile.setCurrentJobTitle(dto.getCurrentJobTitle());
        if (dto.getEducation() != null) profile.setEducation(dto.getEducation());
        if (dto.getUniversity() != null) profile.setUniversity(dto.getUniversity());
        if (dto.getResumeUrl() != null) profile.setResumeUrl(dto.getResumeUrl());
        if (dto.getPortfolioUrl() != null) profile.setPortfolioUrl(dto.getPortfolioUrl());
        if (dto.getGithubUrl() != null) profile.setGithubUrl(dto.getGithubUrl());
        if (dto.getLinkedinUrl() != null) profile.setLinkedinUrl(dto.getLinkedinUrl());
        if (dto.getLanguages() != null) profile.setLanguages(dto.getLanguages());
        if (dto.getExpectedSalary() != null) profile.setExpectedSalary(dto.getExpectedSalary());
        if (dto.getNoticePeriod() != null) profile.setNoticePeriod(dto.getNoticePeriod());
        if (dto.getJobPreference() != null) {
            try {
                profile.setJobPreference(CandidateProfile.JobPreference.valueOf(dto.getJobPreference()));
            } catch (Exception ignored) {}
        }
        if (dto.getOpenToRemote() != null) profile.setOpenToRemote(dto.getOpenToRemote());
        if (dto.getOpenToRelocation() != null) profile.setOpenToRelocation(dto.getOpenToRelocation());

        profile = candidateProfileRepository.save(profile);
        return mapToDTO(profile);
    }

    private CandidateProfileDTO mapToDTO(CandidateProfile profile) {
        return CandidateProfileDTO.builder()
                .id(profile.getId())
                .userId(profile.getUser().getId())
                .headline(profile.getHeadline())
                .summary(profile.getSummary())
                .skills(profile.getSkills())
                .yearsOfExperience(profile.getYearsOfExperience())
                .currentLocation(profile.getCurrentLocation())
                .preferredLocations(profile.getPreferredLocations())
                .currentJobTitle(profile.getCurrentJobTitle())
                .education(profile.getEducation())
                .university(profile.getUniversity())
                .resumeUrl(profile.getResumeUrl())
                .portfolioUrl(profile.getPortfolioUrl())
                .githubUrl(profile.getGithubUrl())
                .linkedinUrl(profile.getLinkedinUrl())
                .languages(profile.getLanguages())
                .expectedSalary(profile.getExpectedSalary())
                .noticePeriod(profile.getNoticePeriod())
                .jobPreference(profile.getJobPreference() != null ? profile.getJobPreference().toString() : null)
                .openToRemote(profile.getOpenToRemote())
                .openToRelocation(profile.getOpenToRelocation())
                .build();
    }
}

