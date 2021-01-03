package com.github.starnowski.posmulten.configuration.yaml.dao

import com.github.starnowski.posmulten.configuration.yaml.model.ForeignKeyConfiguration
import com.github.starnowski.posmulten.configuration.yaml.model.RLSPolicy
import com.github.starnowski.posmulten.configuration.yaml.model.SharedSchemaContextConfiguration
import com.github.starnowski.posmulten.configuration.yaml.model.StringWrapperWithNotBlankValue
import com.github.starnowski.posmulten.configuration.yaml.model.TableEntry
import com.github.starnowski.posmulten.configuration.yaml.model.ValidTenantValueConstraintConfiguration
import org.jeasy.random.EasyRandom
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Unroll

import java.nio.file.Paths

import static com.github.starnowski.posmulten.postgresql.test.utils.MapBuilder.mapBuilder
import static java.util.Arrays.asList
import static java.util.stream.Collectors.toList

class SharedSchemaContextConfigurationYamlDaoTest extends spock.lang.Specification {

    public static final ALL_FIELDS_FILE_PATH = "/com/github/starnowski/posmulten/configuration/yaml/all-fields.yaml"
    public static final ONLY_MANDATORY_FIELDS_FILE_PATH = "/com/github/starnowski/posmulten/configuration/yaml/only-mandatory-fields.yaml"

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
            filePath << [ALL_FIELDS_FILE_PATH, ONLY_MANDATORY_FIELDS_FILE_PATH]
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
            ALL_FIELDS_FILE_PATH            |   new TableEntry().setName("users").setRlsPolicy(new RLSPolicy().setName("users_table_rls_policy").setTenantColumn("tenant_id").setNameForFunctionThatChecksIfRecordExistsInTable("is_user_exists").setPrimaryKeyColumnsNameToTypeMap(mapBuilder().put("id", "bigint").build()))
            ALL_FIELDS_FILE_PATH            |   new TableEntry().setName("posts").setRlsPolicy(new RLSPolicy().setName("posts_table_rls_policy").setTenantColumn("tenant_id").setNameForFunctionThatChecksIfRecordExistsInTable("is_post_exists").setSkipAddingOfTenantColumnDefaultValue(false).setPrimaryKeyColumnsNameToTypeMap(mapBuilder().put("id", "bigint").build()))
            ALL_FIELDS_FILE_PATH            |   new TableEntry().setName("comments").setRlsPolicy(new RLSPolicy().setName("comments_table_rls_policy").setTenantColumn("tenant").setNameForFunctionThatChecksIfRecordExistsInTable("is_comment_exists").setSkipAddingOfTenantColumnDefaultValue(true).setPrimaryKeyColumnsNameToTypeMap(mapBuilder().put("id", "int").put("user_id", "bigint").build()))
            ALL_FIELDS_FILE_PATH            |   new TableEntry().setName("notifications").setRlsPolicy(new RLSPolicy().setName("notifications_table_rls_policy").setTenantColumn("tenant").setNameForFunctionThatChecksIfRecordExistsInTable("is_notification_exists").setCreateTenantColumnForTable(true).setValidTenantValueConstraintName("is_tenant_id_valid").setPrimaryKeyColumnsNameToTypeMap(mapBuilder().put("uuid", "uuid").build()))
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
            EasyRandom easyRandom = new EasyRandom()
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

    private String resolveFilePath(String filePath) {
        Paths.get(this.class.getResource(filePath).toURI()).toFile().getPath()
    }

    private StringWrapperWithNotBlankValue stringWrapper(String value)
    {
        new StringWrapperWithNotBlankValue(value)
    }
}
