package com.github.starnowski.posmulten.configuration.yaml.dao

import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Unroll

import java.nio.file.Paths

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
            def resolvedPath = Paths.get(this.class.getResource(filePath).toURI()).toFile().getPath()

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
            def resolvedPath = Paths.get(this.class.getResource(filePath).toURI()).toFile().getPath()
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
    def "should return object with expected fields : defaultSchema (#defaultSchema), currentTenantIdPropertyType (#currentTenantIdPropertyType), currentTenantIdProperty (#currentTenantIdProperty), getCurrentTenantIdFunctionName (#getCurrentTenantIdFunctionName), setCurrentTenantIdFunctionName (#setCurrentTenantIdFunctionName)"()
    {
        given:
            def resolvedPath = Paths.get(this.class.getResource(filePath).toURI()).toFile().getPath()

        when:
            def result = tested.read(resolvedPath)

        then:
            result.getDefaultSchema() == defaultSchema
            result.getCurrentTenantIdProperty() == currentTenantIdProperty
            result.getCurrentTenantIdPropertyType() == currentTenantIdPropertyType
            result.getGetCurrentTenantIdFunctionName() == getCurrentTenantIdFunctionName
            result.getSetCurrentTenantIdFunctionName() == setCurrentTenantIdFunctionName

        where:
            filePath                        |   defaultSchema   |   currentTenantIdProperty |   currentTenantIdPropertyType |   getCurrentTenantIdFunctionName  |   setCurrentTenantIdFunctionName
            ALL_FIELDS_FILE_PATH            |   "public"        |   "pos.c.ten"             |   "VARCHAR(255)"              |   "get_ten_id"                    |   "set_tenant"
            ONLY_MANDATORY_FIELDS_FILE_PATH |   "public"        |   null                    |   null                        |   null                            |   null
    }
}
