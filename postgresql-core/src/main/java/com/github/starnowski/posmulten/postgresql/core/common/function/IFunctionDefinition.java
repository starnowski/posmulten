package com.github.starnowski.posmulten.postgresql.core.common.function;

import java.util.List;

public interface IFunctionDefinition {

    String getCreateScript();

    String getFunctionReference();

    List<IFunctionArgument> getFunctionArguments();

    String getDropScript();
}
