package com.github.starnowski.posmulten.configuration.yaml.validation.groups

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
}
