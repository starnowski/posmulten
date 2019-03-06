CREATE USER "postgresql-core-superuser" WITH
  SUPERUSER
  ENCRYPTED PASSWORD 'superuser123';

CREATE USER "postgresql-core-owner" WITH
  NOSUPERUSER
  ENCRYPTED PASSWORD 'owner123';

CREATE USER "postgresql-core-user" WITH
  NOSUPERUSER
  ENCRYPTED PASSWORD 'user123';

CREATE DATABASE postgresql_core WITH
  OWNER = "postgresql-core-owner"
  ENCODING = 'UTF8'
  CONNECTION LIMIT = -1;

GRANT ALL PRIVILEGES ON DATABASE postgresql_core to "postgresql-core-owner" ;

\c postgresql_core;

CREATE TABLE public.users
(
    id bigint NOT NULL,
    name character varying(255),
    CONSTRAINT users_pkey PRIMARY KEY (id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

CREATE TABLE public.groups
(
    uuid uuid NOT NULL,
    name character varying(255),
    CONSTRAINT groups_pkey PRIMARY KEY (uuid)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

CREATE TABLE public.users_groups
(
    user_id bigint NOT NULL,
    group_id uuid NOT NULL,
    CONSTRAINT fk_users_groups_user_id FOREIGN KEY (user_id)
        REFERENCES users (id) MATCH SIMPLE,
    CONSTRAINT fk_users_groups_group_id FOREIGN KEY (group_id)
            REFERENCES groups (uuid) MATCH SIMPLE
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

CREATE TABLE public.posts
(
    id bigint NOT NULL,
    text text NOT NULL,
    user_id bigint NOT NULL,
    CONSTRAINT fk_posts_user_id FOREIGN KEY (user_id)
              REFERENCES users (id) MATCH SIMPLE,
    CONSTRAINT posts_pkey PRIMARY KEY (id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.users
    OWNER to "postgresql-core-owner";

ALTER TABLE public.groups
    OWNER to "postgresql-core-owner";

ALTER TABLE public.users_groups
    OWNER to "postgresql-core-owner";

ALTER TABLE public.posts
    OWNER to "postgresql-core-owner";