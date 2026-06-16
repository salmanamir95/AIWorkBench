-- Ensure all databases exist
CREATE DATABASE auth_db;
CREATE DATABASE users;
CREATE DATABASE project_db;
CREATE DATABASE ai_db;
CREATE DATABASE task_db;
CREATE DATABASE security;

-- Grant access
GRANT ALL PRIVILEGES ON DATABASE auth_db TO aiworkbench;
GRANT ALL PRIVILEGES ON DATABASE users TO aiworkbench;
GRANT ALL PRIVILEGES ON DATABASE project_db TO aiworkbench;
GRANT ALL PRIVILEGES ON DATABASE ai_db TO aiworkbench;
GRANT ALL PRIVILEGES ON DATABASE task_db TO aiworkbench;
GRANT ALL PRIVILEGES ON DATABASE security TO aiworkbench;