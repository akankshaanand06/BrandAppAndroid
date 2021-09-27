package com.six.hats.brand.model.booking;

import androidx.annotation.Keep;

import java.util.ArrayList;
import java.util.List;

@Keep
public class MultiBookingRequest {
    private List<BookingReq> multiAppointmentBaseRequests = new ArrayList<>();

    private String userId;
    private String bookingType;


    public List<BookingReq> getMultiAppointmentBaseRequests() {
        return multiAppointmentBaseRequests;
    }

    public void setMultiAppointmentBaseRequests(List<BookingReq> multiAppointmentBaseRequests) {
        this.multiAppointmentBaseRequests = multiAppointmentBaseRequests;
    }

    public String getBookingType() {
        return bookingType;
    }

    public void setBookingType(String bookingType) {
        this.bookingType = bookingType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }




}


