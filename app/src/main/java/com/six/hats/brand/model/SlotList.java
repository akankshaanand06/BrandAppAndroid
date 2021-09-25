package com.six.hats.brand.model;

import androidx.annotation.Keep;

import java.io.Serializable;

@Keep
public class SlotList implements Serializable {
    private String date;

    private TimeSpan endSpan;

    private TimeSpan startSpan;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setEndSpan(TimeSpan endSpan) {
        this.endSpan = endSpan;
    }

    public TimeSpan getEndSpan() {
        return this.endSpan;
    }

    public void setStartSpan(TimeSpan startSpan) {
        this.startSpan = startSpan;
    }

    public TimeSpan getStartSpan() {
        return this.startSpan;
    }
}