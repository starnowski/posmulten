package com.github.starnowski.posmulten.configuration.yaml.dao

import com.github.starnowski.posmulten.configuration.yaml.AbstractSpecification
import com.github.starnowski.posmulten.configuration.yaml.IntegerRandomizer
import com.github.starnowski.posmulten.configuration.yaml.OptionalRandomizer
import com.github.starnowski.posmulten.configuration.yaml.exceptions.YamlInvalidSchema
import com.github.starnowski.posmulten.configuration.yaml.model.*
import org.jeasy.random.EasyRandom
import org.jeasy.random.EasyRandomParameters
import org.jeasy.random.FieldPredicates
import org.jeasy.random.randomizers.misc.EnumRandomizer
import org.jeasy.random.randomizers.text.StringDelegatingRandomizer
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Unroll

import static com.github.starnowski.posmulten.configuration.yaml.TestProperties.*
import static com.github.starnowski.posmulten.postgresql.test.utils.MapBuilder.mapBuilder
import static java.util.Arrays.asList
import static java.util.stream.Collectors.toList

class SharedSchemaContextConfigurationYamlDaoTest extends AbstractSpecification {

    private static String ALL_FIELDS_CONTENT = """
default_schema: public
current_tenant_id_property_type:  "VARCHAR(255)"
current_tenant_id_property: "pos.c.ten"
get_current_tenant_id_function_name: "get_ten_id"
set_current_tenant_id_function_name: "set_tenant"
equals_current_tenant_identifier_function_name: "equals_cur_tenant"
tenant_has_authorities_function_name: "_tenant_hast_auth"
force_row_level_security_for_table_owner: true
default_tenant_id_column: "tenant_id"
grantee: "application-user"
set_current_tenant_identifier_as_default_value_for_tenant_column_in_all_tables: true
valid_tenant_value_constraint:
  is_tenant_valid_function_name:  is_t_valid
  is_tenant_valid_constraint_name:  "is_tenant_valid_constraint_SDFA"
  tenant_identifiers_blacklist:
    - invalid_tenant
    - "Some strange tenant ID"
    - "'; DROP ALL TABLES"
tables:
  - name: users
    rls_policy:
      name: users_table_rls_policy
      tenant_column:  tenant_id
      primary_key_definition:
        name_for_function_that_checks_if_record_exists_in_table: "is_user_exists"
        pk_columns_name_to_type:
          id: bigint
  - name: posts
    rls_policy:
      name: "posts_table_rls_policy"
      tenant_column:  tenant_id
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
  - name: "comments"
    rls_policy:
      name: comments_table_rls_policy
      tenant_column:  tenant
      skip_adding_of_tenant_column_default_value: true
      primary_key_definition:
        name_for_function_that_checks_if_record_exists_in_table: "is_comment_exists"
        pk_columns_name_to_type:
          id: int
          user_id: bigint
    foreign_keys:
      - constraint_name:  "comments_users_tenant_constraint"
        table_name: users
        foreign_key_primary_key_columns_mappings:
          user_id:  id
      - constraint_name:  "comments_posts_tenant_constraint"
        table_name: posts
        foreign_key_primary_key_columns_mappings:
          post_id:  id
      - constraint_name:  "comments_comment_parent_tenant_constraint"
        table_name: comments
        foreign_key_primary_key_columns_mappings:
          parent_comment_id:  id
          parent_comment_user_id:  user_id
  - name: notifications
    rls_policy:
      name: notifications_table_rls_policy
      tenant_column:  tenant
      create_tenant_column_for_table: true
      valid_tenant_value_constraint_name: "is_tenant_id_valid"
      primary_key_definition:
        name_for_function_that_checks_if_record_exists_in_table: "is_notification_exists"
        pk_columns_name_to_type:
          uuid: uuid
    foreign_keys:
      - constraint_name:  "notifications_users_tenant_constraint"
        table_name: users
        foreign_key_primary_key_columns_mappings:
          user_id:  id
  - name: dictionary
    schema: no_other_schema
    rls_policy:
      name: dictionary_table_rls_policy
      tenant_column:  tenant_id
      primary_key_definition:
        name_for_function_that_checks_if_record_exists_in_table: "is_dictionary_exists"
        pk_columns_name_to_type:
          id: bigint
  # table with blank (null) schema
  - name: dictionary_1
    schema:
    rls_policy:
      name: dictionary_1_table_rls_policy
      tenant_column:  tenant_id
      primary_key_definition:
        name_for_function_that_checks_if_record_exists_in_table: "is_dictionary_1_exists"
        pk_columns_name_to_type:
          id: bigint
  # table with null schema
  - name: dictionary_2
    schema:
    rls_policy:
      name: dictionary_2_table_rls_policy
      tenant_column:  tenant_id
      primary_key_definition:
        name_for_function_that_checks_if_record_exists_in_table: "is_dictionary_2_exists"
        pk_columns_name_to_type:
          id: bigint
  - name: notifications_1
    rls_policy:
      name: notifications_1_table_rls_policy
      tenant_column:  tenant
      create_tenant_column_for_table: true
    foreign_keys:
      - constraint_name:  "notifications_1_dictionary_tenant_constraint"
        table_name: dictionary
        table_schema: no_other_schema
        foreign_key_primary_key_columns_mappings:
          dictionary_id:  id
custom_sql_definitions:
  - position: AT_END
    creation_script:  |
      ALTER ...
    validation_scripts:
      - |
        SELECT (6) FROM ...
  - position: AT_BEGINNING
    creation_script: |
      ALTER COLUMN ...
    drop_script: |
      ALTER DROP ...
    validation_scripts:
        - |
          SELECT (13) FROM ...
        - "SELECT (1) ..... FROM DUAL"
  - position: CUSTOM
    custom_position: "Some custom position"
    creation_script:  |
      ALTER ...
    validation_scripts:
      - |
        SELECT (371) FROM ..."""

    private static ONLY_MANDATORY_CONTENT = """
default_schema: non_public
grantee: "db-user"
"""

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder()

    def tested = new SharedSchemaContextConfigurationYamlDao()

    @Unroll
    def "should return non null object based on file #filePath"()
    {
        given:
            def resolvedPath = resolveFilePath(filePath)

        when:
            def result = tested.read(resolvedPath)

        then:
            result

        where:
            filePath << [ALL_FIELDS_FILE_PATH, ONLY_MANDATORY_FIELDS_FILE_PATH, INTEGRATION_TESTS_FILE_PATH, ONLY_MANDATORY_FIELDS_WITH_TEMPLATE_VALUES_FILE_PATH]
    }

    @Unroll
    def "should return object based on file which was created by other test file #filePath"()
    {
        given:
            def resolvedPath = resolveFilePath(filePath)
            def testObject = tested.read(resolvedPath)
            def tmpFile = tempFolder.newFile("temp-config.yaml")
            tested.save(testObject, tmpFile.getAbsoluteFile().getAbsolutePath())

        when:
            def result = tested.read(tmpFile.getAbsoluteFile().getAbsolutePath())

        then:
            result

        and: "tests object should be equal"
            result == testObject

        where:
            filePath << [ALL_FIELDS_FILE_PATH, ONLY_MANDATORY_FIELDS_FILE_PATH]
    }

    @Unroll
    def "should return object based on yaml content"()
    {
        when:
            def result = tested.readFromContent(content)

        then:
            result

        where:
            content << [ALL_FIELDS_CONTENT, ONLY_MANDATORY_CONTENT]
    }

    @Unroll
    def "should not save file when trying to save invalid object based on schema"()
    {
        given:
            def resolvedPath = resolveFilePath(filePath)
            def testObject = tested.read(resolvedPath)
            def tmpFile = tempFolder.newFile("temp-config.yaml")
            testObject.setGrantee(null)

        when:
            tested.save(testObject, tmpFile.getAbsoluteFile().getAbsolutePath())

        then:
            def ex = thrown(YamlInvalidSchema)

        where:
            filePath << [ALL_FIELDS_FILE_PATH, ONLY_MANDATORY_FIELDS_FILE_PATH]
    }

    @Unroll
    def "for file '#filePath' should return object with expected fields : defaultSchema (#defaultSchema), currentTenantIdPropertyType (#currentTenantIdPropertyType), currentTenantIdProperty (#currentTenantIdProperty), getCurrentTenantIdFunctionName (#getCurrentTenantIdFunctionName), setCurrentTenantIdFunctionName (#setCurrentTenantIdFunctionName)"()
    {
        given:
            def resolvedPath = resolveFilePath(filePath)

        when:
            def result = tested.read(resolvedPath)

        then:
            result.getDefaultSchema() == defaultSchema
            result.getCurrentTenantIdProperty() == currentTenantIdProperty
            result.getCurrentTenantIdPropertyType() == currentTenantIdPropertyType
            result.getGetCurrentTenantIdFunctionName() == getCurrentTenantIdFunctionName
            result.getSetCurrentTenantIdFunctionName() == setCurrentTenantIdFunctionName

        where:
            filePath                        |   defaultSchema   |   currentTenantIdProperty                 |   currentTenantIdPropertyType         |   getCurrentTenantIdFunctionName                              |   setCurrentTenantIdFunctionName
            ALL_FIELDS_FILE_PATH            |   "public"        |   stringWrapper("pos.c.ten")          |   stringWrapper("VARCHAR(255)") |   stringWrapper("get_ten_id")    |   stringWrapper("set_tenant")
            ONLY_MANDATORY_FIELDS_FILE_PATH |   "non_public"    |   null                                    |   null                                |   null            |   null
    }

    @Unroll
    def "for file '#filePath' should return object with expected fields : defaultSchema (#defaultSchema), grantee (#grantee)"()
    {
        given:
            def resolvedPath = resolveFilePath(filePath)

        when:
            def result = tested.read(resolvedPath)

        then:
            result.getDefaultSchema() == defaultSchema
            result.getGrantee() == grantee

        where:
            filePath                                                |   defaultSchema       |   grantee
            ONLY_MANDATORY_FIELDS_WITH_TEMPLATE_VALUES_FILE_PATH    |   "{{db_schema}}"     |   "{{db_grantee}}"
            CONFIGURATION_WITH_TEMPLATE_VALUES_FILE_PATH            |   "{{db_schema}}"     |   "{{db_rls_grantee}}"
    }

    @Unroll
    def "for file '#filePath' should return object with expected fields : equalsCurrentTenantIdentifierFunctionName (#equalsCurrentTenantIdentifierFunctionName), tenantHasAuthoritiesFunctionName (#tenantHasAuthoritiesFunctionName), forceRowLevelSecurityForTableOwner (#forceRowLevelSecurityForTableOwner), defaultTenantIdColumn (#defaultTenantIdColumn), grantee (#grantee), setCurrentTenantIdentifierAsDefaultValueForTenantColumnInAllTables (#setCurrentTenantIdentifierAsDefaultValueForTenantColumnInAllTables)"()
    {
        given:
            def resolvedPath = resolveFilePath(filePath)

        when:
            def result = tested.read(resolvedPath)

        then:
            result.getEqualsCurrentTenantIdentifierFunctionName() == equalsCurrentTenantIdentifierFunctionName
            result.getTenantHasAuthoritiesFunctionName() == tenantHasAuthoritiesFunctionName
            result.getForceRowLevelSecurityForTableOwner() == forceRowLevelSecurityForTableOwner
            result.getDefaultTenantIdColumn() == defaultTenantIdColumn
            result.getGrantee() == grantee
            result.getCurrentTenantIdentifierAsDefaultValueForTenantColumnInAllTables() == setCurrentTenantIdentifierAsDefaultValueForTenantColumnInAllTables

        where:
            filePath                        |   equalsCurrentTenantIdentifierFunctionName       |   tenantHasAuthoritiesFunctionName            |   forceRowLevelSecurityForTableOwner  |   defaultTenantIdColumn               |   grantee             |   setCurrentTenantIdentifierAsDefaultValueForTenantColumnInAllTables
            ALL_FIELDS_FILE_PATH            |   stringWrapper("equals_cur_tenant")          |   stringWrapper("_tenant_hast_auth")  |   true                                |   stringWrapper("tenant_id")      |   "application-user"  |   true
            ONLY_MANDATORY_FIELDS_FILE_PATH |   null                                            |   null                                        |   null                                |   null                                |   "db-user"           |   null
    }

    @Unroll
    def "for file '#filePath' should return object with expected fields related to valid tenant value constraint #properties"()
    {
        given:
            def resolvedPath = resolveFilePath(filePath)

        when:
            def result = tested.read(resolvedPath)

        then:
            result.getValidTenantValueConstraint() == properties

        where:
            filePath                        |   properties
            ONLY_MANDATORY_FIELDS_FILE_PATH |   null
            ALL_FIELDS_FILE_PATH            |   new ValidTenantValueConstraintConfiguration().setIsTenantValidConstraintName("is_tenant_valid_constraint_SDFA").setIsTenantValidFunctionName("is_t_valid").setTenantIdentifiersBlacklist(asList("invalid_tenant", "Some strange tenant ID", "'; DROP ALL TABLES"))
    }

    @Unroll
    def "for file '#filePath' should return object that contains expected table configuration #tableEntry (without comparing foreign keys entries)"()
    {
        given:
            def resolvedPath = resolveFilePath(filePath)

        when:
            def results = tested.read(resolvedPath).getTables().stream().map({table -> table.setForeignKeys(null) }).collect(toList())

        then:
            results.contains(tableEntry)

        where:
            filePath                        |   tableEntry
            ALL_FIELDS_FILE_PATH            |   new TableEntry().setName("users").setRlsPolicy(new RLSPolicy().setName("users_table_rls_policy").setTenantColumn("tenant_id").setPrimaryKeyDefinition(new PrimaryKeyDefinition().setNameForFunctionThatChecksIfRecordExistsInTable("is_user_exists").setPrimaryKeyColumnsNameToTypeMap(mapBuilder().put("id", "bigint").build())))
            ALL_FIELDS_FILE_PATH            |   new TableEntry().setName("posts").setRlsPolicy(new RLSPolicy().setName("posts_table_rls_policy").setTenantColumn("tenant_id").setPrimaryKeyDefinition(new PrimaryKeyDefinition().setNameForFunctionThatChecksIfRecordExistsInTable("is_post_exists").setPrimaryKeyColumnsNameToTypeMap(mapBuilder().put("id", "bigint").build())).setSkipAddingOfTenantColumnDefaultValue(false))
            ALL_FIELDS_FILE_PATH            |   new TableEntry().setName("comments").setRlsPolicy(new RLSPolicy().setName("comments_table_rls_policy").setTenantColumn("tenant").setPrimaryKeyDefinition(new PrimaryKeyDefinition().setNameForFunctionThatChecksIfRecordExistsInTable("is_comment_exists").setPrimaryKeyColumnsNameToTypeMap(mapBuilder().put("id", "int").put("user_id", "bigint").build())).setSkipAddingOfTenantColumnDefaultValue(true))
            ALL_FIELDS_FILE_PATH            |   new TableEntry().setName("notifications").setRlsPolicy(new RLSPolicy().setName("notifications_table_rls_policy").setTenantColumn("tenant").setPrimaryKeyDefinition(new PrimaryKeyDefinition().setNameForFunctionThatChecksIfRecordExistsInTable("is_notification_exists").setPrimaryKeyColumnsNameToTypeMap(mapBuilder().put("uuid", "uuid").build())).setCreateTenantColumnForTable(true).setValidTenantValueConstraintName("is_tenant_id_valid"))
            ALL_FIELDS_FILE_PATH            |   new TableEntry().setName("dictionary").setSchema(Optional.of("no_other_schema")).setRlsPolicy(new RLSPolicy().setName("dictionary_table_rls_policy").setTenantColumn("tenant_id").setPrimaryKeyDefinition(new PrimaryKeyDefinition().setNameForFunctionThatChecksIfRecordExistsInTable("is_dictionary_exists").setPrimaryKeyColumnsNameToTypeMap(mapBuilder().put("id", "bigint").build())))
            ALL_FIELDS_FILE_PATH            |   new TableEntry().setName("dictionary_1").setSchema(Optional.ofNullable(null)).setRlsPolicy(new RLSPolicy().setName("dictionary_1_table_rls_policy").setTenantColumn("tenant_id").setPrimaryKeyDefinition(new PrimaryKeyDefinition().setNameForFunctionThatChecksIfRecordExistsInTable("is_dictionary_1_exists").setPrimaryKeyColumnsNameToTypeMap(mapBuilder().put("id", "bigint").build())))
            ALL_FIELDS_FILE_PATH            |   new TableEntry().setName("dictionary_2").setSchema(Optional.ofNullable(null)).setRlsPolicy(new RLSPolicy().setName("dictionary_2_table_rls_policy").setTenantColumn("tenant_id").setPrimaryKeyDefinition(new PrimaryKeyDefinition().setNameForFunctionThatChecksIfRecordExistsInTable("is_dictionary_2_exists").setPrimaryKeyColumnsNameToTypeMap(mapBuilder().put("id", "bigint").build())))
    }

    @Unroll
    def "for file '#filePath' should return object that contains expected foreign key configuration #foreignKey for table #table"()
    {
        given:
            def resolvedPath = resolveFilePath(filePath)

        when:
            def results = tested.read(resolvedPath).getTables().stream().filter({t -> (table == t.getName()) }).flatMap({t -> t.getForeignKeys().stream()}).collect(toList())

        then:
            results.contains(foreignKey)

        where:
            filePath                        |   table               |   foreignKey
            ALL_FIELDS_FILE_PATH            |   "posts"             |   new ForeignKeyConfiguration().setTableName("users").setConstraintName("posts_users_tenant_constraint").setForeignKeyPrimaryKeyColumnsMappings(mapBuilder().put("user_id", "id").build())
            ALL_FIELDS_FILE_PATH            |   "comments"          |   new ForeignKeyConfiguration().setTableName("users").setConstraintName("comments_users_tenant_constraint").setForeignKeyPrimaryKeyColumnsMappings(mapBuilder().put("user_id", "id").build())
            ALL_FIELDS_FILE_PATH            |   "comments"          |   new ForeignKeyConfiguration().setTableName("posts").setConstraintName("comments_posts_tenant_constraint").setForeignKeyPrimaryKeyColumnsMappings(mapBuilder().put("post_id", "id").build())
            ALL_FIELDS_FILE_PATH            |   "comments"          |   new ForeignKeyConfiguration().setTableName("comments").setConstraintName("comments_comment_parent_tenant_constraint").setForeignKeyPrimaryKeyColumnsMappings(mapBuilder().put("parent_comment_id", "id").put("parent_comment_user_id", "user_id").build())
            ALL_FIELDS_FILE_PATH            |   "notifications"     |   new ForeignKeyConfiguration().setTableName("users").setConstraintName("notifications_users_tenant_constraint").setForeignKeyPrimaryKeyColumnsMappings(mapBuilder().put("user_id", "id").build())
            ALL_FIELDS_FILE_PATH            |   "notifications_1"   |   new ForeignKeyConfiguration().setTableName("dictionary").setTableSchema(Optional.of("no_other_schema")).setConstraintName("notifications_1_dictionary_tenant_constraint").setForeignKeyPrimaryKeyColumnsMappings(mapBuilder().put("dictionary_id", "id").build())
    }

    @Unroll
    def "should return object based on file which was created based on test object #testObject"()
    {
        given:
        def tmpFile = tempFolder.newFile("spec-temp-config.yaml")
        tested.save(testObject, tmpFile.getAbsoluteFile().getAbsolutePath())

        when:
            def result = tested.read(tmpFile.getAbsoluteFile().getAbsolutePath())

        then:
            result

        and: "tests objects should be equal"
            result == testObject

        where:
            testObject << [ new SharedSchemaContextConfiguration().setCurrentTenantIdPropertyType("xxx").setDefaultSchema("shema1").setTables(asList(new TableEntry().setName("tab1"))).setGrantee("db-user"),
                            new SharedSchemaContextConfiguration().setDefaultSchema("public").setTables(asList(new TableEntry().setName("users"))).setGrantee("ps-user")
            ]
    }

    def "should return object based on file which was created based on random generated object"()
    {
        given:
            EasyRandomParameters parameters = new EasyRandomParameters()
                    .randomize(FieldPredicates.named("identifierMaxLength").and(FieldPredicates.ofType(Integer.class)).and(FieldPredicates.inClass(SqlDefinitionsValidation.class)), new IntegerRandomizer(1, 255))
                    .randomize(FieldPredicates.named("identifierMinLength").and(FieldPredicates.ofType(Integer.class)).and(FieldPredicates.inClass(SqlDefinitionsValidation.class)), new IntegerRandomizer(1, 255))
                    .randomize(FieldPredicates.named("schema").and(FieldPredicates.ofType(Optional.class)).and(FieldPredicates.inClass(TableEntry.class)), new OptionalRandomizer(StringDelegatingRandomizer.aNewStringDelegatingRandomizer(new IntegerRandomizer(1, 255)), true))
                    .randomize(FieldPredicates.named("tableSchema").and(FieldPredicates.ofType(Optional.class)).and(FieldPredicates.inClass(ForeignKeyConfiguration.class)), new OptionalRandomizer(StringDelegatingRandomizer.aNewStringDelegatingRandomizer(new IntegerRandomizer(1, 255)), true))
                    .randomize(FieldPredicates.named("position").and(FieldPredicates.ofType(String.class)).and(FieldPredicates.inClass(CustomDefinitionEntry.class)), StringDelegatingRandomizer.aNewStringDelegatingRandomizer(new EnumRandomizer(com.github.starnowski.posmulten.configuration.core.model.CustomDefinitionEntry.CustomDefinitionPosition)))
            EasyRandom easyRandom = new EasyRandom(parameters)
            def randomObject = easyRandom.nextObject(SharedSchemaContextConfiguration)
            def tmpFile = tempFolder.newFile("rand-temp-config.yaml")
            tested.save(randomObject, tmpFile.getAbsoluteFile().getAbsolutePath())

        when:
            def result = tested.read(tmpFile.getAbsoluteFile().getAbsolutePath())

        then:
            result

        and: "tests objects should be equal"
            result == randomObject
    }

    private StringWrapperWithNotBlankValue stringWrapper(String value)
    {
        new StringWrapperWithNotBlankValue(value)
    }
}
