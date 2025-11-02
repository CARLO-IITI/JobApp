package com.jobapp.userservice.service;

import com.jobapp.userservice.dto.WorkExperienceDTO;
import com.jobapp.userservice.dto.ProjectShowcaseDTO;
import com.jobapp.userservice.model.WorkExperience;
import com.jobapp.userservice.model.ProjectShowcase;
import com.jobapp.userservice.repository.WorkExperienceRepository;
import com.jobapp.userservice.repository.ProjectShowcaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExperienceService {

    private final WorkExperienceRepository workExperienceRepository;
    private final ProjectShowcaseRepository projectShowcaseRepository;

    @Transactional
    public WorkExperienceDTO addWorkExperience(WorkExperienceDTO dto) {
        WorkExperience experience = WorkExperience.builder()
                .userId(dto.getUserId())
                .jobTitle(dto.getJobTitle())
                .companyName(dto.getCompanyName())
                .companyLogo(dto.getCompanyLogo())
                .location(dto.getLocation())
                .employmentType(dto.getEmploymentType())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .currentlyWorking(dto.getCurrentlyWorking() != null ? dto.getCurrentlyWorking() : false)
                .description(dto.getDescription())
                .achievements(dto.getAchievements())
                .technologiesUsed(dto.getTechnologiesUsed())
                .build();
        
        experience = workExperienceRepository.save(experience);
        return mapWorkExpToDTO(experience);
    }

    @Transactional(readOnly = true)
    public List<WorkExperienceDTO> getUserExperiences(Long userId) {
        return workExperienceRepository.findByUserIdOrderByStartDateDesc(userId)
                .stream()
                .map(this::mapWorkExpToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ProjectShowcaseDTO addProject(ProjectShowcaseDTO dto) {
        ProjectShowcase project = ProjectShowcase.builder()
                .userId(dto.getUserId())
                .title(dto.getTitle())
                .description(dto.getDescription())
                .technologies(dto.getTechnologies())
                .projectUrl(dto.getProjectUrl())
                .githubUrl(dto.getGithubUrl())
                .imageUrl(dto.getImageUrl())
                .type(dto.getType())
                .role(dto.getRole())
                .build();
        
        project = projectShowcaseRepository.save(project);
        return mapProjectToDTO(project);
    }

    @Transactional(readOnly = true)
    public List<ProjectShowcaseDTO> getUserProjects(Long userId) {
        return projectShowcaseRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::mapProjectToDTO)
                .collect(Collectors.toList());
    }

    private WorkExperienceDTO mapWorkExpToDTO(WorkExperience exp) {
        return WorkExperienceDTO.builder()
                .id(exp.getId())
                .userId(exp.getUserId())
                .jobTitle(exp.getJobTitle())
                .companyName(exp.getCompanyName())
                .companyLogo(exp.getCompanyLogo())
                .location(exp.getLocation())
                .employmentType(exp.getEmploymentType())
                .startDate(exp.getStartDate())
                .endDate(exp.getEndDate())
                .currentlyWorking(exp.getCurrentlyWorking())
                .description(exp.getDescription())
                .achievements(exp.getAchievements())
                .technologiesUsed(exp.getTechnologiesUsed())
                .durationInMonths(exp.getDurationInMonths())
                .build();
    }

    private ProjectShowcaseDTO mapProjectToDTO(ProjectShowcase project) {
        return ProjectShowcaseDTO.builder()
                .id(project.getId())
                .userId(project.getUserId())
                .title(project.getTitle())
                .description(project.getDescription())
                .technologies(project.getTechnologies())
                .projectUrl(project.getProjectUrl())
                .githubUrl(project.getGithubUrl())
                .imageUrl(project.getImageUrl())
                .type(project.getType())
                .role(project.getRole())
                .build();
    }
}

