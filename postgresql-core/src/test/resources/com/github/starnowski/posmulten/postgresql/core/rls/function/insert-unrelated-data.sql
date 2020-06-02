--- public schema

-- users
INSERT INTO public.users (id, name, tenant_id) VALUES (1, 'Szymon Tarnowski', 'primary_tenant');
INSERT INTO public.users (id, name, tenant_id) VALUES (2, 'John Doe', 'secondary_tenant');
INSERT INTO public.users (id, name, tenant_id) VALUES (3, 'Jane Doe', 'primary_tenant');

-- comments
--INSERT INTO comments (id, user_id, text, tenant) VALUES (1, 2, 'Some phrase', 'secondary_tenant');
--INSERT INTO comments (id, user_id, text, tenant) VALUES (2, 2, 'Lorem Ipsum', 'secondary_tenant');
--INSERT INTO comments (id, user_id, text, tenant) VALUES (3, 1, 'Lorem Ipsum', 'primary_tenant');

--- non_public_schema schema

-- users
INSERT INTO non_public_schema.users (id, name, tenant_id) VALUES (1, 'Bob Tarnowski', 'third_tenant');
INSERT INTO non_public_schema.users (id, name, tenant_id) VALUES (2, 'John Doe', 'third_tenant');
INSERT INTO non_public_schema.users (id, name, tenant_id) VALUES (3, 'Bill Doe', 'primary_tenant');
INSERT INTO non_public_schema.users (id, name, tenant_id) VALUES (4, 'Michael Doe', 'third_tenant');

-- comments
--INSERT INTO non_public_schema.comments (id, user_id, text, tenant) VALUES (1, 2, 'Some phrase', 'third_tenant');
--INSERT INTO non_public_schema.comments (id, user_id, text, tenant) VALUES (2, 2, 'Lorem Ipsum', 'third_tenant');
--INSERT INTO non_public_schema.comments (id, user_id, text, tenant) VALUES (3, 3, 'Lorem Ipsum', 'primary_tenant');
--INSERT INTO non_public_schema.comments (id, user_id, text, tenant) VALUES (4, 3, 'Lorem Ipsum', 'primary_tenant');