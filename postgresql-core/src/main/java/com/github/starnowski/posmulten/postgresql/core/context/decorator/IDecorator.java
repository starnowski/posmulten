package com.github.starnowski.posmulten.postgresql.core.context.decorator;


public interface IDecorator<T> {

    /**
     *
     * @return the first unwrapped object that does not implement IDecorator but the T type
     */
    T unwrap();
}