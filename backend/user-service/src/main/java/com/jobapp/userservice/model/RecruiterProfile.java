package com.jobapp.userservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "recruiter_profiles")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecruiterProfile {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String companyName;

    @Column(length = 5000)
    private String companyDescription;

    private String companyWebsite;

    private String companyLogo;

    private String companySize; // 1-10, 11-50, 51-200, 201-500, 500+

    private String industry;

    private String companyLocation;

    private String designation; // HR Manager, Talent Acquisition, etc.

    private String linkedinProfile;

    @Column(nullable = false)
    private Boolean isVerified = false;

    private String verificationDocument; // URL to company verification doc
}

