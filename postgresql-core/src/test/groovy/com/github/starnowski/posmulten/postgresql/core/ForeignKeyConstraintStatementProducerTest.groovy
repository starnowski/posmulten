package com.github.starnowski.posmulten.postgresql.core

import com.github.starnowski.posmulten.postgresql.core.context.TableKey
import com.github.starnowski.posmulten.postgresql.core.rls.AbstractConstraintProducerTest
import spock.lang.Unroll

class ForeignKeyConstraintStatementProducerTest extends AbstractConstraintProducerTest<IForeignKeyConstraintStatementParameters, ForeignKeyConstraintStatementProducer> {

    def tested = new ForeignKeyConstraintStatementProducer()

    @Unroll
    def "should return statement (#expectedStatement) that adds '#constraintName' constraint to table (#table) and schema (#schema) as reference schema #referenceSchema and table #referenceTable"()
    {
        given:
            def parameters = DefaultForeignKeyConstraintStatementParameters.builder()
                    .withConstraintName(constraintName)
                    .withTableName(table)
                    .withTableSchema(schema)
                    .withReferenceTableKey(new TableKey(referenceTable, referenceSchema))
                    .withForeignKeyColumnMappings(foreignKeyColumnMappings).build()
        when:
            def definition = tested.produce(parameters)

        then:
            definition.getCreateScript() == expectedStatement

        where:
            constraintName      |   schema      | table     |   referenceSchema |   referenceTable  |   foreignKeyColumnMappings                ||	expectedStatement
            "sss"               |   null        | "users"   |   null            |   "post"          |   [z2 : "id"]                             ||  "ALTER TABLE IF EXISTS \"users\" ADD CONSTRAINT sss FOREIGN KEY (z2) REFERENCES \"post\" (id) MATCH SIMPLE;"
            "f_key"             |   "public"    | "users"   |   null            |   "users"         |   [ff : "id", hggf: "abc_user_id"]        ||  "ALTER TABLE IF EXISTS \"public\".\"users\" ADD CONSTRAINT f_key FOREIGN KEY (ff, hggf) REFERENCES \"users\" (id, abc_user_id) MATCH SIMPLE;"
            "sss"               |   "secondary" | "users"   |   "secondary"     |   "users"         |   [x1 : "userId", asdf: "abc_user_id"]    ||  "ALTER TABLE IF EXISTS \"secondary\".\"users\" ADD CONSTRAINT sss FOREIGN KEY (asdf, x1) REFERENCES \"secondary\".\"users\" (abc_user_id, userId) MATCH SIMPLE;"
            "user_belongs_tt"   |   "secondary" | "users"   |   "public"        |   "notifications" |   [ss : "uuid"]                           ||  "ALTER TABLE IF EXISTS \"secondary\".\"users\" ADD CONSTRAINT user_belongs_tt FOREIGN KEY (ss) REFERENCES \"public\".\"notifications\" (uuid) MATCH SIMPLE;"
            "user_belongs_tt"   |   "secondary" | "users"   |   null            |   "users"         |   [v : "secondary_colId", rv : "uuid"]    ||  "ALTER TABLE IF EXISTS \"secondary\".\"users\" ADD CONSTRAINT user_belongs_tt FOREIGN KEY (rv, v) REFERENCES \"users\" (uuid, secondary_colId) MATCH SIMPLE;"
            "some_fk_const"     |   "secondary" | "users"   |   "secondary"     |   "comments"      |   [x1 : "c", uuu : "a", ranV : "b"]       ||  "ALTER TABLE IF EXISTS \"secondary\".\"users\" ADD CONSTRAINT some_fk_const FOREIGN KEY (ranV, uuu, x1) REFERENCES \"secondary\".\"comments\" (b, a, c) MATCH SIMPLE;"
    }

    @Override
    def shouldSkipDefaultCreationTest() {
        true
    }

    @Override
    protected ForeignKeyConstraintStatementProducer returnTestedObject() {
        tested
    }

    @Override
    protected IForeignKeyConstraintStatementParameters returnCorrectParametersMockObject() {
        IForeignKeyConstraintStatementParameters mock = Mock(IForeignKeyConstraintStatementParameters)
        mock.getTableName() >> "users"
        mock.getTableSchema() >> "secondary"
        mock.getReferenceTableKey() >> new TableKey("comments" , "secondary" )
        mock.getForeignKeyColumnMappings() >> [x1 : "c", uuu : "a", ranV : "b"]
        return mock
    }
}
