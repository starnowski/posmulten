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
| Property name |   Required    |   Nullable    |   Description |
|---------------|---------------|---------------|---------------|
|[default_schema](#default_schema) |   Yes         |   Yes         |   Name of the database schema for which changes should be applied. |
|[current_tenant_id_property_type](#current_tenant_id_property_type) |   No         |   No         |   Type of column that stores tenant identifier and it is also the type of parameters for some generated functions. |

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

#TODO valid_tenant_value_constraint
#TODO tables

## Details
