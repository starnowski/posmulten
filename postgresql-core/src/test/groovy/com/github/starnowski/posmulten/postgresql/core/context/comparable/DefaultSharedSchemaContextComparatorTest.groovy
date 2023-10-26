package com.github.starnowski.posmulten.postgresql.core.context.comparable

import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition
import com.github.starnowski.posmulten.postgresql.core.context.ISharedSchemaContext
import spock.lang.Specification
import spock.lang.Unroll

import java.util.stream.Collectors

import static java.util.stream.Collectors.toList

class DefaultSharedSchemaContextComparatorTest extends Specification {

    @Unroll
    def "should return correct differences (only on left #onlyOnLeft and only on right #onlyOnRight for creation scripts for left #left and right #right"(){
        given:
            SharedSchemaContextComparator tested = new DefaultSharedSchemaContextComparator()
            ISharedSchemaContext leftContext = Mock(ISharedSchemaContext)
            ISharedSchemaContext rightContext = Mock(ISharedSchemaContext)
            leftContext.getSqlDefinitions() >> {
                left.stream().map({
                    def defMock = Mock(SQLDefinition)
                    defMock.getCreateScript() >> it
                    defMock
                }).collect(toList())
            }
            rightContext.getSqlDefinitions() >> {
                right.stream().map({
                    def defMock = Mock(SQLDefinition)
                    defMock.getCreateScript() >> it
                    defMock
                }).collect(toList())
            }

        when:
            SharedSchemaContextComparator.SharedSchemaContextComparableResults result = tested.diff(leftContext, rightContext)

        then:
            result.getCreationScriptsDifferences().getExistedOnlyOnLeft() == onlyOnLeft
            result.getCreationScriptsDifferences().getExistedOnlyOnRight() == onlyOnRight

        and: "there should be no differences for drop scripts"
            result.getDropScriptsDifferences().getExistedOnlyOnLeft().isEmpty()
            result.getDropScriptsDifferences().getExistedOnlyOnRight().isEmpty()

        and: "there should be no differences for checking scripts"
            result.getCheckScriptsDifferences().getExistedOnlyOnLeft().isEmpty()
            result.getCheckScriptsDifferences().getExistedOnlyOnRight().isEmpty()

        where:
            left | right ||  onlyOnLeft || onlyOnRight
            ["x1", "x23", "cdasdf", "com one"]      | ["x1", "cdasdf", "this is new",  "com one"] || ["x23"] || ["this is new"]
            ["x1", "x23", "cdasdf", "com one"]      | ["x1", "cdasdf", "com one"] || ["x23"] || []
            ["x1", "cdasdf", "com one"]      | ["x1", "cdasdf", "this is new",  "com one"] || [] || ["this is new"]
    }

    @Unroll
    def "should return empty list as differences for creation scripts when there is no differences between definitions for creation scripts #collection"(){
        given:
            SharedSchemaContextComparator tested = new DefaultSharedSchemaContextComparator()
            ISharedSchemaContext leftContext = Mock(ISharedSchemaContext)
            ISharedSchemaContext rightContext = Mock(ISharedSchemaContext)
            leftContext.getSqlDefinitions() >> {
                collection.stream().map({
                    def defMock = Mock(SQLDefinition)
                    defMock.getCreateScript() >> it
                    defMock
                }).collect(toList())
            }
            rightContext.getSqlDefinitions() >> {
                collection.stream().map({
                    def defMock = Mock(SQLDefinition)
                    defMock.getCreateScript() >> it
                    defMock
                }).collect(toList())
            }

        when:
            SharedSchemaContextComparator.SharedSchemaContextComparableResults result = tested.diff(leftContext, rightContext)

        then:
            result.getCreationScriptsDifferences().getExistedOnlyOnLeft().isEmpty()
            result.getCreationScriptsDifferences().getExistedOnlyOnRight().isEmpty()

        and: "there should be no differences for drop scripts"
            result.getDropScriptsDifferences().getExistedOnlyOnLeft().isEmpty()
            result.getDropScriptsDifferences().getExistedOnlyOnRight().isEmpty()

        and: "there should be no differences for checking scripts"
            result.getCheckScriptsDifferences().getExistedOnlyOnLeft().isEmpty()
            result.getCheckScriptsDifferences().getExistedOnlyOnRight().isEmpty()

        where:
            collection << [["x1", "cdasdf", "this is new",  "com one"]]
    }

    @Unroll
    def "should return correct differences (only on left #onlyOnLeft and only on right #onlyOnRight for drop scripts for left #left and right #right"(){
        given:
            SharedSchemaContextComparator tested = new DefaultSharedSchemaContextComparator()
            ISharedSchemaContext leftContext = Mock(ISharedSchemaContext)
            ISharedSchemaContext rightContext = Mock(ISharedSchemaContext)
            leftContext.getSqlDefinitions() >> {
                left.stream().map({
                    def defMock = Mock(SQLDefinition)
                    defMock.getDropScript() >> it
                    defMock
                }).collect(toList())
            }
            rightContext.getSqlDefinitions() >> {
                right.stream().map({
                    def defMock = Mock(SQLDefinition)
                    defMock.getDropScript() >> it
                    defMock
                }).collect(toList())
            }

        when:
            SharedSchemaContextComparator.SharedSchemaContextComparableResults result = tested.diff(leftContext, rightContext)

        then:
            result.getDropScriptsDifferences().getExistedOnlyOnLeft() == onlyOnLeft
            result.getDropScriptsDifferences().getExistedOnlyOnRight() == onlyOnRight

        where:
            left | right ||  onlyOnLeft || onlyOnRight
            ["x1", "x23", "cdasdf", "com one"]      | ["x1", "cdasdf", "this is new",  "com one"] || ["x23"] || ["this is new"]
            ["x1", "x23", "cdasdf", "com one"]      | ["x1", "cdasdf", "com one"] || ["x23"] || []
            ["x1", "cdasdf", "com one"]      | ["x1", "cdasdf", "this is new",  "com one"] || [] || ["this is new"]
    }

    @Unroll
    def "should return empty list as differences for creation scripts when there is no differences between definitions for drop scripts #collection"(){
        given:
            SharedSchemaContextComparator tested = new DefaultSharedSchemaContextComparator()
            ISharedSchemaContext leftContext = Mock(ISharedSchemaContext)
            ISharedSchemaContext rightContext = Mock(ISharedSchemaContext)
            leftContext.getSqlDefinitions() >> {
                collection.stream().map({
                    def defMock = Mock(SQLDefinition)
                    defMock.getDropScript() >> it
                    defMock
                }).collect(toList())
            }
            rightContext.getSqlDefinitions() >> {
                collection.stream().map({
                    def defMock = Mock(SQLDefinition)
                    defMock.getDropScript() >> it
                    defMock
                }).collect(toList())
            }

        when:
            SharedSchemaContextComparator.SharedSchemaContextComparableResults result = tested.diff(leftContext, rightContext)

        then:
            result.getDropScriptsDifferences().getExistedOnlyOnLeft().isEmpty()
            result.getDropScriptsDifferences().getExistedOnlyOnRight().isEmpty()

        where:
            collection << [["x1", "cdasdf", "this is new",  "com one"]]
    }

    @Unroll
    def "should return correct differences (only on left #onlyOnLeft and only on right #onlyOnRight for checking scripts for left #left and right #right"(){
        given:
            SharedSchemaContextComparator tested = new DefaultSharedSchemaContextComparator()
            ISharedSchemaContext leftContext = Mock(ISharedSchemaContext)
            ISharedSchemaContext rightContext = Mock(ISharedSchemaContext)
            leftContext.getSqlDefinitions() >> {
                left.stream().map({
                    def defMock = Mock(SQLDefinition)
                    defMock.getCheckingStatements() >> it
                    defMock
                }).collect(toList())
            }
            rightContext.getSqlDefinitions() >> {
                right.stream().map({
                    def defMock = Mock(SQLDefinition)
                    defMock.getCheckingStatements() >> it
                    defMock
                }).collect(toList())
            }

        when:
            SharedSchemaContextComparator.SharedSchemaContextComparableResults result = tested.diff(leftContext, rightContext)

        then:
            result.getCheckScriptsDifferences().getExistedOnlyOnLeft() == onlyOnLeft
            result.getCheckScriptsDifferences().getExistedOnlyOnRight() == onlyOnRight

        where:
            left | right ||  onlyOnLeft || onlyOnRight
            [["x1", "x23"], ["cdasdf", "com one"]]      | [["x1", "cdasdf"], ["this is new",  "com one"]] || ["x23"] || ["this is new"]
            [["x1", "x23", "cdasdf"], ["com one"]]      | [["x1"], ["cdasdf", "com one"]] || ["x23"] || []
            [["x1", "cdasdf"], ["com one"]]      | [["x1", "cdasdf"], ["this is new",  "com one"]] || [] || ["this is new"]
    }

    @Unroll
    def "should return empty list as differences for creation scripts when there is no differences between definitions for checking scripts #collection"(){
        given:
            SharedSchemaContextComparator tested = new DefaultSharedSchemaContextComparator()
            ISharedSchemaContext leftContext = Mock(ISharedSchemaContext)
            ISharedSchemaContext rightContext = Mock(ISharedSchemaContext)
            leftContext.getSqlDefinitions() >> {
                collection.stream().map({
                    def defMock = Mock(SQLDefinition)
                    defMock.getCheckingStatements() >> it
                    defMock
                }).collect(toList())
            }
            rightContext.getSqlDefinitions() >> {
                collection.stream().map({
                    def defMock = Mock(SQLDefinition)
                    defMock.getCheckingStatements() >> it
                    defMock
                }).collect(toList())
            }

        when:
            SharedSchemaContextComparator.SharedSchemaContextComparableResults result = tested.diff(leftContext, rightContext)

        then:
            result.getCheckScriptsDifferences().getExistedOnlyOnLeft().isEmpty()
            result.getCheckScriptsDifferences().getExistedOnlyOnRight().isEmpty()

        where:
        collection << [[["x1", "cdasdf", "this is new"],  ["com one"]]]
    }
}
