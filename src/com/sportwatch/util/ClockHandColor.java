package com.sportwatch.util;

import org.andengine.util.adt.color.Color;

/**
 * User: Breku
 * Date: 10.02.14
 */
public enum ClockHandColor {
    GREEN(Color.GREEN),
    RED(Color.RED),
    BLUE(Color.BLUE),
    BLACK(Color.BLACK),
    PINK(Color.PINK),
    YELLOW(Color.YELLOW),
    WHITE(Color.WHITE);

    private Color color;

    private ClockHandColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public ClockHandColor next() {
        return values()[(ordinal() + 1) % values().length];
    }

    public ClockHandColor previous() {
        if (this == values()[0]) {
            return values()[values().length - 1];
        } else {
            return values()[(ordinal() - 1) % values().length];
        }
    }
}
