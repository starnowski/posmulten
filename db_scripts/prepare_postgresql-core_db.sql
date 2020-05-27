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
    tenant_id character varying(255),
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
    tenant_id character varying(255),
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
    tenant_id character varying(255),
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
    tenant_id character varying(255),
    CONSTRAINT fk_posts_user_id FOREIGN KEY (user_id)
              REFERENCES users (id) MATCH SIMPLE,
    CONSTRAINT posts_pkey PRIMARY KEY (id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

CREATE TABLE public.comments
(
id int NOT NULL,
user_id bigint NOT NULL,
text text NOT NULL,
post_id bigint NOT NULL,
tenant character varying(255),

parent_comment_id int,
parent_comment_user_id bigint,

CONSTRAINT fk_comments_posts_id FOREIGN KEY (post_id)
REFERENCES public.posts (id) MATCH SIMPLE,
CONSTRAINT fk_comments_users_id FOREIGN KEY (user_id)
REFERENCES public.users (id) MATCH SIMPLE,

CONSTRAINT fk_comments_parent_id FOREIGN KEY (parent_comment_id, parent_comment_user_id)
REFERENCES public.comments (id, user_id) MATCH SIMPLE,

CONSTRAINT comments_pkey PRIMARY KEY (id, user_id)
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

ALTER TABLE public.comments
OWNER to "postgresql-core-owner";

-- sequences definition
CREATE SEQUENCE public.primary_sequence
INCREMENT 1
START 32
MINVALUE 1
MAXVALUE 9223372036854775807
CACHE 1;

ALTER SEQUENCE public.primary_sequence
OWNER TO "postgresql-core-owner";

-- The separate database schema, used for the test cases where there are no default privileges set, just like in case of 'public' schema.
CREATE SCHEMA non_public_schema AUTHORIZATION "postgresql-core-owner";

CREATE TABLE non_public_schema.users
(
id bigint NOT NULL,
name character varying(255),
tenant_id character varying(255),
CONSTRAINT users_pkey PRIMARY KEY (id)
)
WITH (
OIDS = FALSE
)
TABLESPACE pg_default;

CREATE TABLE non_public_schema.groups
(
uuid uuid NOT NULL,
name character varying(255),
tenant_id character varying(255),
CONSTRAINT groups_pkey PRIMARY KEY (uuid)
)
WITH (
OIDS = FALSE
)
TABLESPACE pg_default;

CREATE TABLE non_public_schema.users_groups
(
user_id bigint NOT NULL,
group_id uuid NOT NULL,
tenant_id character varying(255),
CONSTRAINT fk_users_groups_user_id FOREIGN KEY (user_id)
REFERENCES non_public_schema.users (id) MATCH SIMPLE,
CONSTRAINT fk_users_groups_group_id FOREIGN KEY (group_id)
REFERENCES non_public_schema.groups (uuid) MATCH SIMPLE
)
WITH (
OIDS = FALSE
)
TABLESPACE pg_default;

CREATE TABLE non_public_schema.posts
(
id bigint NOT NULL,
text text NOT NULL,
user_id bigint NOT NULL,
tenant_id character varying(255),
CONSTRAINT fk_posts_user_id FOREIGN KEY (user_id)
REFERENCES non_public_schema.users (id) MATCH SIMPLE,
CONSTRAINT posts_pkey PRIMARY KEY (id)
)
WITH (
OIDS = FALSE
)
TABLESPACE pg_default;

CREATE TABLE non_public_schema.comments
(
id int NOT NULL,
user_id bigint NOT NULL,
text text NOT NULL,
post_id bigint NOT NULL,
tenant character varying(255),

parent_comment_id int,
parent_comment_user_id bigint,

CONSTRAINT fk_comments_posts_id FOREIGN KEY (post_id)
REFERENCES non_public_schema.posts (id) MATCH SIMPLE,
CONSTRAINT fk_comments_users_id FOREIGN KEY (user_id)
REFERENCES non_public_schema.users (id) MATCH SIMPLE,

CONSTRAINT fk_comments_parent_id FOREIGN KEY (parent_comment_id, parent_comment_user_id)
REFERENCES non_public_schema.comments (id, user_id) MATCH SIMPLE,

CONSTRAINT comments_pkey PRIMARY KEY (id, user_id)
)
WITH (
OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE non_public_schema.groups
OWNER to "postgresql-core-owner";

ALTER TABLE non_public_schema.users_groups
OWNER to "postgresql-core-owner";

ALTER TABLE non_public_schema.posts
OWNER to "postgresql-core-owner";

ALTER TABLE non_public_schema.comments
OWNER to "postgresql-core-owner";

-- sequences definition
CREATE SEQUENCE non_public_schema.primary_sequence
INCREMENT 1
START 32
MINVALUE 1
MAXVALUE 9223372036854775807
CACHE 1;

ALTER SEQUENCE non_public_schema.primary_sequence
OWNER TO "postgresql-core-owner";