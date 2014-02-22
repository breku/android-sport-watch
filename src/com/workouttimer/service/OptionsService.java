package com.workouttimer.service;

import com.workouttimer.util.ClockHandColor;
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
}
