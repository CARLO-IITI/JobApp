package com.jobapp.userservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Project showcase (Better than LinkedIn)
 * Candidates showcase real work to prove skills
 */
@Entity
@Table(name = "projects")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ProjectShowcase {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String title;

    @Column(length = 2000)
    private String description;

    @ElementCollection
    @CollectionTable(name = "project_technologies", joinColumns = @JoinColumn(name = "project_id"))
    @Column(name = "technology")
    private List<String> technologies = new ArrayList<>();

    private String projectUrl;      // Live demo URL

    private String githubUrl;       // Source code

    private String imageUrl;        // Screenshot/banner

    @Enumerated(EnumType.STRING)
    private ProjectType type;       // PERSONAL, PROFESSIONAL, OPEN_SOURCE, FREELANCE

    private String role;            // Your role in project

    @CreatedDate
    private LocalDateTime createdAt;

    public enum ProjectType {
        PERSONAL, PROFESSIONAL, OPEN_SOURCE, FREELANCE, ACADEMIC
    }
}

