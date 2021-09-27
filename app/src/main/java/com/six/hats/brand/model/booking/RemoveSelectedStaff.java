package com.six.hats.brand.model.booking;

import androidx.annotation.Keep;

import java.util.ArrayList;
import java.util.List;

@Keep
public class RemoveSelectedStaff {

    public List<String> staffIdList = new ArrayList<>();
    public String temp_BookingId;

    public List<String> getStaffIdList() {
        return staffIdList;
    }

    public void setStaffIdList(List<String> staffIdList) {
        this.staffIdList = staffIdList;
    }

    public String getTemp_BookingId() {
        return temp_BookingId;
    }

    public void setTemp_BookingId(String temp_BookingId) {
        this.temp_BookingId = temp_BookingId;
    }
}
