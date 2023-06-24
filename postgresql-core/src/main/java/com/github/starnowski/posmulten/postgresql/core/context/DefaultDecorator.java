package com.github.starnowski.posmulten.postgresql.core.context;

import com.github.starnowski.posmulten.postgresql.core.context.decorator.IDecorator;

public class DefaultDecorator<T> implements IDecorator<T> {

    protected final T value;

    DefaultDecorator(T value) {
        this.value = value;
    }

    @Override
    public T unwrap() {
        return value instanceof IDecorator ? ((IDecorator<T>) value).unwrap() : value;
    }
}