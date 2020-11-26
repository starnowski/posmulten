# Posmulten

[![Build Status](https://travis-ci.org/starnowski/posmulten.svg?branch=master)](https://travis-ci.org/starnowski/posmulten)
[![Download](https://api.bintray.com/packages/starnowski/posmulten/posmulten/images/download.svg) ](https://bintray.com/starnowski/posmulten/posmulten/_latestVersion)
[![Maven Central](https://img.shields.io/maven-central/v/com.github.starnowski.posmulten/postgresql-core.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.github.starnowski.posmulten%22%20AND%20a:%22postgresql-core%22)


* [Introduction](#introduction)
* [Multi-tenancy](#multi-tenancy)
    * [Separate database strategy](#separate-database)
    * [Separate schema strategy](#separate-schema)
    * [Shared schema strategy](#shared-schema)
        * [Implementation requirements](#implementation-requirements)
    * [How posmulten helps to implement shared schema strategy?](#how-posmulten-helps-to-implement-shared-schema-strategy)
        * [Setting RLS policy](#setting-rls-policy)
        * [Adding constraints for foreign key columns](#adding-constraints-for-foreign-key-columns)
        * [Connecting to Database](#connecting-to-database)


# Introduction
Posmulten library is an open-source project for the generation of SQL DDL statements that make it easy to implementation of the [shared schema multi-tenancy strategy](#shared-schema) via the [row security policies](https://www.postgresql.org/docs/9.6/ddl-rowsecurity.html) in the Postgres database.
Project is tested for compatibility with the Postgres database in versions 9.6, 10.14, 11.9, 12.4, and 13.0.

# Multi-tenancy

Based on [hibernate documentation](https://docs.jboss.org/hibernate/orm/4.3/devguide/en-US/html/ch16.html) 
_`the term multi-tenancy in general is applied to software development to indicate an architecture in which a single running instance of an application simultaneously serves multiple clients (tenants).`_
There are three main strategies [separate database](#separate-database), [separate schema](#separate-schema) and [shared schema](#shared-schema)
Below you can find short description of those strategies. Of course we will focus more on [shared schema](#shared-schema).
For more information about what pros and cons of each approach please check below links:

* https://docs.jboss.org/hibernate/orm/4.3/devguide/en-US/html/ch16.html
* https://medium.com/@MentorMate/increase-efficiency-with-multi-tenant-cloud-software-architecture-4261fca6025e

## Separate database
In this strategy each tenant's data is stored in separate database. 
For this obvious reason this approach gives the highest isolation level.

<p align="center">
  <img src="https://raw.githubusercontent.com/starnowski/posmulten/master/doc/Separate_database.png">
</p>


## Separate schema
Strategy assumes that each tenant's data is kept in his own schema but all those schemas exists in single database instance.
Obviously this approach offers lower isolation level than [separate database](#separate-database) but allows to potentially save costs for infrastructure by not having multiple database instances.

<p align="center">
  <img src="https://raw.githubusercontent.com/starnowski/posmulten/master/doc/Separate_schema.png">
</p>

## Shared schema
In this strategies data all tenants are kept in single database and same schema.
Although there is no limitation that there has to be only one schema in database but all tenants should have same access to them.
The strategy assumes that all tables in a database (with an exception for tables that stores vocabulary data or data available for all tenants) have a column that stores tenant identifier.
Of course, based on this value column, we know which tenant is the owner of the table raw.
Obviously, this approach offers a lower isolation level than both previous strategies but, just like [shared schema](#shared-schema) strategy, allows to save costs for infrastructure potentially.
In comparison to [shared schema](#shared-schema) strategy we can say that although postgres can handle with multiple schemas and even we can find comments from person who use this strategy postgres.
We can find statements that with larger number of tenants the maintenance for this approach is harder than when schema is shared between tenants.
Executing ddl scripts for hundreds schemas might be easily automated but still it might create complexity and slower deployment.
It does not mean that executing ddl scripts for database with shared schema approach is error free.
Every change should be considered.
There are some other [important considerations](#implementation-requirements) which should taken before deciding for shared schema strategy.


<p align="center">
  <img src="https://raw.githubusercontent.com/starnowski/posmulten/master/doc/Shared_schema.png">
</p>

### Implementation requirements
Just like it mention in section above, the [shared schema](#shared-schema) strategy assumes that all tables shared by tenant have column which defines its owner.
This means that the value of this column has to be checked in each SQL query and operation.
Column has to checked during execution of SELECT statement and all operations that modifies data like UPDATE, INSERT and DELETE.
In case when decision is made that application should handle this checks then every sql statement send to database by application need to contains in WHERE statement condition for tenant column for each table.
It might be easier if application use some ORM framework. 
But still if application code contains any custom query then developer has to be aware that he needs to add checks for tenant identifier column.
The pros for this approach is that if we would ever consider changing database engine for application then it would be quick easy
However there are cons for such situation if for example we would consider of having many application that use same database.
All application should have implemented tenant column checks. 
Not to mention if those application would be writen in different programing languages.

Another approach is that the database engine is going to handle tenant column checks.
Of course, not all database engines support such features.
One such database is Oracle with a feature called the [virtual private database](https://www.oracle.com/database/technologies/virtual-private-db.html).
And also Postgres has a similar feature called the [row security policy](https://www.postgresql.org/docs/9.6/ddl-rowsecurity.html) which posmulten is [using](#setting-rls-policy).
This approach might be better if you planned to have multiple different applications connected to your database.
Besides some connection adjustments, there is no additional logic that has to be added to every SQL statement created by the application code.
Not to mention the situation when connected applications are written in different programming languages.

One thing that might be considered during implementation is constraints that check if the foreign key columns reference rows that belong to the same tenant.
Of course, assuming if the above requirement is fulfilled, then even if SQL injection will succeed, the application that checks tenant column should not display the record for other tenants or modify it.
But there might be a situation when a separate application is also connected to a database but operate on data without checking the tenant column.
For example, statistical data gathering or other specific goals where joining data from different tables is crucial.
Posmulten helps to [create such constraints](#adding-constraints-for-foreign-key-columns) for the foreign key columns.


## How posmulten helps to implement shared schema strategy?

Below there is short explanation how posmulten helps to implement shared schema strategy and what ddl statements are generated.
The ddl statements examples are generated for tables users and posts.

`
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
`

`
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
`

### Setting RLS policy

Posmulten use [row security policy](https://www.postgresql.org/docs/9.6/ddl-rowsecurity.html) mechanism to handle tenant data isolation.
Creates policy for all operations (syntax "ALL" applies to SQL commands like INSERT, SELECT, UPDATE, DELETE) and [specific user](#setting-default-database-user-for-rls-policy).

`
CREATE POLICY posts_table_rls_policy ON posts
FOR ALL
TO "postgresql-core-owner"
USING (tenant_has_authorities(tenant_id, 'ALL', 'USING', 'posts', 'public'))
WITH CHECK (tenant_has_authorities(tenant_id, 'ALL', 'WITH_CHECK', 'posts', 'public'));
`

Creates DDL statement for a function that checks if the current tenant for the database session has access to table row based on tenant column (for the case below it is "tenant_id") value.
***Current function logic is not complex, but this might be changed in the next release.***
The [function name](#setting-function-name-that-checks-if-current-tenant-has-authorities-to-a-table-row) can be customize.
The type of function first argument is the same as the default type for tenant identifier value which can be [customized](#setting-of-type-for-tenant-identifier-value).

`
CREATE OR REPLACE FUNCTION tenant_has_authorities(VARCHAR(255), VARCHAR(255), VARCHAR(255), VARCHAR(255), VARCHAR(255)) RETURNS BOOLEAN AS $$
SELECT is_id_equals_current_tenant_id($1)
$$ LANGUAGE sql
STABLE
PARALLEL SAFE;
`

Creates a DDL statement for a function that checks if the current tenant identifier set for the database session is equal to the passed tenant identifier.
The [function name](#setting-function-name-that-checks-if-passed-identifier-is-the-same-as-current-tenant-identifier) and function [argument type](#setting-of-type-for-tenant-identifier-value) can be changed.

`
CREATE OR REPLACE FUNCTION is_id_equals_current_tenant_id(VARCHAR(255)) RETURNS BOOLEAN AS $$
SELECT $1 = get_current_tenant_id()
$$ LANGUAGE sql
STABLE
PARALLEL SAFE;
`

TODO

### Adding constraints for foreign key columns
TODO

### Connecting to Database 
TODO

# How to start using posmulten
TODO

# Setting maven dependency
TODO

# Setting default database user for RLS policy
TODO

# Adding tenant column to tenant table

# Setting RLS Policy for table
TODO

# Setting of type for tenant identifier value
TODO

# Setting function name that checks if current tenant has authorities to a table row
TODO

# Setting function name that checks if passed identifier is the same as current tenant identifier
TODO




