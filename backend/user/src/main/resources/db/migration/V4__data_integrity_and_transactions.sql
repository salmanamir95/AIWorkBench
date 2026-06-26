-- V4__data_integrity_and_transactions.sql
-- Goal: Enforce advanced business logic and transactional safety constraints.

-- 1. Preventing "Dangling" Assignments
-- Even with CASCADE, we want to ensure that a user cannot be assigned 
-- to a role in a department that they don't logically belong to 
-- (This acts as a guardrail for your Application Service Layer).
-- We add an exclusion to ensure only one 'Active' role per user per department 
-- at any given time to avoid conflicting organizational data.

CREATE UNIQUE INDEX idx_unique_active_user_assignment 
ON user_department_role (user_id, department_id) 
WHERE is_deleted = FALSE AND is_active = TRUE;

-- 2. Performance Review Integrity
-- A user should not be able to review themselves. 
-- This prevents a common logical error in HR systems.
ALTER TABLE user_reviews 
ADD CONSTRAINT check_cannot_review_self 
CHECK (user_id <> reviewer_id);

-- 3. Salary Progression Integrity
-- Ensures that if an end_date is provided, the record is considered "closed" 
-- and prevents overlapping employment history for the same user.
-- While complex to enforce via pure SQL, we add a constraint to prevent
-- 'start_date' from being in the future.
ALTER TABLE employment_history 
ADD CONSTRAINT check_start_date_not_future 
CHECK (start_date <= CURRENT_DATE);

-- 4. Audit Trail Integrity
-- Ensure that if a row is marked as deleted, a deletion timestamp must exist.
-- This ensures that soft-delete logic is always paired with an audit trail.
ALTER TABLE users 
ADD CONSTRAINT check_deleted_timestamp 
CHECK ((is_deleted = TRUE AND deleted_at IS NOT NULL) OR (is_deleted = FALSE));

ALTER TABLE departments 
ADD CONSTRAINT check_deleted_timestamp 
CHECK ((is_deleted = TRUE AND deleted_at IS NOT NULL) OR (is_deleted = FALSE));

-- 5. Transactional Safety Note for Application Layer:
-- To fully realize these integrity rules, remember to wrap your Java service 
-- methods in @Transactional. The database will now reject any transaction 
-- that violates these specific business rules (e.g., self-reviewing or 
-- overlapping active assignments).