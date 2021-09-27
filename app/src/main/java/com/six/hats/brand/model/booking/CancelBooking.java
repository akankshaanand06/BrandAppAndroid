package com.six.hats.brand.model.booking;

import androidx.annotation.Keep;

import java.util.ArrayList;
import java.util.List;

@Keep
public class CancelBooking {
    private List<CancelBookingItem> appSignalList = new ArrayList<>();

    public List<CancelBookingItem> getAppSignalList() {
        return appSignalList;
    }

    public void setAppSignalList(List<CancelBookingItem> appSignalList) {
        this.appSignalList = appSignalList;
    }

   
}

