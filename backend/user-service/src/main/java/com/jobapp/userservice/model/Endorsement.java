package com.jobapp.userservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * Skill endorsements (LinkedIn-style)
 * Helps verify genuine skills through social proof
 */
@Entity
@Table(name = "endorsements")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Endorsement {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long candidateId;       // User being endorsed

    @Column(nullable = false)
    private Long endorserId;        // User giving endorsement

    @Column(nullable = false)
    private String skillName;       // Skill being endorsed

    @Column(length = 500)
    private String comment;         // Optional comment

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}

