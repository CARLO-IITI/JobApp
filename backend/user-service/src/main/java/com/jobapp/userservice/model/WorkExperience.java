package com.jobapp.userservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * LinkedIn-style work experience
 */
@Entity
@Table(name = "work_experiences")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class WorkExperience {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String jobTitle;

    @Column(nullable = false)
    private String companyName;

    private String companyLogo;

    private String location;

    @Enumerated(EnumType.STRING)
    private EmploymentType employmentType; // FULL_TIME, PART_TIME, CONTRACT, INTERNSHIP

    @Column(nullable = false)
    private LocalDate startDate;

    private LocalDate endDate; // null if current job

    @Column(nullable = false)
    private Boolean currentlyWorking = false;

    @Column(length = 2000)
    private String description;

    @ElementCollection
    @CollectionTable(name = "experience_achievements", joinColumns = @JoinColumn(name = "experience_id"))
    @Column(name = "achievement", length = 500)
    private List<String> achievements = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "experience_technologies", joinColumns = @JoinColumn(name = "experience_id"))
    @Column(name = "technology")
    private List<String> technologiesUsed = new ArrayList<>();

    private Integer displayOrder; // For ordering experiences

    @CreatedDate
    private LocalDateTime createdAt;

    public enum EmploymentType {
        FULL_TIME, PART_TIME, CONTRACT, INTERNSHIP, FREELANCE
    }

    // Calculate duration in months
    public Integer getDurationInMonths() {
        LocalDate end = currentlyWorking ? LocalDate.now() : endDate;
        if (end == null) return 0;
        
        int years = end.getYear() - startDate.getYear();
        int months = end.getMonthValue() - startDate.getMonthValue();
        return (years * 12) + months;
    }
}

