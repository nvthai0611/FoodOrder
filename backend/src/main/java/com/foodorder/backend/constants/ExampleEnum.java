package com.foodorder.backend.constants;

public enum ExampleEnum {
    THIS("This"),
    IS("Is"),
    AN("An"),
    EXAMPLE_ENUM("Example enum");

    private final String displayName;

    ExampleEnum(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
