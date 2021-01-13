package com.github.starnowski.posmulten.configuration.core.context

import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

class DDLWriterTest extends Specification {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder()

    def "should save all DDL statements that creates shared schema strategy to file"()
    {

        where:
            ddlStatementsEntries                                                                        ||  expectedFileLines
            [te("create some ...", "drop this record"), te("CREATE RLS_POLICY", "REVOKE Policy")]       ||  ["create some ...", "CREATE RLS_POLICY"]
            [te("Alter table etc;", "drop this record"), te("CREATE RLS_POLICY", "DROP some object")]   ||  ["Alter table etc;", "CREATE RLS_POLICY"]
            [te("grant privilege to object ", "revoke privilege")]                                      ||  ["grant privilege to object "]
    }

    static TestEntry te(String createScript, String dropScript)
    {
        new TestEntry(createScript, dropScript)
    }

    static class TestEntry
    {
        private String createScript
        private String dropScript

        TestEntry(String createScript, String dropScript) {
            this.createScript = createScript
            this.dropScript = dropScript
        }

        String getCreateScript() {
            return createScript
        }

        String getDropScript() {
            return dropScript
        }

        @Override
        public String toString() {
            return "TestEntry{" +
                    "createScript='" + createScript + '\'' +
                    ", dropScript='" + dropScript + '\'' +
                    '}';
        }
    }
}
