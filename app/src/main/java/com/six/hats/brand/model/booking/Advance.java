package com.six.hats.brand.model.booking;

import androidx.annotation.Keep;

import com.six.hats.brand.model.TimeSpan;

import java.io.Serializable;

@Keep
public class Advance implements Serializable {
    private Boolean advance;
    private String bookingDate;
    private TimeSpan startSpan = new TimeSpan();

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public Boolean getAdvance() {
        return advance;
    }

    public void setAdvance(Boolean advance) {
        this.advance = advance;
    }

    public TimeSpan getStartSpan() {
        return startSpan;
    }

    public void setStartSpan(TimeSpan startSpan) {
        this.startSpan = startSpan;
    }
}

