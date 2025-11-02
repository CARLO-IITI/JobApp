package com.jobapp.userservice.dto;

import com.jobapp.userservice.model.WorkExperience;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkExperienceDTO {
    private Long id;
    private Long userId;
    private String jobTitle;
    private String companyName;
    private String companyLogo;
    private String location;
    private WorkExperience.EmploymentType employmentType;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean currentlyWorking;
    private String description;
    private List<String> achievements;
    private List<String> technologiesUsed;
    private Integer durationInMonths;
}

