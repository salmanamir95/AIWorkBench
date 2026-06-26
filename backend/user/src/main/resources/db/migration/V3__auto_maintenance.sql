-- Just for the reference has been deal with the updated_at column in the core tables, so we don't need to create a trigger for it anymore.

-- CREATE OR REPLACE FUNCTION update_updated_at_column()
-- RETURNS TRIGGER AS $$
-- BEGIN
--     NEW.updated_at = CURRENT_TIMESTAMP;
--     RETURN NEW;
-- END;
-- $$ language 'plpgsql';

-- -- Triggers for all core tables
-- CREATE TRIGGER trg_departments_update BEFORE UPDATE ON departments FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
-- CREATE TRIGGER trg_users_update BEFORE UPDATE ON users FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
-- CREATE TRIGGER trg_roles_update BEFORE UPDATE ON roles FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
-- CREATE TRIGGER trg_employment_history_update BEFORE UPDATE ON employment_history FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
-- CREATE TRIGGER trg_user_department_role_update BEFORE UPDATE ON user_department_role FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
-- CREATE TRIGGER trg_user_reviews_update BEFORE UPDATE ON user_reviews FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

