package com.github.starnowski.posmulten.postgresql.core.rls

import com.github.starnowski.posmulten.postgresql.core.TestApplication
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import spock.lang.Specification

@SpringBootTest(classes = [TestApplication.class])
class SetCurrentTenantIdFunctionProducerItTest extends Specification {

    @Autowired
    JdbcTemplate jdbcTemplate
}
