package com.jobapp.jobservice.repository;

import com.jobapp.jobservice.model.Application;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    
    Optional<Application> findByJobIdAndCandidateId(Long jobId, Long candidateId);
    
    Boolean existsByJobIdAndCandidateId(Long jobId, Long candidateId);
    
    Page<Application> findByCandidateId(Long candidateId, Pageable pageable);
    
    Page<Application> findByJobId(Long jobId, Pageable pageable);
    
    List<Application> findByJobIdAndStatus(Long jobId, Application.ApplicationStatus status);
    
    Long countByJobId(Long jobId);
    
    Long countByCandidateId(Long candidateId);
}

