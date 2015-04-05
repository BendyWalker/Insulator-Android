package com.bendywalker.insulator;

public enum BloodGlucoseUnit {
    MMOL ("mmol"),
    MGDL ("mgdl");

    private final String unit;

    BloodGlucoseUnit(String unit) {
        this.unit = unit;
    }
}
