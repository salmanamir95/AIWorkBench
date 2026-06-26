-- V5__insert_dummy_data.sql

-- 1. Departments
INSERT INTO departments (name, cost_center_code) VALUES 
('Engineering', 'ENG-101'),
('Human Resources', 'HR-202'),
('Marketing', 'MKT-303');

-- 2. Users
INSERT INTO users (email, full_name, base_salary, status) VALUES 
('alice@company.com', 'Alice Engineer', 95000.00, 'ACTIVE'),
('bob@company.com', 'Bob Manager', 120000.00, 'ACTIVE'),
('charlie@company.com', 'Charlie Recruiter', 80000.00, 'ON_LEAVE');

-- 3. Roles (Linked to Departments)
INSERT INTO roles (role_name, department_id) VALUES 
('Senior Software Engineer', 1), -- Engineering
('HR Manager', 2),               -- HR
('Marketing Specialist', 3);     -- Marketing

-- 4. Employment History
INSERT INTO employment_history (user_id, salary, start_date) VALUES 
(1, 95000.00, '2025-01-01'),
(2, 120000.00, '2024-06-15'),
(3, 80000.00, '2025-03-01');

-- 5. User Department Role Assignments
INSERT INTO user_department_role (user_id, department_id, role_id) VALUES 
(1, 1, 1), -- Alice in Engineering as Senior Eng
(2, 2, 2), -- Bob in HR as HR Manager
(3, 3, 3); -- Charlie in Marketing as MKT Specialist

-- 6. Performance Reviews
INSERT INTO user_reviews (user_id, reviewer_id, rating, comment) VALUES 
(1, 2, 5, 'Exceptional technical delivery and team leadership.'),
(3, 2, 4, 'Very good recruiting pipeline management.');