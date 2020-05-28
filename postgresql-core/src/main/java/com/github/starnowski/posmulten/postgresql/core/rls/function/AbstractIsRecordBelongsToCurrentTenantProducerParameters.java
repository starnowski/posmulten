package com.github.starnowski.posmulten.postgresql.core.rls.function;

import com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValue;
import com.github.starnowski.posmulten.postgresql.core.common.function.IFunctionFactoryParameters;
import javafx.util.Pair;

import java.util.List;

public interface AbstractIsRecordBelongsToCurrentTenantProducerParameters extends IFunctionFactoryParameters {

    List<Pair<String, FunctionArgumentValue>> getKeyColumnsPairsList();

    Pair<String, FunctionArgumentValue> getTenantColumnPair();

    String getRecordTableName();

    String getRecordSchemaName();

    IGetCurrentTenantIdFunctionInvocationFactory getIGetCurrentTenantIdFunctionInvocationFactory();
}
