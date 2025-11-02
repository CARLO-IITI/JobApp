package com.jobapp.userservice.repository;

import com.jobapp.userservice.model.WorkExperience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkExperienceRepository extends JpaRepository<WorkExperience, Long> {
    List<WorkExperience> findByUserIdOrderByStartDateDesc(Long userId);
    List<WorkExperience> findByUserIdAndCurrentlyWorking(Long userId, Boolean currentlyWorking);
}

