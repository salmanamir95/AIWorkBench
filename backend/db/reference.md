
postgres=# CREATE DATABASE aiworkbench_user_db;
CREATE DATABASE
postgres=# CREATE USER aiworkbench_user_usr WITH ENCRYPTED PASSWORD '123456';
CREATE ROLE
postgres=# GRANT ALL PRIVILEGES ON DATABASE aiworkbench_user_db TO aiworkbench_user_usr;
GRANT
postgres=# 