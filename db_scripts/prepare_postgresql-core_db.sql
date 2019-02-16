CREATE USER "postgresql-core-owner" WITH
  LOGIN
  NOSUPERUSER
  ENCRYPTED PASSWORD 'owner123';

CREATE DATABASE postgresql_core WITH
  OWNER = "postgresql-core-owner"
  ENCODING = 'UTF8'
  CONNECTION LIMIT = -1;

GRANT ALL PRIVILEGES ON DATABASE postgresql_core to "postgresql-core-owner" ;