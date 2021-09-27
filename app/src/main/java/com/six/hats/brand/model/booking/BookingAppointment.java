package com.six.hats.brand.model.booking;

import androidx.annotation.Keep;

import java.util.ArrayList;
import java.util.List;

@Keep
public class BookingAppointment {

    public String bookingType;
    private String tempAppointmentId;
    private String userId;
    private List<String> staffIdList = new ArrayList<>();

    public String getBookingType() {
        return bookingType;
    }

    public void setBookingType(String bookingType) {
        this.bookingType = bookingType;
    }

    public String getTempAppointmentId() {
        return tempAppointmentId;
    }

    public void setTempAppointmentId(String tempAppointmentId) {
        this.tempAppointmentId = tempAppointmentId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String> getStaffIdList() {
        return staffIdList;
    }

    public void setStaffIdList(List<String> staffIdList) {
        this.staffIdList = staffIdList;
    }
}
