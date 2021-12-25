package com.github.starnowski.posmulten.configuration.yaml;

import org.jeasy.random.api.Randomizer;

import java.util.Random;

public class IntegerRandomizer  implements Randomizer<Integer> {

    private final int min;
    private final int max;

    public IntegerRandomizer(int min, int max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public Integer getRandomValue() {
        return new Random().nextInt(max) + min;
    }
}
