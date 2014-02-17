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
    YELLOW(Color.YELLOW);

    private Color color;

    private ClockHandColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}
