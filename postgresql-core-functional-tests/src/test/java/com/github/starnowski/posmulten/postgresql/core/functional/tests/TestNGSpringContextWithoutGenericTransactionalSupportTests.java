package com.github.starnowski.posmulten.postgresql.core.functional.tests;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(classes = TestApplication.class)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class TestNGSpringContextWithoutGenericTransactionalSupportTests extends AbstractTransactionalTestNGSpringContextTests {
}
