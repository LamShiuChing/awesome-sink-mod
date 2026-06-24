package com.awesomesink.block;

/** How a configured face exposes the inventory to automation. */
public enum SideMode {
    DISABLED(0xFF555555),
    INPUT(0xFF4D6BFF),
    OUTPUT(0xFFFF9A2E);

    public final int color;

    SideMode(int color) {
        this.color = color;
    }

    public SideMode next() {
        return values()[(ordinal() + 1) % values().length];
    }

    public boolean allowsInsert() {
        return this == INPUT;
    }

    public boolean allowsExtract() {
        return this == OUTPUT;
    }
}
