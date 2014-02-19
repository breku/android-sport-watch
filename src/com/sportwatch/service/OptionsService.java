package com.sportwatch.service;

import com.sportwatch.model.scene.OptionsScene;
import com.sportwatch.util.ClockHandColor;
import org.andengine.util.adt.color.Color;

/**
 * User: Breku
 * Date: 10.02.14
 */
public class OptionsService extends BaseService {

    private DatabaseHelper databaseHelper = new DatabaseHelper(activity);

    public Integer getNumberOfHandClocks() {
        return databaseHelper.getNumberOfHandClocks();
    }


    public void updateNumberOfHandClocks(int numberOfHandClocks) {
        databaseHelper.updateNumberOfHandClocks(numberOfHandClocks);
    }

    public void setClockHandColor(int clockNumber, String color) {
        databaseHelper.setClockHandColor(clockNumber, color);
    }

    public Color getColorForClockHand(int clockHandNumber) {
        return databaseHelper.getColorForClockHand(clockHandNumber);
    }

    public boolean isClockHandColored(int clockNumber, ClockHandColor color) {
        return databaseHelper.isClockHandColored(clockNumber, color);
    }

    public void setClockDialColor(int clockNumber, String name) {
        databaseHelper.setClockHandColor(clockNumber, name);
    }

    public Color getClockDialColor() {
        return databaseHelper.getColorForClockHand(OptionsScene.CLOCK_DIAL_NUMER);
    }
}
