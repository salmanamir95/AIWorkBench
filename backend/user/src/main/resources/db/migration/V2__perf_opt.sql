-- Departments table performance optimization

CREATE INDEX idx_departments_cost_center_active 
ON departments(cost_center_code) 
WHERE is_deleted = FALSE;


CREATE INDEX idx_departments_active_lookup 
ON departments(is_deleted, name) 
WHERE is_deleted = FALSE;

-- 1. Index for Email lookups (Used for Auth and User profile retrieval)
-- Using a partial index since we almost never search for deleted users.
CREATE INDEX idx_users_email_active ON users(email) WHERE is_deleted = FALSE;

-- 2. Index for Status filtering
-- Crucial if you have dashboards showing "All Active Users" or "All Terminated Users"
CREATE INDEX idx_users_status_active ON users(status) WHERE is_deleted = FALSE;

-- 3. Composite index for name searches (Optional but recommended)
-- Useful if your UI has a search bar to find users by full_name
CREATE INDEX idx_users_full_name ON users(full_name) WHERE is_deleted = FALSE;

-- Index for rapid lookups of all roles within a specific department
CREATE INDEX idx_roles_department_id_active 
ON roles(department_id) 
WHERE is_deleted = FALSE;

-- Index for searching roles by name (useful for search/autocomplete features)
CREATE INDEX idx_roles_name_active 
ON roles(role_name) 
WHERE is_deleted = FALSE;

-- Index to quickly fetch salary history for a specific user
CREATE INDEX idx_employment_history_user_active 
ON employment_history(user_id) 
WHERE is_deleted = FALSE;

-- Index for date-based reporting (e.g., "What was the salary on Date X?")
CREATE INDEX idx_employment_history_dates 
ON employment_history(start_date, end_date) 
WHERE is_deleted = FALSE;

-- Index for finding all assignments for a specific user
CREATE INDEX idx_udr_user_active ON user_department_role(user_id) 
WHERE is_deleted = FALSE;

-- Index for finding all users within a specific department
CREATE INDEX idx_udr_dept_active ON user_department_role(department_id) 
WHERE is_deleted = FALSE;

-- Index for finding all users in a specific role
CREATE INDEX idx_udr_role_active ON user_department_role(role_id) 
WHERE is_deleted = FALSE;

-- Index for fetching all reviews received by a user
CREATE INDEX idx_user_reviews_user_active 
ON user_reviews(user_id) 
WHERE is_deleted = FALSE;

-- Index for fetching all reviews written by a specific reviewer
CREATE INDEX idx_user_reviews_reviewer_active 
ON user_reviews(reviewer_id) 
WHERE is_deleted = FALSE;