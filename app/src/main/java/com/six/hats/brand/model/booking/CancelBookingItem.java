package com.six.hats.brand.model.booking;

import androidx.annotation.Keep;

@Keep
public class CancelBookingItem {

    private String staffId;
    private String tempAppointmentId;

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getTempAppointmentId() {
        return tempAppointmentId;
    }

    public void setTempAppointmentId(String tempAppointmentId) {
        this.tempAppointmentId = tempAppointmentId;
    }
}