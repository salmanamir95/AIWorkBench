CREATE TRIGGER before_user_insert
BEFORE INSERT ON users
FOR EACH ROW
IF NEW.username NOT REGEXP '^[A-Za-z][A-Za-z0-9_]{2,49}$'
    OR NEW.email NOT REGEXP '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$'
THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Invalid insert format';
END IF;