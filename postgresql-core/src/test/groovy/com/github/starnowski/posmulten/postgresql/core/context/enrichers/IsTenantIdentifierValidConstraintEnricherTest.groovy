package com.github.starnowski.posmulten.postgresql.core.context.enrichers

import com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder
import com.github.starnowski.posmulten.postgresql.core.context.SharedSchemaContext
import com.github.starnowski.posmulten.postgresql.core.rls.IIsTenantIdentifierValidConstraintProducerParameters
import com.github.starnowski.posmulten.postgresql.core.rls.IsTenantIdentifierValidConstraintProducer
import com.github.starnowski.posmulten.postgresql.core.rls.function.IsTenantValidBasedOnConstantValuesFunctionDefinition
import javafx.util.Pair
import spock.lang.Specification
import spock.lang.Unroll

class IsTenantIdentifierValidConstraintEnricherTest extends Specification {

    /*
     * TODO
     */
    @Unroll
    def "should enrich shared schema context with SQL definition for the constraint that checks if tenant value is correct based on default values for shares schema context builder for schema #schema and black list values (#blacklist)"()
    {
        given:
        def builder = new DefaultSharedSchemaContextBuilder(schema)
        builder.createValidTenantValueConstraint(["ADFZ", "DFZCXVZ"], null, null)
                .createRLSPolicyForTable("table", null, "tenant_xxx", null)
        def sharedSchemaContextRequest = builder.getSharedSchemaContextRequestCopy()
        def context = new SharedSchemaContext()
        List<IIsTenantIdentifierValidConstraintProducerParameters> capturedParameters = new ArrayList<>()
        def mockedSQLDefinition = Mock(IsTenantValidBasedOnConstantValuesFunctionDefinition)
        def producer = Mock(IsTenantIdentifierValidConstraintProducer)
        IsTenantIdentifierValidConstraintEnricher tested = new IsTenantIdentifierValidConstraintEnricher(producer)

        when:
        def result = tested.enrich(context, sharedSchemaContextRequest)

        then:
        1 * producer.produce(_) >>  {
            parameters ->
                capturedParameters.add(parameters[0])
                mockedSQLDefinition
        }
        result.getSqlDefinitions().contains(mockedSQLDefinition)
        result.getIIsTenantValidFunctionInvocationFactory().is(mockedSQLDefinition)

        and: "passed parameters should match default values"
        capturedParameters.getSchema() == schema
        capturedParameters.getBlacklistTenantIds() == new HashSet<String>(blacklist)
        capturedParameters.getArgumentType() == sharedSchemaContextRequest.getCurrentTenantIdPropertyType()
        capturedParameters.getFunctionName() == "is_tenant_identifier_valid"

        where:
            schema          |   tableNameTenantNamePairs                                        ||  expectedPassedParameters
            null            |   [new Pair("users", "tenant_id"), new Pair("leads", "t_xxx")]    ||  [tp("tenant_identifier_valid", "leads", null, "t_xxx"), tp("tenant_identifier_valid", "users", null, "tenant_id")]
            "public"        |   [new Pair("users", "tenant_id"), new Pair("leads", "t_xxx")]    ||  [tp("tenant_identifier_valid", "leads", "public", "t_xxx"), tp("tenant_identifier_valid", "users", "public", "tenant_id")]
            "some_schema"   |   [new Pair("users", "tenant_id"), new Pair("leads", "t_xxx")]    ||  [tp("tenant_identifier_valid", "leads", "public", "t_xxx"), tp("tenant_identifier_valid", "users", "public", "tenant_id")]
    }

    static IsTenantIdentifierValidConstraintProducerKey tp(String constraintName, String tableName, String tableSchema, String tenantColumnName)
    {
        new IsTenantIdentifierValidConstraintProducerKey(constraintName, tableName, tableSchema, tenantColumnName)
    }

    static class IsTenantIdentifierValidConstraintProducerKey
    {
        private final String constraintName
        private final String tableName
        private final String tableSchema
        private final String tenantColumnName

        String getConstraintName() {
            return constraintName
        }

        String getTableName() {
            return tableName
        }

        String getTableSchema() {
            return tableSchema
        }

        String getTenantColumnName() {
            return tenantColumnName
        }
        IsTenantIdentifierValidConstraintProducerKey(String constraintName, String tableName, String tableSchema, String tenantColumnName)
        {
            this.constraintName = constraintName
            this.tableName = tableName
            this.tableSchema = tableSchema
            this.tenantColumnName = tenantColumnName
        }

        static IsTenantIdentifierValidConstraintProducerKey map(IIsTenantIdentifierValidConstraintProducerParameters parameters)
        {
            new IsTenantIdentifierValidConstraintProducerKey(parameters.getConstraintName(), parameters.getTableName(), parameters.getTableSchema(), parameters.getTenantColumnName())
        }

        boolean equals(o) {
            if (this.is(o)) return true
            if (getClass() != o.class) return false

            IsTenantIdentifierValidConstraintProducerKey that = (IsTenantIdentifierValidConstraintProducerKey) o

            if (constraintName != that.constraintName) return false
            if (tableName != that.tableName) return false
            if (tableSchema != that.tableSchema) return false
            if (tenantColumnName != that.tenantColumnName) return false

            return true
        }

        int hashCode() {
            int result
            result = (constraintName != null ? constraintName.hashCode() : 0)
            result = 31 * result + (tableName != null ? tableName.hashCode() : 0)
            result = 31 * result + (tableSchema != null ? tableSchema.hashCode() : 0)
            result = 31 * result + (tenantColumnName != null ? tenantColumnName.hashCode() : 0)
            return result
        }
    }
}
