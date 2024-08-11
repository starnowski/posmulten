package com.github.starnowski.posmulten.configuration.yaml.core.mappers;

import com.github.starnowski.posmulten.configuration.yaml.core.IConfigurationMapper;

public abstract class AbstractConfigurationMapper<I, O> implements IConfigurationMapper<I, O> {

    abstract protected O createNewInstanceOfOutput();
}
