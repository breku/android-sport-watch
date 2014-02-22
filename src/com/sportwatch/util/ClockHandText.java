package com.sportwatch.util;

import com.sportwatch.model.scene.OptionsScene;

/**
 * User: Breku
 * Date: 21.02.14
 */
public enum ClockHandText {
    A("Clock Dial", OptionsScene.CLOCK_DIAL_NUMER),
    B("Clock Hand 1",0),
    C("Clock Hand 2",1),
    D("Clock Hand 3",2),
    E("Clock Hand 4",3),
    F("Clock Hand 5",4),
    G("Clock Hand 6",5)
    ;
    private String text;
    private Integer number;


    private ClockHandText(String text, Integer number) {
        this.text = text;
        this.number = number;
    }

    public ClockHandText next(){
        return values()[(ordinal()+1)%values().length];
    }

    public ClockHandText previous(){
        if(this == values()[0]){
            return values()[values().length-1];
        }else {
            return values()[(ordinal()-1)%values().length];
        }
    }

    public String getText() {
        return text;
    }

    public Integer getNumber() {
        return number;
    }
}
