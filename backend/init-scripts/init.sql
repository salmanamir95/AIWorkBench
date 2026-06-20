DO $$ 
BEGIN
    IF NOT EXISTS (SELECT FROM pg_database WHERE datname = 'auth_db') THEN
        CREATE DATABASE auth_db;
    END IF;
    IF NOT EXISTS (SELECT FROM pg_database WHERE datname = 'security_db') THEN
        CREATE DATABASE security_db;
    END IF;
    IF NOT EXISTS (SELECT FROM pg_database WHERE datname = 'project_db') THEN
        CREATE DATABASE project_db;
    END IF;
    IF NOT EXISTS (SELECT FROM pg_database WHERE datname = 'ai_db') THEN
        CREATE DATABASE ai_db;
    END IF;
    IF NOT EXISTS (SELECT FROM pg_database WHERE datname = 'task_db') THEN
        CREATE DATABASE task_db;
    END IF;
    IF NOT EXISTS (SELECT FROM pg_database WHERE datname = 'users') THEN
        CREATE DATABASE users;
    END IF;
END $$;