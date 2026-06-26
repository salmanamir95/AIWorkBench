-- DROP TABLE IF EXISTS user_reviews, user_department_role, employment_history, roles, users, departments, flyway_schema_history CASCADE;


-- Departments with uniqueness constraints
CREATE TABLE departments (
    id BIGSERIAL PRIMARY KEY,
    -- Ensure names don't clash
    name VARCHAR(100) NOT NULL UNIQUE, 
    -- Ensure every department has a distinct financial ID
    cost_center_code VARCHAR(50) UNIQUE, 
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE
);

-- User status ENUM type for better data integrity
CREATE TYPE user_status AS ENUM ('ACTIVE', 'ON_LEAVE', 'TERMINATED', 'SUSPENDED');

-- Users with constraints and ENUM type for status
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    -- Simple Regex for email format validation
    email VARCHAR(255) UNIQUE NOT NULL 
        CONSTRAINT email_format CHECK (email ~* '^[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,4}$'),
    full_name VARCHAR(255) NOT NULL,
    -- Salary constraint
    base_salary DECIMAL(19, 4) CHECK (base_salary > 0),
    -- Using the custom ENUM type
    status user_status NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE
);

-- Roles
CREATE TABLE roles (
    id BIGSERIAL PRIMARY KEY,
    role_name VARCHAR(100) NOT NULL,
    department_id BIGINT, -- Allow NULL if a role becomes unassigned
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE,
    
    -- Applying the constraints for referential integrity
    CONSTRAINT fk_roles_department 
        FOREIGN KEY (department_id) 
        REFERENCES departments(id) 
        ON UPDATE CASCADE 
        ON DELETE SET NULL
);


-- Employment History
CREATE TABLE employment_history (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL, -- Ensure it cannot be null
    salary DECIMAL(19, 4) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE,

    -- 1. Ensure end_date is logical
    CONSTRAINT check_dates CHECK (end_date IS NULL OR end_date >= start_date),
    
    -- 2. Salary constraint (must be positive)
    CONSTRAINT check_salary_positive CHECK (salary > 0),
    
    -- 3. Referential integrity with CASCADE
    CONSTRAINT fk_employment_user 
        FOREIGN KEY (user_id) 
        REFERENCES users(id) 
        ON UPDATE CASCADE 
        ON DELETE CASCADE
);

-- 1. Junction table for User assignments
-- This links a User to a specific Department AND Role
CREATE TABLE user_department_role (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE ON UPDATE CASCADE,
    department_id BIGINT NOT NULL REFERENCES departments(id) ON DELETE CASCADE ON UPDATE CASCADE,
    role_id BIGINT NOT NULL REFERENCES roles(id) ON DELETE CASCADE ON UPDATE CASCADE,
    assigned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ended_at TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE
);

-- 2. Performance Reviews and Ratings
CREATE TABLE user_reviews (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    reviewer_id BIGINT NOT NULL,
    rating INT NOT NULL CHECK (rating BETWEEN 1 AND 5),
    comment TEXT,
    review_date DATE DEFAULT CURRENT_DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE,

    -- Constraint for the subject of the review
    CONSTRAINT fk_review_user 
        FOREIGN KEY (user_id) 
        REFERENCES users(id) 
        ON UPDATE CASCADE 
        ON DELETE CASCADE,

    -- Constraint for the person giving the review
    CONSTRAINT fk_review_reviewer 
        FOREIGN KEY (reviewer_id) 
        REFERENCES users(id) 
        ON UPDATE CASCADE 
        ON DELETE CASCADE
);
