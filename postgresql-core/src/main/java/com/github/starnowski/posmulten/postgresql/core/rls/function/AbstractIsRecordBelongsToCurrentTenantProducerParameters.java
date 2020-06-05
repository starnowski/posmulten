package com.github.starnowski.posmulten.postgresql.core.rls.function;

import com.github.starnowski.posmulten.postgresql.core.common.function.IFunctionArgument;
import com.github.starnowski.posmulten.postgresql.core.common.function.IFunctionFactoryParameters;
import javafx.util.Pair;

import java.util.List;

import static com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentBuilder.forType;

public interface AbstractIsRecordBelongsToCurrentTenantProducerParameters extends IFunctionFactoryParameters {

    //TODO cannot be null
    //TODO cannot be empty
    //TODO types validation similar to TenantColumnPair
    List<Pair<String, IFunctionArgument>> getKeyColumnsPairsList();

    //TODO key cannot be empty
    String getTenantColumn();

    String getRecordTableName();

    String getRecordSchemaName();

    //TODO cannot be null
    IGetCurrentTenantIdFunctionInvocationFactory getIGetCurrentTenantIdFunctionInvocationFactory();

    static Pair<String, IFunctionArgument> pairOfColumnWithType(String column, String type)
    {
        return new Pair<>(column, forType(type));
    }
}
