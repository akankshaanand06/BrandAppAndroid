package com.six.hats.brand.model.booking;

import androidx.annotation.Keep;

import java.util.ArrayList;
import java.util.List;

@Keep
public class BookingReq {

    private List<String> staffId = new ArrayList<>();
    private String tempBookingId;
    private String userName;

    public List<String> getStaffId() {
        return staffId;
    }

    public void setStaffId(List<String> staffId) {
        this.staffId = staffId;
    }

    public String getTempBookingId() {
        return tempBookingId;
    }

    public void setTempBookingId(String tempBookingId) {
        this.tempBookingId = tempBookingId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}