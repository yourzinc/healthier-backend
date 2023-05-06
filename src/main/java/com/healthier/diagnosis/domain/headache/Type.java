package com.healthier.diagnosis.domain.headache;

public enum Type {
    BASIC("basic"),
    REDFLAGSIGN("red-flag-sign"),
    PRIMARYHEADACHEC("primary-headache-c"),
    PRIMARYHEADACHE("primary-headache"),
    PAINAREA("pain-area"),
    ADDITIONALFACTOR("additional-factor")
    ;

    private final String label;

    Type(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }
}
