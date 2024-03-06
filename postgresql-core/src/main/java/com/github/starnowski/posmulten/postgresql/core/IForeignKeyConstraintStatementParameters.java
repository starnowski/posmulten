package com.github.starnowski.posmulten.postgresql.core;

import com.github.starnowski.posmulten.postgresql.core.context.TableKey;
import com.github.starnowski.posmulten.postgresql.core.rls.IConstraintProducerParameters;

import java.util.Map;

public interface IForeignKeyConstraintStatementParameters extends IConstraintProducerParameters {

    Map<String, String> getForeignKeyColumnMappings();

    TableKey getReferenceTableKey();

}
