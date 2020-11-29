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
        * [Connecting to Database](#connecting-to-database)
        * [Adding constraints for foreign key columns](#adding-constraints-for-foreign-key-columns)
        * [Other columns modifications](#other-columns-modifications)
* [How to start using posmulten](#how-to-start-using-posmulten)
    * [Setting maven dependency](#setting-maven-dependency)
    * [How to start using builder](#how-to-start-using-builder)
        * [Applying builder changes](#applying-builder-changes)
        * [Dropping builder changes](#dropping-builder-changes)
    * [Setting default database schema](#setting-default-database-schema)
    * [Setting default database user for RLS policy](#setting-default-database-user-for-rls-policy)


# Introduction
Posmulten library is an open-source project for the generation of SQL DDL statements that make it easy to implementation of the [shared schema multi-tenancy strategy](#shared-schema) via the [row security policies](https://www.postgresql.org/docs/9.6/ddl-rowsecurity.html) in the Postgres database.
Project is tested for compatibility with the Postgres database in versions 9.6, 10.14, 11.9, 12.4, and 13.0.
The library is written in a java programming language.

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

```sql
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
```

### Setting RLS policy

Posmulten use [row security policy](https://www.postgresql.org/docs/9.6/ddl-rowsecurity.html) mechanism to handle tenant data isolation.
Creates policy for all operations (syntax "ALL" applies to SQL commands like INSERT, SELECT, UPDATE, DELETE) and [specific user](#setting-default-database-user-for-rls-policy).
<br/>

```sql
CREATE POLICY posts_table_rls_policy ON posts
FOR ALL
TO "postgresql-core-owner"
USING (tenant_has_authorities(tenant_id, 'ALL', 'USING', 'posts', 'public'))
WITH CHECK (tenant_has_authorities(tenant_id, 'ALL', 'WITH_CHECK', 'posts', 'public'));
```
<br/>
<br/>

Creates a DDL statement for a function that checks if the current tenant for the database session has access to table row based on tenant column (for the case below it is "tenant_id") value.
***Current function logic is not complex, but this might be changed in the next release.***
The [function name](#setting-function-name-that-checks-if-current-tenant-has-authorities-to-a-table-row) can be customize.
The type of function first argument is the same as the default type for tenant identifier value which can be [customized](#setting-of-type-for-tenant-identifier-value).
<br/>

```sql
CREATE OR REPLACE FUNCTION tenant_has_authorities(VARCHAR(255), VARCHAR(255), VARCHAR(255), VARCHAR(255), VARCHAR(255)) RETURNS BOOLEAN AS $$
SELECT is_id_equals_current_tenant_id($1)
$$ LANGUAGE sql
STABLE
PARALLEL SAFE;
```
<br/>
<br/>

Creates a DDL statement for a function that checks if the current tenant identifier set for the database session is equal to the passed tenant identifier.
The [function name](#setting-function-name-that-checks-if-passed-identifier-is-the-same-as-current-tenant-identifier) and function [argument type](#setting-of-type-for-tenant-identifier-value) can be changed.
<br/>

```sql
CREATE OR REPLACE FUNCTION is_id_equals_current_tenant_id(VARCHAR(255)) RETURNS BOOLEAN AS $$
SELECT $1 = get_current_tenant_id()
$$ LANGUAGE sql
STABLE
PARALLEL SAFE;
```
<br/>
<br/>

Next function that is created by Posmulten project is function that returns value of identifier for current tenant.
Function reads property value that is save for database session. 
The [property name](#setting-the-property-name-that-stores-tenant-identifier-value) and [function name](#setting-function-name-that-returns-the-current-tenant-identifier) can customized.
<br/>

```sql
CREATE OR REPLACE FUNCTION get_current_tenant_id() RETURNS VARCHAR(255) AS $$
SELECT current_setting('c.c_ten')
$$ LANGUAGE sql
STABLE
PARALLEL SAFE;
```
<br/>
<br/>

Another crucial function is that one which sets current tenant identifier in database connection.
The [property name](#setting-the-property-name-that-stores-tenant-identifier-value) and [function name](#setting-function-name-that-sets-the-current-tenant-identifier) can customized.
The type of function argument is the same as the default type for tenant identifier value which can be [customized](#setting-of-type-for-tenant-identifier-value).
<br/>

```sql
CREATE OR REPLACE FUNCTION set_current_tenant_id(VARCHAR(255)) RETURNS VOID AS $$
BEGIN
PERFORM set_config('c.c_ten', $1, false);
END
$$ LANGUAGE plpgsql
VOLATILE;
```
<br/>
<br/>

### Connecting to Database 
TODO

### Adding constraints for foreign key columns
The library has the possibility to add a [foreign key constraint](#adding-a-foreign-key-constraint) that checks if foreign key value references to the row which belongs to the current tenant.
<br/>

```sql
ALTER TABLE "posts" ADD CONSTRAINT posts_users_fk_cu CHECK ((user_id IS NULL) OR (is_user_belongs_to_current_tenant(user_id)));
```
<br/>
<br/>

The library also creates the function that checks if the passed identifier exists in a specific table.
The statement is created only when the is a request for the creation of a [foreign key constraint](#adding-a-foreign-key-constraint).
The types of arguments for this function are based on column types that a part of the primary key [declared for the table](#setting-rls-policy-for-table) which foreign key references to.
<br/>

```sql
CREATE OR REPLACE FUNCTION is_user_belongs_to_current_tenant(bigint) RETURNS BOOLEAN AS $$
SELECT EXISTS (
	SELECT 1 FROM users rt WHERE rt.id = $1 AND rt.tenant_id = get_current_tenant_id()
)
$$ LANGUAGE sql
STABLE
PARALLEL SAFE;
```
<br/>
<br/>

### Other columns modifications
There are also operations generated by the library that alters table columns.
For example, the library can add a statement that [adds default value](#adding-default-value-for-tenant-column) for the tenant identifier column or [add a tenant column](#adding-tenant-column-to-tenant-table) to the table.
<br/>

# How to start using posmulten
Posmulten is a java project, so besides other projects written in java, you can also use the project as a dependency on projects written in languages executed on java virtual machine.
The project required at least java 8.

### Setting maven dependency
The project is available in the central maven repository.
You can use it just by adding it as a dependency in the project descriptor file (pom.xml).

```xml
        <dependency>
            <groupId>com.github.starnowski.posmulten</groupId>
            <artifactId>postgresql-core</artifactId>
            <version>0.2.0</version>
        </dependency>
```

### How to start using builder
The library's main public component is DefaultSharedSchemaContextBuilder, which produces all required DDL statements based on passed criteria.
For example:

```java
import com.github.starnowski.posmulten.postgresql.core.context.ISharedSchemaContext;
import com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder;
//...
    Map<String, String> usersTablePrimaryKeyNameToType = new HashMap();
    usersTablePrimaryKeyNameToType.put("id", "bigint");
    Map<String, String> postsTablePrimaryKeyNameToType = new HashMap();
    postsTablePrimaryKeyNameToType.put("id", "bigint");
    DefaultSharedSchemaContextBuilder defaultSharedSchemaContextBuilder = new DefaultSharedSchemaContextBuilder(null);
    defaultSharedSchemaContextBuilder.setGrantee("db_user");
    defaultSharedSchemaContextBuilder.createRLSPolicyForTable("users", usersTablePrimaryKeyNameToType, "tenant_id", "users_table_rls_policy");
    defaultSharedSchemaContextBuilder.createRLSPolicyForTable("posts", postsTablePrimaryKeyNameToType, "tenant_id", "posts_table_rls_policy");
    //... other criteria
    ISharedSchemaContext sharedSchemaContext = defaultSharedSchemaContextBuilder.build();
```

The builder component, as a result of method build(), returns an object of type ISharedSchemaContext.
The type contains all properties required to create a shared schema strategy and components that help using it correctly in your java code.

#### Applying builder changes
One of the crucial methods of the ISharedSchemaContext interface is "getSqlDefinitions()".
It returns list of object of type "SQLDefinition" that contains method getCreateScript().
The method returns DDL statement that should be applied to implement shared schema strategy.
<br/>
<b>IMPORTANT!</b>
The list's order is crucial because the DDL statements returned by the getCreateScript() method that represents each object of the list should be applied based on the list's order.
Just like in code example below:
<br/>


```java
import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
//...
        @Autowired
        JdbcTemplate jdbcTemplate;
//...
        List<SQLDefinition> sqlDefinitions = sharedSchemaContext.getSqlDefinitions();
        sqlDefinitions.forEach(sqlDefinition ->
        {
            jdbcTemplate.execute(sqlDefinition.getCreateScript());
        });
```

#### Dropping builder changes
The second important method of the SQLDefinition type is getDropScript().
It returns a statement that drops changes applied by the statement returned by the getCreateScript() method.
<br/>
<b>IMPORTANT!</b>
<br/>
By default, there is no assumption that statement has to contains the compensation operation for operation returned by the {@link #getCreateScript()} method.
This means that the operation can not be by default treated as a rollback operation, but an operation that removes changes applied by statement returned by the {@link #getCreateScript()} method.

<br/>
<b>IMPORTANT!</b>
<br/>
Just like was mentioned in the previous section that the statements returned by the getCreateScript() method for objects from the list returned from the getSqlDefinitions() method, should be applied based on the list's order.
Based on that fact, the statements returned by the getDropScript() method for objects, should be executed in the reverse list's order. 
TODO

```java
import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
//...
        @Autowired
        JdbcTemplate jdbcTemplate;
//...
        List<SQLDefinition> sqlDefinitions = sharedSchemaContext.getSqlDefinitions();
        //Run sql statements in reverse order
        LinkedList<SQLDefinition> stack = new LinkedList<>();
        sqlDefinitions.forEach(stack::push);
        stack.forEach(sqlDefinition ->
        {
            jdbcTemplate.execute(sqlDefinition.getDropScript());
        });
```

TODO

### Setting default database schema
TODO

### Setting default database user for RLS policy
TODO

### Setting RLS Policy for table
TODO

### Adding a foreign key constraint
TODO

### Setting of type for tenant identifier value
TODO

### Setting the property name that stores tenant identifier value
TODO

### Adding default value for tenant column
TODO

### Adding tenant column to tenant table
TODO

### Setting function name that returns the current tenant identifier
TODO

### Setting function name that sets the current tenant identifier
TODO

### Setting function name that checks if current tenant has authorities to a table row
TODO

### Setting function name that checks if passed identifier is the same as current tenant identifier
TODO

### Other maven repositories
TODO

# Reporting issues
TODO

# Project contribution
TODO


