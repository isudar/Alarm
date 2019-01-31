package com.example.sudo.hardtogetup.models;

import io.realm.RealmList;
import io.realm.RealmObject;

public class Alarm extends RealmObject {

    private int hour;
    private int minutes;
    private String tagMillis;
    private RealmList<Boolean> alarmDays;

    public RealmList<Boolean> getAlarmDays() {
        return alarmDays;
    }

    public void setAlarmDays(RealmList<Boolean> alarmDays) {
        this.alarmDays = alarmDays;
    }

    public String getTagMillis() {
        return tagMillis;
    }

    public void setTagMillis(String tagMillis) {
        this.tagMillis = tagMillis;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }
}
