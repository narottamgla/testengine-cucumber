package com.api.hooks;

import io.cucumber.java.Scenario;

import java.util.HashMap;

public class Storage {

    private static final HashMap<Thread, Scenario> SCENARIO_HASH_MAP = new HashMap<>();

    public static void putScenario(final Scenario scenario) {
        SCENARIO_HASH_MAP.put(Thread.currentThread(), scenario);
    }

    public static Scenario getScenario() {
        return SCENARIO_HASH_MAP.get(Thread.currentThread());
    }
}
