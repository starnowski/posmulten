# Configuration-yaml-interpreter

The configuration-yaml-interpreter module can interpreted configuration file in yaml format and based on that it creates
DDL statements that allows to create the shared schema strategy.
The YAML schema description is below.

- #simple-example
- #root-properties
- #setting-a-list-of-invalid-tenant-identifier-values
- #tables-configuration
- #sql-definitions-validation
- #custom-sql-definitions
- #details

## Simple example:

Bellow, there is a configuration example for a schema with the name "public".
The schema contains four tables, users, posts, groups, and users\_groups (many-to-many relation).
All generated changes are going to be applied for the database user with the name "application-user".

```yaml
default_schema: public
default_tenant_id_column: "tenant_id"
grantee: "application-user"
set_current_tenant_identifier_as_default_value_for_tenant_column_in_all_tables: true
valid_tenant_value_constraint:
  tenant_identifiers_blacklist:
    - invalid_tenant
    - "wrong tenant ID"
tables:
  - name: users
    rls_policy:
      name: users_table_rls_policy
      create_tenant_column_for_table: true
      primary_key_definition:
        name_for_function_that_checks_if_record_exists_in_table: "is_user_exists"
        pk_columns_name_to_type:
          id: bigint
  - name: posts
    rls_policy:
      name: "posts_table_rls_policy"
      create_tenant_column_for_table: true
      skip_adding_of_tenant_column_default_value: false
      primary_key_definition:
        name_for_function_that_checks_if_record_exists_in_table: "is_post_exists"
        pk_columns_name_to_type:
          id: bigint
    foreign_keys:
      - constraint_name:  "posts_users_tenant_constraint"
        table_name: "users"
        foreign_key_primary_key_columns_mappings:
          user_id:  id
  - name: groups
    rls_policy:
      name: groups_table_rls_policy
      create_tenant_column_for_table: true
      primary_key_definition:
        name_for_function_that_checks_if_record_exists_in_table: "is_group_exists"
        pk_columns_name_to_type:
          uuid: UUID
  - name: users_groups
    rls_policy:
      name: groups_table_rls_policy
      create_tenant_column_for_table: true
    foreign_keys:
      - constraint_name:  "users_tenant_constraint"
        table_name: "users"
        foreign_key_primary_key_columns_mappings:
          user_id:  id
      - constraint_name:  "groups_tenant_constraint"
        table_name: "groups"
        foreign_key_primary_key_columns_mappings:
          group_id:  uuid
```

## Root properties

| Property name |   Type    |   Required    |   Nullable    |   Description |
|---------------|-----------|---------------|---------------|---------------|
|#default_schema |    String  |   Yes |   Yes |   Name of the database schema for which changes should be applied. |
|#current_tenant_id_property_type |  String  |   No    |   No  |   Type of column that stores tenant identifier and it is also the type of parameters for some generated functions. |
|#current_tenant_id_property |  String  |   No    |   No  |   Property name that stores the value of tenant identifier in the database connection. |
|#get_current_tenant_id_function_name |  String  |   No    |   No  |   Name of the function that returns the current tenant identifier. |
|#set_current_tenant_id_function_name |  String  |   No    |   No  |   Name of the function that sets the current tenant identifier. |
|#tenant_has_authorities_function_name |  String  |   No    |   No  |   Name of the function name that checks if the current tenant has authority to a table row. |
|#force_row_level_security_for_table_owner |  Boolean  |   No    |   Yes  |   Option that force RLS policy for table owner. |
|#default_tenant_id_column |  String  |   No    |   No  |   Default name of column that stores tenant identifier. |
|#grantee |  String  |   Yes    |   No  |   Database user for which RLS policy is going to be created. |

| Property name |   Type    |   Required    |   Nullable    |   Description |
|---------------|-----------|---------------|---------------|---------------|
|#equals_current_tenant_identifier_function_name |  String  |   No    |   No  |   Name of the function name that checks if passed identifier is the same as the current tenant identifier. |
|#set_current_tenant_identifier_as_default_value_for_tenant_column_in_all_tables |  Boolean  |   No    |   Yes  |   Generate a statement that sets a default value for the tenant column in all tables. |

## Setting a list of invalid tenant identifier values

The valid_tenant_value_constraint object is used to configure constraint that will be added to all tenant column in all tables.
Constraint keeps tenant column value valid.

| Property name |   Type    |   Required    |   Nullable    |   Description |
|---------------|-----------|---------------|---------------|---------------|
|tenant\_identifiers\_blacklist|  Array of strings   |   Yes |   No  |   An array of invalid values for tenant identifier. Array need have at least one element |
|#is_tenant_valid_function_name|  String   |   No |   No  |   Name of the function that checks if passed tenant identifier is valid |
|#is_tenant_valid_constraint_name|  String   |   No |   No  |   Name of the constraint that checks if the tenant column has a valid value |

For example, if we want to specify "ROOT", "some\_id" as invalid values for tenant identifier, the configuration should look just like below:

```yaml
valid_tenant_value_constraint:
  tenant_identifiers_blacklist:
    - ROOT
    - "some_id"
```

For more information please check https://github.com/starnowski/posmulten#setting-a-list-of-invalid-tenant-identifier-values.

## Tables configuration

The tables property is an array of objects that each references to a single table.
The table object ables to configure things like the RLS policy or the constraint that checks if the foreign key belongs to the same tenant as a current logged tenant.
It is not mandatory to configure all tables. In some cases, there is no sense to create an entry for the table.
For example, tables that are supposed to use by all tenants, like dictionary tables, do not need to have a configured RLS policy.

Table object

| Property name |   Type    |   Required    |   Nullable    |   Description |
|---------------|-----------|---------------|---------------|---------------|
|name   |   String  |   Yes |   No  |   Name of table   |
|schema   |   String  |   No |   Yes  |   Name of schema. This property overrides the value of the #default_schema for its table   |
|#rls_policy   |   Object  |   No |   No  |   Object that defines RLS for table   |
|#foreign_keys   |   Array of objects  |   No |   No  |   An array of objects that defines foreign key constraint for a table with RLS policy  |

### rls_policy

The rls\_policy entry is required to specify the RLS policy for table.

| Property name |   Type    |   Required    |   Nullable    |   Description |
|---------------|-----------|---------------|---------------|---------------|
|name   |   String  |   Yes |   No  |   Name of the RLS policy   |
|tenant\_column   |   String  |   No |   No  |   Name of the column that stores tenant identifier. This property overrides the value of the #default_tenant_id_column for its table  |
|#create_tenant_column_for_table   |   Boolean  |   No |   No  | Option force to create tenant column for table    |
|valid\_tenant\_value\_constraint\_name   |   String  |   No |   No  |   Name of the constraint that checks if the tenant column has a valid value. This property overrides the value of the #is_tenant_valid_constraint_name for its table  |
|#skip_adding_of_tenant_column_default_value   |   Boolean  |   No |   No  | Option force to skip adding default value to tenant column for a table    |
|#primary_key_definition   |   Object  |   No |   No  | Object that defines primary keys for a table |

### primary_key_definition

| Property name |   Type    |   Required    |   Nullable    |   Description |
|---------------|-----------|---------------|---------------|---------------|
|#pk_columns_name_to_type   |   Map  |   No |   Yes  |   Map of primary key columns where the key is column name and value is its type   |
|#name_for_function_that_checks_if_record_exists_in_table   |   String  |   Yes |   No  |   Function name that checks if passed primary key for a specific table exists for the current tenant   |

### foreign_keys

An array of objects that defines foreign key constraint for a table with RLS policy.
Based on each array object, there is going to be created constraint that checks if a foreign key value refers to a record that exists for the current logged tenant.
| Property name |   Type    |   Required    |   Nullable    |   Description |
|---------------|-----------|---------------|---------------|---------------|
|constraint\_name   |   String  |   Yes |   No  |   Name of the constraint  |
|table\_name   |   String  |   Yes |   No  |   Name of table that foreign key refers to  |
|table\_schema   |   String  |   No |   Yes  |   Name of schema. This property overrides the value of the #default_schema for its table   |
|foreign\_key\_primary\_key\_columns\_mappings   |   Map  |   Yes |   No  |   The map that defines reference between foreign key and primary key columns. The map key is the foreign key column name and the value is the primary key column name  |

Below there are two examples of foreign keys configuration:
First is a simple example where we have table "users" and table "posts" that has a reference (foreign key) to the "users" table (user\_id -> id)

```yaml
default_schema: public
grantee: "application-user"
tables:
  - name: users
    rls_policy:
      name: users_table_rls_policy
      primary_key_definition:
        name_for_function_that_checks_if_record_exists_in_table: "is_user_exists"
        pk_columns_name_to_type:
          id: bigint
  - name: posts
    rls_policy:
      name: "posts_table_rls_policy"
      primary_key_definition:
        name_for_function_that_checks_if_record_exists_in_table: "is_post_exists"
        pk_columns_name_to_type:
          id: bigint
    foreign_keys:
      - constraint_name:  "posts_users_tenant_constraint"
        table_name: "users"
        foreign_key_primary_key_columns_mappings:
          user_id:  id
```

The second example shows a table that has a foreign key with two columns but also it has reference to itself (we assume that comment can have parent comment).

```yaml
default_schema: public
grantee: "application-user"
tables:
  - name: "comments"
    rls_policy:
      name: comments_table_rls_policy
      primary_key_definition:
        name_for_function_that_checks_if_record_exists_in_table: "is_comment_exists"
        pk_columns_name_to_type:
          id: int
          random_uuid: UUID
    foreign_keys:
      - constraint_name:  "comments_comment_parent_tenant_constraint"
        table_name: comments
        foreign_key_primary_key_columns_mappings:
          parent_comment_id:  id
          parent_comment_random_uuid:  random_uuid
```

## SQL definitions validation

The sql_definitions_validation property is used to configure the validation of generated sql definitions.
It is a complex type.

| Property name |   Type    |   Required    |   Nullable    |   Description |
|---------------|-----------|---------------|---------------|---------------|
|disabled   |   Boolean  |   No |   No  |   Validation toggle, by default property has value false which means that validation is enabled |
|identifier\_max\_length   |   Integer  |   No |   No  |   Maximum allowed length for the identifier |
|identifier\_min\_length   |   Integer  |   No |   No  |   Minimum allowed length for the identifier |

Example:

```yaml
sql_definitions_validation:
  identifier_max_length:  76
  identifier_min_length:  5
  disabled: false
```

## Custom SQL Definitions

The custom_sql_definitions property is an array of objects that purpose is to define custom SQL definitions that should be added to the generated script.
Just like the #tables-configuration property, it is also a top element of the configuration file just like in the below example.

Example:

```yaml
tables:
  - name: groups

...

custom_sql_definitions:
  - position: AT_END
    creation_script:  |
      ALTER TABLE groups ADD COLUMN text_col text;
    drop_script: |
      ALTER TABLE groups DROP COLUMN text_col;
    validation_scripts:
      - |
        SELECT COUNT(1) FROM information_schema.columns WHERE table_catalog = 'postgresql_core' AND table_schema = 'public' AND table_name = 'groups' AND column_name = 'text_col';
  - position: CUSTOM
    custom_position: "Some custom position"
    creation_script:  |
      ALTER ...
    validation_scripts:
      - |
        SELECT (371) FROM ...
```

custom_sql_definitions object

| Property name |   Type    |   Required    |   Nullable    |   Description |
|---------------|-----------|---------------|---------------|---------------|
|position   |   String  |   Yes |   No  |   Specify where definition should be added in generated script. Available values are AT\_END (at the end), AT\_BEGINNING (at the beginning) and CUSTOM. For the CUSTOM the custom_position property has to be also specified  |
|creation\_script   |   String  |   Yes |   No  |   SQL statement added to creation script |
|drop\_script   |   String  |   No |   No  |   SQL statement added to dropping script |
|validation\_scripts   |   Array of strings  |   Yes |   No  |   SQL statements that check if changes made by creation_script were added. Each statement as result should return one integer column, value bigger than zero means that changes were applied correctly otherwise the changes were not applied |
|custom\_position   |   String  |   Only if position has the CUSTOM value  |   No  |   Custom position of the script. This does not have to be an integer value. There might be a case that some custom component was added in the code and it handles the definition with a specific position  |

## Details

### default_schema

Name of the database schema for which changes should be applied.

```yaml
default_schema: my_db_schema
```

Value can be null, in such case the changes are going to be applied to default schema (public).

```yaml
default_schema:
```

For more information please check https://github.com/starnowski/posmulten/tree/master#setting-default-database-schema.

### current_tenant_id_property_type

Type of column that stores tenant identifier and it is also the type of parameters for some generated functions.
For example, for the below entries:

```yaml
current_tenant_id_property_type:  "VARCHAR(255)"
get_current_tenant_id_function_name: "get_ten_id"
```

the framework generates the below function:

```sql
CREATE OR REPLACE FUNCTION get_current_tenant_id() RETURNS UUID AS $$
SELECT current_setting('c.c_ten')
$$ LANGUAGE sql
STABLE
PARALLEL SAFE;
```

For more information please check https://github.com/starnowski/posmulten/tree/master#setting-of-type-for-tenant-identifier-value.

### current_tenant_id_property

Property name that stores the value of tenant identifier in the database connection.
Example:

```yaml
current_tenant_id_property: "pos.c.ten"
```

For more information please check https://github.com/starnowski/posmulten#setting-the-property-name-that-stores-tenant-identifier-value.

### get_current_tenant_id_function_name

Name of the function that returns the current tenant identifier.
For example, for the below entries:

```yaml
get_current_tenant_id_function_name: "what_is_current_tenant"
```

the framework generates the below function:

```sql
CREATE OR REPLACE FUNCTION what_is_current_tenant() RETURNS VARCHAR(255) AS $$
SELECT current_setting('c.c_ten')
$$ LANGUAGE sql
STABLE
PARALLEL SAFE;
```

### set_current_tenant_id_function_name

Name of the function that sets the current tenant identifier.
For example, for the below entries:

```yaml
set_current_tenant_id_function_name: "this_will_be_tenant"
```

the framework generates the below function:

```sql
CREATE OR REPLACE FUNCTION this_will_be_tenant(VARCHAR(255)) RETURNS VOID AS $$
BEGIN
PERFORM set_config('c.c_ten', $1, false);
END
$$ LANGUAGE plpgsql
VOLATILE;
```

### equals_current_tenant_identifier_function_name

Name of the function name that checks if passed identifier is the same as the current tenant identifier.
For example, for the below entries:

```yaml
equals_current_tenant_identifier_function_name: "is_tenant_equals"
```

the framework generates the below function:

```sql
CREATE OR REPLACE FUNCTION is_tenant_equals(VARCHAR(255)) RETURNS BOOLEAN AS $$
SELECT $1 = get_current_tenant_id()
$$ LANGUAGE sql
STABLE
PARALLEL SAFE;
```

### tenant_has_authorities_function_name

Name of the function name that checks if the current tenant has authority to a table row.
For example, for the below entries:

```yaml
tenant_has_authorities_function_name: "ten_has_auth"
```

the framework generates the below function:

```sql
CREATE OR REPLACE FUNCTION ten_has_auth(VARCHAR(255), VARCHAR(255), VARCHAR(255), VARCHAR(255), VARCHAR(255)) RETURNS BOOLEAN AS $$
SELECT is_id_equals_current_tenant_id($1)
$$ LANGUAGE sql
STABLE
PARALLEL SAFE;
```

### force_row_level_security_for_table_owner

Option that force RLS policy for table owner.
This option is required if the database user for which RLS is created is also a table owner.
By default option is turned off.
Example:

```yaml
force_row_level_security_for_table_owner: true
```

For more information please check https://github.com/starnowski/posmulten#force-rls-policy-for-table-owner.

### default_tenant_id_column

The default name of the column that stores the tenant identifier.
This name is used when the table does not have a specified custom name for the column that stores the tenant identifier.
Example:

```yaml
default_tenant_id_column: "tenant_uuid"
```

For more information please check https://github.com/starnowski/posmulten#setting-default-tenant-column-name.

### grantee

Database user for which RLS policy is going to be created.
Example:

```yaml
grantee: "application-user"
```

For more information please check https://github.com/starnowski/posmulten#setting-default-database-user-for-rls-policy.

### set_current_tenant_identifier_as_default_value_for_tenant_column_in_all_tables

Generate a statement that sets a default value for the tenant column in all tables.
This functionality is useful when we don't always want to specified tenant id value in the insert statement.
By default option is turned off.
Example:

```yaml
set_current_tenant_identifier_as_default_value_for_tenant_column_in_all_tables: true
```

For more information please check https://github.com/starnowski/posmulten#adding-default-value-for-tenant-column.

### is_tenant_valid_function_name

Name of the function that checks if passed tenant identifier is valid.
For example, for the below entries:

```yaml
valid_tenant_value_constraint:
  is_tenant_valid_function_name:  is_this_tenant_valid
  tenant_identifiers_blacklist:
    - DUMMMY_TENANT
    - "XXX-INVAlid_tenant"
```

the framework generates the below function:

```sql
CREATE OR REPLACE FUNCTION is_this_tenant_valid(VARCHAR(255)) RETURNS BOOLEAN AS $$
SELECT $1 <> CAST ('DUMMMY_TENANT' AS VARCHAR(255)) AND $1 <> CAST ('XXX-INVAlid_tenant' AS VARCHAR(255))
$$ LANGUAGE sql
IMMUTABLE
PARALLEL SAFE;
```

For more information please check https://github.com/starnowski/posmulten#setting-a-list-of-invalid-tenant-identifier-values.

### is_tenant_valid_constraint_name

Name of the constraint that checks if the tenant column has a valid value.
For example, for the below entries:

```yaml
valid_tenant_value_constraint:
  is_tenant_valid_constraint_name:  tenant_must_be_valid
  is_tenant_valid_function_name:  is_this_tenant_valid
  tenant_identifiers_blacklist:
    - .....
```

the framework generates the below function:

```sql
ALTER TABLE "posts" ADD CONSTRAINT tenant_must_be_valid CHECK (tenant_id IS NULL OR is_this_tenant_valid(tenant_id));
```

For more information please check https://github.com/starnowski/posmulten#setting-a-list-of-invalid-tenant-identifier-values.

### create_tenant_column_for_table

Option force to create tenant column for the table.
By default, the Posmulten library assumes that the tenant column exists.
To check what kind of DDL statements are generated for tenant column definition please check the https://github.com/starnowski/posmulten#adding-tenant-column-to-tenant-table.

### skip_adding_of_tenant_column_default_value

Option force to skip adding default value to tenant column for table.
This option make sense to use when the #set_current_tenant_identifier_as_default_value_for_tenant_column_in_all_tables option is turned on.
It simply allows to skip adding default value definition for tenant column is specified table.
For more information please check https://github.com/starnowski/posmulten#skipping-adding-default-value-for-tenant-column-for-a-single-table.

### pk_columns_name_to_type

Map of primary key columns where the key is column name and value is its type.
The map can be empty or even null.
This is a valid case for example for a many-to-many relation table without any primary key, just with foreign keys for tables that it refers to.

### name_for_function_that_checks_if_record_exists_in_table

Function name that checks if passed primary key for a specific table exists for the current tenant.
This function is generated only when some other table has a #foreign_keys that refers to the primary key table.

For more information please check:

- https://github.com/starnowski/posmulten#adding-constraints-for-foreign-key-columns
- https://github.com/starnowski/posmulten#setting-function-name-that-checks-if-passed-primary-key-for-a-specific-table-exists-for-the-current-tenant