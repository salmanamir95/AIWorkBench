DELIMITER $$

CREATE TRIGGER before_user_insert
BEFORE INSERT ON users
FOR EACH ROW
BEGIN

    -- Username validation
    IF NEW.username NOT REGEXP '^[A-Za-z][A-Za-z0-9_]{2,49}$' THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Invalid username format';
    END IF;

    -- Email validation
    IF NEW.email NOT REGEXP '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$' THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Invalid email format';
    END IF;

    -- Password validation
    IF NEW.password NOT REGEXP '^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*()_+\\-={}:;\"\\'<>,.?/]).{8,}$' THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Password does not meet security requirements';
    END IF;

END$$

CREATE TRIGGER before_user_update
BEFORE UPDATE ON users
FOR EACH ROW
BEGIN

    -- Username validation (only if changed)
    IF NEW.username != OLD.username THEN
        IF NEW.username NOT REGEXP '^[A-Za-z][A-Za-z0-9_]{2,49}$' THEN
            SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Invalid username format';
        END IF;
    END IF;

    -- Email validation (only if changed)
    IF NEW.email != OLD.email THEN
        IF NEW.email NOT REGEXP '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$' THEN
            SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Invalid email format';
        END IF;
    END IF;

    -- Password validation (only if changed)
    IF NEW.password != OLD.password THEN
        IF NEW.password NOT REGEXP '^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*()_+\\-={}:;\"\\'<>,.?/]).{8,}$' THEN
            SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Password does not meet security requirements';
        END IF;
    END IF;

END$$

DELIMITER ;