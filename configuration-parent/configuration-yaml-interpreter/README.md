The configuration-yaml-interpreter module can interpreted configuration file in yaml format and based on that it creates
DDL statements that allows to create the shared schema strategy.
The YAML schema description is below   

## Simple example:
Bellow, there is a configuration example for a schema with the name "public".
The schema contains four tables, users, posts, groups, and users_groups (many-to-many relation).
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

#TODO Root properties
| Property name |   Type    |   Required    |   Nullable    |   Description |
|---------------|-----------|---------------|---------------|---------------|
|[default_schema](#default_schema) |    String  |   Yes |   Yes |   Name of the database schema for which changes should be applied. |
|[current_tenant_id_property_type](#current_tenant_id_property_type) |  String  |   No    |   No  |   Type of column that stores tenant identifier and it is also the type of parameters for some generated functions. |
|[current_tenant_id_property](#current_tenant_id_property) |  String  |   No    |   No  |   Property name that stores the value of tenant identifier in the database connection. |
|[get_current_tenant_id_function_name](#get_current_tenant_id_function_name) |  String  |   No    |   No  |   Name of the function that returns the current tenant identifier. |
|[set_current_tenant_id_function_name](#set_current_tenant_id_function_name) |  String  |   No    |   No  |   Name of the function that sets the current tenant identifier. |
|[tenant_has_authorities_function_name](#tenant_has_authorities_function_name) |  String  |   No    |   No  |   Name of the function name that checks if the current tenant has authority to a table row. |
|[force_row_level_security_for_table_owner](#force_row_level_security_for_table_owner) |  Boolean  |   No    |   Yes  |   Option that force RLS policy for table owner. |
|[default_tenant_id_column](#default_tenant_id_column) |  String  |   No    |   No  |   Default name of column that stores tenant identifier. |
|[grantee](#grantee) |  String  |   Yes    |   No  |   Database user for which RLS policy is going to be created. |

| Property name |   Type    |   Required    |   Nullable    |   Description |
|---------------|-----------|---------------|---------------|---------------|
|[equals_current_tenant_identifier_function_name](#equals_current_tenant_identifier_function_name) |  String  |   No    |   No  |   Name of the function name that checks if passed identifier is the same as the current tenant identifier. |
|[set_current_tenant_identifier_as_default_value_for_tenant_column_in_all_tables](#set_current_tenant_identifier_as_default_value_for_tenant_column_in_all_tables) |  Boolean  |   No    |   Yes  |   Generate a statement that sets a default value for the tenant column in all tables. |

### default_schema
Name of the database schema for which changes should be applied.

```yaml
default_schema: my_db_schema
```

Value can be null, in such case the changes are going to be applied to default schema (public).

```yaml
default_schema:
```

For more information please check [setting default schema](https://github.com/starnowski/posmulten/tree/master#setting-default-database-schema).

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

For more information please check [setting of type for tenant identifier value](https://github.com/starnowski/posmulten/tree/master#setting-of-type-for-tenant-identifier-value).

### current_tenant_id_property
Property name that stores the value of tenant identifier in the database connection.
Example:
```yaml
current_tenant_id_property: "pos.c.ten"
```
For more information please check [setting the property name that stores tenant identifier value](https://github.com/starnowski/posmulten#setting-the-property-name-that-stores-tenant-identifier-value).

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
For more information please check [force RLS Policy for table owner](https://github.com/starnowski/posmulten#force-rls-policy-for-table-owner).

### default_tenant_id_column
The default name of the column that stores the tenant identifier.
This name is used when the table does not have a specified custom name for the column that stores the tenant identifier.
Example:
```yaml
default_tenant_id_column: "tenant_uuid"
```
For more information please check [setting default tenant column name](https://github.com/starnowski/posmulten#setting-default-tenant-column-name).

### grantee
Database user for which RLS policy is going to be created.
Example:
```yaml
grantee: "application-user"
```
For more information please check [setting default database user for RLS policy](https://github.com/starnowski/posmulten#setting-default-database-user-for-rls-policy).

### set_current_tenant_identifier_as_default_value_for_tenant_column_in_all_tables
Generate a statement that sets a default value for the tenant column in all tables.
This functionality is useful when we don't always want to specified tenant id value in the insert statement.
By default option is turned off.
Example:
```yaml
set_current_tenant_identifier_as_default_value_for_tenant_column_in_all_tables: true
```
For more information please check [adding default value for tenant column](https://github.com/starnowski/posmulten#adding-default-value-for-tenant-column).

#TODO valid_tenant_value_constraint
#TODO tables

## Details
