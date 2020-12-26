package com.github.starnowski.posmulten.configuration.yaml.dao

import spock.lang.Unroll

class SharedSchemaContextConfigurationYamlDaoTest extends spock.lang.Specification {

    public static final ALL_FIELDS_FILE_PATH = "all-fields.yaml"

    def tested = new SharedSchemaContextConfigurationYamlDao()

    @Unroll
    def "should return non null object based on file #filePath"()
    {
        when:
            def result = tested.read(filePath)

        then:
            result

        where:
            filePath << [ALL_FIELDS_FILE_PATH]
    }
}
