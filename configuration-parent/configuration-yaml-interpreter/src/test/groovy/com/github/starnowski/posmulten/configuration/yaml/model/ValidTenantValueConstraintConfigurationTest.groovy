package com.github.starnowski.posmulten.configuration.yaml.model

import spock.lang.Specification
import spock.lang.Unroll

class ValidTenantValueConstraintConfigurationTest extends Specification {

    @Unroll
    def "equals method should return true for objects with same values [ob1 (#ob1), ob2 (#ob2)]"()
    {
        expect:
            ob1.equals(ob2) && ob2.equals(ob1)

        where:
            ob1 |   ob2
            new ValidTenantValueConstraintConfiguration()   |   new ValidTenantValueConstraintConfiguration()
            new ValidTenantValueConstraintConfiguration().setIsTenantValidConstraintName("i_f_v")   |   new ValidTenantValueConstraintConfiguration().setIsTenantValidConstraintName("i_f_v")
            new ValidTenantValueConstraintConfiguration().setTenantIdentifiersBlacklist(Arrays.asList("invalid_t", "UUID-SOME-13")) |   new ValidTenantValueConstraintConfiguration().setTenantIdentifiersBlacklist(Arrays.asList("invalid_t", "UUID-SOME-13"))
    }

    @Unroll
    def "hashCode method should return same result for objects with same values [ob1 (#ob1), ob2 (#ob2)]"()
    {
        expect:
            ob1.hashCode() == ob2.hashCode()

        where:
            ob1 |   ob2
            new ValidTenantValueConstraintConfiguration()   |   new ValidTenantValueConstraintConfiguration()
            new ValidTenantValueConstraintConfiguration().setIsTenantValidConstraintName("i_f_v")   |   new ValidTenantValueConstraintConfiguration().setIsTenantValidConstraintName("i_f_v")
            new ValidTenantValueConstraintConfiguration().setTenantIdentifiersBlacklist(Arrays.asList("invalid_t", "UUID-SOME-13")) |   new ValidTenantValueConstraintConfiguration().setTenantIdentifiersBlacklist(Arrays.asList("invalid_t", "UUID-SOME-13"))
    }

    @Unroll
    def "equals method should return false for objects with same values [ob1 (#ob1), ob2 (#ob2)]"()
    {
        expect:
            !ob1.equals(ob2) && !ob2.equals(ob1)

        where:
            ob1 |   ob2
            new ValidTenantValueConstraintConfiguration().setIsTenantValidConstraintName("i_f_v")   |   new ValidTenantValueConstraintConfiguration().setIsTenantValidConstraintName("i_xxx")
            new ValidTenantValueConstraintConfiguration().setTenantIdentifiersBlacklist(Arrays.asList("invalid_t", "UUID-SOME-13")) |   new ValidTenantValueConstraintConfiguration().setTenantIdentifiersBlacklist(Arrays.asList("invalid_t", "UXID-SOME-13"))
    }
}
