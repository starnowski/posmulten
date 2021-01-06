package com.github.starnowski.posmulten.configuration.yaml;

public interface IConfigurationMapper<I, O>{

    O map(I input);

    I unmap(O output);
}
