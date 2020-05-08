package com.github.starnowski.posmulten.postgresql.core;

public interface IFunctionArgument {
    //( [ [ argmode ] [ argname ] argtype [ { DEFAULT | = } default_expr ] [, ...] ] )

    String getType();
}
