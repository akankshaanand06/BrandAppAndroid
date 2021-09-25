package com.six.hats.brand.model;

import androidx.annotation.Keep;

import java.io.Serializable;

@Keep
public class TimeSpan implements Serializable {
    private int hour;

    private int minutes;

    private int total;

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getHour() {
        return this.hour;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getMinutes() {
        return this.minutes;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotal() {
        return this.total;
    }
}
