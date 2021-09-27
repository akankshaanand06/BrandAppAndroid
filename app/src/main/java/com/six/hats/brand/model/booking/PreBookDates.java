package com.six.hats.brand.model.booking;

import androidx.annotation.Keep;

import java.io.Serializable;

@Keep
public class PreBookDates implements Serializable {
    private Boolean selected;
    private String mValues;

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public String getmValues() {
        return mValues;
    }

    public void setmValues(String mValues) {
        this.mValues = mValues;
    }
}

