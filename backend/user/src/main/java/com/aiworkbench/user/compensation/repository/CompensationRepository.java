package com.aiworkbench.user.compensation.repository;

import com.aiworkbench.user.compensation.entity.Compensation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;


public interface CompensationRepository extends JpaRepository<Compensation, Long> {

    // =========================================================
    // 1. USER HISTORY (PAGINATED)
    // =========================================================
    Page<Compensation> findByUserId(Long userId, Pageable pageable);

    Page<Compensation> findByUserIdOrderByEffectiveFromDesc(Long userId, Pageable pageable);

    // =========================================================
    // 2. RANGE QUERIES (ALL USERS / PAGINATED)
    // =========================================================
    Page<Compensation> findByEffectiveFromLessThanEqualAndEffectiveToGreaterThanEqual(
            LocalDate from,
            LocalDate to,
            Pageable pageable
    );

    // =========================================================
    // 3. RANGE QUERIES (SPECIFIC USER / PAGINATED)
    // =========================================================
    Page<Compensation> findByUserIdAndEffectiveFromLessThanEqualAndEffectiveToGreaterThanEqual(
            Long userId,
            LocalDate from,
            LocalDate to,
            Pageable pageable
    );

    // =========================================================
    // 4. EFFECTIVE_FROM FILTERS
    // =========================================================
    Page<Compensation> findByEffectiveFromGreaterThanEqual(LocalDate from, Pageable pageable);

    Page<Compensation> findByUserIdAndEffectiveFromGreaterThanEqual(
            Long userId,
            LocalDate from,
            Pageable pageable
    );

    // =========================================================
    // 5. SALARY FILTERS
    // =========================================================
    Page<Compensation> findBySalaryGreaterThanEqual(BigDecimal salary, Pageable pageable);

    Page<Compensation> findByUserIdAndSalaryGreaterThanEqual(
            Long userId,
            BigDecimal salary,
            Pageable pageable
    );

    // =========================================================
    // 6. BONUS FILTERS
    // =========================================================
    Page<Compensation> findByBonusGreaterThanEqual(BigDecimal bonus, Pageable pageable);

    Page<Compensation> findByUserIdAndBonusGreaterThanEqual(
            Long userId,
            BigDecimal bonus,
            Pageable pageable
    );

    // =========================================================
    // 7. ACTIVE COMPENSATION
    // =========================================================
    @Query("""
        SELECT c FROM Compensation c
        WHERE c.user.id = :userId
        AND c.effectiveFrom <= :date
        AND (c.effectiveTo IS NULL OR c.effectiveTo >= :date)
    """)
    Optional<Compensation> findActiveCompensation(
            @Param("userId") Long userId,
            @Param("date") LocalDate date
    );

    // =========================================================
    // 8. OVERLAP CHECK (still list is fine, pagination not needed)
    // =========================================================
    @Query("""
        SELECT c FROM Compensation c
        WHERE c.user.id = :userId
        AND (
            (c.effectiveTo IS NULL OR c.effectiveTo >= :from)
            AND c.effectiveFrom <= :to
        )
    """)
    Page<Compensation> findOverlapping(
            @Param("userId") Long userId,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to,
            Pageable pageable
    );

    // =========================================================
    // 9. PAYROLL ANALYTICS (KEEP NON-PAGEABLE — AGGREGATES)
    // =========================================================

    @Query("""
        SELECT COALESCE(SUM(c.salary + c.bonus), 0)
        FROM Compensation c
        WHERE c.user.id = :userId
        AND c.effectiveFrom <= :date
        AND (c.effectiveTo IS NULL OR c.effectiveTo >= :date)
    """)
    BigDecimal getUserTotalPay(@Param("userId") Long userId,
                               @Param("date") LocalDate date);

    @Query("""
        SELECT COALESCE(SUM(c.salary), 0)
        FROM Compensation c
        WHERE c.user.id = :userId
    """)
    BigDecimal getUserBasePaySum(@Param("userId") Long userId);

    @Query("""
        SELECT COALESCE(SUM(c.bonus), 0)
        FROM Compensation c
        WHERE c.user.id = :userId
    """)
    BigDecimal getUserBonusSum(@Param("userId") Long userId);

    @Query("""
        SELECT COALESCE(SUM(c.salary + c.bonus), 0)
        FROM Compensation c
        WHERE c.effectiveFrom <= :date
        AND (c.effectiveTo IS NULL OR c.effectiveTo >= :date)
    """)
    BigDecimal getTotalCompanyPayroll(@Param("date") LocalDate date);

    @Query("""
        SELECT COALESCE(SUM(c.salary), 0)
        FROM Compensation c
    """)
    BigDecimal getTotalBaseSalary();

    @Query("""
        SELECT COALESCE(SUM(c.bonus), 0)
        FROM Compensation c
    """)
    BigDecimal getTotalBonusPool();
}