DELIMITER $$

CREATE TRIGGER before_user_insert
BEFORE INSERT ON users
FOR EACH ROW
BEGIN

    IF NEW.username NOT REGEXP '^[A-Za-z][A-Za-z0-9_]{2,49}$' THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Invalid username format';
    END IF;

    IF NEW.email NOT REGEXP '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$' THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Invalid email format';
    END IF;

END$$


CREATE TRIGGER before_user_update
BEFORE UPDATE ON users
FOR EACH ROW
BEGIN

    IF NEW.username <> OLD.username THEN
        IF NEW.username NOT REGEXP '^[A-Za-z][A-Za-z0-9_]{2,49}$' THEN
            SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Invalid username format';
        END IF;
    END IF;

    IF NEW.email <> OLD.email THEN
        IF NEW.email NOT REGEXP '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$' THEN
            SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Invalid email format';
        END IF;
    END IF;

END$$

DELIMITER ;