package com.github.starnowski.posmulten.postgresql.core.context;

import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition;
import com.github.starnowski.posmulten.postgresql.core.rls.EnableRowLevelSecurityProducer;
import com.github.starnowski.posmulten.postgresql.core.rls.ForceRowLevelSecurityProducer;

import java.util.ArrayList;
import java.util.List;

public class TableRLSSettingsSQLDefinitionsProducer {

    private EnableRowLevelSecurityProducer enableRowLevelSecurityProducer = new EnableRowLevelSecurityProducer();
    private ForceRowLevelSecurityProducer forceRowLevelSecurityProducer = new ForceRowLevelSecurityProducer();

    public List<SQLDefinition> produce(TableKey tableKey, boolean forceRowLevelSecurity)
    {
        List<SQLDefinition> results = new ArrayList<>();
        return results;
    }

    void setEnableRowLevelSecurityProducer(EnableRowLevelSecurityProducer enableRowLevelSecurityProducer) {
        this.enableRowLevelSecurityProducer = enableRowLevelSecurityProducer;
    }

    void setForceRowLevelSecurityProducer(ForceRowLevelSecurityProducer forceRowLevelSecurityProducer) {
        this.forceRowLevelSecurityProducer = forceRowLevelSecurityProducer;
    }
}
