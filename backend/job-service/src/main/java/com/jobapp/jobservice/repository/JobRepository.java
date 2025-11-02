package com.jobapp.jobservice.repository;

import com.jobapp.jobservice.model.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    
    Page<Job> findByStatus(Job.JobStatus status, Pageable pageable);
    
    Page<Job> findByPostedBy(Long postedBy, Pageable pageable);
    
    Page<Job> findByCompanyId(Long companyId, Pageable pageable);
    
    @Query("SELECT j FROM Job j WHERE j.status = 'ACTIVE' AND " +
           "(LOWER(j.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(j.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(j.companyName) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Job> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
    
    @Query("SELECT j FROM Job j WHERE j.status = 'ACTIVE' AND " +
           "LOWER(j.location) = LOWER(:location)")
    Page<Job> findByLocation(@Param("location") String location, Pageable pageable);
    
    List<Job> findTop10ByStatusOrderByCreatedAtDesc(Job.JobStatus status);
}

