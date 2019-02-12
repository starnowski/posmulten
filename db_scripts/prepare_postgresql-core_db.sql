CREATE USER "postgresql-core-owner" WITH
  LOGIN
  NOSUPERUSER;

CREATE DATABASE postgresql_core WITH
  OWNER = "postgresql-core-owner"
  ENCODING = 'UTF8'
  CONNECTION LIMIT = -1;