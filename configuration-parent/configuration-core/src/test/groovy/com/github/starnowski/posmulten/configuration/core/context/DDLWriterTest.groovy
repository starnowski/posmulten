package com.github.starnowski.posmulten.configuration.core.context

import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition
import com.github.starnowski.posmulten.postgresql.core.context.ISharedSchemaContext
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification
import spock.lang.Unroll

import static java.util.stream.Collectors.toList

class DDLWriterTest extends Specification {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder()

    def tested = new DDLWriter()

    @Unroll
    def "should save all DDL statements that creates shared schema strategy to file, expected lines : #expectedFileLines"()
    {
        given:
            def tmpFile = tempFolder.newFile("output.sql")
            def context = Mock(ISharedSchemaContext)
            List<SQLDefinition> mockedSQLDefinitions = mockSQLDefinitions(ddlStatementsEntries)
            context.getSqlDefinitions() >> mockedSQLDefinitions

        when:
            tested.saveCreteScripts(tmpFile.getAbsolutePath(), context)

        then:
            List<String> actualFileLines = returnFileLines(tmpFile)
            expectedFileLines == actualFileLines

        and: "file does not contain any DDL statements that drop definitions"
            !ddlStatementsEntries.stream().map({it -> it.getDropScript()}).anyMatch({it -> actualFileLines.contains(it)})

        where:
            ddlStatementsEntries                                                                        ||  expectedFileLines
            [te("create some ...", "drop this record"), te("CREATE RLS_POLICY", "REVOKE Policy")]       ||  ["create some ...", "CREATE RLS_POLICY"]
            [te("Alter table etc;", "drop this record"), te("CREATE RLS_POLICY", "DROP some object")]   ||  ["Alter table etc;", "CREATE RLS_POLICY"]
            [te("grant privilege to object ", "revoke privilege")]                                      ||  ["grant privilege to object "]
    }

    @Unroll
    def "should save all DDL statements that drops shared schema strategy to file, expected lines : #expectedFileLines"()
    {
        given:
            def tmpFile = tempFolder.newFile("output.sql")
            def context = Mock(ISharedSchemaContext)
            List<SQLDefinition> mockedSQLDefinitions = mockSQLDefinitions(ddlStatementsEntries)
            context.getSqlDefinitions() >> mockedSQLDefinitions

        when:
            tested.saveDropScripts(tmpFile.getAbsolutePath(), context)

        then:
            List<String> actualFileLines = returnFileLines(tmpFile)
            expectedFileLines == actualFileLines

        and: "file does not contain any DDL statements that creates definitions"
            !ddlStatementsEntries.stream().map({it -> it.getCreateScript()}).anyMatch({it -> actualFileLines.contains(it)})

        where:
            ddlStatementsEntries                                                                        ||  expectedFileLines
            [te("create some ...", "drop this record"), te("CREATE RLS_POLICY", "REVOKE Policy")]       ||  ["REVOKE Policy", "drop this record"]
            [te("Alter table etc;", "drop this record"), te("CREATE RLS_POLICY", "DROP some object")]   ||  ["DROP some object", "drop this record"]
            [te("grant privilege to object ", "revoke privilege")]                                      ||  ["revoke privilege"]
    }

    @Unroll
    def "should save all checking statements to file, expected lines : #expectedFileLines"()
    {
        given:
            def tmpFile = tempFolder.newFile("output.sql")
            def context = Mock(ISharedSchemaContext)
            List<SQLDefinition> mockedSQLDefinitions = mockSQLDefinitions(ddlStatementsEntries)
            context.getSqlDefinitions() >> mockedSQLDefinitions

        when:
            tested.saveCheckingStatements(tmpFile.getAbsolutePath(), context)

        then:
            List<String> actualFileLines = returnFileLines(tmpFile)
            expectedFileLines == actualFileLines

        and: "file does not contain any DDL statements that creates definitions"
            !ddlStatementsEntries.stream().map({it -> it.getCreateScript()}).anyMatch({it -> actualFileLines.contains(it)})

        and: "file does not contain any DDL statements that drop definitions"
            !ddlStatementsEntries.stream().map({it -> it.getDropScript()}).anyMatch({it -> actualFileLines.contains(it)})

        where:
            ddlStatementsEntries                                                                                                                                ||  expectedFileLines
            [te("create some ...", "drop this record", ["SELECT COUNT(1)"]), te("CREATE RLS_POLICY", "REVOKE Policy", ["SELECT 1555"])]                         ||  ["SELECT COUNT(1)", "SELECT 1555"]
            [te("Alter table etc;", "drop this record", ["SELECT 87"]), te("CREATE RLS_POLICY", "DROP some object", ["SELECT COUNT(*)", "SELECT COUNT(6)"])]    ||  ["SELECT 87", "SELECT COUNT(*)", "SELECT COUNT(6)"]
            [te("grant privilege to object ", "revoke privilege", ["xxxxx 1555"])]                                                                              ||  ["xxxxx 1555"]
    }

    List<SQLDefinition> mockSQLDefinitions(List<TestEntry> testEntries)
    {
        testEntries.stream().map({it ->
            def mockedDefinition = Mock(SQLDefinition)
            mockedDefinition.getCreateScript() >> it.getCreateScript()
            mockedDefinition.getDropScript() >> it.getDropScript()
            mockedDefinition.getCheckingStatements() >> it.getCheckingStatements()
            mockedDefinition
        }).collect(toList())
    }

    List<String> returnFileLines(File file)
    {
        Scanner scanner = new Scanner(file)
        List<String> lines = new ArrayList<>()
        while (scanner.hasNextLine()) {
            lines.add(scanner.nextLine())
        }
        lines
    }

    static TestEntry te(String createScript, String dropScript)
    {
        new TestEntry(createScript, dropScript)
    }

    static TestEntry te(String createScript, String dropScript, List<String> checkingStatements)
    {
        new TestEntry(createScript, dropScript, checkingStatements)
    }

    static class TestEntry
    {
        private String createScript
        private String dropScript
        private List<String> checkingStatements

        TestEntry(String createScript, String dropScript) {
            this(createScript, dropScript, ["SELECT COUNT(1)"])
        }

        TestEntry(String createScript, String dropScript, List<String> checkingStatements) {
            this.createScript = createScript
            this.dropScript = dropScript
            this.checkingStatements = checkingStatements
        }

        String getCreateScript() {
            return createScript
        }

        String getDropScript() {
            return dropScript
        }

        List<String> getCheckingStatements() {
            return checkingStatements
        }

        @Override
        String toString() {
            return "TestEntry{" +
                    "createScript='" + createScript + '\'' +
                    ", dropScript='" + dropScript + '\'' +
                    ", checkingStatements='" + checkingStatements + '\'' +
                    '}';
        }
    }
}
