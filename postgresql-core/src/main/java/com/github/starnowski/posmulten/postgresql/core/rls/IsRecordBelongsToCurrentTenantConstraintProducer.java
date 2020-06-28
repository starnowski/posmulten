package com.github.starnowski.posmulten.postgresql.core.rls;

import com.github.starnowski.posmulten.postgresql.core.common.DefaultSQLDefinition;
import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition;

public class IsRecordBelongsToCurrentTenantConstraintProducer {

    public SQLDefinition produce(IsRecordBelongsToCurrentTenantConstraintProducerParameters parameters)
    {
        //TODO
        return new DefaultSQLDefinition("ALTER TABLE \"users\" ADD CONSTRAINT sss CHECK (cccsss);", "");
    }
}
