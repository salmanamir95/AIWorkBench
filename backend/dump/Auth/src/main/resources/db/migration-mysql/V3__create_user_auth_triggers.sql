CREATE TRIGGER trg_user_auth_before_insert
BEFORE INSERT ON user_auth
FOR EACH ROW
BEGIN
    IF NEW.email IS NULL OR TRIM(NEW.email) = '' THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Email cannot be empty';
    END IF;

    IF NEW.password NOT LIKE '$argon2%' THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Password must be Argon2 hashed before storing';
    END IF;
END;

CREATE TRIGGER trg_user_auth_before_update
BEFORE UPDATE ON user_auth
FOR EACH ROW
BEGIN
    IF NEW.password NOT LIKE '$argon2%' THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Password must be Argon2 hashed before storing';
    END IF;

    IF OLD.email_verified = TRUE AND NEW.email_verified = FALSE
       AND OLD.email = NEW.email THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Cannot unverify email without changing it';
    END IF;
END;
