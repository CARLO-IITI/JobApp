package com.jobapp.userservice.repository;

import com.jobapp.userservice.model.ProjectShowcase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectShowcaseRepository extends JpaRepository<ProjectShowcase, Long> {
    List<ProjectShowcase> findByUserIdOrderByCreatedAtDesc(Long userId);
}

