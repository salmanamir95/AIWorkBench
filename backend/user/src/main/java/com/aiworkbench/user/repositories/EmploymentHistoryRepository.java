package com.aiworkbench.user.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.aiworkbench.user.entities.EmploymentHistory;


@Repository
public interface EmploymentHistoryRepository extends JpaRepository<EmploymentHistory, Long> {

    // Fetch all history records for a specific user
    List<EmploymentHistory> findByUserId(Long userId);

    // Find the currently active record (where end_date is null)
    @Query("SELECT e FROM EmploymentHistory e WHERE e.user.id = :userId AND e.endDate IS NULL")
    Optional<EmploymentHistory> findCurrentByUserId(@Param("userId") Long userId);

    // Fetch history within a specific timeframe
    @Query("SELECT e FROM EmploymentHistory e WHERE e.user.id = :userId AND e.startDate >= :start")
    List<EmploymentHistory> findHistorySince(@Param("userId") Long userId, @Param("start") java.time.LocalDate start);
    
}
