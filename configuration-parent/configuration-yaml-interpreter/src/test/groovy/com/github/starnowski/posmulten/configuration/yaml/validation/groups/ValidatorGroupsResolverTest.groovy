package com.github.starnowski.posmulten.configuration.yaml.validation.groups

import com.github.starnowski.posmulten.configuration.yaml.model.SharedSchemaContextConfiguration
import spock.lang.Specification

import java.util.stream.Collectors

class ValidatorGroupsResolverTest extends Specification {

    def"should have expected list of validator groups resolvers"()
    {
        given:
            def tested = new ValidatorGroupsResolver()

        when:
            def results = tested.getValidatorGroupResolvers()

        then:
            results.stream().map {it -> it.getClass()}.collect(Collectors.toList()) == [NameForFunctionThatChecksIfRecordExistsInTableNotBlankGroupResolver.class]
    }

    def"should return expected list of classed based on result from combined list of validator groups resolvers"()
    {
        given:
            def vgr1 = Mock(ValidatorGroupResolver)
            def vgr2 = Mock(ValidatorGroupResolver)
            def tested = new ValidatorGroupsResolver([vgr1, vgr2])
            def sharedSchemaContextConfiguration = Mock(SharedSchemaContextConfiguration)
            def validatorGroupResolverContext = Mock(ValidatorGroupResolver.ValidatorGroupResolverContext)

        when:
            def results = tested.resolveForSharedSchemaContextConfiguration(sharedSchemaContextConfiguration, validatorGroupResolverContext)

        then:
            1 * vgr1.resolveForSharedSchemaContextConfiguration(sharedSchemaContextConfiguration, validatorGroupResolverContext) >> ValidationClass1
            1 * vgr2.resolveForSharedSchemaContextConfiguration(sharedSchemaContextConfiguration, validatorGroupResolverContext) >> ValidationClass2
            results == [ValidationClass1, ValidationClass2]
    }

    class ValidationClass1 {}

    class ValidationClass2 {}
}
