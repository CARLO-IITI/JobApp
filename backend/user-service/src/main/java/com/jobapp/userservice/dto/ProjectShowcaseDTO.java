package com.jobapp.userservice.dto;

import com.jobapp.userservice.model.ProjectShowcase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectShowcaseDTO {
    private Long id;
    private Long userId;
    private String title;
    private String description;
    private List<String> technologies;
    private String projectUrl;
    private String githubUrl;
    private String imageUrl;
    private ProjectShowcase.ProjectType type;
    private String role;
}

