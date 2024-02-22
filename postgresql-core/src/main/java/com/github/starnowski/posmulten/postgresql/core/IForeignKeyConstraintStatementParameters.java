package com.github.starnowski.posmulten.postgresql.core;

import com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValue;
import com.github.starnowski.posmulten.postgresql.core.context.TableKey;

import java.util.Map;

public interface IForeignKeyConstraintStatementParameters {

    Map<String, FunctionArgumentValue> getPrimaryColumnsValuesMap();

    TableKey getTableKey();

    TableKey getReferenceTableKey();

}
