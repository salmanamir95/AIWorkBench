CREATE TRIGGER before_user_update
BEFORE UPDATE ON users
FOR EACH ROW
IF (NEW.username <> OLD.username AND NEW.username NOT REGEXP '^[A-Za-z][A-Za-z0-9_]{2,49}$')
    OR (NEW.email <> OLD.email AND NEW.email NOT REGEXP '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$')
THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Invalid update format';
END IF;