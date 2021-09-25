package com.six.hats.brand.model;

import androidx.annotation.Keep;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Keep
public class BookingSlot implements Serializable {

    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("startSpan")
    @Expose
    private TimeSpan startSpan = new TimeSpan();
    @SerializedName("endSpan")
    @Expose
    private TimeSpan endSpan = new TimeSpan();
    @SerializedName("startSpanDate")
    @Expose
    private long startSpanDate;
    @SerializedName("endSpanDate")
    @Expose
    private long endSpanDate;

    public long getStartSpanDate() {
        return startSpanDate - (5 * 60 * 60 * 1000) - (30 * 60 * 1000);
    }

    public void setStartSpanDate(long startSpanDate) {
        this.startSpanDate = startSpanDate;
    }

    public long getEndSpanDate() {
        return endSpanDate - (5 * 60 * 60 * 1000) - (30 * 60 * 1000);
    }

    public void setEndSpanDate(long endSpanDate) {
        this.endSpanDate = endSpanDate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public TimeSpan getStartSpan() {
        return startSpan;
    }

    public void setStartSpan(TimeSpan startSpan) {
        this.startSpan = startSpan;
    }

    public TimeSpan getEndSpan() {
        return endSpan;
    }

    public void setEndSpan(TimeSpan endSpan) {
        this.endSpan = endSpan;
    }
}
