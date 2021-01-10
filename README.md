# Posmulten

[![Build Status](https://travis-ci.org/starnowski/posmulten.svg?branch=master)](https://travis-ci.org/starnowski/posmulten)
[![Download](https://api.bintray.com/packages/starnowski/posmulten/posmulten/images/download.svg) ](https://bintray.com/starnowski/posmulten/posmulten/_latestVersion)
[![Maven Central](https://img.shields.io/maven-central/v/com.github.starnowski.posmulten/postgresql-core.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.github.starnowski.posmulten%22%20AND%20a:%22postgresql-core%22)
[![CodeQL](https://github.com/starnowski/posmulten/workflows/CodeQL/badge.svg)](https://github.com/starnowski/posmulten/actions?query=workflow%3ACodeQL)


* [Introduction](#introduction)
* [Multi-tenancy](#multi-tenancy)
    * [Separate database strategy](#separate-database)
    * [Separate schema strategy](#separate-schema)
    * [Shared schema strategy](#shared-schema)
        * [Implementation requirements](#implementation-requirements)
    * [How posmulten helps to implement shared schema strategy?](#how-posmulten-helps-to-implement-shared-schema-strategy)
        * [Setting RLS policy](#setting-rls-policy)
            * [Function that checks tenant access to a table row](#function-that-checks-tenant-access-to-a-table-row)
            * [Function that checks if the passed identifier is the same as the current tenant identifier](#function-that-checks-if-the-passed-identifier-is-the-same-as-the-current-tenant-identifier)
            * [Function that returns the current tenant identifier](#function-that-returns-the-current-tenant-identifier)
            * [Function that set the current tenant identifier](#function-that-set-the-current-tenant-identifier)
        * [Connecting to Database](#connecting-to-database)
        * [Adding constraints for foreign key columns](#adding-constraints-for-foreign-key-columns)
        * [Other columns modifications](#other-columns-modifications)
* [How to start using posmulten](#how-to-start-using-posmulten)
    * [Setting maven dependency](#setting-maven-dependency)
    * [How to start using builder](#how-to-start-using-builder)
        * [Applying builder changes](#applying-builder-changes)
        * [Dropping builder changes](#dropping-builder-changes)
        * [Using posmulten components with database connection](#using-posmulten-components-with-database-connection)
    * [Setting default database schema](#setting-default-database-schema)
    * [Setting default database user for RLS policy](#setting-default-database-user-for-rls-policy)
    * [Setting RLS Policy for table](#setting-rls-policy-for-table)
        * [Setting RLS Policy for a table with a multi-column primary key](#setting-rls-policy-for-a-table-with-a-multi-column-primary-key)
        * [Setting RLS Policy for a table without primary key](#setting-rls-policy-for-a-table-without-primary-key)
    * [Force RLS Policy for table owner](#force-rls-policy-for-table-owner)
    * [Adding a foreign key constraint](#adding-a-foreign-key-constraint)
        * [Adding a foreign key constraint with a multi-column primary key](#adding-a-foreign-key-constraint-with-a-multi-column-primary-key)
    * [Setting of type for tenant identifier value](#setting-of-type-for-tenant-identifier-value)
    * [Setting the property name that stores tenant identifier value](#setting-the-property-name-that-stores-tenant-identifier-value)
    * [Adding default value for tenant column](#adding-default-value-for-tenant-column)
        * [Skipping adding default value for tenant column for a single table](#skipping-adding-default-value-for-tenant-column-for-a-single-table)
    * [Setting default tenant column name](#setting-default-tenant-column-name)
    * [Setting function name that returns the current tenant identifier](#setting-function-name-that-returns-the-current-tenant-identifier)
    * [Setting function name that sets the current tenant identifier](#setting-function-name-that-sets-the-current-tenant-identifier)
    * [Setting function name that checks if current tenant has authorities to a table row](#setting-function-name-that-checks-if-current-tenant-has-authorities-to-a-table-row)
    * [Setting function name that checks if passed identifier is the same as current tenant identifier](#setting-function-name-that-checks-if-passed-identifier-is-the-same-as-current-tenant-identifier)
    * [Setting function name that checks if passed primary key for a specific table exists for the current tenant](#setting-function-name-that-checks-if-passed-primary-key-for-a-specific-table-exists-for-the-current-tenant)
    * [Setting a list of invalid tenant identifier values](#setting-a-list-of-invalid-tenant-identifier-values)
        * [Setting custom name for table tenant column constraint](#setting-custom-name-for-table-tenant-column-constraint)
    * [Naming convention and its constraints](#naming-convention-and-its-constraints)
    * [Other maven repositories](#other-maven-repositories)
* [Reporting issues](#reporting-issues)
* [Project contribution](#project-contribution)


# Introduction
Posmulten library is an open-source project for the generation of SQL DDL statements that make it easy to implementation of the [shared schema multi-tenancy strategy](#shared-schema) via the [row security policies](https://www.postgresql.org/docs/9.6/ddl-rowsecurity.html) in the Postgres database.
Project is tested for compatibility with the Postgres database in versions 9.6, 10.14, 11.9, 12.4, and 13.0.
The library is written in a java programming language.
The project required at least java 8.

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

A statement that enables the [row security policy](https://www.postgresql.org/docs/9.6/ddl-rowsecurity.html) mechanism is also created.
<br/>
```sql
ALTER TABLE "posts" ENABLE ROW LEVEL SECURITY;
```
<b>IMPORTANT!</b>
<br/>
When the database user for who row security policy is supposed to be created is table owner, it is required to [force row security policy](#force-rls-policy-for-table-owner) mechanism.
By default, posmulten does not force this mechanism.
<br/>
<br/>

#### Function that checks tenant access to a table row
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

#### Function that checks if the passed identifier is the same as the current tenant identifier
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

#### Function that returns the current tenant identifier
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

#### Function that set the current tenant identifier
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

### Connecting to Database 
After correct setup of RLS policies the way how we connect to database and execute sql script has to be changed a little bit.
For example let's assume that the name of session property that stores current tenant identifier is "c.c_ten".
The function that set value for this property is set_current_tenant_id(VARCHAR(255)) and function that return its value is called get_current_tenant_id().
If opening connection as user for which the RLS was configured it is required to set tenant identifier.
Below there is example that show what happens in situation when tenant identifier is not set.
<b>Important</b>, all examples were generated in the Postgres database in version 9.6. 
```sql
SELECT * FROM users;
```
For displaying rows from the users table without setting tenant identifier there is going to be thrown exception.
```sql
ERROR:  unrecognized configuration parameter "c.c_ten"
SQL state: 42704
```

Let's pretend that our newly created tenant should have the name "SOME_TENANT_1".
```sql
SELECT set_current_tenant_id('SOME_TENANT_1');
SELECT COUNT(*) FROM users;
```

In the beginning, there are no records.
Below there is an example how to insert into table for tenant.
```sql
SELECT set_current_tenant_id('SOME_TENANT_1');
INSERT INTO users (id, name) VALUES (1, 'Szymon Tarnowski');
INSERT INTO users (id, name, tenant_id) VALUES (2, 'John Doe', 'SOME_TENANT_1');
```
<b>IMPORTANT!</b>The first insert statement without tenant_id is possible because it was used a statement that [adds default value](#adding-default-value-for-tenant-column).
After inserting above records, the previous select statements should return result 2.
In case if we want to change current tenant to 'TENANT_X_2' and display all rows we should get zero results. 
```sql
SELECT set_current_tenant_id('TENANT_X_2');
SELECT COUNT(*) FROM users;
```
After adding a single record we should get the one as a select query result.
```sql
SELECT set_current_tenant_id('TENANT_X_2');
INSERT INTO users (id, name) VALUES (3, 'Jimmy Doe');
SELECT COUNT(*) FROM users;
```
After deleting all results from the users table, the select query should return zero results.
```sql
SELECT set_current_tenant_id('TENANT_X_2');
DELETE FROM users;
SELECT COUNT(*) FROM users;
```  

But if we change current tenant to the 'SOME_TENANT_1' then for select query we should get two records.
```sql
SELECT set_current_tenant_id('SOME_TENANT_1');
SELECT COUNT(*) FROM users;
```

### Adding constraints for foreign key columns
The library has the possibility to add a [foreign key constraint](#adding-a-foreign-key-constraint) that checks if foreign key value references to the row which belongs to the current tenant.
<br/>

```sql
ALTER TABLE "posts" ADD CONSTRAINT posts_users_fk_cu CHECK ((user_id IS NULL) OR (is_user_belongs_to_current_tenant(user_id)));
```
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
            <version>0.2.2</version>
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
<br/>
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
By default, there is no assumption that statement has to contains the compensation operation for operation returned by the getCreateScript() method.
This means that the operation can not be by default treated as a rollback operation, but an operation that removes changes applied by statement returned by the getCreateScript() method.

<br/>
<b>IMPORTANT!</b>
<br/>
Just like was mentioned in the previous section that the statements returned by the getCreateScript() method for objects from the list returned from the getSqlDefinitions() method, should be applied based on the list's order.
Based on that fact, the statements returned by the getDropScript() method for objects, should be executed in the reverse list's order. 

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

#### Using posmulten components with database connection
Other useful components that type ISharedSchemaContext contains is object of type "ISetCurrentTenantIdFunctionPreparedStatementInvocationFactory" returned by method getISetCurrentTenantIdFunctionPreparedStatementInvocationFactory().
Component of type ISetCurrentTenantIdFunctionPreparedStatementInvocationFactory returns statement that sets current tenant identifier and can be used by PreparedStatement object.

```java
import com.github.starnowski.posmulten.postgresql.core.rls.function.ISetCurrentTenantIdFunctionPreparedStatementInvocationFactory;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
//...
        @Autowired
        JdbcTemplate jdbcTemplate;
//...
        ISetCurrentTenantIdFunctionPreparedStatementInvocationFactory setCurrentTenantIdFunctionPreparedStatementInvocationFactory = sharedSchemaContext.getISetCurrentTenantIdFunctionPreparedStatementInvocationFactory();
        jdbcTemplate.execute(setCurrentTenantIdFunctionPreparedStatementInvocationFactory.returnPreparedStatementThatSetCurrentTenant(), (PreparedStatementCallback<Integer>) preparedStatement -> {
            preparedStatement.setString(1, "some-tenant-id-SDFAFD-DZXCV");
            preparedStatement.executeQuery();
            ResultSet rs = preparedStatement.getConnection().createStatement().executeQuery("SELECT COUNT(*) FROM users");
            rs.next();
            return rs.getInt(1);
        });
```

Assuming that function that [sets current tenant](#setting-function-name-that-sets-the-current-tenant-identifier) has name 'set_tenant' the ISetCurrentTenantIdFunctionPreparedStatementInvocationFactory#returnPreparedStatementThatSetCurrentTenant() method is going to return string just like below:
```sql
SELECT set_tenant(?);
```

Type ISharedSchemaContext contains also object of type ISetCurrentTenantIdFunctionInvocationFactory returned by method getISetCurrentTenantIdFunctionInvocationFactory().
The ISetCurrentTenantIdFunctionInvocationFactory type has method that returns similar result as ISetCurrentTenantIdFunctionPreparedStatementInvocationFactory but without "?" mark.
Instead it sets specific value passed as argument, for example:

```java
    String tenant = "TXDS-tenant-id";
    ISetCurrentTenantIdFunctionInvocationFactory setCurrentTenantIdFunctionDefinition = sharedSchemaContext.getISetCurrentTenantIdFunctionInvocationFactory();
    jdbcTemplate.execute(String.format("%1$s UPDATE users %2$s SET name = '%2$s' WHERE id = %3$d ;", setCurrentTenantIdFunctionDefinition.generateStatementThatSetTenant(tenant), updatedName, user.getId()));
```
<br/>
<b>IMPORTANT!</b>
<br/>

Please in mind that if it is required to use custom sql statement that might use values passed out side application, for instance from application user then is more secure to use PreparedStatement type.
It is also worth mentioning that some frameworks for optimization purposes use database connection pools.
Just like in the examples above where the JdbcTemplate is used.
In case when the component is used in the context of the already existed transaction, then the Spring framework might use a connection that is kept in context for the local thread. 


### Setting default database schema
Builder component has one constructor with one String parameter (there is a task to add non-argument constructor [135](https://github.com/starnowski/posmulten/issues/135)).
The constructor is name of default schema for which statement are going to be created.
If null is going to passed then by default the schema name is not going to be added to created DDL statements.
For example the code below:
```java
import com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder;
//...
    DefaultSharedSchemaContextBuilder defaultSharedSchemaContextBuilder = new DefaultSharedSchemaContextBuilder(null); // Constructor with passed null parameter
    Map<String, String> usersTablePrimaryKeyNameToType = new HashMap();
    usersTablePrimaryKeyNameToType.put("id", "bigint");
    Map<String, String> postsTablePrimaryKeyNameToType = new HashMap();
    postsTablePrimaryKeyNameToType.put("id", "bigint");
    Map<String, String> postsTableForeignKeyToUserPrimaryKey = new HashMap();
    postsTableForeignKeyToUserPrimaryKey.put("user_id", "id");
    defaultSharedSchemaContextBuilder.setGrantee("postgresql-core-owner");
    defaultSharedSchemaContextBuilder.createRLSPolicyForTable("users", usersTablePrimaryKeyNameToType, "tenant_id", "users_table_rls_policy");
    defaultSharedSchemaContextBuilder.createRLSPolicyForTable("posts", postsTablePrimaryKeyNameToType, "tenant_id", "posts_table_rls_policy");
    //...
    defaultSharedSchemaContextBuilder.createSameTenantConstraintForForeignKey("posts", "users, postsTableForeignKeyToUserPrimaryKey, "posts_users_fk_cu");
```

is going to produce below statements (displayed only few results)

```sql
CREATE POLICY users_table_rls_policy ON users
FOR ALL
TO "postgresql-core-owner"
USING (tenant_has_authorities(tenant_id, 'ALL', 'USING', 'users', 'public'))
WITH CHECK (tenant_has_authorities(tenant_id, 'ALL', 'WITH_CHECK', 'users', 'public'));

---..
ALTER TABLE "posts" ADD CONSTRAINT posts_users_fk_cu CHECK ((user_id IS NULL) OR (is_user_belongs_to_current_tenant(user_id)));
```

In case if the "non_public_schema" value would be passed to constructor the result would look different.

```java
//...
    DefaultSharedSchemaContextBuilder defaultSharedSchemaContextBuilder = new DefaultSharedSchemaContextBuilder("non_public_schema"); // Constructor with passed "non_public_schema" parameter
//...
```

It would contain the "non_public_schema" schema name.

```sql
CREATE POLICY users_table_rls_policy ON non_public_schema.users
FOR ALL
TO "postgresql-core-owner"
USING (non_public_schema.tenant_has_authorities(tenant_id, 'ALL', 'USING', 'users', 'non_public_schema'))
WITH CHECK (non_public_schema.tenant_has_authorities(tenant_id, 'ALL', 'WITH_CHECK', 'users', 'non_public_schema'));

---..
ALTER TABLE "non_public_schema"."posts" ADD CONSTRAINT posts_users_fk_cu CHECK ((user_id IS NULL) OR (non_public_schema.is_user_belongs_to_current_tenant(user_id)));
```

### Setting default database user for RLS policy
Builder required to specify default database user for which the [row security policies](https://www.postgresql.org/docs/9.6/ddl-rowsecurity.html) are going to be created.
For the below criteria:
```java
import com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder;
//...
    DefaultSharedSchemaContextBuilder defaultSharedSchemaContextBuilder = new DefaultSharedSchemaContextBuilder(null); // Constructor with passed null parameter
    Map<String, String> usersTablePrimaryKeyNameToType = new HashMap();
    usersTablePrimaryKeyNameToType.put("id", "bigint");
    defaultSharedSchemaContextBuilder.createRLSPolicyForTable("users", usersTablePrimaryKeyNameToType, "tenant_id", "users_table_rls_policy");
    //...
```
and database user with name "postgresql-core-owner"
```java
    //...
    defaultSharedSchemaContextBuilder.setGrantee("postgresql-core-owner");
    //...
```
the builder will produce:
```sql
CREATE POLICY users_table_rls_policy ON users
FOR ALL
TO "postgresql-core-owner"
USING (tenant_has_authorities(tenant_id, 'ALL', 'USING', 'users', 'public'))
WITH CHECK (tenant_has_authorities(tenant_id, 'ALL', 'WITH_CHECK', 'users', 'public'));
```
For user "app-first-user"
```java
    //...
    defaultSharedSchemaContextBuilder.setGrantee("app-first-user");
    //...
```
the builder will produce:
```sql
CREATE POLICY users_table_rls_policy ON users
FOR ALL
TO "app-first-user"
USING (tenant_has_authorities(tenant_id, 'ALL', 'USING', 'users', 'public'))
WITH CHECK (tenant_has_authorities(tenant_id, 'ALL', 'WITH_CHECK', 'users', 'public'));
```

### Setting RLS Policy for table
The most crucial thing from builder perspective is to define which tables need have created [row security policy](https://www.postgresql.org/docs/9.6/ddl-rowsecurity.html).
The RLS policy is added via method:
```javadoc
com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder#createRLSPolicyForTable(String table, Map<String, String> primaryKeyColumnsList, String tenantColumnName, String rlsPolicyName)
```
<b>table</b> - <b>(Required)</b> table name.<br/>
<b>primaryKeyColumnsList</b> - <b>(Required)</b> map of primary key columns and their types in the table. The column name is the map key and the column type is its value.<br/>
<b>tenantColumnName</b> - <b>(Optional)</b> name of the column that stores tenant identifier in table. In null value will be passed then default tenant column name will be used.
Set by builder or [custom value](#setting-default-tenant-column-name).<br/>
<b>rlsPolicyName</b> - <b>(Required)</b> name of row level security policy (see [task 48](https://github.com/starnowski/posmulten/issues/48)).<br/>

For the below criteria:
```java
import com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder;
//...
    DefaultSharedSchemaContextBuilder defaultSharedSchemaContextBuilder = new DefaultSharedSchemaContextBuilder("some_schema");
    defaultSharedSchemaContextBuilder.setGrantee("db-us");
    //...
```
and database policy declaration for table users_tab:
```java
    //...
    usersTablePrimaryKeyNameToType.put("user_uuid", "UUID");
    defaultSharedSchemaContextBuilder.createRLSPolicyForTable("users_tab", usersTablePrimaryKeyNameToType, "ten_col", "users_policy");
    //...
```
the builder will produce:
```sql
CREATE POLICY users_policy ON some_schema.users_tab
FOR ALL
TO "db-us"
USING (some_schema.tenant_has_authorities(ten_col, 'ALL', 'USING', 'users_tab', 'some_schema'))
WITH CHECK (some_schema.tenant_has_authorities(ten_col, 'ALL', 'WITH_CHECK', 'users_tab', 'some_schema'));
```

#### Setting RLS Policy for a table with a multi-column primary key
In case when the table has a more complex primary key than a single column. 
All columns that are part of the primary key have to be passed to the method. 
For example, for below comments table with two-column primary:
```sql
CREATE TABLE public.comments
(
id int NOT NULL,
user_id bigint NOT NULL,
text text NOT NULL,
tenant character varying(255),

parent_comment_id int,
parent_comment_user_id bigint,

CONSTRAINT fk_comments_users_id FOREIGN KEY (user_id)
REFERENCES public.users (id) MATCH SIMPLE,

CONSTRAINT fk_comments_parent_id FOREIGN KEY (parent_comment_id, parent_comment_user_id)
REFERENCES public.comments (id, user_id) MATCH SIMPLE,

CONSTRAINT comments_pkey PRIMARY KEY (id, user_id)
```

the declaration should like below:
```java
    //...
    Map<String, String> commentsTablePrimaryKeyNameToType = new HashMap();
    commentsTablePrimaryKeyNameToType.put("id", "int");
    commentsTablePrimaryKeyNameToType.put("user_id", "bigint");
    defaultSharedSchemaContextBuilder.createRLSPolicyForTable("comments", commentsTablePrimaryKeyNameToType, "tenant", "comments_table_rls_policy");
    //...
```

<b>IMPORTANT!</b><br/>
The primary key columns map is not required to create RLS policy, but it is required when there is any relation between tables (or a single table has a relation to itself) where there is a foreign key constraint. 
In such a case, posmulten needs to know the primary key table definition to create the correct function that checks if a table row exists for a specific tenant.
Such a function is created only if there is any [foreign key constraint declaration](#adding-a-foreign-key-constraint). 

#### Setting RLS Policy for a table without primary key
In case when RLS policy has to be created for a table without a primary key, for example, for join table in many to many relations.
The empty map has to be passed as a method argument.

For example, for the below SQL table:

```sql
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
```
the RLS policy declaration should look like this:
```java
    //...
    defaultSharedSchemaContextBuilder.createRLSPolicyForTable("users_groups", new HashMap<>(), "tenant_id", "users_groups_table_rls_policy");
    //...
```
<b>INFORMATION!</b> There is a [task](https://github.com/starnowski/posmulten/issues/138) whose goal is to add the ability to pass a null value as a map to give the same result.

### Force RLS Policy for table owner
In situation when RLS policy has to be created for database user that is a tables owner in schema then there has to additional DDL instruction created for each table.
It is because just like is mentioned in Postgres documentation 
_Table owners normally bypass row security as well, though a table owner can choose to be subject to row security with ALTER TABLE ... FORCE ROW LEVEL SECURITY._
To specify this option builder has method:
```javadoc
com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder#setForceRowLevelSecurityForTableOwner(boolean forceRowLevelSecurityForTableOwner)
```

For example, for below requirements:
```java
import com.github.starnowski.posmulten.postgresql.core.context.ISharedSchemaContext;
import com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder;
//...
    Map<String, String> usersTablePrimaryKeyNameToType = new HashMap();
    usersTablePrimaryKeyNameToType.put("id", "bigint");
    Map<String, String> postsTablePrimaryKeyNameToType = new HashMap();
    postsTablePrimaryKeyNameToType.put("id", "bigint");
    DefaultSharedSchemaContextBuilder defaultSharedSchemaContextBuilder = new DefaultSharedSchemaContextBuilder(null);
    defaultSharedSchemaContextBuilder.setForceRowLevelSecurityForTableOwner(true);
    defaultSharedSchemaContextBuilder.createRLSPolicyForTable("users", usersTablePrimaryKeyNameToType, "tenant_id", "users_table_rls_policy");
    defaultSharedSchemaContextBuilder.createRLSPolicyForTable("posts", postsTablePrimaryKeyNameToType, "tenant_id", "posts_table_rls_policy");
    //... other criteria
```
builder will produce 
```sql
ALTER TABLE "users" FORCE ROW LEVEL SECURITY;
ALTER TABLE "posts" FORCE ROW LEVEL SECURITY;
```

### Adding a foreign key constraint
The builder can create an additional constraint that checks if foreign key value references to the table row that belongs to the current tenant.
```javadoc
createSameTenantConstraintForForeignKey(String mainTable, String foreignKeyTable, Map<String, String> foreignKeyPrimaryKeyColumnsMappings, String constraintName)
```
<b>mainTable</b> - <b>(Required)</b> table name that has foreign key columns.<br/>
<b>foreignKeyTable</b> - <b>(Required)</b> table name that is in relation.<br/>
<b>foreignKeyPrimaryKeyColumnsMappings</b> - <b>(Required)</b> map of foreign key columns and primary key columns from table which is in relation. The foreign key column name is the map key and the column name from relation table is its value.<br/>
<b>constraintName</b> - <b>(Required)</b> constraint name.<br/>

For example, assuming that we have two tables, "users" and "posts":
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

for below criteria:
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
    //... foreign key constraint declaration
    Map<String, String> foreignKeyColumnToPrimaryKeyColumn = new HashMap();
    foreignKeyColumnToPrimaryKeyColumn.put("user_id", "id");
    defaultSharedSchemaContextBuilder.createSameTenantConstraintForForeignKey("posts", "users", foreignKeyColumnToPrimaryKeyColumn, "posts_users_fk_cu");

    // setting name for function
    defaultSharedSchemaContextBuilder.setNameForFunctionThatChecksIfRecordExistsInTable("users", "is_user_belongs_to_current_tenant");
```
posmulten is going to produce the below statements related to foreign key constraint:
```sql
CREATE OR REPLACE FUNCTION is_user_belongs_to_current_tenant(bigint) RETURNS BOOLEAN AS $$
SELECT EXISTS (
	SELECT 1 FROM users rt WHERE rt.id = $1 AND rt.tenant_id = get_current_tenant_id()
)
$$ LANGUAGE sql
STABLE
PARALLEL SAFE;

ALTER TABLE "posts" ADD CONSTRAINT posts_users_fk_cu CHECK ((user_id IS NULL) OR (is_user_belongs_to_current_tenant(user_id)));
```
As was mentioned in previous [sections](#setting-rls-policy-for-a-table-with-a-multi-column-primary-key), posmulten creates a function that checks if a row with a specified primary key exists for the current tenant.
For this example, the is_user_belongs_to_current_tenant function was created because table posts have a foreign key column that references table users.
At this moment, the [name](#setting-function-name-that-checks-if-passed-primary-key-for-a-specific-table-exists-for-the-current-tenant) for such function has to be specified; otherwise, the builder can throw an exception.

#### Adding a foreign key constraint with a multi-column primary key
Below there is an example of how to specify a foreign key constraint when the key has many columns. 
The comments table has a primary key with two columns.
```sql
CREATE TABLE public.comments
(
id int NOT NULL,
user_id bigint NOT NULL,
text text NOT NULL,
tenant character varying(255),

parent_comment_id int,
parent_comment_user_id bigint,

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
```
The table has a relation to itself (the comment has its parent).
Below is an example of how to define foreign key constraints with the builder component.
```java
import com.github.starnowski.posmulten.postgresql.core.context.ISharedSchemaContext;
import com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder;
//...

    Map<String, String> commentsTablePrimaryKeyNameToType = new HashMap();
    commentsTablePrimaryKeyNameToType.put("id", "int");
    commentsTablePrimaryKeyNameToType.put("user_id", "bigint");
    defaultSharedSchemaContextBuilder.createRLSPolicyForTable("comments", commentsTablePrimaryKeyNameToType, "tenant", "comments_table_rls_policy");
    Map<String, String> foreignKeyColumnToPrimaryKeyColumn = new HashMap();
    foreignKeyColumnToPrimaryKeyColumn.put("parent_comment_id", "id");
    foreignKeyColumnToPrimaryKeyColumn.put("parent_comment_user_id", "user_id");
    defaultSharedSchemaContextBuilder.createSameTenantConstraintForForeignKey("comments", "comments", foreignKeyColumnToPrimaryKeyColumn, "comments_parent_comments_fk_cu");
```
the builder will produce the below statements:
```sql
CREATE OR REPLACE FUNCTION is_comment_belongs_to_current_tenant(bigint, int) RETURNS BOOLEAN AS $$
SELECT EXISTS (
	SELECT 1 FROM comments rt WHERE rt.user_id = $1 AND rt.id = $2 AND rt.tenant = get_current_tenant_id()
)
$$ LANGUAGE sql
STABLE
PARALLEL SAFE;
--
ALTER TABLE "comments" ADD CONSTRAINT comments_parent_comments_fk_cu CHECK ((parent_comment_id IS NULL AND parent_comment_user_id IS NULL) OR (is_comment_belongs_to_current_tenant(parent_comment_user_id, parent_comment_id)));
```

### Setting of type for tenant identifier value
By default, the builder assumes that the tenant column type is going to be `VARCHAR(255)`.
This also the type for parameters of a few function:

- [Function that checks tenant access to a table row](#function-that-checks-tenant-access-to-a-table-row)
- [Function that checks if the passed identifier is the same as the current tenant identifier](#function-that-checks-if-the-passed-identifier-is-the-same-as-the-current-tenant-identifier)
- [Function that set the current tenant identifier](#function-that-set-the-current-tenant-identifier)

It is also return type for function [function that returns the current tenant identifier](#function-that-returns-the-current-tenant-identifier).
The method that set this type is:

```javadoc
com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder#setCurrentTenantIdPropertyType(String currentTenantIdPropertyType
```

For example, if the value is going to be set to the "UUID" then the builder is going to produce a statement like this:

```sql
CREATE OR REPLACE FUNCTION get_current_tenant_id() RETURNS UUID AS $$
SELECT current_setting('c.c_ten')
$$ LANGUAGE sql
STABLE
PARALLEL SAFE;

CREATE OR REPLACE FUNCTION set_current_tenant_id(UUID) RETURNS VOID AS $$
BEGIN
PERFORM set_config('c.c_ten', $1, false);
END
$$ LANGUAGE plpgsql
VOLATILE;
```

### Setting the property name that stores tenant identifier value
By default builder use property name "posmulten.tenant_id" to set current tenant identifier.
This property is used in function that [set current tenant identifier](#function-that-set-the-current-tenant-identifier) or [gets its value](#function-that-returns-the-current-tenant-identifier).
```javadoc
com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder#setCurrentTenantIdProperty(String currentTenantIdProperty)
```

### Adding default value for tenant column
The builder can create statements that add default values statements for the tenant column in each table. 
By default, the builder does not do that. 
To specify behavior builder use method:
```javadoc
com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder#setCurrentTenantIdentifierAsDefaultValueForTenantColumnInAllTables(boolean value)
```
For example, for database tables below:
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
and builder criteria:
```java
import com.github.starnowski.posmulten.postgresql.core.context.ISharedSchemaContext;
import com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder;
    Map<String, String> usersTablePrimaryKeyNameToType = new HashMap();
    usersTablePrimaryKeyNameToType.put("id", "bigint");
    Map<String, String> postsTablePrimaryKeyNameToType = new HashMap();
    postsTablePrimaryKeyNameToType.put("id", "bigint");
    DefaultSharedSchemaContextBuilder defaultSharedSchemaContextBuilder = new DefaultSharedSchemaContextBuilder(null);
    defaultSharedSchemaContextBuilder.createRLSPolicyForTable("users", usersTablePrimaryKeyNameToType, "tenant_id", "users_table_rls_policy");
    defaultSharedSchemaContextBuilder.createRLSPolicyForTable("posts", postsTablePrimaryKeyNameToType, "tenant_id", "posts_table_rls_policy");
//... other criteria
    defaultSharedSchemaContextBuilder.setCurrentTenantIdentifierAsDefaultValueForTenantColumnInAllTables(true);
```
the builder will produce below statements:
```sql
ALTER TABLE users ALTER COLUMN tenant_id SET DEFAULT get_current_tenant_id();
ALTER TABLE posts ALTER COLUMN tenant_id SET DEFAULT get_current_tenant_id();
```

#### Skipping adding default value for tenant column for a single table
The builder has a method that allows skipping the creation of the default value statements for a specific table when the option of adding default value for all tenant columns is on.
```javadoc
com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder#skipAddingOfTenantColumnDefaultValueForTable(String value)
```
For below criteria:
```java
import com.github.starnowski.posmulten.postgresql.core.context.ISharedSchemaContext;
import com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder;
    Map<String, String> usersTablePrimaryKeyNameToType = new HashMap();
    usersTablePrimaryKeyNameToType.put("id", "bigint");
    Map<String, String> postsTablePrimaryKeyNameToType = new HashMap();
    postsTablePrimaryKeyNameToType.put("id", "bigint");
    DefaultSharedSchemaContextBuilder defaultSharedSchemaContextBuilder = new DefaultSharedSchemaContextBuilder(null);
    defaultSharedSchemaContextBuilder.createRLSPolicyForTable("users", usersTablePrimaryKeyNameToType, "tenant_id", "users_table_rls_policy");
    defaultSharedSchemaContextBuilder.createRLSPolicyForTable("posts", postsTablePrimaryKeyNameToType, "tenant_id", "posts_table_rls_policy");
//... other criteria
    defaultSharedSchemaContextBuilder.setCurrentTenantIdentifierAsDefaultValueForTenantColumnInAllTables(true);
    defaultSharedSchemaContextBuilder.skipAddingOfTenantColumnDefaultValueForTable("posts");
```
the builder will produce below statements:
```sql
ALTER TABLE users ALTER COLUMN tenant_id SET DEFAULT get_current_tenant_id();
```
builder will not produce a statement for the posts table.

### Adding tenant column to tenant table
The builder can produce a statement that creates a tenant column for a table that does not have it.
```javadoc
com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder#createTenantColumnForTable(String table)
```
For below criteria:
```java
import com.github.starnowski.posmulten.postgresql.core.context.ISharedSchemaContext;
import com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder;
    Map<String, String> notificationsTablePrimaryKeyNameToType = new HashMap();
    notificationsTablePrimaryKeyNameToType.put("uuid", "uuid");
    DefaultSharedSchemaContextBuilder defaultSharedSchemaContextBuilder = new DefaultSharedSchemaContextBuilder(null);
    defaultSharedSchemaContextBuilder.createTenantColumnForTable("notifications")
        .createRLSPolicyForTable("notifications", notificationsTablePrimaryKeyNameToType, "tenant_x", "notifications_table_rls_policy")
//... other criteria
```
the builder will produce below statements:
```sql
ALTER TABLE notifications ADD COLUMN tenant_x VARCHAR(255);
ALTER TABLE notifications ALTER COLUMN tenant_x SET NOT NULL;
ALTER TABLE notifications ALTER COLUMN tenant_x SET DEFAULT get_current_tenant_id();
```

### Setting default tenant column name
By default tenant, the builder assumes that tenant column name is "tenant_id".
It is important during [adding tenant column](#adding-default-value-for-tenant-column).
Or specifying [RLS policy](#setting-rls-policy-for-table) for a table without specifying a tenant column name. 
In such a case, the builder assumes that the tenant column for such a table is equal to the default value. 
The builder allows to change default tenant column name via method:
```javadoc
com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder#setDefaultTenantIdColumn(String defaultTenantIdColumn)
```

### Setting function name that returns the current tenant identifier
The builder allows to set the name of [function that returns the current tenant identifier](#function-that-returns-the-current-tenant-identifier) via method:
```javadoc
com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder#setGetCurrentTenantIdFunctionName(String getCurrentTenantIdFunctionName)
```

### Setting function name that sets the current tenant identifier
The builder allows to set the name of [function that set the current tenant identifier](#function-that-set-the-current-tenant-identifier) via method:
```javadoc
com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder#setSetCurrentTenantIdFunctionName(String setCurrentTenantIdFunctionName)
```

### Setting function name that checks if current tenant has authorities to a table row
The builder allows to set the name of [function that checks tenant access to a table row](#function-that-checks-tenant-access-to-a-table-row) via method:
```javadoc
com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder#setTenantHasAuthoritiesFunctionName(String tenantHasAuthoritiesFunctionName)
```

### Setting function name that checks if passed identifier is the same as current tenant identifier
The builder allows to set the name of [function that checks if the passed identifier is the same as the current tenant identifier](#function-that-checks-if-the-passed-identifier-is-the-same-as-the-current-tenant-identifier) via method:
```javadoc
com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder#setEqualsCurrentTenantIdentifierFunctionName(String equalsCurrentTenantIdentifierFunctionName)
```

### Setting function name that checks if passed primary key for a specific table exists for the current tenant
It is required to specify the function that checks if the passed identifier exists in a specific table.
The builder has a single method for this purpose.
```javadoc
com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder#setNameForFunctionThatChecksIfRecordExistsInTable(String recordTable, String functionName)
```
<b>recordTable</b> - <b>(Required)</b> table name.<br/>
<b>functionName</b> - <b>(Required)</b> function name.<br/>
TODO

### Setting a list of invalid tenant identifier values
The builder allows to specify the list of invalid tenant identifier values via method:
```javadoc
com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder#createValidTenantValueConstraint(List<String> tenantValuesBlacklist, String isTenantValidFunctionName, String isTenantValidConstraintName)
```
<b>tenantValuesBlacklist</b> - <b>(Required)</b> list of invalid tenant identifier values.<br/>
<b>isTenantValidFunctionName</b> - <b>(Optional)</b> name of the function that checks if passed tenant identifier is valid. If passed value is null then function name is "is_tenant_identifier_valid".<br/>
<b>isTenantValidConstraintName</b> - <b>(Optional)</b> name of the constraint that checks if the tenant column has a valid value. If the passed value is null then the constraint name is "tenant_identifier_valid".<br/>

For below criteria:
```java
import com.github.starnowski.posmulten.postgresql.core.context.ISharedSchemaContext;
import com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder;
    Map<String, String> usersTablePrimaryKeyNameToType = new HashMap();
    usersTablePrimaryKeyNameToType.put("id", "bigint");
    Map<String, String> postsTablePrimaryKeyNameToType = new HashMap();
    postsTablePrimaryKeyNameToType.put("id", "bigint");
    DefaultSharedSchemaContextBuilder defaultSharedSchemaContextBuilder = new DefaultSharedSchemaContextBuilder(null);
    defaultSharedSchemaContextBuilder.createRLSPolicyForTable("users", usersTablePrimaryKeyNameToType, "tenant_id", "users_table_rls_policy");
    defaultSharedSchemaContextBuilder.createRLSPolicyForTable("posts", postsTablePrimaryKeyNameToType, "tenant_id", "posts_table_rls_policy");
//... other criteria
    defaultSharedSchemaContextBuilder.createValidTenantValueConstraint(asList("DUMMMY_TENANT",  "XXX-INVAlid_tenant"), null, null);
```
the builder will produce below statements:
```sql
CREATE OR REPLACE FUNCTION is_tenant_id_valid(VARCHAR(255)) RETURNS BOOLEAN AS $$
SELECT $1 <> CAST ('DUMMMY_TENANT' AS VARCHAR(255)) AND $1 <> CAST ('XXX-INVAlid_tenant' AS VARCHAR(255))
$$ LANGUAGE sql
IMMUTABLE
PARALLEL SAFE;
ALTER TABLE "users" ADD CONSTRAINT tenant_should_be_valid CHECK (tenant_id IS NULL OR is_tenant_id_valid(tenant_id));
ALTER TABLE "posts" ADD CONSTRAINT tenant_should_be_valid CHECK (tenant_id IS NULL OR is_tenant_id_valid(tenant_id));
```
#### Setting custom name for table tenant column constraint
Builder allows specifying custom constraint name via the method:
```javadoc
com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder#registerCustomValidTenantValueConstraintNameForTable(String table, String constraintName)
```
<b>table</b> - <b>(Required)</b> name of table.<br/>
<b>constraintName</b> - <b>(Required)</b> name of constraint.<br/>

for below criteria:
```java
import com.github.starnowski.posmulten.postgresql.core.context.ISharedSchemaContext;
import com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder;
    Map<String, String> usersTablePrimaryKeyNameToType = new HashMap();
    usersTablePrimaryKeyNameToType.put("id", "bigint");
    Map<String, String> postsTablePrimaryKeyNameToType = new HashMap();
    postsTablePrimaryKeyNameToType.put("id", "bigint");
    DefaultSharedSchemaContextBuilder defaultSharedSchemaContextBuilder = new DefaultSharedSchemaContextBuilder(null);
    defaultSharedSchemaContextBuilder.createRLSPolicyForTable("users", usersTablePrimaryKeyNameToType, "tenant_id", "users_table_rls_policy");
    defaultSharedSchemaContextBuilder.createRLSPolicyForTable("posts", postsTablePrimaryKeyNameToType, "tenant_id", "posts_table_rls_policy");
    defaultSharedSchemaContextBuilder.createValidTenantValueConstraint(asList("DUMMMY_TENANT",  "XXX-INVAlid_tenant"), null, null);
//... other criteria
    defaultSharedSchemaContextBuilder.registerCustomValidTenantValueConstraintNameForTable("posts", "posts_tenant_is_valid");
```
the builder will produce below statements:
```sql
ALTER TABLE "users" ADD CONSTRAINT tenant_should_be_valid CHECK (tenant_id IS NULL OR is_tenant_id_valid(tenant_id));
ALTER TABLE "posts" ADD CONSTRAINT posts_tenant_is_valid CHECK (tenant_id IS NULL OR is_tenant_id_valid(tenant_id));
```

### Naming convention and its constraints
TODO - [Create a validator component that checks if the passed identifier has the correct name.](https://github.com/starnowski/posmulten/issues/137)

### Other maven repositories
TODO

# Reporting issues
* Any new issues please report in [GitHub site](https://github.com/starnowski/posmulten/issues)

# Project contribution
At this moment please create issue on page the [issues](https://github.com/starnowski/posmulten/issues) with "[CONTRIBUTION]".
Or send message to me directly on [LinkedIn site](https://pl.linkedin.com/in/szymon-tarnowski-a104b4150).
TODO


