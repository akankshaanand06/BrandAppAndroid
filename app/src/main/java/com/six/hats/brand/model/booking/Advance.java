package com.six.hats.brand.model.booking;

import androidx.annotation.Keep;

import com.six.hats.brand.model.TimeSpan;

import java.io.Serializable;

@Keep
public class Advance implements Serializable {
    private Boolean advance;
    private String apntDate;
    private TimeSpan bkngRequestStartDateTime = new TimeSpan();

    public String getApntDate() {
        return apntDate;
    }

    public void setApntDate(String apntDate) {
        this.apntDate = apntDate;
    }

    public Boolean getAdvance() {
        return advance;
    }

    public void setAdvance(Boolean advance) {
        this.advance = advance;
    }

    public TimeSpan getBkngRequestStartDateTime() {
        return bkngRequestStartDateTime;
    }

    public void setBkngRequestStartDateTime(TimeSpan bkngRequestStartDateTime) {
        this.bkngRequestStartDateTime = bkngRequestStartDateTime;
    }
}

