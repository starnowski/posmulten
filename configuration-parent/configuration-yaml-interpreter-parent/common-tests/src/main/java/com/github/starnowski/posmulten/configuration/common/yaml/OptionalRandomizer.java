package com.github.starnowski.posmulten.configuration.common.yaml;

import org.jeasy.random.api.Randomizer;

import java.util.Optional;
import java.util.Random;

public class OptionalRandomizer<T> implements Randomizer<Optional<T>> {

    public OptionalRandomizer(Randomizer<T> randomizer, boolean nullAble) {
        this.randomizer = randomizer;
        this.nullAble = nullAble;
    }

    private final Randomizer<T> randomizer;
    private final boolean nullAble;

    @Override
    public Optional<T> getRandomValue() {
        if (nullAble && new Random().nextBoolean()) {
            return Optional.empty();
        }
        T value = randomizer.getRandomValue();
        return Optional.of(value);
    }
}
