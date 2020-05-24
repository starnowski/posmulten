package com.github.starnowski.posmulten.postgresql.core.common.function;

import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition;

import java.util.List;

public interface IFunctionDefinition extends SQLDefinition {

    String getFunctionReference();

    List<IFunctionArgument> getFunctionArguments();
}
